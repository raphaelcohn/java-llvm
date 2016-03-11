package com.stormmq.nio;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface PathUser
{
	void use(@NotNull final Path path);
}
