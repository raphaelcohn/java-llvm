package com.stormmq.java.parsing.adaptors.javac;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;
import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.stormmq.nio.FileAndFolderHelper.detectCharacterSet;
import static java.nio.file.Files.*;
import static java.nio.file.StandardOpenOption.*;
import static javax.tools.JavaFileObject.Kind.SOURCE;

public final class PathSourceJavaFileObject implements JavaFileObject
{
	@NotNull private static final Charset Utf8 = Charset.forName("UTF-8");

	@NotNull private final Path sourceFilePath;

	public PathSourceJavaFileObject(@NotNull final Path sourceFilePath)
	{
		this.sourceFilePath = sourceFilePath;
	}

	@Override
	@NotNull
	public Kind getKind()
	{
		return SOURCE;
	}

	@Override
	@Nullable
	public NestingKind getNestingKind()
	{
		return null;
	}

	@Override
	@Nullable
	public Modifier getAccessLevel()
	{
		return null;
	}

	@Override
	public boolean isNameCompatible(@NotNull final String simpleName, @NotNull final Kind kind)
	{
		return kind == SOURCE && simpleName.equals(sourceFilePath.getFileName().toString());
	}

	@Override
	public boolean delete()
	{
		try
		{
			Files.delete(sourceFilePath);
		}
		catch (IOException e)
		{
			return false;
		}
		return true;
	}

	@Override
	@NotNull
	public URI toUri()
	{
		return sourceFilePath.toAbsolutePath().toUri();
	}

	@Override
	@NotNull
	public String getName()
	{
		return sourceFilePath.toString();
	}

	@Override
	@NotNull
	public OutputStream openOutputStream() throws IOException
	{
		return newOutputStream(sourceFilePath, CREATE, WRITE, TRUNCATE_EXISTING, CREATE_NEW);
	}

	@Override
	@NotNull
	public InputStream openInputStream() throws IOException
	{
		return newInputStream(sourceFilePath, READ);
	}

	@Override
	@NotNull
	public Reader openReader(final boolean ignoreEncodingErrors) throws IOException
	{
		final Charset encoding = detectCharset();
		return newBufferedReader(sourceFilePath, encoding);
	}

	@Override
	public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException
	{
		final StringBuffer stringBuffer = new StringBuffer(8192);
		final char[] data = new char[8192];
		try(final Reader reader = openReader(ignoreEncodingErrors))
		{
			while (true)
			{
				final int charactersRead = reader.read(data);
				if (charactersRead == -1)
				{
					break;
				}
				stringBuffer.append(data, 0, charactersRead);
			}
		}
		return stringBuffer;
	}

	@Override
	@NotNull
	public Writer openWriter() throws IOException
	{
		return newBufferedWriter(sourceFilePath, Utf8, CREATE, WRITE, TRUNCATE_EXISTING, CREATE_NEW);
	}

	@Override
	public long getLastModified()
	{
		try
		{
			return getLastModifiedTime(sourceFilePath).toMillis();
		}
		catch (IOException e)
		{
			return 0L;
		}
	}

	@NotNull
	private Charset detectCharset() throws IOException
	{
		return detectCharacterSet(readAllBytes(sourceFilePath));
	}
}
