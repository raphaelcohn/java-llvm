package com.stormmq.java.parsing.ast.details.bodyDetails;

import org.jetbrains.annotations.NotNull;

public final class EmptyBodyDetail implements BodyDetail
{
	@NotNull public static final BodyDetail Native = new EmptyBodyDetail(true, false);
	@NotNull public static final BodyDetail Abstract = new EmptyBodyDetail(false, true);

	private final boolean isNative;
	private final boolean isAbstract;

	private EmptyBodyDetail(final boolean isNative, final boolean isAbstract)
	{
		if (isNative == isAbstract)
		{
			throw new IllegalArgumentException("Can not be both native and abstract or neither");
		}
		this.isNative = isNative;
		this.isAbstract = isAbstract;
	}

	@Override
	public boolean isNative()
	{
		return isNative;
	}
}
