package com.stormmq.java.parsing.adaptors.asm.ast;

import com.stormmq.java.parsing.ast.details.bodyDetails.BodyDetail;
import com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.BodyIntermediate;
import org.jetbrains.annotations.NotNull;

public final class AsmBodyIntermediate implements BodyIntermediate
{
	public AsmBodyIntermediate()
	{
	}

	@Override
	public boolean isConcrete()
	{
		return true;
	}

	@Override
	public boolean isAbstract()
	{
		return false;
	}

	@Override
	public boolean isNative()
	{
		return false;
	}

	@NotNull
	@Override
	public BodyDetail resolve()
	{
		return new BodyDetail()
		{
			@Override
			public boolean isNative()
			{
				return false;
			}
		};
	}
}
