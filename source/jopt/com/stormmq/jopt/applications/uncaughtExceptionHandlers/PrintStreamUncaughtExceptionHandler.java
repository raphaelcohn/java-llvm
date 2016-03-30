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

package com.stormmq.jopt.applications.uncaughtExceptionHandlers;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import static com.stormmq.string.Formatting.formatPrintLineAndFlushWhilstSynchronized;
import static java.lang.System.err;

public final class PrintStreamUncaughtExceptionHandler implements UncaughtExceptionHandler
{
	@SuppressWarnings("UseOfSystemOutOrSystemErr") @NotNull public static final UncaughtExceptionHandler StandardErrorUncaughtExceptionHandler = new PrintStreamUncaughtExceptionHandler(err);

	@NotNull private final PrintStream printStream;

	private PrintStreamUncaughtExceptionHandler(@NotNull final PrintStream printStream)
	{
		this.printStream = printStream;
	}

	@Override
	public void uncaughtException(@NotNull final Thread thread, @NotNull final Throwable uncaughtThrowable)
	{
		if (uncaughtThrowable instanceof MustExitBecauseOfFailureException)
		{
			formatPrintLineAndFlushWhilstSynchronized(printStream, uncaughtThrowable.getMessage());
		}
		else
		{
			synchronized (printStream)
			{
				formatPrintLineAndFlushWhilstSynchronized(printStream, "Exception '%1$s' on thread '%2$s'", uncaughtThrowable.getMessage(), thread.getName());
				uncaughtThrowable.printStackTrace(printStream);
				printStream.flush();
			}
		}
	}
}
