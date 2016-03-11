package com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.compilationUnitWrapperUsers;

import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.CompilationUnitWrapper;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface CompilationUnitWrapperUser
{
	void use(@NotNull final Path relativeFilePath, @NotNull final CompilationUnitWrapper compilationUnitWrapper);
}
