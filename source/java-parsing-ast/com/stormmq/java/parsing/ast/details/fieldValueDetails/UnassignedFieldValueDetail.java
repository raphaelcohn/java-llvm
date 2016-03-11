package com.stormmq.java.parsing.ast.details.fieldValueDetails;

import org.jetbrains.annotations.NotNull;

public final class UnassignedFieldValueDetail implements FieldValueDetail
{
	@NotNull
	public static final FieldValueDetail Unassigned = new UnassignedFieldValueDetail();

	private UnassignedFieldValueDetail()
	{
	}
}
