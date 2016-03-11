package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import org.jetbrains.annotations.NotNull;

public interface NestedTypeResolverContext extends TypeResolver
{
	@NotNull
	TypeResolverContext nestedAnnotation(@NotNull final String annotationName);

	@NotNull
	TypeResolverContext nestedInterface(@NotNull final String interfaceName, @NotNull final UsefulTypeParameters interfaceTypeParameters);

	// raw is that without any '$' if it happens to start with '$'
	@NotNull
	TypeResolverContext nestedStaticClass(@NotNull final String rawStaticClassName, @NotNull final UsefulTypeParameters classTypeParameters);

	// raw is that without any '$' if it happens to start with '$'
	@NotNull
	TypeResolverContext nestedInnerClass(@NotNull final String rawInnerClassName, @NotNull final UsefulTypeParameters classTypeParameters);

	@NotNull
	TypeResolverContext nestedEnum(@NotNull final String enumName);

	// Only within a method or field or initializer
	@NotNull
	TypeResolverContext nestedAnonymousClass(int ordinal);

}
