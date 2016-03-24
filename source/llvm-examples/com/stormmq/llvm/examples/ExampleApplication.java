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

package com.stormmq.llvm.examples;

import com.stormmq.jopt.Application;
import com.stormmq.jopt.ExitCode;
import com.stormmq.llvm.examples.parsing.Coordination;
import com.stormmq.llvm.examples.parsing.EnqueuePathsWalker;
import com.stormmq.llvm.examples.parsing.fileParsers.FileParser;
import com.stormmq.llvm.examples.parsing.fileParsers.JavaClassFileParser;
import com.stormmq.llvm.examples.parsing.files.ParsableFile;
import com.stormmq.llvm.examples.parsing.parseFailueLogs.ParseFailureLog;
import com.stormmq.llvm.examples.parsing.typeInformationUsers.NaiveTypeInformationUser;
import com.stormmq.llvm.examples.parsing.typeInformationUsers.TypeInformationUser;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.stormmq.jopt.ExitCode.ExitCodeGeneralError;
import static com.stormmq.jopt.ExitCode.ExitCodeOk;
import static com.stormmq.llvm.examples.parsing.parseFailueLogs.PrintStreamParseFailureLog.StandardError;
import static java.lang.System.err;
import static java.lang.System.out;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.deleteIfExists;
import static java.util.Locale.ENGLISH;

public final class ExampleApplication implements Application
{
	@NotNull private final LinkedHashSet<Path> sourcePaths;
	@NotNull private final Path outputPath;
	@NotNull private final ParseFailureLog parseFailureLog;
	@NotNull private final EnqueuePathsWalker multiplePathsParser;

	public ExampleApplication(@NotNull final LinkedHashSet<Path> sourcePaths, @NotNull final Path outputPath, final boolean permitConstantsInInstanceFields)
	{
		this.sourcePaths = sourcePaths;
		this.outputPath = outputPath;
		parseFailureLog = StandardError();

		final TypeInformationUser typeInformationUser = new NaiveTypeInformationUser(outputPath);
		final FileParser javaClassFileParser = new JavaClassFileParser(parseFailureLog, permitConstantsInInstanceFields, typeInformationUser);
		final ConcurrentLinkedQueue<ParsableFile> parsableFileQueue = new ConcurrentLinkedQueue<>();

		// Can easily become saturated with JAR file processing
		final Coordination coordination = new Coordination(16, parsableFileQueue, javaClassFileParser, parseFailureLog);
		multiplePathsParser = new EnqueuePathsWalker(parsableFileQueue, coordination);
	}

	@Override
	@NotNull
	public ExitCode execute()
	{
		if (couldNotRecreateOutputDirectory())
		{
			return ExitCodeGeneralError;
		}
		try
		{
			multiplePathsParser.parse(sourcePaths);
		}
		finally
		{
			final int successCount = parseFailureLog.successCount();
			final int failureCount = parseFailureLog.failureCount();
			final int total = successCount + failureCount;
			//noinspection HardCodedStringLiteral
			out.printf(ENGLISH, "Success: %1$s.  Failure: %2$s.  Total: %3$s%n.", successCount, failureCount, total);
		}
		if (parseFailureLog.hasFailures())
		{
			return ExitCodeGeneralError;
		}
		return ExitCodeOk;
	}

	public boolean couldNotRecreateOutputDirectory()
	{
		try
		{
			deleteIfExists(outputPath);
		}
		catch (final IOException ignored)
		{
			// Lots of legitimate reasons; may be like $HOME for instance (can't delete)
		}

		try
		{
			createDirectories(outputPath);
		}
		catch (final IOException e)
		{
			//noinspection HardCodedStringLiteral
			err.printf(ENGLISH, "Could not create output directory (or one of its parents) because of '%1$s'%n", e.getMessage());
			return true;
		}

		return false;
	}
}
