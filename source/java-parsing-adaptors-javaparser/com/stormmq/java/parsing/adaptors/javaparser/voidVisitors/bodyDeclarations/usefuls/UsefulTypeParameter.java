package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls;

import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverContext;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.stormmq.java.parsing.adaptors.javaparser.genericVisitors.TypeArgumentConversionTypeGenericVisitor.TypeArgumentConverter;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.assertNotNull;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.validateName;

public final class UsefulTypeParameter
{
	@NotNull public static final UsefulTypeParameter[] EmptyUsefulTypeParameters = {};
	@NotNull private static final ClassOrInterfaceIntermediateTypeArgument[] EmptyTypeParameterBounds = {};

	@NotNull
	public static UsefulTypeParameter[] convertTypeParameters(@NotNull final TypeResolverContext typeResolverContext, @Nullable final List<TypeParameter> typeParameters)
	{
		if (typeParameters == null)
		{
			return EmptyUsefulTypeParameters;
		}

		final int size = typeParameters.size();
		if (size == 0)
		{
			return EmptyUsefulTypeParameters;
		}

		final Set<String> encounteredTypeParameterNames = new HashSet<>(size);
		final UsefulTypeParameter[] usefulTypeParameters = new UsefulTypeParameter[size];
		for(int index = 0; index < size; index++)
		{
			final TypeParameter typeParameter = typeParameters.get(index);
			final ClassOrInterfaceIntermediateTypeArgument[] upperBounds = convertTypeBounds(typeResolverContext, typeParameter);

			final String typeParameterName = validateName(typeParameter.getName(), false);
			if (!encounteredTypeParameterNames.add(typeParameterName))
			{
				throw new IllegalArgumentException("TypeParameter name duplicated");
			}

			usefulTypeParameters[index] = new UsefulTypeParameter(typeParameterName, upperBounds, typeResolverContext.convertToAnnotations(typeParameter.getAnnotations()));
		}
		return usefulTypeParameters;
	}

	@NotNull
	private static ClassOrInterfaceIntermediateTypeArgument[] convertTypeBounds(@NotNull final TypeResolverContext typeResolverContext, @NotNull final TypeParameter typeParameter)
	{
		@Nullable final List<ClassOrInterfaceType> typeBound = typeParameter.getTypeBound();

		if (typeBound == null)
		{
			return EmptyTypeParameterBounds;
		}

		final int size = typeBound.size();
		if (size == 0)
		{
			return EmptyTypeParameterBounds;
		}

		final Set<String> alreadyEncountered = new HashSet<>(size);
		final ClassOrInterfaceIntermediateTypeArgument[] upperBounds = new ClassOrInterfaceIntermediateTypeArgument[size];
		for(int index = 0; index < size; index++)
		{
			final ClassOrInterfaceType classOrInterfaceType = assertNotNull(typeBound.get(index));
			final String name = assertNotNull(classOrInterfaceType.getName());
			if (!alreadyEncountered.add(name))
			{
				throw new IllegalArgumentException("Duplicate type bound");
			}
			upperBounds[index] = TypeArgumentConverter.visit(classOrInterfaceType, typeResolverContext);
		}
		return upperBounds;
	}

	@NotNull private final String name;
	@NotNull private final ClassOrInterfaceIntermediateTypeArgument[] upperBounds;
	@NotNull private final AnnotationInstanceIntermediate[] annotations;

	// upperBounds == 'T extends SomeObject & Runnable & Comparable'
	// Only first bound can be a class, others must be interfaces
	// In the absence of generics, the Java .class type is the first of the upper bounds
	// A method's type parameters hide a class / interfaces (unless static)
	// A local class definition in a method hides a method / class type parameter
	// Static methods can be accessed through a type parameters, eg define <T extends String>, then can do T.format()
	public UsefulTypeParameter(@NotNull final String name, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] upperBounds, @NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		this.name = name;
		this.upperBounds = upperBounds;
		this.annotations = annotations;
	}

	public boolean addTo(@NotNull final Map<String, UsefulTypeParameter> typeParametersIndex)
	{
		return typeParametersIndex.put(name, this) != null;
	}

	@NotNull
	public KnownReferenceTypeName resolve(@NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeResolverContext typeResolverContext)
	{
		// need to supply prototype mutable type parameter objects
		throw new UnsupportedOperationException("Finish me");
	}
}
