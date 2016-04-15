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

import com.stormmq.applications.Application;
import com.stormmq.applications.Verbosity;
import com.stormmq.commandLineArgumentsParsing.CommandLineArgumentsParser;
import com.stormmq.jopt.JoptSimpleCommandLineArgumentsParser;
import com.stormmq.llvm.domain.module.TargetModuleCreator;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

import static com.stormmq.applications.Application.run;
import static com.stormmq.applications.TimedApplication.standardErrorReportingTimedApplication;
import static com.stormmq.applications.Verbosity.Everything;
import static com.stormmq.applications.uncaughtExceptionHandlers.PrintStreamUncaughtExceptionHandler.StandardErrorUncaughtExceptionHandler;
import static com.stormmq.jopt.JoptSimpleCommandLineArgumentsParser.jopSimpleCommandLineArgumentsParser;
import static com.stormmq.llvm.domain.module.TargetModuleCreator.MacOsXMavericksX86_64;
import static com.stormmq.path.Constants.CurrentFolder;

public final class ConsoleEntryPoint
{
	@SuppressWarnings("HardcodedFileSeparator")
	public static void main(@NotNull @NonNls final String... commandLineArguments)
	{
		final CommandLineArgumentsParser commandLineArgumentsParser = jopSimpleCommandLineArgumentsParser(commandLineArguments);
		final Supplier<Verbosity> verbosityOption = commandLineArgumentsParser.verboseOption();
		final Supplier<List<Path>> sourceOption = commandLineArgumentsParser.extantWritableFolderPathsOption("source", "source root path", "/path/to/source", CurrentFolder);
		final Supplier<Path> outputPathOption = commandLineArgumentsParser.creatableFolderPathOption("output", "output folder path, created if doesn't exist", "/path/to/output", "./out/llvm");
		final Supplier<TargetModuleCreator> targetModuleCreatorOption = commandLineArgumentsParser.enumOption("target", "target details", MacOsXMavericksX86_64);

		final UncaughtExceptionHandler uncaughtExceptionHandler = StandardErrorUncaughtExceptionHandler;
		final Verbosity chosenVerbosity = verbosityOption.get();
		final List<Path> sourcePaths = sourceOption.get();
		final Path outputPath = outputPathOption.get();
		final TargetModuleCreator targetModuleCreator = targetModuleCreatorOption.get();

		final Application application = new ExampleApplication(uncaughtExceptionHandler, chosenVerbosity, sourcePaths, outputPath,  true, targetModuleCreator);
		final Application applicationToExecute = chosenVerbosity == Everything ? standardErrorReportingTimedApplication(application) : application;
		run(applicationToExecute, uncaughtExceptionHandler);
	}

	private ConsoleEntryPoint()
	{
	}
}
