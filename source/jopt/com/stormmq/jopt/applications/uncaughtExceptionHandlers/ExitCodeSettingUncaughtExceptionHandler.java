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

import com.stormmq.jopt.ExitCode;
import org.jetbrains.annotations.NotNull;

import java.io.IOError;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.atomic.AtomicReference;

import static com.stormmq.jopt.ExitCode.IOError;
import static com.stormmq.jopt.ExitCode.Software;

public final class ExitCodeSettingUncaughtExceptionHandler implements UncaughtExceptionHandler
{
	@NotNull private final UncaughtExceptionHandler delegate;
	@NotNull private final AtomicReference<ExitCode> exitCode;

	public ExitCodeSettingUncaughtExceptionHandler(@NotNull final UncaughtExceptionHandler delegate, @NotNull final AtomicReference<ExitCode> exitCode)
	{
		this.delegate = delegate;
		this.exitCode = exitCode;
	}

	@Override
	public void uncaughtException(final Thread thread, final Throwable uncaughtThrowable)
	{
		exitCode.set(uncaughtThrowable instanceof IOException || uncaughtThrowable instanceof IOError ? IOError : Software);
		delegate.uncaughtException(thread, uncaughtThrowable);
	}
}
