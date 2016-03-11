package com.stormmq.java.parsing.adaptors.javaparser.helpers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.stormmq.java.parsing.utilities.ReservedIdentifiers.validateIsJavaIdentifier;
import static java.util.Collections.emptyList;

public final class JavaparserHelper
{
	private JavaparserHelper()
	{
	}

	@NotNull
	public static <T> List<T> nullToEmptyList(@Nullable final List<T> values)
	{
		if (values == null)
		{
			return emptyList();
		}
		return values;
	}

	@NotNull
	public static <T> T assertNotNull(@Nullable final T value)
	{
		if (value == null)
		{
			throw new AssertionError("Null values are not allowed");
		}
		return value;
	}

	@NotNull
	public static String validateName(@Nullable final String name, final boolean canBeInitializerName)
	{
		final String notNullName = assertNotNull(name);
		validateIsJavaIdentifier(notNullName, canBeInitializerName);
		return notNullName;
	}
}
