package com.stormmq.java.parsing.adaptors.asm.classReaderUsers;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;

import java.nio.file.Path;

public interface ClassReaderUser
{
	void use(@NotNull final Path relativeFilePath, @NotNull final ClassReader classReader);
}
