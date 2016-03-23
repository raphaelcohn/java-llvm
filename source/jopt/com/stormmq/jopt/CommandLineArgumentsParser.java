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
import org.jetbrains.annotations.*;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.lang.System.err;
import static java.lang.System.out;
import static java.util.Arrays.copyOfRange;
import static java.util.Locale.ENGLISH;

public class CommandLineArgumentsParser
{
	@NotNull @NonNls static final String help = "help";
	@NotNull @NonNls private static final String show_help = "show help";
	@NotNull @NonNls private static final String UTF_8 = "UTF-8";

	@NotNull
	static IllegalStateException newShouldHaveExited()
	{
		return new IllegalStateException("Should have exited");
	}

	@NotNull
	static IllegalStateException newShouldHaveExited(@SuppressWarnings("UnusedParameters") @NotNull final Throwable ignored)
	{
		return newShouldHaveExited();
	}

	@SuppressWarnings("MethodCanBeVariableArityMethod")
	@NotNull
	public static CommandLineArgumentsParser commandLineArgumentsParser(@NotNull final String[] commandLineArguments)
	{
		return new CommandLineArgumentsParser(commandLineArguments);
	}

	@NotNull private final String[] commandLineArguments;
	@NotNull private final PrintStream standardOut;
	@NotNull private final PrintStream standardError;
	@NotNull private final Set<String> allKnownOptions;
	@NotNull private final OptionParser optionParser;
	@NotNull private final Set<String> optionsThatMustBePresent;
	@Nullable private CommandLineArguments arguments;

	@SuppressWarnings({"UseOfSystemOutOrSystemErr", "MethodCanBeVariableArityMethod"})
	public CommandLineArgumentsParser(@NotNull final String[] commandLineArguments)
	{
		this(commandLineArguments, out, err);
	}

	public CommandLineArgumentsParser(@NotNull final String[] commandLineArguments, @NotNull final PrintStream standardOut, @NotNull final PrintStream standardError)
	{
		this.commandLineArguments = commandLineArguments;
		this.standardOut = standardOut;
		this.standardError = standardError;
		allKnownOptions = new HashSet<>(16);
		optionParser = newOptionParser();
		optionsThatMustBePresent = new LinkedHashSet<>(8);
		arguments = null;
	}

	@NotNull
	private OptionParser newOptionParser()
	{
		final OptionParser optionParser = new OptionParser();
		optionParser.posixlyCorrect(true);
		optionParser.accepts(help, show_help).forHelp();
		allKnownOptions.add(help);
		return optionParser;
	}

	@NotNull
	private CommandLineArguments newArgumentsOnce()
	{
		if (arguments == null)
		{
			arguments = new CommandLineArguments(optionParser, standardOut, standardError, optionsThatMustBePresent, commandLineArguments);
		}
		return arguments;
	}

	@NotNull
	public final Supplier<Path> extantWritableFolderPathOption(final boolean optionMustBePresent, @NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String defaultsTo, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		optionWithRequiredValue(optionMustBePresent, optionName, description, valueExample, requireIfTheseOptionsArePresent).ofType(String.class).defaultsTo(defaultsTo);
		return () -> newArgumentsOnce().extantWritableFolderPathOptionValue(optionName);
	}

	@NotNull
	public final Supplier<Path> creatableFolderPathOption(final boolean optionMustBePresent, @NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String defaultsTo, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		optionWithRequiredValue(optionMustBePresent, optionName, description, valueExample, requireIfTheseOptionsArePresent).ofType(String.class).defaultsTo(defaultsTo);
		return () -> newArgumentsOnce().creatableFolderPathOptionValue(optionName);
	}

	@NotNull
	public final Supplier<LinkedHashSet<Path>> extantWritableFolderPathsOption(final boolean optionMustBePresent, @NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String defaultsTo, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		optionWithRequiredValue(optionMustBePresent, optionName, description, valueExample, requireIfTheseOptionsArePresent).withValuesSeparatedBy(':').ofType(String.class).defaultsTo(defaultsTo);
		return () -> newArgumentsOnce().extantWritableFolderPathsOptionValue(optionName);
	}

	@NotNull
	public final Supplier<Path> pathOption(final boolean optionMustBePresent, @NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String defaultsTo, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		optionWithRequiredValue(optionMustBePresent, optionName, description, valueExample, requireIfTheseOptionsArePresent).ofType(String.class).defaultsTo(defaultsTo);
		return () -> newArgumentsOnce().pathOptionValue(optionName);
	}

	@NotNull
	public final Supplier<Charset> charsetOption(final boolean optionMustBePresent, @NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		optionWithRequiredValue(optionMustBePresent, optionName, description, UTF_8, requireIfTheseOptionsArePresent).ofType(String.class).defaultsTo(UTF_8);
		return () -> newArgumentsOnce().charsetOptionValue(optionName);
	}

	@NotNull
	protected final ArgumentAcceptingOptionSpec<String> optionWithRequiredValue(final boolean optionMustBePresent, @NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		return option(optionMustBePresent, optionName, description, requireIfTheseOptionsArePresent).withRequiredArg().describedAs(valueExample);
	}

	@NotNull
	protected final ArgumentAcceptingOptionSpec<String> optionWithOptionalValue(final boolean optionMustBePresent, @NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		return option(optionMustBePresent, optionName, description, requireIfTheseOptionsArePresent).withOptionalArg().describedAs(valueExample);
	}

	@NotNull
	protected final OptionSpecBuilder optionWithNoValue(final boolean optionMustBePresent, @NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		return option(optionMustBePresent, optionName, description, requireIfTheseOptionsArePresent);
	}

	@NotNull
	private OptionSpecBuilder option(final boolean optionMustBePresent, @NonNls @NotNull final String optionName, @NonNls @NotNull final String description, @NonNls @NotNull final String[] requireIfTheseOptionsArePresent)
	{
		if (arguments != null)
		{
			throw new IllegalStateException("commandLineArguments have already been parsed");
		}

		if (!allKnownOptions.add(optionName))
		{
			throw new IllegalStateException(format(ENGLISH, "Option '%1$s' has already been added", optionName));
		}

		if (optionMustBePresent)
		{
			if (!optionsThatMustBePresent.add(optionName))
			{
				throw new IllegalArgumentException(format(ENGLISH, "Option '%1$s' is already one that must be present", optionName));
			}
		}

		final OptionSpecBuilder optionSpecBuilder = optionParser.accepts(optionName, description);
		final int length = requireIfTheseOptionsArePresent.length;
		if (length == 0)
		{
			return optionSpecBuilder;
		}
		if (length == 1)
		{
			optionSpecBuilder.requiredIf(requireIfTheseOptionsArePresent[0]);
			return optionSpecBuilder;
		}
		optionSpecBuilder.requiredIf(requireIfTheseOptionsArePresent[0], copyOfRange(requireIfTheseOptionsArePresent, 1, length));
		return optionSpecBuilder;
	}
}