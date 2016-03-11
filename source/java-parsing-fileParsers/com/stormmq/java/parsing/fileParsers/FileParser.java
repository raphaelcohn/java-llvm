package com.stormmq.java.parsing.fileParsers;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public interface FileParser
{
	void parseFile(@NotNull final Path filePath, @NotNull final Path sourceRootPath);

	void parseFile(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry);
}
