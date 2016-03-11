package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import org.jetbrains.annotations.NotNull;

public interface TopLevelTypeResolverContext extends TypeResolver
{
	@NotNull
	TypeResolverContext topLevelAnnotation(@NotNull final String annotationName);

	@NotNull
	TypeResolverContext topLevelInterface(@NotNull final String interfaceName, @NotNull final UsefulTypeParameters interfaceTypeParameters);

	@NotNull
	TypeResolverContext topLevelClass(@NotNull final String className, @NotNull final UsefulTypeParameters classTypeParameters);

	@NotNull
	TypeResolverContext topLevelEnum(@NotNull final String enumName);
}
