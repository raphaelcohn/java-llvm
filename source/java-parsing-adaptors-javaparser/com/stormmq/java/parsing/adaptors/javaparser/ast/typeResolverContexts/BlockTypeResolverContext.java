package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;

public final class BlockTypeResolverContext extends VeryAbstractTypeResolverContext
{
	@NotNull private final TypeResolverContext parent;
	@NotNull private final LinkedHashMap<String, KnownReferenceTypeName> predecessorsInBlock;

	public BlockTypeResolverContext(@NotNull final TypeResolverContext parent, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final String ourIdentifier)
	{
		// may have type parameters if this is a method block?
		this.parent = parent;
		predecessorsInBlock = new LinkedHashMap<>(16);
	}

	@Nullable
	@Override
	public KnownReferenceTypeName resolveAsTypeParameter(@NotNull final String identifier)
	{
		return parent.resolveAsTypeParameter(identifier);
	}

	@NotNull
	@Override
	public KnownReferenceTypeName resolveAsType(@NotNull final String identifier)
	{
		xxx;
		// block needs to be segmented!
		@Nullable final KnownReferenceTypeName knownReferenceTypeName = predecessorsInBlock.get(identifier);
		if (knownReferenceTypeName != null)
		{
			return knownReferenceTypeName;
		}
		return parent.resolveAsType(identifier);
	}

	@NotNull
	@Override
	public KnownReferenceTypeName resolve(@NotNull final String identifier)
	{
		@Nullable final KnownReferenceTypeName resolveAsTypeParameter = resolveAsTypeParameter(identifier);
		if (resolveAsTypeParameter != null)
		{
			return resolveAsTypeParameter;
		}
		return resolveAsType(identifier);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedLocalClass(@NotNull final KnownReferenceTypeName knownReferenceTypeName, @NotNull final String rawLocalClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		return new NestedLocalClassTypeResolverContext(this, new LinkedHashMap<>(predecessorsInBlock));
	}

	@NotNull
	@Override
	public TypeResolverContext nestedInnerClass(@NotNull final String rawInnerClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext topLevelAnnotation(@NotNull final String annotationName)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext topLevelInterface(@NotNull final String interfaceName, @NotNull final UsefulTypeParameters interfaceTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext topLevelClass(@NotNull final String className, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext topLevelEnum(@NotNull final String enumName)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedAnnotation(@NotNull final String annotationName)
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
	public TypeResolverContext nestedStaticClass(@NotNull final String rawStaticClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext enumConstantClassBody(@NotNull final String enumConstantName)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext constructor(@NotNull final UsefulTypeParameters constructorTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext method(@NotNull final UsefulTypeParameters methodTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedEnum(@NotNull final String enumName)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}
}
