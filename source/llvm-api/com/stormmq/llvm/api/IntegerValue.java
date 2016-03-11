package com.stormmq.llvm.api;

import org.jetbrains.annotations.NotNull;

public class IntegerValue<T extends Number>
{
	@NotNull private final T value;

	public IntegerValue(@NotNull final T value)
	{
		this.value = value;
	}
}
