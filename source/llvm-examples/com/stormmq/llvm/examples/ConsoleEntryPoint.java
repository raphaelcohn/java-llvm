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

import com.stormmq.jopt.CommandLineArgumentsParser;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.function.Supplier;

import static com.stormmq.jopt.Application.run;
import static com.stormmq.jopt.CommandLineArgumentsParser.commandLineArgumentsParser;
import static com.stormmq.path.Constants.CurrentFolder;

public final class ConsoleEntryPoint
{
	public static void main(@NotNull @NonNls final String... commandLineArguments)
	{
		final CommandLineArgumentsParser commandLineArgumentsParser = commandLineArgumentsParser(commandLineArguments);
		@SuppressWarnings("HardcodedFileSeparator") final Supplier<LinkedHashSet<Path>> source = commandLineArgumentsParser.extantWritableFolderPathsOption(true, "source", "source root path", "/path/to/source", CurrentFolder);

		run(new ExampleApplication(source.get()));
	}

	private ConsoleEntryPoint()
	{
	}
}
