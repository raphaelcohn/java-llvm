package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class MultipleTypeDefinitionRule
{
	@NotNull public static final MultipleTypeDefinitionRule Lax = new MultipleTypeDefinitionRule(false, "");

	@NotNull
	public static MultipleTypeDefinitionRule FileSystem(@NotNull final String javaClassFileNameWithoutExtension)
	{
		return new MultipleTypeDefinitionRule(true, javaClassFileNameWithoutExtension);
	}

	private final boolean canNotHaveMultiplePublicTypes;
	@NotNull private final String javaSourceFileNameWithoutExtension;

	public MultipleTypeDefinitionRule(final boolean canNotHaveMultiplePublicTypes, @NotNull final String javaSourceFileNameWithoutExtension)
	{
		this.canNotHaveMultiplePublicTypes = canNotHaveMultiplePublicTypes;
		this.javaSourceFileNameWithoutExtension = javaSourceFileNameWithoutExtension;
	}

	public void secondPublicTypeEncountered()
	{
		if (canNotHaveMultiplePublicTypes)
		{
			throw new IllegalStateException("Multiple public types are not allowed");
		}
	}

	public void finish(@NotNull final Set<String> encounteredTopLevelTypeNames)
	{
		if (javaSourceFileNameWithoutExtension.isEmpty())
		{
			return;
		}

		if (javaSourceFileNameWithoutExtension.equals("package-info") || javaSourceFileNameWithoutExtension.equals("module-info"))
		{
			if (!encounteredTopLevelTypeNames.isEmpty())
			{
				throw new IllegalStateException("Encountered top level types in a file that should not contain them");
			}
			return;
		}

		if (encounteredTopLevelTypeNames.contains(javaSourceFileNameWithoutExtension))
		{
			return;
		}

		throw new IllegalStateException("No types matching top-level name encountered in java sourec file");
	}
}
