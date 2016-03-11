package com.stormmq.java.parsing.ast.typeUsages;

import org.jetbrains.annotations.NotNull;

public interface TypeUsage
{
	@NotNull TypeUsage[] EmptyTypeUsages = {};

	int numberOfArrayDimensions();

	@NotNull
	TypeUsage resolve();

	boolean isMultiDimensionalArray();

	boolean isPrimitive();

	boolean isArray();

	boolean isVoid();

	boolean couldBeString();
}
