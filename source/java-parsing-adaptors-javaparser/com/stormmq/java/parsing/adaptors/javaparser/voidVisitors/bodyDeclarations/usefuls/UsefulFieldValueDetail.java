package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls;

import com.github.javaparser.ast.expr.Expression;
import com.stormmq.java.parsing.ast.details.fieldValueDetails.FieldValueDetail;
import org.jetbrains.annotations.NotNull;

public final class UsefulFieldValueDetail implements FieldValueDetail
{
	private final boolean shouldBeConstant;

	public UsefulFieldValueDetail(@NotNull final Expression init, final boolean shouldBeConstant, final boolean isAnnotationConstant)
	{
		this.shouldBeConstant = shouldBeConstant;
	}
}
