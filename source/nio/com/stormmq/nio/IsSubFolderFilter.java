package com.stormmq.nio;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.isReadable;

public final class IsSubFolderFilter extends AbstractDirectoryFilter
{
	@NotNull public static final IsSubFolderFilter IsSubFolder = new IsSubFolderFilter();

	private IsSubFolderFilter()
	{
	}

	@Override
	public boolean accept(@NotNull final Path entry)
	{
		return isReadable(entry) && isDirectory(entry);
	}
}
