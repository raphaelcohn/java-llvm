package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

public interface TypeResolverContext extends TypeResolver, TopLevelTypeResolverContext, NestedTypeResolverContext
{
	@NotNull
	TypeResolverContext nestedLocalClass(@NotNull final KnownReferenceTypeName knownReferenceTypeName, @NotNull final String rawLocalClassName, @NotNull final UsefulTypeParameters classTypeParameters);

	@NotNull
	TypeResolverContext enumConstantClassBody(@NotNull final String enumConstantName);

	@NotNull
	TypeResolverContext constructor(@NotNull final UsefulTypeParameters constructorTypeParameters);

	@NotNull
	TypeResolverContext method(@NotNull final UsefulTypeParameters methodTypeParameters);
}
