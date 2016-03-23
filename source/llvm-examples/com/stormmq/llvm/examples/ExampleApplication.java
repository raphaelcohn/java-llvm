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
import com.stormmq.java.parsing.fileParsers.MultiplePathsParser;
import com.stormmq.jopt.Application;
import com.stormmq.jopt.ExitCode;
import com.stormmq.llvm.examples.parsing.TypeInformationUser;
import com.stormmq.llvm.examples.parsing.WrappingJavaClassFileParser;
import com.stormmq.llvm.examples.parsing.parseFailueLogs.ParseFailureLog;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.LinkedHashSet;

import static com.stormmq.java.parsing.fileParsers.FileParser.DoNothingFileParser;
import static com.stormmq.jopt.ExitCode.ExitCodeGeneralError;
import static com.stormmq.jopt.ExitCode.ExitCodeOk;
import static com.stormmq.llvm.examples.parsing.parseFailueLogs.PrintStreamParseFailureLog.StandardError;

public final class ExampleApplication implements Application
{
	@NotNull private final LinkedHashSet<Path> sourcePaths;
	@NotNull private final ParseFailureLog parseFailureLog;
	@NotNull private final MultiplePathsParser multiplePathsParser;

	public ExampleApplication(@NotNull final LinkedHashSet<Path> sourcePaths)
	{
		this.sourcePaths = sourcePaths;
		parseFailureLog = StandardError();

		final TypeInformationUser typeInformationUser = new TypeInformationUser()
		{
			@Override
			public void use(@NotNull final TypeInformation typeInformation, @NotNull final String relativeFilePath, @NotNull final String sourceRootPath)
			{
			}
		};
		final WrappingJavaClassFileParser javaClassFileParser = new WrappingJavaClassFileParser(parseFailureLog, typeInformationUser);
		multiplePathsParser = new MultiplePathsParser(DoNothingFileParser, javaClassFileParser);
	}

	@Override
	@NotNull
	public ExitCode execute()
	{
		multiplePathsParser.parse(sourcePaths);
		if (parseFailureLog.hasFailures())
		{
			return ExitCodeGeneralError;
		}
		return ExitCodeOk;
	}
}
