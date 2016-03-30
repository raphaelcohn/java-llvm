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

package com.stormmq.java.llvm.examples;

import com.stormmq.java.classfile.parser.javaClassFileParsers.exceptions.InvalidJavaClassFileException;
import com.stormmq.java.classfile.parser.javaClassFileParsers.exceptions.JavaClassFileContainsDataTooLongToReadException;
import com.stormmq.java.classfile.processing.processLogs.ProcessLog;
import com.stormmq.java.classfile.processing.typeInformationUsers.TypeInformationTriplet;
import com.stormmq.jopt.ExitCode;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.*;

import static com.stormmq.jopt.ExitCode.*;

public final class ExitCodeSettingProcessLog implements ProcessLog
{
	@NotNull private final ProcessLog delegate;
	@NotNull private final AtomicReference<ExitCode> exitCode;

	public ExitCodeSettingProcessLog(@NotNull final ProcessLog delegate, @NotNull final AtomicReference<ExitCode> exitCode)
	{
		this.delegate = delegate;
		this.exitCode = exitCode;
	}

	@Override
	public int failureCount()
	{
		return delegate.failureCount();
	}

	@Override
	public int successCount()
	{
		return delegate.successCount();
	}

	@Override
	public void success(@NotNull final String filePath)
	{
		delegate.success(filePath);
	}

	@Override
	public void genericSuccess(@NonNls @NotNull final String messageTemplate, @NotNull final Object... arguments)
	{
		delegate.genericSuccess(messageTemplate, arguments);
	}

	@Override
	public void failureZip(@NotNull final Path zipFilePath, @NotNull final IOException e)
	{
		delegate.failureZip(zipFilePath, e);
		exitCode.compareAndSet(Success, IOError);
	}

	@Override
	public void failureZip(@NotNull final Path zipFilePath, @NotNull final ZipException e)
	{
		delegate.failureZip(zipFilePath, e);
		exitCode.compareAndSet(Success, IOError);
	}

	@Override
	public void failure(@NotNull final Path filePath, @NotNull final IOException e)
	{
		delegate.failure(filePath, e);
		exitCode.compareAndSet(Success, IOError);
	}

	@Override
	public void failure(@NotNull final String filePath, @NotNull final InvalidJavaClassFileException e)
	{
		delegate.failure(filePath, e);
		exitCode.compareAndSet(Success, DataError);
	}

	@Override
	public void failure(@NotNull final String filePath, @NotNull final JavaClassFileContainsDataTooLongToReadException e)
	{
		delegate.failure(filePath, e);
		exitCode.compareAndSet(Success, DataError);
	}

	@Override
	public void failure(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry, @NotNull final IOException e)
	{
		delegate.failure(zipFile, zipEntry, e);
		exitCode.compareAndSet(Success, IOError);
	}

	@Override
	public void failureJavaClassFileIsTooLarge(@NotNull final String filePath)
	{
		delegate.failureJavaClassFileIsTooLarge(filePath);
		exitCode.compareAndSet(Success, DataError);
	}

	@Override
	public void duplicateTypeInformationWarning(@NotNull final TypeInformationTriplet extant, @NotNull final TypeInformationTriplet replacement)
	{
		delegate.duplicateTypeInformationWarning(extant, replacement);
	}
}
