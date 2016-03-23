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

package com.stormmq.llvm.examples.parsing;

import com.stormmq.java.classfile.parser.JavaClassFileParser;
import com.stormmq.java.classfile.parser.byteReaders.*;
import com.stormmq.java.classfile.parser.javaClassFileParsers.exceptions.InvalidJavaClassFileException;
import com.stormmq.java.classfile.parser.javaClassFileParsers.exceptions.JavaClassFileContainsDataTooLongToReadException;
import com.stormmq.java.parsing.fileParsers.FileParser;
import com.stormmq.llvm.examples.parsing.parseFailueLogs.ParseFailureLog;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.nio.file.Files.readAllBytes;

public final class WrappingJavaClassFileParser implements FileParser
{
	@NotNull private final ParseFailureLog parseFailureLog;
	@NotNull private final TypeInformationUser typeInformationUser;

	public WrappingJavaClassFileParser(@NotNull final ParseFailureLog parseFailureLog, @NotNull final TypeInformationUser typeInformationUser)
	{
		this.parseFailureLog = parseFailureLog;
		this.typeInformationUser = typeInformationUser;
	}

	@Override
	public void parseFile(@NotNull final Path filePath, @NotNull final Path sourceRootPath)
	{
		final byte[] byteArray;
		try
		{
			byteArray = readAllBytes(filePath);
		}
		catch (final IOException e)
		{
			parseFailureLog.log(filePath, e);
			return;
		}
		try(final ByteReader byteReader = new ByteArrayByteReader(byteArray))
		{
			try
			{
				typeInformationUser.use(JavaClassFileParser.parseJavaClassFile(byteReader), filePath.toString(), sourceRootPath.toString());
			}
			catch (final InvalidJavaClassFileException e)
			{
				parseFailureLog.log(filePath, e);
			}
			catch (final JavaClassFileContainsDataTooLongToReadException e)
			{
				parseFailureLog.log(filePath, e);
			}
		}
		catch (final IOException e)
		{
			throw new IllegalStateException("Should not happen", e);
		}
	}

	@Override
	public void parseFile(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry)
	{
		try(final InputStream inputStream = zipFile.getInputStream(zipEntry))
		{
			try(final InputStreamByteReader byteReader = new InputStreamByteReader(inputStream))
			{
				try
				{
					typeInformationUser.use(JavaClassFileParser.parseJavaClassFile(byteReader), zipEntry.getName(), zipFile.getName());
				}
				catch (final InvalidJavaClassFileException e)
				{
					parseFailureLog.log(zipFile, zipEntry, e);
				}
				catch (final JavaClassFileContainsDataTooLongToReadException e)
				{
					parseFailureLog.log(zipFile, zipEntry, e);
				}
			}
			catch (final IOException e)
			{
				parseFailureLog.log(zipFile, zipEntry, e);
			}
		}
		catch (final IOException e)
		{
			parseFailureLog.log(zipFile, zipEntry, e);
		}
	}
}
