package com.stormmq.nio;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

import static java.nio.file.Files.*;

public abstract class AbstractDirectoryFilter implements DirectoryStream.Filter<Path>
{
	protected AbstractDirectoryFilter()
	{
	}

	public abstract boolean accept(@NotNull final Path entry);

	public final void filter(@NotNull final Path folderPath, @NotNull final PathUser pathUser) throws IOException
	{
		if (isReadable(folderPath) && isDirectory(folderPath))
		{
			try (final DirectoryStream<Path> directoryStream = newDirectoryStream(folderPath, this))
			{
				for (final Path path : directoryStream)
				{
					pathUser.use(path);
				}
			}
		}
	}
}
