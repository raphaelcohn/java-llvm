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

import com.stormmq.java.classfile.domain.information.TypeInformation;
import com.stormmq.jopt.Application;
import com.stormmq.jopt.ExitCode;
import com.stormmq.llvm.examples.parsing.*;
import com.stormmq.llvm.examples.parsing.files.ParsableFile;
import com.stormmq.llvm.examples.parsing.parseFailueLogs.ParseFailureLog;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.stormmq.jopt.ExitCode.ExitCodeGeneralError;
import static com.stormmq.jopt.ExitCode.ExitCodeOk;
import static com.stormmq.llvm.examples.parsing.parseFailueLogs.PrintStreamParseFailureLog.StandardError;

public final class ExampleApplication implements Application
{
	@NotNull private final LinkedHashSet<Path> sourcePaths;
	@NotNull private final ParseFailureLog parseFailureLog;
	@NotNull private final EnqueuePathsWalker multiplePathsParser;

	public ExampleApplication(@NotNull final LinkedHashSet<Path> sourcePaths, final boolean permitConstantsInInstanceFields)
	{
		this.sourcePaths = sourcePaths;
		parseFailureLog = StandardError();

		final TypeInformationUser typeInformationUser = new TypeInformationUser()
		{
			@Override
			public void use(@NotNull final TypeInformation typeInformation, @NotNull final String sourceRootPath, @NotNull final String relativeFilePath)
			{
			}
		};
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
		int extra = 1;
		try
		{
			multiplePathsParser.parse(sourcePaths);
			extra = 0;
		}
		finally
		{
			System.out.printf(Locale.ENGLISH, "Success: %1$s\tFailure: %2$s\tTotal: %3$s%n", parseFailureLog.successCount(), parseFailureLog.failureCount() + extra, parseFailureLog.successCount() + parseFailureLog.failureCount() + extra);
		}
		if (parseFailureLog.hasFailures())
		{
			return ExitCodeGeneralError;
		}
		return ExitCodeOk;
	}
}
