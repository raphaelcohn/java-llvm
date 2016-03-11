package com.stormmq.nio;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

import static java.lang.System.arraycopy;

public abstract class AbstractPathUser implements PathUser
{
	@NotNull private final String[] relativePath;

	protected AbstractPathUser(@NotNull final String[] relativePath)
	{
		this.relativePath = relativePath;
	}

	@Override
	public void use(@NotNull final Path path)
	{
		final String[] newPath = addFolderOrFile(relativePath, getFolderOrFileName(path));
		use(path, newPath);
	}

	protected abstract void use(@NotNull final Path path, @NotNull final String[] newPath);

	@NotNull
	private static String getFolderOrFileName(@NotNull final Path path)
	{
		return path.getFileName().toString();
	}

	@NotNull
	private String[] addFolderOrFile(@NotNull final String[] folderPath, @NotNull final String name)
	{
		final int length = folderPath.length;
		final String[] newPaths = new String[length + 1];
		arraycopy(folderPath, 0, newPaths, 0, length);
		newPaths[length] = name;
		return newPaths;
	}
}
