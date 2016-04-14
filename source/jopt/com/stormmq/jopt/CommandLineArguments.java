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

package com.stormmq.jopt;

import joptsimple.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;

import static com.stormmq.jopt.CommandLineArgumentsParser.help;
import static com.stormmq.jopt.ExitCode.*;
import static com.stormmq.jopt.Verbosity.None;
import static com.stormmq.path.IsSubFolderFilter.IsSubFolder;
import static com.stormmq.string.Formatting.formatPrintLineAndFlushWhilstSynchronized;
import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.*;
import static java.nio.file.Paths.get;

final class CommandLineArguments
{
	@NotNull
	public static IllegalStateException newShouldHaveExited(@SuppressWarnings("UnusedParameters") @NotNull final Throwable cause)
	{
		return newShouldHaveExited();
	}

	@NotNull
	static IllegalStateException newShouldHaveExited()
	{
		return new IllegalStateException("Should have exited");
	}

	@NotNull private final OptionParser optionParser;
	@NotNull private final PrintStream out;
	@NotNull private final PrintStream error;
	@NotNull private final OptionSet arguments;

	CommandLineArguments(@NotNull final OptionParser optionParser, @NotNull final PrintStream out, @NotNull final PrintStream error, @SuppressWarnings("TypeMayBeWeakened") @NotNull final Set<String> requiredOptions, @NonNls@NotNull final String... commandLineArguments)
	{
		this.optionParser = optionParser;
		this.out = out;
		this.error = error;

		arguments = parse(requiredOptions, commandLineArguments);
	}

	@NotNull
	private OptionSet parse(@NotNull final Iterable<String> requiredOptions, @NonNls @NotNull final String... commandLineArguments)
	{
		final OptionSet arguments;
		try
		{
			arguments = optionParser.parse(commandLineArguments);
		}
		catch (final OptionException e)
		{
			printErrorMessageShowHelpAndExit(Usage, e.getMessage());
			throw newShouldHaveExited(e);
		}

		if (arguments.has(help))
		{
			printHelpAndExit(Success);
			throw newShouldHaveExited();
		}

		for (final String requiredOption : requiredOptions)
		{
			if (!arguments.has(requiredOption))
			{
				printErrorMessageShowHelpAndExitWithUsageError("Missing required option --%1$s", requiredOption);
				throw newShouldHaveExited();
			}
		}

		return arguments;
	}

	@NotNull
	public Path extantWritableFolderPathOptionValue(@NotNull final String optionName)
	{
		final Path potentialWritableFolderPath = pathOptionValue(optionName);
		extantWritableFolderPath(optionName, potentialWritableFolderPath);
		return potentialWritableFolderPath;
	}

	@NotNull
	public Path creatableFolderPathOptionValue(@NotNull final String optionName)
	{
		final Path potentialWritableFolderPath = pathOptionValue(optionName);
		if (exists(potentialWritableFolderPath))
		{
			if (!IsSubFolder.accept(potentialWritableFolderPath) || !isWritable(potentialWritableFolderPath))
			{
				printErrorMessageShowHelpAndExit(NoInput, "Option --%1$s is extant but not a readable, writable sub-folder path (%2$s)", optionName, potentialWritableFolderPath.toString());
			}
		}
		else
		{
			try
			{
				createDirectories(potentialWritableFolderPath);
			}
			catch (final IOException ignored)
			{
				printErrorMessageShowHelpAndExit(CanNotCreate, "Option --%1$s could not be created (%2$s)", optionName, potentialWritableFolderPath.toString());
			}
		}

		return potentialWritableFolderPath;
	}

	@SuppressWarnings("CollectionDeclaredAsConcreteClass")
	@NotNull
	public List<Path> extantWritableFolderPathsOptionValue(@NotNull final String optionName)
	{
		@SuppressWarnings("unchecked") final Collection<String> rawValues = (List<String>) arguments.valuesOf(optionName);
		final List<Path> paths = new ArrayList<>(rawValues.size());
		rawValues.stream().map(rawValue ->
		{
			final Path rawPath = get(rawValue);
			final Path absolutePath;
			try
			{
				absolutePath = rawPath.toAbsolutePath();
			}
			catch (final InvalidPathException ignored)
			{
				printErrorMessageShowHelpAndExit(NoInput, "Option --%1$s is an invalid writable path (%2$s)", optionName, rawValue);
				throw newShouldHaveExited();
			}
			return extantWritableFolderPath(optionName, absolutePath);
		}).forEach(paths::add);
		return paths;
	}

	@NotNull
	public Path pathOptionValue(@NotNull final String optionName)
	{
		final String rawValue = (String) arguments.valueOf(optionName);
		try
		{
			return get(rawValue).toAbsolutePath();
		}
		catch (final InvalidPathException ignored)
		{
				printErrorMessageShowHelpAndExit(NoInput, "Option --%1$s is an invalid path (%2$s)", optionName, rawValue);
			throw newShouldHaveExited();
		}
	}

	@NotNull
	public Charset charsetOptionValue(@NotNull final String optionName)
	{
		final String rawValue = (String) arguments.valueOf(optionName);
		try
		{
			return forName(rawValue);
		}
		catch (final IllegalCharsetNameException | UnsupportedCharsetException ignored)
		{
			printErrorMessageShowHelpAndExitWithUsageError("Option --%1$s is an invalid charset (%2$s)", optionName, rawValue);
			throw newShouldHaveExited();
		}
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public <E extends Enum<E>> E enumOptionValue(@NotNull final String optionName, @NotNull final Class<E> declaringClass)
	{
		return (E) arguments.valueOf(optionName);
	}

	@NotNull
	public Verbosity verbosityOptionValue(@NonNls final String optionName)
	{
		if (arguments.has(optionName))
		{
			return (Verbosity) arguments.valueOf(optionName);
		}
		return None;
	}

	@NotNull
	private Path extantWritableFolderPath(@NotNull final String optionName, @NotNull final Path potentialWritableFolderPath)
	{
		if (!exists(potentialWritableFolderPath) || !IsSubFolder.accept(potentialWritableFolderPath) || !isWritable(potentialWritableFolderPath))
		{
			printErrorMessageShowHelpAndExit(NoInput, "Option --%1$s is not a readable, writable sub-folder path (%2$s)", optionName, potentialWritableFolderPath.toString());
			throw newShouldHaveExited();
		}
		return potentialWritableFolderPath;
	}

	private void printErrorMessageShowHelpAndExitWithUsageError(@NonNls @NotNull final String errorLineTemplateWithoutLineEnding, @NotNull final Object... arguments)
	{
		printErrorMessageShowHelpAndExit(Usage, errorLineTemplateWithoutLineEnding, arguments);
	}

	private void printErrorMessageShowHelpAndExit(@NotNull final ExitCode exitCode, @NonNls @NotNull final String errorLineTemplateWithoutLineEnding, @NotNull final Object... arguments)
	{
		printErrorLine(errorLineTemplateWithoutLineEnding, arguments);
		printHelpAndExit(exitCode);
	}

	private void printErrorLine(@NonNls @NotNull final String errorLineTemplateWithoutLineEnding, @NotNull final Object... arguments)
	{
		formatPrintLineAndFlushWhilstSynchronized(error, errorLineTemplateWithoutLineEnding, arguments);
	}

	private void printHelpAndExit(@NotNull final ExitCode exitCode)
	{
		try
		{
			optionParser.printHelpOn(exitCode == Success ? out : error);
		}
		catch (final IOException e)
		{
			printErrorLine("Unexpected failure printing help %1$s", e.getMessage());
			Failure.exit();
			throw newShouldHaveExited(e);
		}
		exitCode.exit();
		throw newShouldHaveExited();
	}
}
