package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeResolution.TypeResolver;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverChoice.Type;

public final class NestedInnerClassTypeResolverContext extends AbstractTypeResolverContext
{
	public NestedInnerClassTypeResolverContext(@NotNull final TypeResolver typeResolver, @NotNull final TypeResolverContext parent, @NotNull final KnownReferenceTypeName ourParentKnownReferenceTypeName, @NotNull final String rawInnerClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		super(typeResolver, Type, parent, ourParentKnownReferenceTypeName, rawInnerClassName, classTypeParameters);
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

	@NotNull
	@Override
	public TypeResolverContext nestedStaticClass(@NotNull final String rawStaticClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedEnum(@NotNull final String enumName)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedInterface(@NotNull final String interfaceName, @NotNull final UsefulTypeParameters interfaceTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedAnnotation(@NotNull final String annotationName)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}
}
