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

package com.stormmq.llvm.examples.parsing.files;

import com.stormmq.java.parsing.fileParsers.FileParser;
import com.stormmq.llvm.examples.parsing.parseFailueLogs.ParseFailureLog;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import static com.stormmq.path.IsFileTypeFilter.isClassFile;

public final class JarOrZipParsableFile implements ParsableFile
{
	@NotNull private final Path zipFilePath;

	public JarOrZipParsableFile(@NotNull final Path zipFilePath)
	{
		this.zipFilePath = zipFilePath;
	}

	@Override
	public void process(@NotNull final FileParser fileParser, @NotNull final ParseFailureLog parseFailureLog)
	{
		try (final ZipFile zipFile = new ZipFile(zipFilePath.toFile()))
		{
			if (zipFile.size() == 0)
			{
				return;
			}

			zipFile.stream().filter((zipEntry) -> !zipEntry.isDirectory() && isClassFile(zipEntry.getName())).forEach(zipEntry -> fileParser.parseFile(zipFile, zipEntry));
		}
		catch (final ZipException e)
		{
			parseFailureLog.failureZip(zipFilePath, e);
		}
		catch (final IOException e)
		{
			parseFailureLog.failureZip(zipFilePath, e);
		}
	}
}
