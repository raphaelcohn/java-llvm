package com.stormmq.java.parsing.ast.details.valueDetails;

import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

public final class KnownReferenceTypeNameSingleValueDetail implements SingleValueDetail
{
	@NotNull private final KnownReferenceTypeName value;

	public KnownReferenceTypeNameSingleValueDetail(@NotNull final KnownReferenceTypeName value)
	{
		this.value = value;
	}
}
