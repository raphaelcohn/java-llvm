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

package com.stormmq.java.llvm.examples;

import com.stormmq.java.classfile.processing.*;
import com.stormmq.java.classfile.processing.processLogs.ProcessLog;
import com.stormmq.java.llvm.xxx.*;
import com.stormmq.jopt.Verbosity;
import com.stormmq.jopt.applications.AbstractMultithreadedApplication;
import com.stormmq.jopt.applications.uncaughtExceptionHandlers.MustExitBecauseOfFailureException;
import com.stormmq.llvm.domain.module.ModuleWriter;
import org.jetbrains.annotations.NotNull;

import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.Path;

import static com.stormmq.java.classfile.processing.processLogs.PrintStreamProcessLog.standardErrorParseFailureLog;
import static com.stormmq.llvm.domain.module.TargetModuleCreator.MacOsXMavericksX86_64;

public final class ExampleApplication extends AbstractMultithreadedApplication
{
	@NotNull private final Iterable<Path> sourcePaths;
	@NotNull private final Path outputPath;
	@NotNull private final Processor processor;
	@NotNull private final TypeInformationTripletUser typeInformationTripletUser;

	public ExampleApplication(@NotNull final UncaughtExceptionHandler delegate, @NotNull final Verbosity verbosity, @NotNull final Iterable<Path> sourcePaths, @NotNull final Path outputPath, final boolean permitConstantsInInstanceFields)
	{
		super(delegate);

		this.sourcePaths = sourcePaths;
		this.outputPath = outputPath;

		final ProcessLog processLog = new ExitCodeSettingProcessLog(standardErrorParseFailureLog(verbosity.isAtLeastVerbose()), exitCode);
		processor = new Processor(permitConstantsInInstanceFields, processLog, exitCodeSettingUncaughtExceptionHandler);
		typeInformationTripletUser = new JavaConvertingTypeInformationTripletUser(new ModuleCreatorAndWriter(MacOsXMavericksX86_64, new ModuleWriter(outputPath)));
	}

	@Override
	protected void executeInternally() throws MustExitBecauseOfFailureException
	{
		recreateOutputPath(outputPath);

		final Records records = processor.process(sourcePaths);

		if (mustExitBecauseOfFailure())
		{
			return;
		}

		records.iterate(typeInformationTripletUser);
	}
}
