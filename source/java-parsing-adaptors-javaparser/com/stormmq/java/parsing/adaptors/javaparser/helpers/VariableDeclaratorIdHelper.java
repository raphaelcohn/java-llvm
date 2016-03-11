package com.stormmq.java.parsing.adaptors.javaparser.helpers;

import com.github.javaparser.ast.body.VariableDeclaratorId;
import org.jetbrains.annotations.NotNull;

public final class VariableDeclaratorIdHelper
{
	private VariableDeclaratorIdHelper()
	{
	}

	public static int guardCStyleArrayCount(@NotNull final VariableDeclaratorId variableDeclaratorId)
	{
		final int cStyleArrayCount = variableDeclaratorId.getArrayCount();
		if (cStyleArrayCount < 0)
		{
			throw new IllegalArgumentException("C-Style array count can not be negative");
		}
		return cStyleArrayCount;
	}
}
