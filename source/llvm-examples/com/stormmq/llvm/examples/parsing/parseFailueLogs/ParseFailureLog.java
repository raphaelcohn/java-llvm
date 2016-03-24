// The MIT License (MIT)
//
// Copyright © 2016, Raphael Cohn <raphael.cohn@stormmq.com>
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package com.stormmq.llvm.examples.parsing.parseFailueLogs;

import com.stormmq.java.classfile.parser.javaClassFileParsers.exceptions.InvalidJavaClassFileException;
import com.stormmq.java.classfile.parser.javaClassFileParsers.exceptions.JavaClassFileContainsDataTooLongToReadException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.*;

public interface ParseFailureLog
{
	@SuppressWarnings("HardcodedFileSeparator")
	@NonNls
	@NotNull
	static String zipPathDetails(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry)
	{
		return zipFile.getName() + "!/" + zipEntry.getName();
	}

	boolean hasFailures();

	int failureCount();

	int successCount();

	void success(@NotNull final String filePath);

	void failureZip(@NotNull final Path zipFilePath, @NotNull final IOException e);

	void failureZip(@NotNull final Path zipFilePath, @NotNull final ZipException e);

	void failure(@NotNull final Path filePath, @NotNull final IOException e);

	void failure(@NotNull final String filePath, @NotNull final InvalidJavaClassFileException e);

	void failure(@NotNull final String filePath, @NotNull final JavaClassFileContainsDataTooLongToReadException e);

	void failure(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry, @NotNull final IOException e);

	void failureJavaClassFileIsTooLarge(@NotNull final String filePath);
}
