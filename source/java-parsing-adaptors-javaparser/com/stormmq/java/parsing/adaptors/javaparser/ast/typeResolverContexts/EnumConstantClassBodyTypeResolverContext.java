package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeResolution.TypeResolver;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverChoice.Type;
import static com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters.NoUsefulTypeParameters;

public final class EnumConstantClassBodyTypeResolverContext extends AbstractTypeResolverContext
{
	@NotNull private final String enumConstantName;

	public EnumConstantClassBodyTypeResolverContext(@NotNull final TypeResolver typeResolver, @NotNull final TypeResolverContext parent, @NotNull final KnownReferenceTypeName ourParentKnownReferenceTypeName, @NotNull final String ourSimpleTypeName, @NotNull final String enumConstantName)
	{
		super(typeResolver, Type, parent, ourParentKnownReferenceTypeName, ourSimpleTypeName, NoUsefulTypeParameters);
		this.enumConstantName = enumConstantName;
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
}
