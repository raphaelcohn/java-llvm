package com.stormmq.llvm.api;

import org.jetbrains.annotations.NotNull;

public enum VisibilityStyle
{
	_default,
	hidden,
	_protected,
	;

	@NotNull
	private final String name;

	VisibilityStyle()
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
