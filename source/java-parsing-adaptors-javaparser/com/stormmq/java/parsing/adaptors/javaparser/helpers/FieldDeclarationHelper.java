package com.stormmq.java.parsing.adaptors.javaparser.helpers;

import com.github.javaparser.ast.body.FieldDeclaration;
import org.jetbrains.annotations.NotNull;

import static com.github.javaparser.ast.body.ModifierSet.*;

public final class FieldDeclarationHelper
{
	private FieldDeclarationHelper()
	{
	}

	public static int fieldModifiers(@NotNull final FieldDeclaration fieldDeclaration)
	{
		final int modifiers = fieldDeclaration.getModifiers();

		if (isAbstract(modifiers))
		{
			throw new IllegalArgumentException("A field can not be abstract");
		}

		if (isStrictfp(modifiers))
		{
			throw new IllegalArgumentException("A field can not be strictfp");
		}

		if (isNative(modifiers))
		{
			throw new IllegalArgumentException("A field can not be native");
		}

		if (isSynchronized(modifiers))
		{
			throw new IllegalArgumentException("A field can not be synchronized");
		}

		if (isFinal(modifiers) && isVolatile(modifiers))
		{
			throw new IllegalArgumentException("A field can not be volatile and final");
		}

		return modifiers;
	}
}
