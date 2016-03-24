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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.stormmq.path.FileAndFolderHelper.FollowLinks;
import static com.stormmq.path.IsFileTypeFilter.IsClassFile;
import static com.stormmq.path.IsFileTypeFilter.IsJarOrZipFile;
import static com.stormmq.path.IsSubFolderFilter.IsSubFolder;
import static java.lang.Integer.MAX_VALUE;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.Files.walkFileTree;

public final class EnqueuePathsWalker
{
	@NotNull private final ConcurrentLinkedQueue<ParsableFile> parsableFileQueue;
	@NotNull private final Coordination coordination;

	public EnqueuePathsWalker(@NotNull final ConcurrentLinkedQueue<ParsableFile> parsableFileQueue, @NotNull final Coordination coordination)
	{
		this.parsableFileQueue = parsableFileQueue;
		this.coordination = coordination;
	}

	public void parse(@NotNull final Iterable<Path> paths)
	{
		coordination.start();

		for (final Path path : paths)
		{
			parse(path);
		}

		coordination.finish();
	}

	private void parse(@NotNull final Path fullRootPath)
	{
		if(IsSubFolder.accept(fullRootPath))
		{
			processFolder(fullRootPath);
		}
		else if (IsJarOrZipFile.accept(fullRootPath))
		{
			processJarOrZipFile(fullRootPath, fullRootPath.getFileName());
		}
		else if(IsClassFile.accept(fullRootPath))
		{
			processClassFile(fullRootPath, Paths.get("."), fullRootPath.getFileName());
		}
	}

	private void processFolder(@NotNull final Path fullRootPath)
	{
		try
		{
			walkFileTree(fullRootPath, FollowLinks, MAX_VALUE, new FileVisitor<Path>()
			{
				@Override
				public FileVisitResult preVisitDirectory(@NotNull final Path directory, @NotNull final BasicFileAttributes basicFileAttributes)
				{
					return CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(@NotNull final Path file, @NotNull final BasicFileAttributes basicFileAttributes)
				{
					if (IsJarOrZipFile.accept(file))
					{
						final Path relativeRootFolderPath = fullRootPath.relativize(file);
						processJarOrZipFile(file, relativeRootFolderPath);
					}
					else if (IsClassFile.accept(file))
					{
						final Path relativeRootFolderPath = fullRootPath.relativize(file).getParent();
						processClassFile(file, relativeRootFolderPath, fullRootPath.relativize(file).getFileName());
					}
					return CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(@NotNull final Path file, @NotNull final IOException exception)
				{
					if (exception instanceof FileSystemLoopException)
					{
						return CONTINUE;
					}
					throw new IllegalStateException("Could not walk tree", exception);
				}

				@Override
				public FileVisitResult postVisitDirectory(@Nullable final Path directory, @Nullable final IOException exception)
				{
					return CONTINUE;
				}
			});
		}
		catch (final IOException e)
		{
			throw new IllegalStateException("Could not walk tree", e);
		}
	}

	private void processJarOrZipFile(@NotNull final Path jarOrZipFilePath, @NotNull final Path relativeRootFolderPath)
	{
		parsableFileQueue.add(new JarOrZipParsableFile(jarOrZipFilePath, relativeRootFolderPath, parsableFileQueue));
	}

	private void processClassFile(@NotNull final Path javaClassFilePath, @NotNull final Path relativeRootFolderPath, @NotNull final Path relativeJavaClassFilePath)
	{
		parsableFileQueue.add((fileParser, parseFailureLog) -> fileParser.parseFile(javaClassFilePath, relativeRootFolderPath, relativeJavaClassFilePath));
	}
}
