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

import java.io.*;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.lang.System.err;
import static java.util.Locale.ENGLISH;

public final class PrintStreamParseFailureLog implements ParseFailureLog
{
	@SuppressWarnings("UseOfSystemOutOrSystemErr")
	@NotNull public static ParseFailureLog StandardError()
	{
		return new PrintStreamParseFailureLog(err);
	}

	@NotNull private final PrintStream printStream;
	private int failureCount;
	private int successCount;

	public PrintStreamParseFailureLog(@NotNull final PrintStream printStream)
	{
		this.printStream = printStream;
		failureCount = 0;
		successCount = 0;
	}

	@Override
	public boolean hasFailures()
	{
		return failureCount != 0;
	}

	@Override
	public int failureCount()
	{
		return failureCount;
	}

	@Override
	public int successCount()
	{
		return successCount;
	}

	@Override
	public void success(@NotNull final Path filePath)
	{
		successCount++;
	}

	@Override
	public void success(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry)
	{
		successCount++;
	}

	@Override
	public void failure(@NotNull final Path filePath, @NotNull final IOException e)
	{
		failure("File '%1$s' on disk could not be read because of an input error '%2$s'", filePath, e.getMessage());
	}

	@Override
	public void failure(@NotNull final Path filePath, @NotNull final InvalidJavaClassFileException e)
	{
		failure("File '%1$s' on disk could not be parsed because it is an invalid Java class file ('%2$s')", filePath, e.getMessage());
	}

	@Override
	public void failure(@NotNull final Path filePath, @NotNull final JavaClassFileContainsDataTooLongToReadException e)
	{
		failure("File '%1$s' on disk could not be parsed because it contains data that is too long to read ('%2$s'); this is extremely unusual", filePath, e.getMessage());
	}

	@Override
	public void failure(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry, @NotNull final IOException e)
	{
		failure("File '%1$s' in zip archive could not be read because of an input error '%2$s'", zipPathDetails(zipFile, zipEntry), e.getMessage());
	}

	@Override
	public void failure(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry, @NotNull final InvalidJavaClassFileException e)
	{
		failure("File '%1$s' in zip archive could not be parsed because it is an invalid Java class file ('%2$s')", zipPathDetails(zipFile, zipEntry), e.getMessage());
	}

	@Override
	public void failure(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry, @NotNull final JavaClassFileContainsDataTooLongToReadException e)
	{
		failure("File '%1$s' in zip archive could not be parsed because it contains data that is too long to read ('%2$s'); this is extremely unusual", zipPathDetails(zipFile, zipEntry), e.getMessage());
	}

	@SuppressWarnings("HardcodedFileSeparator")
	@NonNls
	@NotNull
	private static String zipPathDetails(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry)
	{
		return zipFile.getName() + "!/" + zipEntry.getName();
	}

	@SuppressWarnings({"resource", "SynchronizationOnLocalVariableOrMethodParameter", "OverloadedVarargsMethod"})
	private void failure(@NotNull @NonNls final String template, @NotNull final Object... arguments)
	{
		failureCount++;

		final PrintStream printStream = this.printStream;
		synchronized (printStream)
		{
			printStream.format(ENGLISH, template, arguments);
			printStream.println();
			printStream.flush();
		}
	}
}
