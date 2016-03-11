package com.stormmq.nio;

import org.jetbrains.annotations.NotNull;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.String.format;
import static java.nio.charset.Charset.forName;
import static java.nio.file.FileVisitOption.FOLLOW_LINKS;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Files.walkFileTree;
import static java.util.EnumSet.of;
import static java.util.Locale.ENGLISH;

public final class FileAndFolderHelper
{
	@NotNull public static final EnumSet<FileVisitOption> FollowLinks = of(FOLLOW_LINKS);
	public static final int MaximumBytesToUseToDetectCharacterSet = 1024;

	private FileAndFolderHelper()
	{
	}

	public static void walkTreeFollowingSymlinks(@NotNull final Path rootPath, @NotNull final SimpleFileVisitor<Path> simpleFileVisitor)
	{
		try
		{
			walkFileTree(rootPath, FollowLinks, MAX_VALUE, simpleFileVisitor);
		}
		catch (final IOException e)
		{
			throw new IllegalStateException("Could not walk tree", e);
		}
	}

	// Only works if is an exact subset
	@NotNull
	public static Path relativeToRootPath(@NotNull final Path rootPath, @NotNull final Path filePathDescendingFromRootPath)
	{
		final int size = rootPath.getNameCount();
		return filePathDescendingFromRootPath.subpath(size, filePathDescendingFromRootPath.getNameCount());
	}

	public static boolean hasFileExtension(@NotNull final Path path, @NotNull final String fileExtension)
	{
		return path.getFileName().toString().endsWith('.' + fileExtension);
	}

	public static void removeAllFoldersAndFilesBelowPath(@NotNull final Path path)
	{
		try
		{
			walkFileTree(path, new FileVisitor<Path>()
			{
				@Override
				public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException
				{
					return CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException
				{
					deleteIfExists(file);
					return CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException
				{
					throw new IllegalStateException(format(ENGLISH, "Could not visit file '%1$s' because of '%2$s'", file.toString(), exc.getMessage()), exc);
				}

				@Override
				public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException
				{
					deleteIfExists(dir);
					return CONTINUE;
				}
			});
		}
		catch (final IOException e)
		{
			throw new IllegalStateException(format(ENGLISH, "Could not remove all folders and files below '%1$s' because of '%2$s'", path.toString(), e.getMessage()), e);
		}
	}

	@NotNull
	public static Charset detectCharacterSet(@NotNull final byte[] code)
	{
		final String[] charsetDetected = {"UTF-8"};
		final nsDetector nsDetector = new nsDetector(nsPSMDetector.ALL);
		nsDetector.Init(new nsICharsetDetectionObserver()
		{
			public void Notify(final String charset)
			{
				charsetDetected[0] = charset;
			}
		});

		final int length = code.length;
		final int bytesToUseToDetectCharacterSet = length > MaximumBytesToUseToDetectCharacterSet ? MaximumBytesToUseToDetectCharacterSet : length;

		final boolean isAscii = nsDetector.isAscii(code, bytesToUseToDetectCharacterSet);
		if (!isAscii)
		{
			// There is a slight possibility that the amount of data to read is too short; we'll default to UTF-8
			nsDetector.DoIt(code, bytesToUseToDetectCharacterSet, false);
			nsDetector.DataEnd();
		}
		else
		{
			charsetDetected[0] = "ASCII";
		}

		return forName(charsetDetected[0]);
	}

	@NotNull
	public static byte[] getZipEntryBytes(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry)
	{
		final byte[] code;
		try (final InputStream inputStream = zipFile.getInputStream(zipEntry))
		{
			final int length = (int) zipEntry.getSize();
			code = new byte[length];
			int offset = 0;
			int remaining = length;
			while (remaining > 0)
			{
				final int read = inputStream.read(code, offset, remaining);
				if (read < 0)
				{
					break;
				}
				offset = offset + read;
				remaining = remaining - read;
			}
		}
		catch (final IOException e)
		{
			throw new IllegalStateException(format(ENGLISH, "Could not read jar / zip entry '1$s' in archive '%2$s' because of '%3$s'", zipEntry.getName(), zipFile.getName(), e.getMessage()), e);
		}
		return code;
	}

	@NotNull
	public static String getFilePathName(@NotNull final ZipFile zipFile, @NotNull final String name)
	{
		return zipFile.getName() + "!/" + name;
	}
}
