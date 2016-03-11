package com.stormmq.java.parsing.ast.detailMaps.indices;

import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class UnknownReferenceTypeException extends RuntimeException
{
	public UnknownReferenceTypeException(@NotNull final KnownReferenceTypeName knownReferenceTypeName)
	{
		super(format(ENGLISH, "The type reference '%1$s' is not known", knownReferenceTypeName.fullyQualifiedNameUsingDotsAndDollarSigns()));
	}
}
