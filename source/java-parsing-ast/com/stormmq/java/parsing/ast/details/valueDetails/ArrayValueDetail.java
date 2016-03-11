package com.stormmq.java.parsing.ast.details.valueDetails;

import org.jetbrains.annotations.NotNull;

public final class ArrayValueDetail<D extends SingleValueDetail> implements ValueDetail
{
	@NotNull private final D[] singletonDefaultValueDetails;

	public ArrayValueDetail(@NotNull final D[] singletonDefaultValueDetails)
	{
		this.singletonDefaultValueDetails = singletonDefaultValueDetails;
	}
}
