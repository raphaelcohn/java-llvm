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
import static com.stormmq.jopt.CommandLineArgumentsParser.newShouldHaveExited;
import static com.stormmq.jopt.ExitCode.ExitCodeGeneralError;
import static com.stormmq.jopt.ExitCode.ExitCodeOk;
import static com.stormmq.path.IsSubFolderFilter.IsSubFolder;
import static java.lang.String.format;
import static java.lang.System.exit;
import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.*;
import static java.nio.file.Paths.get;
import static java.util.Locale.ENGLISH;

final class CommandLineArguments
{
	@NotNull private final OptionParser optionParser;
	@NotNull private final PrintStream standardOut;
	@NotNull private final PrintStream standardError;
	@NotNull private final OptionSet arguments;

	CommandLineArguments(@NotNull final OptionParser optionParser, @NotNull final PrintStream standardOut, @NotNull final PrintStream standardError, @NotNull final Set<String> requiredOptions, @NonNls@NotNull final String... commandLineArguments)
	{
		this.optionParser = optionParser;
		this.standardOut = standardOut;
		this.standardError = standardError;

		arguments = parse(requiredOptions, commandLineArguments);
	}

	@NotNull
	private OptionSet parse(@NotNull final Set<String> requiredOptions, @NonNls @NotNull final String... commandLineArguments)
	{
		final OptionSet arguments;
		try
		{
			arguments = optionParser.parse(commandLineArguments);
		}
		catch (final OptionException e)
		{
			printHelp(ExitCodeGeneralError, e.getMessage());
			throw newShouldHaveExited(e);
		}

		if (arguments.has(help))
		{
			printHelp(ExitCodeOk);
			throw newShouldHaveExited();
		}

		for (final String requiredOption : requiredOptions)
		{
			if (!arguments.has(requiredOption))
			{
				printHelp(ExitCodeGeneralError, format(ENGLISH, "Missing required option --%1$s", requiredOption));
				throw newShouldHaveExited();
			}
		}

		return arguments;
	}

	private void printHelp(@NotNull final ExitCode exitCode, @NotNull @NonNls final String errorLineWithoutLineEnding)
	{
		printErrorLine(errorLineWithoutLineEnding);
		printHelp(exitCode);
	}

	private void printHelp(@NotNull final ExitCode exitCode)
	{
		try
		{
			optionParser.printHelpOn(exitCode == ExitCodeOk ? standardOut : standardError);
		}
		catch (final IOException e)
		{
			printErrorLine(format(ENGLISH, "Unexpected failure printing help %1$s", e.getMessage()));
			ExitCodeGeneralError.exit();
			throw newShouldHaveExited(e);
		}
		exitCode.exit();
		throw newShouldHaveExited();
	}

	private void printErrorLine(@NotNull @NonNls final String errorLineWithoutLineEnding)
	{
		standardError.println(errorLineWithoutLineEnding);
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
				printErrorLine(format(ENGLISH, "Option --%1$s is extant but not a readable, writable sub-folder path (%2$s)", optionName, potentialWritableFolderPath.toString()));
				printHelp(ExitCodeGeneralError);
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
				printErrorLine(format(ENGLISH, "Option --%1$s could not be created (%2$s)", optionName, potentialWritableFolderPath.toString()));
				printHelp(ExitCodeGeneralError);
			}
		}

		return potentialWritableFolderPath;
	}

	@SuppressWarnings({"ThrowInsideCatchBlockWhichIgnoresCaughtException", "CollectionDeclaredAsConcreteClass"})
	@NotNull
	public LinkedHashSet<Path> extantWritableFolderPathsOptionValue(@NotNull final String optionName)
	{
		@SuppressWarnings("unchecked") final List<String> rawValues = (List<String>) arguments.valuesOf(optionName);
		final LinkedHashSet<Path> paths = new LinkedHashSet<>(rawValues.size());
		for (final String rawValue : rawValues)
		{
			final Path path;
			try
			{
				path = get(rawValue).toAbsolutePath();
			}
			catch (final InvalidPathException ignored)
			{
				printErrorLine(format(ENGLISH, "Option --%1$s is an invalid writable path (%2$s)", optionName, rawValue));
				printHelp(ExitCodeGeneralError);
				throw newShouldHaveExited();
			}
			extantWritableFolderPath(optionName, path);
			paths.add(path);
		}
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
			printErrorLine(format(ENGLISH, "Option --%1$s is an invalid path (%2$s)", optionName, rawValue));
			printHelp(ExitCodeGeneralError);
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
			printErrorLine(format(ENGLISH, "Option --%1$s is an invalid charset (%2$s)", optionName, rawValue));
			printHelp(ExitCodeGeneralError);
			throw newShouldHaveExited();
		}
	}

	private void extantWritableFolderPath(@NotNull final String optionName, @NotNull final Path potentialWritableFolderPath)
	{
		if (!exists(potentialWritableFolderPath) || !IsSubFolder.accept(potentialWritableFolderPath) || !isWritable(potentialWritableFolderPath))
		{
			printErrorLine(format(ENGLISH, "Option --%1$s is not a readable, writable sub-folder path (%2$s)", optionName, potentialWritableFolderPath.toString()));
			printHelp(ExitCodeGeneralError);
			throw newShouldHaveExited();
		}
	}
}
