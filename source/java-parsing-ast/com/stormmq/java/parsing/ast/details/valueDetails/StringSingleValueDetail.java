package com.stormmq.java.parsing.ast.details.valueDetails;

import org.jetbrains.annotations.NotNull;

public final class StringSingleValueDetail implements SingleValueDetail
{
	@NotNull private final String value;

	public StringSingleValueDetail(@NotNull final String value)
	{
		this.value = value;
	}
}
