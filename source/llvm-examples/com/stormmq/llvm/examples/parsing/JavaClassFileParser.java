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
import com.stormmq.java.classfile.parser.byteReaders.ByteArrayByteReader;
import com.stormmq.java.classfile.parser.javaClassFileParsers.exceptions.*;
import com.stormmq.llvm.examples.parsing.parseFailueLogs.ParseFailureLog;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.stormmq.java.classfile.parser.JavaClassFileParser.parseJavaClassFile;
import static com.stormmq.llvm.examples.parsing.parseFailueLogs.ParseFailureLog.zipPathDetails;
import static com.stormmq.path.FileAndFolderHelper.relativeToRootPath;
import static java.nio.file.Files.readAllBytes;

public final class JavaClassFileParser implements FileParser
{
	@NotNull private final ParseFailureLog parseFailureLog;
	private final boolean permitConstantsInInstanceFields;
	@NotNull private final TypeInformationUser typeInformationUser;

	public JavaClassFileParser(@NotNull final ParseFailureLog parseFailureLog, final boolean permitConstantsInInstanceFields, @NotNull final TypeInformationUser typeInformationUser)
	{
		this.parseFailureLog = parseFailureLog;
		this.permitConstantsInInstanceFields = permitConstantsInInstanceFields;
		this.typeInformationUser = typeInformationUser;
	}

	@Override
	public void parseFile(@NotNull final Path javaClassFilePath, @NotNull final Path dependencyPath)
	{
		final byte[] fileData;
		try
		{
			fileData = readAllBytes(javaClassFilePath);
		}
		catch (final IOException e)
		{
			parseFailureLog.failure(javaClassFilePath, e);
			return;
		}
		catch (@SuppressWarnings("ErrorNotRethrown") final OutOfMemoryError ignored)
		{
			parseFailureLog.failureJavaClassFileIsTooLarge(javaClassFilePath.toString());
			return;
		}

		final Path relativeFilePath = relativeToRootPath(dependencyPath, javaClassFilePath);
		useFileData(javaClassFilePath.toString(), dependencyPath.toString(), relativeFilePath.toString(), fileData);
	}

	@Override
	public void parseFile(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry, @NotNull final byte[] fileData)
	{
		useFileData(zipPathDetails(zipFile, zipEntry), zipEntry.getName(), zipFile.getName(), fileData);
	}

	private void useFileData(@NotNull final String javaClassFilePath, @NotNull final String sourceRootPath, @NotNull final String relativeFilePath, @NotNull final byte[] fileData)
	{
		final TypeInformation typeInformation;
		try(final ByteArrayByteReader byteReader = new ByteArrayByteReader(fileData))
		{
			try
			{
				typeInformation = parseJavaClassFile(byteReader, permitConstantsInInstanceFields);
			}
			catch (final NotAJavaClassFileException ignored)
			{
				return;
			}
			catch (final InvalidJavaClassFileException e)
			{
				parseFailureLog.failure(javaClassFilePath, e);
				return;
			}
			catch (final JavaClassFileContainsDataTooLongToReadException e)
			{
				parseFailureLog.failure(javaClassFilePath, e);
				return;
			}
		}

		typeInformationUser.use(typeInformation, sourceRootPath, relativeFilePath);
		parseFailureLog.success(javaClassFilePath);
	}
}
