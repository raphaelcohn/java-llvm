package com.stormmq.java.parsing.ast.intermediates.bodyIntermediates;

import com.stormmq.java.parsing.ast.details.bodyDetails.BodyDetail;
import com.stormmq.java.parsing.ast.details.bodyDetails.EmptyBodyDetail;
import org.jetbrains.annotations.NotNull;

public final class EmptyBodyIntermediate implements BodyIntermediate
{
	@NotNull public static final BodyIntermediate Native = new EmptyBodyIntermediate(true, false);
	@NotNull public static final BodyIntermediate Abstract = new EmptyBodyIntermediate(false, true);

	private final boolean isNative;
	private final boolean isAbstract;

	private EmptyBodyIntermediate(final boolean isNative, final boolean isAbstract)
	{
		if (isNative == isAbstract)
		{
			throw new IllegalArgumentException("Can not be both native and abstract or neither");
		}
		this.isNative = isNative;
		this.isAbstract = isAbstract;
	}

	@Override
	public boolean isConcrete()
	{
		return false;
	}

	@Override
	public boolean isAbstract()
	{
		return isAbstract;
	}

	@Override
	public boolean isNative()
	{
		return isNative;
	}

	@NotNull
	@Override
	public BodyDetail resolve()
	{
		return isNative ? EmptyBodyDetail.Native : EmptyBodyDetail.Abstract;
	}
}
