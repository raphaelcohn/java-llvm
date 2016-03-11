package com.stormmq.java.parsing.ast.details.fieldValueDetails;

import org.jetbrains.annotations.NotNull;

public final class StringInvariantStaticFieldValueDetail implements InvariantStaticFieldValueDetail
{
	@NotNull private final String value;

	public StringInvariantStaticFieldValueDetail(@NotNull final String value)
	{
		this.value = value;
	}
}
