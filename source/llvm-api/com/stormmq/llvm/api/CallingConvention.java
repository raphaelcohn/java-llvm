package com.stormmq.llvm.api;

import org.jetbrains.annotations.NotNull;

public enum CallingConvention
{
	ccc,
	fastcc,
	coldcc,
	cc10("cc 10"),
	cc11("cc 11"),
	webkit_jscc,
	anyregcc,
	preserve_mostcc,
	preserve_allcc,
	cxx_fast_tlscc,
	;

	@NotNull
	private final String name;

	private CallingConvention()
	{
		this.name = this.name();
	}

	private CallingConvention(@NotNull final String name)
	{
		this.name = name;
	}
}
