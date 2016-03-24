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

import com.stormmq.java.classfile.domain.information.TypeInformation;
import com.stormmq.java.classfile.parser.JavaClassFileParser;
import com.stormmq.java.classfile.parser.byteReaders.ByteArrayByteReader;
import com.stormmq.java.classfile.parser.byteReaders.InputStreamByteReader;
import com.stormmq.java.classfile.parser.javaClassFileParsers.exceptions.*;
import com.stormmq.java.parsing.fileParsers.FileParser;
import com.stormmq.llvm.examples.parsing.parseFailueLogs.ParseFailureLog;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.nio.file.Files.readAllBytes;

public final class WrappingJavaClassFileParser implements FileParser
{
	private static final int SixtyFourKilobytes = 65536;
	private static final int OneMegabyte = 1048576;

	@NotNull private final ParseFailureLog parseFailureLog;
	private final boolean permitConstantsInInstanceFields;
	@NotNull private final TypeInformationUser typeInformationUser;

	public WrappingJavaClassFileParser(@NotNull final ParseFailureLog parseFailureLog, final boolean permitConstantsInInstanceFields, @NotNull final TypeInformationUser typeInformationUser)
	{
		this.parseFailureLog = parseFailureLog;
		this.permitConstantsInInstanceFields = permitConstantsInInstanceFields;
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
			parseFailureLog.failure(filePath, e);
			return;
		}
		catch (@SuppressWarnings("ErrorNotRethrown") final OutOfMemoryError ignored)
		{
			// Java class file exceeds 2Gb. This is exceedingly unlikely, and can be fixed by switching to a less efficient InputStreamByteReader
			parseFailureLog.failure(filePath, new JavaClassFileContainsDataTooLongToReadException());
			return;
		}

		final TypeInformation typeInformation;
		try(final ByteArrayByteReader byteReader = new ByteArrayByteReader(byteArray))
		{
			try
			{
				typeInformation = JavaClassFileParser.parseJavaClassFile(byteReader, permitConstantsInInstanceFields);
			}
			catch (final NotAJavaClassFileException ignored)
			{
				return;
			}
			catch (final InvalidJavaClassFileException e)
			{
				parseFailureLog.failure(filePath, e);
				return;
			}
			catch (final JavaClassFileContainsDataTooLongToReadException e)
			{
				parseFailureLog.failure(filePath, e);
				return;
			}
		}
		parseFailureLog.success(filePath);
		typeInformationUser.use(typeInformation, sourceRootPath.toString(), filePath.toString());
	}

	@Override
	public void parseFile(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry)
	{
		final TypeInformation typeInformation;
		try(final InputStream inputStream = zipFile.getInputStream(zipEntry))
		{
			try(final InputStreamByteReader byteReader = new InputStreamByteReader(new BufferedInputStream(inputStream, optimalZipEntryBufferSize(zipEntry))))
			{
				try
				{
					typeInformation = JavaClassFileParser.parseJavaClassFile(byteReader, permitConstantsInInstanceFields);
				}
				catch (final NotAJavaClassFileException ignored)
				{
					return;
				}
				catch (final InvalidJavaClassFileException e)
				{
					parseFailureLog.failure(zipFile, zipEntry, e);
					return;
				}
				catch (final JavaClassFileContainsDataTooLongToReadException e)
				{
					parseFailureLog.failure(zipFile, zipEntry, e);
					return;
				}
			}
			catch (final IOException e)
			{
				parseFailureLog.failure(zipFile, zipEntry, e);
				return;
			}
		}
		catch (final IOException e)
		{
			parseFailureLog.failure(zipFile, zipEntry, e);
			return;
		}

		parseFailureLog.success(zipFile, zipEntry);
		typeInformationUser.use(typeInformation, zipFile.getName(), zipEntry.getName());
	}

	public static int optimalZipEntryBufferSize(@NotNull final ZipEntry zipEntry)
	{
		final long size = zipEntry.getSize();

		if (size == -1)
		{
			return SixtyFourKilobytes;
		}

		if (size < OneMegabyte)
		{
			//noinspection NumericCastThatLosesPrecision
			return (int) size;
		}

		return OneMegabyte;
	}
}
