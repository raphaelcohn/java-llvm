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

import com.stormmq.applications.Verbosity;
import com.stormmq.commandLineArgumentsParsing.CommandLineArgumentsParser;
import com.stormmq.commandLineArgumentsParsing.OptionMustBe;
import joptsimple.*;
import org.jetbrains.annotations.*;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

import static com.stormmq.functions.CollectionHelper.addOnce;
import static com.stormmq.commandLineArgumentsParsing.OptionMustBe.OptionCanBeAbsent;
import static com.stormmq.commandLineArgumentsParsing.OptionMustBe.OptionMustBePresent;
import static com.stormmq.applications.Verbosity.*;
import static com.stormmq.string.Formatting.format;
import static java.lang.System.err;
import static java.lang.System.out;
import static java.util.Arrays.copyOfRange;

public class JoptSimpleCommandLineArgumentsParser implements CommandLineArgumentsParser
{
	@NotNull @NonNls private static final String show_help = "show help";
	@NotNull @NonNls private static final String UTF_8 = "UTF-8";

	@SuppressWarnings({"MethodCanBeVariableArityMethod", "StaticMethodOnlyUsedInOneClass"})
	@NotNull
	public static CommandLineArgumentsParser jopSimpleCommandLineArgumentsParser(@NotNull final String[] commandLineArguments)
	{
		return new JoptSimpleCommandLineArgumentsParser(commandLineArguments);
	}

	@NotNull private final String[] commandLineArguments;
	@NotNull private final PrintStream standardOut;
	@NotNull private final PrintStream standardError;
	@NotNull private final Set<String> allKnownOptions;
	@NotNull private final OptionParser optionParser;
	@NotNull private final Set<String> optionsThatMustBePresent;
	@Nullable private JoptSimpleCommandLineArguments arguments;

	@SuppressWarnings({"UseOfSystemOutOrSystemErr", "MethodCanBeVariableArityMethod", "WeakerAccess"})
	public JoptSimpleCommandLineArgumentsParser(@NotNull final String[] commandLineArguments)
	{
		this(commandLineArguments, out, err);
	}

	@SuppressWarnings("WeakerAccess")
	public JoptSimpleCommandLineArgumentsParser(@NotNull final String[] commandLineArguments, @NotNull final PrintStream standardOut, @NotNull final PrintStream standardError)
	{
		this.commandLineArguments = commandLineArguments;
		this.standardOut = standardOut;
		this.standardError = standardError;
		allKnownOptions = new HashSet<>(16);
		optionParser = new OptionParser();
		optionsThatMustBePresent = new LinkedHashSet<>(8);
		arguments = null;

		configureOptionParser();
	}

	private void configureOptionParser()
	{
		optionParser.posixlyCorrect(true);

		optionParser.accepts(helpOptionName, show_help).forHelp();
		addOnce(allKnownOptions, helpOptionName);

		optionWithOptionalValue(OptionCanBeAbsent, verboseOptionName, format("verbosity level (%1$s - %2$s)", None.ordinal(), Everything.ordinal()), Integer.toString(Verbose.ordinal())).withValuesConvertedBy(new VerbosityValueConverter()).defaultsTo(Verbose);
	}

	@NotNull
	private JoptSimpleCommandLineArguments newArgumentsOnce()
	{
		if (arguments == null)
		{
			arguments = new JoptSimpleCommandLineArguments(optionParser, standardOut, standardError, optionsThatMustBePresent, commandLineArguments);
		}
		return arguments;
	}

	@Override
	@NotNull
	public final Supplier<Verbosity> verboseOption()
	{
		return () -> newArgumentsOnce().verbosityOptionValue(verboseOptionName);
	}

	@Override
	@SuppressWarnings("unused")
	@NotNull
	public final Supplier<Path> extantWritableFolderPathOption(@NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String defaultsTo, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		optionWithRequiredValue(OptionCanBeAbsent, optionName, description, valueExample, requireIfTheseOptionsArePresent).ofType(String.class).defaultsTo(defaultsTo);
		return () -> newArgumentsOnce().extantWritableFolderPathOptionValue(optionName);
	}

	@Override
	@NotNull
	public final Supplier<Path> creatableFolderPathOption(@NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String defaultsTo, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		optionWithRequiredValue(OptionCanBeAbsent, optionName, description, valueExample, requireIfTheseOptionsArePresent).ofType(String.class).defaultsTo(defaultsTo);
		return () -> newArgumentsOnce().creatableFolderPathOptionValue(optionName);
	}

	@Override
	@NotNull
	public final Supplier<List<Path>> extantWritableFolderPathsOption(@NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String defaultsTo, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		optionWithRequiredValue(OptionCanBeAbsent, optionName, description, valueExample, requireIfTheseOptionsArePresent).withValuesSeparatedBy(':').ofType(String.class).defaultsTo(defaultsTo);
		return () -> newArgumentsOnce().extantWritableFolderPathsOptionValue(optionName);
	}

	@Override
	@NotNull
	public final Supplier<Path> pathOption(@NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String defaultsTo, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		optionWithRequiredValue(OptionCanBeAbsent, optionName, description, valueExample, requireIfTheseOptionsArePresent).ofType(String.class).defaultsTo(defaultsTo);
		return () -> newArgumentsOnce().pathOptionValue(optionName);
	}

	@Override
	@NotNull
	public final Supplier<Charset> charsetOption(@NotNull final OptionMustBe optionMustBePresent, @NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		optionWithRequiredValue(optionMustBePresent, optionName, description, UTF_8, requireIfTheseOptionsArePresent).ofType(String.class).defaultsTo(UTF_8);
		return () -> newArgumentsOnce().charsetOptionValue(optionName);
	}

	@Override
	@SuppressWarnings("unchecked")
	@NotNull
	public final <E extends Enum<E>> Supplier<E> enumOption(@NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final E defaultsTo, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		final String name = defaultsTo.name();
		optionWithRequiredValue(OptionCanBeAbsent, optionName, description, name, requireIfTheseOptionsArePresent).withValuesConvertedBy(new EnumValueConverter<>(optionName, defaultsTo)).defaultsTo(defaultsTo);
		return () -> newArgumentsOnce().enumOptionValue(optionName);
	}

	@NotNull
	private ArgumentAcceptingOptionSpec<String> optionWithRequiredValue(@NotNull final OptionMustBe optionMustBePresent, @NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		return option(optionMustBePresent, optionName, description, requireIfTheseOptionsArePresent).withRequiredArg().describedAs(valueExample);
	}

	@NotNull
	private ArgumentAcceptingOptionSpec<String> optionWithOptionalValue(@NotNull final OptionMustBe optionMustBePresent, @NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		return option(optionMustBePresent, optionName, description, requireIfTheseOptionsArePresent).withOptionalArg().describedAs(valueExample);
	}

	@NotNull
	protected final OptionSpecBuilder optionWithNoValue(@NotNull final OptionMustBe optionMustBePresent, @NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String... requireIfTheseOptionsArePresent)
	{
		return option(optionMustBePresent, optionName, description, requireIfTheseOptionsArePresent);
	}

	@NotNull
	private OptionSpecBuilder option(@NotNull final OptionMustBe optionMustBePresent, @NonNls @NotNull final String optionName, @NonNls @NotNull final String description, @NonNls @NotNull final String[] requireIfTheseOptionsArePresent)
	{
		if (haveArgumentsAlreadyParsed())
		{
			throw new IllegalStateException("commandLineArguments have already been parsed");
		}

		addOnce(allKnownOptions, optionName);

		if (optionMustBePresent == OptionMustBePresent)
		{
			addOnce(optionsThatMustBePresent, optionName);
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

	@SuppressWarnings("InstanceVariableUsedBeforeInitialized")
	private boolean haveArgumentsAlreadyParsed()
	{
		return arguments != null;
	}

	private static final class VerbosityValueConverter implements ValueConverter<Verbosity>
	{
		@Override
		public Verbosity convert(@NotNull @NonNls final String value)
		{
			final int integer;
			try
			{
				integer = Integer.valueOf(value, 10);
			}
			catch (final NumberFormatException ignored)
			{
				try
				{
					return valueOf(value);
				}
				catch (final IllegalArgumentException e)
				{
					throw new ValueConversionException(format("No verbosity level '%1$s' is known for option --%2$s", value, verboseOptionName), e);
				}
			}
			if (integer < 0)
			{
				throw new ValueConversionException(format("Verbosity level '%1$s' is negative for option --%2$s", value, verboseOptionName));
			}
			Verbosity mostVerbosePossibilityIfValueTooHigh = None;
			for (final Verbosity verbosity : values())
			{
				if (verbosity.ordinal() == integer)
				{
					return verbosity;
				}
				mostVerbosePossibilityIfValueTooHigh = verbosity;
			}
			return mostVerbosePossibilityIfValueTooHigh;
		}

		@Override
		@NotNull
		public Class<Verbosity> valueType()
		{
			return Verbosity.class;
		}

		@Override
		@Nullable
		public String valuePattern()
		{
			return null;
		}
	}

	private static final class EnumValueConverter<E extends Enum<E>> implements ValueConverter<E>
	{
		@NotNull private final E defaultsTo;
		@NotNull private final String optionName;

		private EnumValueConverter(@NotNull final String optionName, @NonNls @NotNull final E defaultsTo)
		{
			this.defaultsTo = defaultsTo;
			this.optionName = optionName;
		}

		@Override
		public E convert(@NotNull @NonNls final String value)
		{
			try
			{
				return Enum.valueOf(valueType(), value);
			}
			catch (final IllegalArgumentException e)
			{
				throw new ValueConversionException(format("No value '%1$s' is known for option --%2$s", value, optionName), e);
			}
		}

		@Override
		@NotNull
		public Class<E> valueType()
		{
			return defaultsTo.getDeclaringClass();
		}

		@Override
		@Nullable
		public String valuePattern()
		{
			return null;
		}
	}
}
