package com.stormmq.java.parsing.adaptors.javaparser.genericVisitors;

import com.github.javaparser.ast.type.*;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverContext;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.PrimitiveArrayIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.IntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.PrimitiveTypeName;
import com.stormmq.java.parsing.ast.typeUsages.PrimitiveTypeUsage;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import com.stormmq.java.parsing.ast.typeUsages.VoidTypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument.mergeAnnotations;
import static com.stormmq.java.parsing.adaptors.javaparser.genericVisitors.TypeArgumentConversionTypeGenericVisitor.convertUpperBoundsOfClassOrInterfaceType;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.validateName;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.PrimitiveTypeHelper.convertToPrimitiveTypeName;
import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate.EmptyEmptyAnnotations;

public final class MethodReturnMethodParameterFieldAndAnnotationMemberTypeGenericVisitor extends AbstractTypeGenericVisitor<TypeUsage, Integer>
{
	// Only valid for field types, method return types and method parameters
	// Also seems valid for annotation member types, but the javaparser API AnnotationMemberDeclaration does not seem to have getArrayCount();
	@NotNull
	public static TypeUsage methodReturnMethodParameterFieldAndAnnotationMemberType(@NotNull final Type type, final int cStyleArrayCount, @NotNull final TypeResolverContext typeResolverContext, final boolean isMethodReturnType)
	{
		return type.accept(new MethodReturnMethodParameterFieldAndAnnotationMemberTypeGenericVisitor(typeResolverContext, isMethodReturnType), cStyleArrayCount);
	}

	@NotNull private final TypeResolverContext typeResolverContext;
	private final boolean isMethodReturnType;

	public MethodReturnMethodParameterFieldAndAnnotationMemberTypeGenericVisitor(@NotNull final TypeResolverContext typeResolverContext, final boolean isMethodReturnType)
	{
		this.typeResolverContext = typeResolverContext;
		this.isMethodReturnType = isMethodReturnType;
	}

	@NotNull
	@Override
	public VoidTypeUsage visit(@NotNull final VoidType voidType, @NotNull final Integer state)
	{
		if (!isMethodReturnType)
		{
			throw new IllegalArgumentException("Only a method return type can use void");
		}

		if (state != 0)
		{
			throw new IllegalArgumentException("A void type can not have a c-style array count");
		}

		return new VoidTypeUsage(typeResolverContext.convertToAnnotations(voidType.getAnnotations()));
	}

	@NotNull
	@Override
	public TypeUsage visit(@NotNull final PrimitiveType primitiveType, @NotNull final Integer state)
	{
		final int cStyleArrayAdjustment = state;
		final PrimitiveTypeName primitiveTypeName = convertToPrimitiveTypeName(primitiveType);
		if (cStyleArrayAdjustment == 0)
		{
			return new PrimitiveTypeUsage(typeResolverContext.convertToAnnotations(primitiveType.getAnnotations()), primitiveTypeName);
		}
		return new PrimitiveArrayIntermediateTypeArgument(typeResolverContext.convertToAnnotations(primitiveType.getAnnotations()), new AnnotationInstanceIntermediate[cStyleArrayAdjustment][], primitiveTypeName);
	}

	@NotNull
	@Override
	public ClassOrInterfaceIntermediateTypeArgument visit(@NotNull final ClassOrInterfaceType classOrInterfaceType, @NotNull final Integer state)
	{
		@Nullable final ClassOrInterfaceType rawScope = classOrInterfaceType.getScope();
		@Nullable final ClassOrInterfaceIntermediateTypeArgument scope = (rawScope == null) ? null : visit(rawScope, state);

		final String name = validateName(classOrInterfaceType.getName(), false);
		final IntermediateTypeArgument[] upperBounds = convertUpperBoundsOfClassOrInterfaceType(typeResolverContext, classOrInterfaceType);

		return new ClassOrInterfaceIntermediateTypeArgument(typeResolverContext.convertToAnnotations(classOrInterfaceType.getAnnotations()), EmptyEmptyAnnotations, scope, name, upperBounds);
	}

	@NotNull
	@Override
	public TypeUsage visit(@NotNull final ReferenceType referenceType, @NotNull final Integer state)
	{
		final Type underlying = referenceType.getType();
		final AnnotationInstanceIntermediate[] annotations = typeResolverContext.convertToAnnotations(referenceType.getAnnotations());
		final AnnotationInstanceIntermediate[][] arrayAnnotations = typeResolverContext.convertReferenceTypeArrayAnnotations(referenceType, state);
		final AnnotationInstanceIntermediate[] underlyingAnnotations = typeResolverContext.convertToAnnotations(underlying.getAnnotations());

		if (arrayAnnotations.length == 0)
		{
			if (underlying instanceof ClassOrInterfaceType)
			{
				return ((ClassOrInterfaceIntermediateTypeArgument) underlying.accept(this, state)).merge(annotations, arrayAnnotations);
			}

			if (underlying instanceof PrimitiveType)
			{
				return new PrimitiveTypeUsage(mergeAnnotations(underlyingAnnotations, annotations), convertToPrimitiveTypeName((PrimitiveType) underlying));
			}

			if (underlying instanceof VoidType)
			{
				return new VoidTypeUsage(mergeAnnotations(underlyingAnnotations, annotations));
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

		throw new IllegalArgumentException("A reference type when used as a type usage may not have an underlying type of Wildcard or Unknown, and may not have one as Primitive or Void if the array dimensionality is not zero");
	}

	@NotNull
	@Override
	public TypeUsage visit(@NotNull final WildcardType wildcardType, @NotNull final Integer state)
	{
		throw new IllegalArgumentException("A wildcard type can not be in use as method return type, method parameter, field type or annotation member type");
	}

	@NotNull
	@Override
	public TypeUsage visit(@NotNull final UnknownType unknownType, @NotNull final Integer state)
	{
		throw new IllegalArgumentException("An unknownType can not surely have a c-style array count");
	}
}
