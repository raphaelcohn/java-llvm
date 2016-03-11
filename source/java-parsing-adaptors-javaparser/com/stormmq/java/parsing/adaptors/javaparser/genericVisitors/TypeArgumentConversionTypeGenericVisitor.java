package com.stormmq.java.parsing.adaptors.javaparser.genericVisitors;

import com.github.javaparser.ast.type.*;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverContext;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ArrayPossibleIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.IntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.PrimitiveArrayIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.wildcardIntermediateTypeArguments.AnyWildcardIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.wildcardIntermediateTypeArguments.LowerBoundedWildcardIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.wildcardIntermediateTypeArguments.UpperBoundedWildcardIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.assertNotNull;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.validateName;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.PrimitiveTypeHelper.convertToPrimitiveTypeName;
import static com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument.mergeAnnotations;
import static com.stormmq.java.parsing.ast.intermediateTypeArguments.IntermediateTypeArgument.EmptyIntermediateTypeArguments;
import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate.EmptyEmptyAnnotations;

public final class TypeArgumentConversionTypeGenericVisitor extends AbstractTypeGenericVisitor<IntermediateTypeArgument, TypeResolverContext>
{
	@NotNull public static final TypeArgumentConversionTypeGenericVisitor TypeArgumentConverter = new TypeArgumentConversionTypeGenericVisitor();

	@NotNull
	public static IntermediateTypeArgument[] convertUpperBoundsOfClassOrInterfaceType(@NotNull final TypeResolverContext typeResolverContext, @NotNull final ClassOrInterfaceType classOrInterfaceType)
	{
		@Nullable final List<Type> typeArguments = classOrInterfaceType.getTypeArgs();
		return TypeArgumentConverter.convertTypesToTypeArguments(typeResolverContext, typeArguments);
	}

	private TypeArgumentConversionTypeGenericVisitor()
	{
	}

	@NotNull
	@Override
	public ArrayPossibleIntermediateTypeArgument visit(@NotNull final ReferenceType referenceType, @NotNull final TypeResolverContext state)
	{
		final Type underlying = referenceType.getType();
		final AnnotationInstanceIntermediate[] annotations = state.convertToAnnotations(referenceType.getAnnotations());
		final AnnotationInstanceIntermediate[][] arrayAnnotations = state.convertReferenceTypeArrayAnnotations(referenceType, 0);
		final AnnotationInstanceIntermediate[] underlyingAnnotations = state.convertToAnnotations(underlying.getAnnotations());

		if (arrayAnnotations.length == 0)
		{
			if (underlying instanceof ClassOrInterfaceType)
			{
				return ((ClassOrInterfaceIntermediateTypeArgument) underlying.accept(this, state)).merge(annotations, arrayAnnotations);
			}

			throw new IllegalArgumentException("An array reference type of dimensionality zero can only have an underlying of ClassOrInterfaceType when processed as a type argument");
		}

		if (underlying instanceof ClassOrInterfaceType)
		{
			return ((ClassOrInterfaceIntermediateTypeArgument) underlying.accept(this, state)).merge(annotations, arrayAnnotations);
		}

		if (underlying instanceof PrimitiveType)
		{
			return new PrimitiveArrayIntermediateTypeArgument(mergeAnnotations(underlyingAnnotations, annotations), arrayAnnotations, convertToPrimitiveTypeName((PrimitiveType) underlying));
		}

		throw new IllegalArgumentException("A reference type when used as a type argument may not have an underlying type of Wildcard, Unknown or Void, and may not have one as Primitive if the array dimensionality is not zero");
	}

	@NotNull
	@Override
	public ClassOrInterfaceIntermediateTypeArgument visit(@NotNull final ClassOrInterfaceType classOrInterfaceType, @NotNull final TypeResolverContext state)
	{
		@Nullable final ClassOrInterfaceType rawScope = classOrInterfaceType.getScope();
		@Nullable final ClassOrInterfaceIntermediateTypeArgument scope = (rawScope == null) ? null : visit(rawScope, state);

		final String name = validateName(classOrInterfaceType.getName(), false);
		final IntermediateTypeArgument[] upperBounds = convertUpperBoundsOfClassOrInterfaceType(state, classOrInterfaceType);

		return new ClassOrInterfaceIntermediateTypeArgument(state.convertToAnnotations(classOrInterfaceType.getAnnotations()), EmptyEmptyAnnotations, scope, name, upperBounds);
	}

	@NotNull
	@Override
	public IntermediateTypeArgument visit(@NotNull final WildcardType wildcardType, @NotNull final TypeResolverContext state)
	{
		final AnnotationInstanceIntermediate[] annotations = state.convertToAnnotations(wildcardType.getAnnotations());
		@Nullable final ReferenceType upperBound = wildcardType.getExtends();
		@Nullable final ReferenceType lowerBound = wildcardType.getSuper();

		if (upperBound == null)
		{
			// <?>
			if (lowerBound == null)
			{
				return new AnyWildcardIntermediateTypeArgument(annotations);
			}

			// <? super lowerBound>
			return new LowerBoundedWildcardIntermediateTypeArgument(annotations, convertWildcardBoundToTypeArgument(state, lowerBound));
		}

		if (lowerBound == null)
		{
			// <? extends upperBound>
			return new UpperBoundedWildcardIntermediateTypeArgument(annotations, convertWildcardBoundToTypeArgument(state, upperBound));
		}

		throw new IllegalArgumentException("Wildcard types can not have both an upper and a lower bound");
	}

	@NotNull
	@Override
	public IntermediateTypeArgument visit(@NotNull final PrimitiveType primitiveType, @NotNull final TypeResolverContext state)
	{
		throw new IllegalArgumentException("A primitive can not be a type argument");
	}

	@NotNull
	@Override
	public IntermediateTypeArgument visit(@NotNull final VoidType voidType, @NotNull final TypeResolverContext state)
	{
		throw new IllegalArgumentException("A void can not be a type argument");
	}

	@NotNull
	@Override
	public IntermediateTypeArgument visit(@NotNull final UnknownType unknownType, @NotNull final TypeResolverContext state)
	{
		throw new IllegalArgumentException("An unknown can not be a type argument");
	}

	@NotNull
	private IntermediateTypeArgument[] convertTypesToTypeArguments(@NotNull final TypeResolverContext unresolvedReferenceTypeNameMaker, @Nullable final List<Type> types)
	{
		if (types == null)
		{
			return EmptyIntermediateTypeArguments;
		}

		final int size = types.size();
		if (size == 0)
		{
			return EmptyIntermediateTypeArguments;
		}

		final IntermediateTypeArgument[] intermediateTypeArguments = new IntermediateTypeArgument[size];
		for (int index = 0; index < size; index++)
		{
			final Type typeArg = types.get(index);
			intermediateTypeArguments[index] = convertTypeToTypeArgument(unresolvedReferenceTypeNameMaker, typeArg);
		}
		return intermediateTypeArguments;
	}

	@NotNull
	private IntermediateTypeArgument convertTypeToTypeArgument(@NotNull final TypeResolverContext typeResolverContext, @Nullable final Type type)
	{
		return assertNotNull(type).accept(this, typeResolverContext);
	}

	@NotNull
	private ArrayPossibleIntermediateTypeArgument convertWildcardBoundToTypeArgument(@NotNull final TypeResolverContext typeResolverContext, @NotNull final Type wildcardBoundType)
	{
		if ((wildcardBoundType instanceof ClassOrInterfaceType) || (wildcardBoundType instanceof ReferenceType))
		{
			return (ArrayPossibleIntermediateTypeArgument) wildcardBoundType.accept(this, typeResolverContext);
		}

		throw new IllegalArgumentException("A wildcard type can not have a bound which is other than a ClassOrInterfaceType or a ReferenceType");
	}
}
