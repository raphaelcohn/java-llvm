package com.stormmq.java.parsing.adaptors.asm.ast;

import com.stormmq.java.parsing.adaptors.asm.classReaderUsers.ClassReaderUser;
import com.stormmq.java.parsing.fileParsers.caches.Cache;
import com.stormmq.java.parsing.fileParsers.FileParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.stormmq.nio.FileAndFolderHelper.*;
import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static java.util.Locale.ENGLISH;

public final class AsmClassFileParser implements FileParser
{
	@NotNull private final Cache<byte[]> codeCache;
	@NotNull private final ClassReaderUser classReaderUser;

	public AsmClassFileParser(@NotNull final Cache<byte[]> codeCache, @NotNull final ClassReaderUser classReaderUser)
	{
		this.codeCache = codeCache;
		this.classReaderUser = classReaderUser;
	}

	@Override
	public void parseFile(@NotNull final Path filePath, @NotNull final Path sourceRootPath)
	{
		final Path relativeFilePath = relativeToRootPath(sourceRootPath, filePath);
		final String filePathName = filePath.toString();

		@Nullable final byte[] cachedCode = codeCache.retrieve(filePathName);
		@NotNull final byte[] code;
		if (cachedCode == null)
		{
			try
			{
				code = readAllBytes(filePath);
			}
			catch (IOException e)
			{
				throw new IllegalStateException(format(ENGLISH, "Could not read class file '%1$s' because of '%2$s'", filePathName, e.getMessage()), e);
			}
			codeCache.cache(filePathName, code);
		}
		else
		{
			code = cachedCode;
		}

		useCode(relativeFilePath, code);
	}

	@Override
	public void parseFile(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry)
	{
		final String name = zipEntry.getName();
		final String filePathName = getFilePathName(zipFile, name);

		@Nullable final byte[] cachedCode = codeCache.retrieve(filePathName);
		@NotNull final byte[] code;
		if (cachedCode == null)
		{
			code = getZipEntryBytes(zipFile, zipEntry);
			codeCache.cache(filePathName, code);
		}
		else
		{
			code = cachedCode;
		}

		final Path relativeFilePath = get(name);
		useCode(relativeFilePath, code);
	}

	public void useCode(@NotNull final Path relativeFilePath, @NotNull final byte[] code)
	{
		classReaderUser.use(relativeFilePath, new ClassReader(code));
	}
}
