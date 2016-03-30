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

package com.stormmq.jopt.applications;

import com.stormmq.jopt.ExitCode;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.lang.Thread.UncaughtExceptionHandler;

import static com.stormmq.jopt.ExitCode.*;
import static com.stormmq.jopt.applications.uncaughtExceptionHandlers.PrintStreamUncaughtExceptionHandler.StandardErrorUncaughtExceptionHandler;
import static java.lang.Thread.currentThread;

@FunctionalInterface
public interface Application
{
	@NotNull
	ExitCode execute();

	static void run(@NotNull final Application application)
	{
		run(application, StandardErrorUncaughtExceptionHandler);
	}

	@SuppressWarnings("ErrorNotRethrown")
	static void run(@NotNull final Application application, @NotNull final UncaughtExceptionHandler uncaughtExceptionHandler)
	{
		ExitCode exitCode;
		try
		{
			exitCode = application.execute();
		}
		catch (final IOError e)
		{
			exitCode = handle(uncaughtExceptionHandler, e, IOError);
		}
		catch (final Throwable e)
		{
			exitCode = handle(uncaughtExceptionHandler, e, Software);
		}

		if (exitCode != Success)
		{
			exitCode.exit();
		}
	}

	@NotNull
	static ExitCode handle(@NotNull final UncaughtExceptionHandler uncaughtExceptionHandler, @NotNull final Throwable throwable, @NotNull final ExitCode exitCode)
	{
		uncaughtExceptionHandler.uncaughtException(currentThread(), throwable);
		return exitCode;
	}
}
