package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls;

import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverContext;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameter.EmptyUsefulTypeParameters;

public final class UsefulTypeParameters
{
	@NotNull public static final UsefulTypeParameters NoUsefulTypeParameters = new UsefulTypeParameters(EmptyUsefulTypeParameters);

	@NotNull private final Map<String, UsefulTypeParameter> typeParametersIndex;

	// upperBounds == 'T extends SomeObject & Runnable & Comparable'
	// Only first bound can be a class, others must be interfaces
	// In the absence of generics, the Java .class type is the first of the upper bounds
	// A method's type parameters hide a class / interfaces (unless static)
	// A local class definition in a method hides a method / class type parameter
	// Static methods can be accessed through a type parameters, eg define <T extends String>, then can do T.format()
	private UsefulTypeParameters(@NotNull final UsefulTypeParameter[] ours)
	{
		typeParametersIndex = new HashMap<>();
		for (UsefulTypeParameter our : ours)
		{
			if (our.addTo(typeParametersIndex))
			{
				throw new IllegalArgumentException("Duplicate type parameter");
			}
		}
	}

	@Nullable
	public KnownReferenceTypeName resolve(@NotNull final String identifier, @NotNull final TypeResolverContext typeResolverContext)
	{
		@Nullable final UsefulTypeParameter usefulTypeParameter = typeParametersIndex.get(identifier);
		if (usefulTypeParameter == null)
		{
			return null;
		}
		return usefulTypeParameter.resolve(this, typeResolverContext);
	}

	public boolean isNotEmpty()
	{
		return !typeParametersIndex.isEmpty();
	}

	public UsefulTypeParameters descend(@NotNull final UsefulTypeParameter[] typeParameters);
}
