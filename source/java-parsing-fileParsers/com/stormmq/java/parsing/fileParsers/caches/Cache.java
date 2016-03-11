package com.stormmq.java.parsing.fileParsers.caches;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Cache<V>
{
	void cache(@NotNull final String filePathName, @NotNull final V parsedForm);

	@Nullable
	V retrieve(@NotNull final String filePathName);

}
