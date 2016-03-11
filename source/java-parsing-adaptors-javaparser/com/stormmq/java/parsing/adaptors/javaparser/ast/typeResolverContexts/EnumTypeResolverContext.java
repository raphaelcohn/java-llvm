package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.utilities.names.parentNames.ParentName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeResolution.TypeResolver;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters.NoUsefulTypeParameters;

public final class EnumTypeResolverContext extends AbstractTypeResolverContext
{
	public EnumTypeResolverContext(@NotNull final TypeResolver typeResolver, @NotNull final TypeResolverChoice typeResolverChoice, @NotNull final TypeResolverContext parent, @NotNull final ParentName ourParentName, @NotNull final String enumName)
	{
		super(typeResolver, typeResolverChoice, parent, ourParentName, enumName, NoUsefulTypeParameters);
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
		if (constructorTypeParameters.isNotEmpty())
		{
			throw new IllegalArgumentException("Enum constructors can not have type parameters");
		}
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
		return newEnumConstantClassBodyTypeResolverContext(enumConstantName);
	}
}
