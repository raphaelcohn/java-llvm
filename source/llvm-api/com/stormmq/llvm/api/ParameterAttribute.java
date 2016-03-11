package com.stormmq.llvm.api;

public enum ParameterAttribute
{
	zeroext,
	signext,
	inreg,
	byval(true),
	inalloca,
	sret,
	noalias(true),
	nocapture(true),
	nest(true),
	returned, // useful for humane interfaces
	nonnull, // useful with any pointer
	//deferenceable
	//derefenceable_or_null
	;

	// TODO: align 8 / 16 / 24 / 32 / 48 / 64 / 128, etc

	private final boolean isPointerOnly;

	private ParameterAttribute()
	{
		this.isPointerOnly = false;
	}

	private ParameterAttribute(final boolean isPointerOnly)
	{
		this.isPointerOnly = isPointerOnly;
	}
}
