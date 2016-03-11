package com.stormmq.java.parsing.application;

import com.stormmq.nio.Constants;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.*;

import static com.stormmq.nio.IsSubFolderFilter.IsSubFolder;
import static java.lang.String.format;
import static java.lang.System.*;
import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.*;
import static java.nio.file.Paths.get;
import static java.util.Locale.ENGLISH;

public final class ConsoleEntryPoint
{
	private static final String help = "help";
	private static final String source = "source";
	private static final String destination = "destination";
	private static final String imports = "imports";
	private static final OptionParser CommandLineArgumentsParser = new OptionParser();

	static
	{
		CommandLineArgumentsParser.posixlyCorrect(true);
		CommandLineArgumentsParser.accepts(help, "show help").forHelp();
		CommandLineArgumentsParser.accepts(source, "source root path").withRequiredArg().describedAs("/Users/raph/Documents/java-llvm/source").ofType(String.class).defaultsTo(Constants.CurrentFolder);
		CommandLineArgumentsParser.accepts(imports, "import paths").withRequiredArg().describedAs("/Users/raph/Documents/java-llvm/library").withValuesSeparatedBy(':').ofType(String.class).defaultsTo(Constants.CurrentFolder);
		CommandLineArgumentsParser.accepts(destination, "destination root path").withRequiredArg().describedAs("/Users/raph/Documents/java-llvm/destination").ofType(String.class);
	}

	public static void main(@NotNull final String... commandLineArguments)
	{
		final OptionSet arguments;
		try
		{
			arguments = CommandLineArgumentsParser.parse(commandLineArguments);
		}
		catch (OptionException e)
		{
			err.printf(e.getMessage() + '\n');
			printHelp(1);
			return;
		}

		if (arguments.has(help))
		{
			printHelp(2);
			return;
		}

		for (final String argument : requiredArguments(source, destination))
		{
			if (!arguments.has(argument))
			{
				err.printf(format(ENGLISH, "Missing required argument %1$s\n", argument));
				printHelp(1);
				return;
			}
		}

		@NotNull final Path sourceRootPath = extantWritableFolderPathArgument(arguments, source);
		@NotNull final Path destinationRootPath = creatableFolderPathArgument(arguments, destination);
		@NotNull final LinkedHashSet<Path> importPaths = extantWritableFolderPathsArgument(arguments, imports);

		new Application(sourceRootPath, destinationRootPath, importPaths).run();
	}

	@NotNull
	private static Path extantWritableFolderPathArgument(@NotNull final OptionSet arguments, @NotNull final String argumentName)
	{
		final Path potentialWritableFolderPath = pathArgument(arguments, argumentName);
		extantWritableFolderPath(argumentName, potentialWritableFolderPath);
		return potentialWritableFolderPath;
	}

	private static void extantWritableFolderPath(@NotNull final String argumentName, @NotNull final Path potentialWritableFolderPath)
	{
		if (!exists(potentialWritableFolderPath) || !IsSubFolder.accept(potentialWritableFolderPath) || !isWritable(potentialWritableFolderPath))
		{
			err.printf(format(ENGLISH, "Argument --%1$s is not a readable, writable sub-folder path (%2$s)\n", argumentName, potentialWritableFolderPath.toString()));
			printHelp(1);
		}
	}

	@NotNull
	private static Path creatableFolderPathArgument(@NotNull final OptionSet arguments, @NotNull final String argumentName)
	{
		final Path potentialWritableFolderPath = pathArgument(arguments, argumentName);
		if (exists(potentialWritableFolderPath))
		{
			if (!IsSubFolder.accept(potentialWritableFolderPath) || !isWritable(potentialWritableFolderPath))
			{
				err.printf(format(ENGLISH, "Argument --%1$s is extant but not a readable, writable sub-folder path (%2$s)\n", argumentName, potentialWritableFolderPath.toString()));
				printHelp(1);
			}
		}
		else
		{
			try
			{
				createDirectories(potentialWritableFolderPath);
			}
			catch (IOException e)
			{
				err.printf(format(ENGLISH, "Argument --%1$s could not be created (%2$s)\n", argumentName, potentialWritableFolderPath.toString()));
				printHelp(1);
			}
		}

		return potentialWritableFolderPath;
	}

	@NotNull
	private static LinkedHashSet<Path> extantWritableFolderPathsArgument(@NotNull final OptionSet arguments, @NotNull final String argumentName)
	{
		@SuppressWarnings("unchecked") final List<String> rawValues = (List<String>) arguments.valuesOf(argumentName);
		final LinkedHashSet<Path> paths = new LinkedHashSet<>(rawValues.size());
		for (String rawValue : rawValues)
		{
			final Path path;
			try
			{
				path = get(rawValue).toAbsolutePath();
			}
			catch (InvalidPathException e)
			{
				err.printf(format(ENGLISH, "Argument --%1$s is an invalid path (%2$s)\n", argumentName, rawValue));
				printHelp(1);
				throw new IllegalStateException("Should have exited");
			}
			extantWritableFolderPath(argumentName, path);
			paths.add(path);
		}
		return paths;
	}

	@NotNull
	private static Path pathArgument(@NotNull final OptionSet arguments, @NotNull final String argumentName)
	{
		final String rawValue = (String) arguments.valueOf(argumentName);
		try
		{
			return get(rawValue).toAbsolutePath();
		}
		catch (InvalidPathException e)
		{
			err.printf(format(ENGLISH, "Argument --%1$s is an invalid path (%2$s)\n", argumentName, rawValue));
			printHelp(1);
			throw new IllegalStateException("Should have exited");
		}
	}

	@NotNull
	private static Charset charsetArgument(@NotNull final OptionSet arguments, @NotNull final String argumentName)
	{
		final String rawValue = (String) arguments.valueOf(argumentName);
		try
		{
			return forName(rawValue);
		}
		catch (final IllegalCharsetNameException | UnsupportedCharsetException e)
		{
			err.printf(format(ENGLISH, "Argument --%1$s is an invalid charset (%2$s)\n", argumentName, rawValue));
			printHelp(1);
			throw new IllegalStateException("Should have exited");
		}
	}

	private static void printHelp(final int exitCode)
	{
		try
		{
			CommandLineArgumentsParser.printHelpOn(out);
		}
		catch (IOException e)
		{
			err.printf(format(ENGLISH, "Unexpected failure printing help %1$s\n", e.getMessage()));
		}
		exit(exitCode);
	}

	@NotNull
	private static String[] requiredArguments(@NotNull final String... requiredArguments)
	{
		return requiredArguments;
	}

	private ConsoleEntryPoint()
	{
	}
}
