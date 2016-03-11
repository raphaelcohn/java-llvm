package com.stormmq.nio;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

import static com.stormmq.nio.FileAndFolderHelper.hasFileExtension;
import static java.nio.file.Files.isReadable;
import static java.nio.file.Files.isRegularFile;

public class IsFileTypeFilter extends AbstractDirectoryFilter
{
	@NotNull private static final String java = "java";
	@NotNull public static final IsFileTypeFilter IsJavaFile = new IsFileTypeFilter(java);
	@SuppressWarnings("DuplicateStringLiteralInspection") @NotNull public static final IsFileTypeFilter IsJavaOrClassFile = new IsFileTypeFilter(java, "class");
	@NotNull public static final IsFileTypeFilter IsJarOrZipFile = new IsFileTypeFilter("jar", "zip");

	@NotNull private final String[] fileExtensions;

	public IsFileTypeFilter(@NonNls @NotNull final String... fileExtensions)
	{
		this.fileExtensions = fileExtensions;
	}

	@Override
	public final boolean accept(@NotNull final Path entry)
	{
		for (final String fileExtension : fileExtensions)
		{
			if (hasFileExtension(entry, fileExtension))
			{
				return isReadable(entry) && isRegularFile(entry);
			}
		}
		return false;
	}
}
