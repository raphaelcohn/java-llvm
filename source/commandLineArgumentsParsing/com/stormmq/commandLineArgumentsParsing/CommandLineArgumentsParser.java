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

package com.stormmq.commandLineArgumentsParsing;

import com.stormmq.applications.Verbosity;
import org.jetbrains.annotations.*;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

public interface CommandLineArgumentsParser
{
	@NotNull @NonNls String helpOptionName = "help";

	@NotNull @NonNls String verboseOptionName = "verbose";

	@NotNull
	Supplier<Verbosity> verboseOption();

	@SuppressWarnings("unused")
	@NotNull
	Supplier<Path> extantWritableFolderPathOption(@NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String defaultsTo, @NotNull @NonNls final String... requireIfTheseOptionsArePresent);

	@NotNull
	Supplier<Path> creatableFolderPathOption(@NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String defaultsTo, @NotNull @NonNls final String... requireIfTheseOptionsArePresent);

	@NotNull
	Supplier<List<Path>> extantWritableFolderPathsOption(@NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls String valueExample, @NotNull @NonNls final String defaultsTo, @NotNull @NonNls final String... requireIfTheseOptionsArePresent);

	@NotNull
	Supplier<Path> pathOption(@NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String valueExample, @NotNull @NonNls final String defaultsTo, @NotNull @NonNls final String... requireIfTheseOptionsArePresent);

	@NotNull
	Supplier<Charset> charsetOption(@NotNull final OptionMustBe optionMustBePresent, @NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final String... requireIfTheseOptionsArePresent);

	@SuppressWarnings("unchecked")
	@NotNull
	<E extends Enum<E>> Supplier<E> enumOption(@NotNull @NonNls final String optionName, @NotNull @NonNls final String description, @NotNull @NonNls final E defaultsTo, @NotNull @NonNls final String... requireIfTheseOptionsArePresent);
}
