package com.stormmq.llvm.api;

public enum ThreadLocalStorageModel
{
	localdynamic,
	initialexec,
	localexec,
	none,
	;

	public boolean isNeverPartOfLLvmIrCode()
	{
		return this == none;
	}
}
