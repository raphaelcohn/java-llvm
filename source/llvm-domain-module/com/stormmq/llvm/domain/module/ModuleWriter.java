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

package com.stormmq.llvm.domain.module;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.byteWriters.OutputStreamByteWriter;
import com.stormmq.string.AbstractToString;
import org.jetbrains.annotations.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

public final class ModuleWriter extends AbstractToString
{
	@NotNull @NonNls private static final String dotLL = ".ll";

	@NotNull private final Path outputRootFolderPath;

	public ModuleWriter(@NotNull final Path outputRootFolderPath)
	{
		this.outputRootFolderPath = outputRootFolderPath;
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(outputRootFolderPath);
	}

	public void writeModule(@NotNull final String relativeFilePath, @NotNull final Path relativeRootFolderPath, @NotNull final Module module)
	{
		final Path llvmFilePath = llvmFilePath(relativeFilePath, relativeRootFolderPath);
		try (final OutputStream outputStream = newOutputStream(llvmFilePath, WRITE, CREATE, TRUNCATE_EXISTING))
		{
			module.write(new OutputStreamByteWriter(outputStream));
		}
		catch (final IOException e)
		{
			throw new IllegalStateException("Could not create output file '" + llvmFilePath + '\'', e);
		}
	}

	@NotNull
	private Path llvmFilePath(@NotNull final String relativeFilePath, @NotNull final Path relativeRootFolderPath)
	{
		final int lastIndex = relativeFilePath.lastIndexOf('.');
		final String relativeFilePathWithoutFileExtension = lastIndex == -1 ? relativeFilePath : relativeFilePath.substring(0, lastIndex);
		final Path llvmFilePath = outputRootFolderPath.resolve(relativeRootFolderPath).resolve(relativeFilePathWithoutFileExtension + dotLL);
		final Path parentFolder = llvmFilePath.getParent();
		try
		{
			createDirectories(parentFolder);
		}
		catch (final IOException e)
		{
			throw new IllegalStateException("Could not create output file parent folder '" + parentFolder + '\'', e);
		}
		return llvmFilePath;
	}
}
