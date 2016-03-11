package com.stormmq.java.parsing.adaptors.javaparser.ast;

import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.compilationUnitWrapperUsers.CompilationUnitWrapperUser;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.CompilationUnitWrapper;
import com.stormmq.java.parsing.fileParsers.caches.Cache;
import com.stormmq.java.parsing.fileParsers.FileParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.stormmq.nio.FileAndFolderHelper.*;
import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static java.util.Locale.ENGLISH;
import static com.github.javaparser.AstParserAdaptor.parse;

public final class JavaparserSourceFileParser implements FileParser
{
	@NotNull private final Cache<CompilationUnitWrapper> compilationUnitHelperCache;
	@NotNull private final CompilationUnitWrapperUser compilationUnitWrapperUser;

	public JavaparserSourceFileParser(@NotNull final Cache<CompilationUnitWrapper> compilationUnitHelperCache, @NotNull final CompilationUnitWrapperUser compilationUnitWrapperUser)
	{
		this.compilationUnitHelperCache = compilationUnitHelperCache;
		this.compilationUnitWrapperUser = compilationUnitWrapperUser;
	}

	public void parseFile(@NotNull final Path filePath, @NotNull final Path sourceRootPath)
	{
		final Path relativeFilePath = relativeToRootPath(sourceRootPath, filePath);
		final String filePathName = filePath.toString();

		@NotNull final CompilationUnitWrapper compilationUnitWrapper;
		@Nullable final CompilationUnitWrapper cachedCompilationUnitWrapper = retrieveCachedCompilationUnitHelper(filePathName);
		if (cachedCompilationUnitWrapper == null)
		{
			final byte[] code;
			try
			{
				code = readAllBytes(filePath);
			}
			catch (IOException e)
			{
				throw new IllegalStateException(format(ENGLISH, "Could not read file '%s'", filePathName));
			}

			compilationUnitWrapper = parseBytes(filePathName, code);
		}
		else
		{
			compilationUnitWrapper = cachedCompilationUnitWrapper;
		}

		useCompilationUnit(relativeFilePath, compilationUnitWrapper);
	}

	@Override
	public void parseFile(@NotNull final ZipFile zipFile, @NotNull final ZipEntry zipEntry)
	{
		final String name = zipEntry.getName();
		final String filePathName = getFilePathName(zipFile, name);

		@NotNull final CompilationUnitWrapper compilationUnitWrapper;
		@Nullable final CompilationUnitWrapper cachedCompilationUnitWrapper = retrieveCachedCompilationUnitHelper(filePathName);
		if (cachedCompilationUnitWrapper == null)
		{
			final byte[] code = getZipEntryBytes(zipFile, zipEntry);

			compilationUnitWrapper = parseBytes(filePathName, code);
		}
		else
		{
			compilationUnitWrapper = cachedCompilationUnitWrapper;
		}
		final Path relativeFilePath = get(name);

		useCompilationUnit(relativeFilePath, compilationUnitWrapper);
	}

	@Nullable
	private CompilationUnitWrapper retrieveCachedCompilationUnitHelper(@NotNull final String filePathName)
	{
		return compilationUnitHelperCache.retrieve(filePathName);
	}

	@NotNull
	private CompilationUnitWrapper parseBytes(@NotNull final String filePathName, @NotNull final byte[] code)
	{
		final Charset encoding = detectCharacterSet(code);

		@NotNull final InputStream inputStream = new ByteArrayInputStream(code);

		final CompilationUnit compilationUnit;
		try
		{
			compilationUnit = parse(inputStream, encoding);
		}
		catch (final ParseException e)
		{
			throw new IllegalStateException(format(ENGLISH, "Could not parse file '%s' because '%s'", filePathName, e.getMessage()), e);
		}

		final CompilationUnitWrapper compilationUnitWrapper = new CompilationUnitWrapper(compilationUnit);
		compilationUnitHelperCache.cache(filePathName, compilationUnitWrapper);

		return compilationUnitWrapper;
	}

	private void useCompilationUnit(@NotNull final Path relativeFilePath, @NotNull final CompilationUnitWrapper compilationUnitWrapper)
	{
		compilationUnitWrapperUser.use(relativeFilePath, compilationUnitWrapper);
	}
}
