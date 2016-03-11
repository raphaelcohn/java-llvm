package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states;

import com.github.javaparser.ast.body.*;
import com.stormmq.java.parsing.utilities.names.parentNames.ParentName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.validateName;

public final class ImplementedPackageState extends AbstractState implements PackageState
{
	@NotNull private final MultipleTypeDefinitionRule multipleTypeDefinitionRule;
	private boolean hasAPublicTypeDefinition;

	protected ImplementedPackageState(@NotNull final ParentName parentName, @NotNull final MultipleTypeDefinitionRule multipleTypeDefinitionRule)
	{
		super(parentName);
		this.multipleTypeDefinitionRule = multipleTypeDefinitionRule;
		hasAPublicTypeDefinition = false;
	}

	@Override
	@NotNull
	public KnownReferenceTypeName typeName(@NotNull final TypeDeclaration typeDeclaration)
	{
		if (typeDeclaration instanceof EmptyTypeDeclaration)
		{
			throw new IllegalArgumentException(com.stormmq.java.parsing.utilities.StringConstants.Should_be_impossible);
		}

		final int modifiers = typeDeclaration.getModifiers();
		final boolean isPublic = ModifierSet.isPublic(modifiers);
		if (isPublic)
		{
			if (hasAPublicTypeDefinition)
			{
				multipleTypeDefinitionRule.secondPublicTypeEncountered();
			}
			hasAPublicTypeDefinition = true;
		}


		final String simpleTypeName = validateName(typeDeclaration.getName(), false);

		return child(simpleTypeName, "top-level type");
	}

	public void end()
	{
		multipleTypeDefinitionRule.finish(alreadyVisitedTypeNames);
	}
}
