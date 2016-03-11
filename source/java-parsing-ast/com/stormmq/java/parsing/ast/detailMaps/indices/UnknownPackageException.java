package com.stormmq.java.parsing.ast.detailMaps.indices;

import com.stormmq.java.parsing.utilities.names.PackageName;
import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class UnknownPackageException extends RuntimeException
{
	public UnknownPackageException(@NotNull final PackageName packageName)
	{
		super(format(ENGLISH, "The package '%1$s' is not known", packageName.fullyQualifiedNameUsingDotsAndDollarSigns()));
	}
}
