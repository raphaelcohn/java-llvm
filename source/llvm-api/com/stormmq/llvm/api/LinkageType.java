package com.stormmq.llvm.api;

import org.jetbrains.annotations.NotNull;

public enum LinkageType
{
	_private,
	internal,
	available_externally,
	linkonce,
	weak,
	common,
	appending,
	extern_weak,
	linkonce_odr,
	weak_odr,
	external,
	;

	@NotNull
	private final String name;

	LinkageType()
	{
		final String name = name();
		if (name.charAt(0) == '_')
		{
			this.name = name.substring(1);
		}
		else
		{
			this.name = name;
		}
	}
}
