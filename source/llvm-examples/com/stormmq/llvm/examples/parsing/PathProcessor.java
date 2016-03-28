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

package com.stormmq.llvm.examples.parsing;

import com.stormmq.llvm.examples.parsing.files.JarOrZipParsableFile;
import com.stormmq.llvm.examples.parsing.files.ParsableFile;
import org.jetbrains.annotations.*;

import java.nio.file.Path;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class PathProcessor
{
	@NotNull private final ConcurrentLinkedQueue<ParsableFile> parsableFileQueue;

	public PathProcessor(@NotNull final ConcurrentLinkedQueue<ParsableFile> parsableFileQueue)
	{
		this.parsableFileQueue = parsableFileQueue;
	}

	public void processJarOrZipFile(@NotNull final Path jarOrZipFilePath, @NotNull final Path relativeRootFolderPath)
	{
		parsableFileQueue.add(new JarOrZipParsableFile(jarOrZipFilePath, relativeRootFolderPath, parsableFileQueue));
	}

	public void processClassFile(@NotNull final Path javaClassFilePath, @NotNull final Path relativeRootFolderPath, @NotNull final Path relativeJavaClassFilePath)
	{
		parsableFileQueue.add((fileParser, parseFailureLog) -> fileParser.parseFile(javaClassFilePath, relativeRootFolderPath, relativeJavaClassFilePath));
	}
}
