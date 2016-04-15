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

package com.stormmq.applications;

import com.stormmq.applications.uncaughtExceptionHandlers.ExitCodeSettingUncaughtExceptionHandler;
import com.stormmq.applications.uncaughtExceptionHandlers.MustExitBecauseOfFailureException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import static com.stormmq.applications.ExitCode.CanNotCreate;
import static com.stormmq.applications.ExitCode.Success;
import static com.stormmq.string.Formatting.format;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.deleteIfExists;

public abstract class AbstractMultithreadedApplication implements Application
{
	@NotNull private final UncaughtExceptionHandler delegate;
	@NotNull protected final AtomicReference<ExitCode> exitCode;
	@NotNull protected final ExitCodeSettingUncaughtExceptionHandler exitCodeSettingUncaughtExceptionHandler;

	protected AbstractMultithreadedApplication(@NotNull final UncaughtExceptionHandler delegate)
	{
		this.delegate = delegate;
		exitCode = new AtomicReference<>(Success);
		exitCodeSettingUncaughtExceptionHandler = new ExitCodeSettingUncaughtExceptionHandler(delegate, exitCode);
	}

	@Override
	@NotNull
	public final ExitCode execute()
	{
		try
		{
			executeInternally();
		}
		catch (final MustExitBecauseOfFailureException e)
		{
			delegate.uncaughtException(Thread.currentThread(), e);
		}
		return exitCode.get();
	}

	protected final boolean mustExitBecauseOfFailure()
	{
		return exitCode.get() != Success;
	}

	protected final void recreateOutputPath(@NotNull final Path outputPath) throws MustExitBecauseOfFailureException
	{
		try
		{
			deleteIfExists(outputPath);
		}
		catch (final IOException ignored)
		{
			// Lots of legitimate reasons; may be like $HOME for instance (can't delete)
		}

		try
		{
			createDirectories(outputPath);
		}
		catch (final IOException e)
		{
			exitCode.set(CanNotCreate);
			throw new MustExitBecauseOfFailureException(format("Could not create output path '%1$s'", outputPath), e);
		}
	}

	protected abstract void executeInternally() throws MustExitBecauseOfFailureException;

}
