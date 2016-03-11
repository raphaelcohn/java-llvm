package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.utilities.names.parentNames.ParentName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeResolution.TypeResolver;
import org.jetbrains.annotations.NotNull;

public final class StaticClassTypeResolverContext extends AbstractTypeResolverContext
{
	public StaticClassTypeResolverContext(final TypeResolver typeResolver, @NotNull final TypeResolverChoice typeResolverChoice, final TypeResolverContext parent, final ParentName ourParentName, final String rawStaticClassName, final UsefulTypeParameters classTypeParameters)
	{
		super(typeResolver, typeResolverChoice, parent, ourParentName, rawStaticClassName, classTypeParameters);
	}

	@NotNull
	@Override
	public TypeResolverContext constructor(@NotNull final UsefulTypeParameters constructorTypeParameters)
	{
		return newConstructorOrMethodTypeResolverContext(constructorTypeParameters);
	}

	@NotNull
	@Override
	public TypeResolverContext method(@NotNull final UsefulTypeParameters methodTypeParameters)
	{
		return newConstructorOrMethodTypeResolverContext(methodTypeParameters);
	}

	@NotNull
	@Override
	public TypeResolverContext enumConstantClassBody(@NotNull final String enumConstantName)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedLocalClass(@NotNull final KnownReferenceTypeName knownReferenceTypeName, @NotNull final String rawLocalClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}
}
