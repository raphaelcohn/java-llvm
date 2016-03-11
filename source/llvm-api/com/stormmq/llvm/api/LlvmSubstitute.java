package com.stormmq.llvm.api;

import org.jetbrains.annotations.NotNull;

public @interface LlvmSubstitute
{
	@NotNull Class<?> clazz();

	@NotNull String methodName();

	@NotNull Class<?>[] arguments();
}
