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

package com.stormmq.logs;

import com.stormmq.string.*;
import org.jetbrains.annotations.*;

import java.io.PrintStream;

import static java.lang.Character.isBmpCodePoint;
import static java.lang.Character.isISOControl;
import static java.lang.Character.toChars;
import static java.lang.System.err;

public class PrintStreamLog implements Log
{
	@SuppressWarnings({"UseOfSystemOutOrSystemErr", "resource"}) @NotNull public static final PrintStreamLog NoFacilityStandardErrorPrintStreamLog = new PrintStreamLog(null, err)
	{
		@SuppressWarnings("RefusedBequest")
		@Override
		public void close()
		{
			// close() ignored because we do not want to close standard error
		}
	};

	@SuppressWarnings("HardcodedFileSeparator") @NotNull private static final String Prefix = "\\u";
	@NotNull @NonNls private static final String InvalidUtf16StringPassedToLog = "Invalid UTF-16 string passed to log";

	@NotNull private final PrintStream printStream;
	@NotNull private final String preamble;

	@SuppressWarnings("WeakerAccess")
	public PrintStreamLog(@Nullable final Rfc3164Facility rfc3164Facility, @NotNull final PrintStream printStream)
	{
		this.printStream = printStream;
		preamble = rfc3164Facility == null ? "" : rfc3164Facility.name() + ':';
	}

	@Override
	public void log(@NotNull final LogLevel logLevel, @NotNull @NonNls final String utf8SafeMessageWithoutTrailingNewLine)
	{
		synchronized (printStream)
		{
			printStream.print(preamble);

			try
			{
				((CodePointUser<RuntimeException>) (index, codePoint) ->
				{
					if (isISOControl(codePoint))
					{
						printStream.print(Prefix);
						printStream.print(Formatting.zeroPaddedUpperCaseHexString((char) codePoint));
					}
					else if (isBmpCodePoint(codePoint))
					{
						printStream.print((char) codePoint);
					}
					else
					{
						final char[] chars = toChars(codePoint);
						printStream.print(chars[0]);
						printStream.print(chars[1]);
					}
				}).iterateOverStringCodePoints(utf8SafeMessageWithoutTrailingNewLine);
			}
			catch (final InvalidUtf16StringException ignored)
			{
				printStream.println();
				printStream.println(InvalidUtf16StringPassedToLog);
				return;
			}

			printStream.println();
			printStream.flush();
		}
	}

	@Override
	public void close()
	{
		printStream.close();
	}
}
