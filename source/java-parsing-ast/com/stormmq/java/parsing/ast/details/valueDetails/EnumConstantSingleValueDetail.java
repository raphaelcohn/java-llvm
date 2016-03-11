package com.stormmq.java.parsing.ast.details.valueDetails;

import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

public final class EnumConstantSingleValueDetail implements SingleValueDetail
{
	@NotNull private final KnownReferenceTypeName value;
	@NotNull private final String enumConstantName;

	public EnumConstantSingleValueDetail(@NotNull final KnownReferenceTypeName value, @NotNull final String enumConstantName)
	{
		this.value = value;
		this.enumConstantName = enumConstantName;
	}
}
