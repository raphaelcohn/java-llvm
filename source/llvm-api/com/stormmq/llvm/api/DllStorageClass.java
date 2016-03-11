package com.stormmq.llvm.api;

public enum DllStorageClass
{
	dllimport,
	dllexport,
	none,
	;

	public boolean isNeverPartOfLLvmIrCode()
	{
		return this == none;
	}
}
