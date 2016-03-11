package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public final class NestedLocalClassTypeResolverContext implements TypeResolverContext
{
	@NotNull private final TypeResolverContext parent;
	@NotNull private final LinkedHashMap<String, KnownReferenceTypeName> predecessorsInBlock;

	public NestedLocalClassTypeResolverContext(@NotNull final TypeResolverContext parent, @NotNull final LinkedHashMap<String, KnownReferenceTypeName> predecessorsInBlock)
	{
		this.parent = parent;
		this.predecessorsInBlock = predecessorsInBlock;
	}

	@NotNull
	@Override
	public TypeResolverContext nestedLocalClass(@NotNull final KnownReferenceTypeName knownReferenceTypeName, @NotNull final String rawLocalClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}
}
