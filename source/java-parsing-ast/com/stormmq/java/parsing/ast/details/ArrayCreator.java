package com.stormmq.java.parsing.ast.details;

import org.jetbrains.annotations.NotNull;

public interface ArrayCreator<D>
{
	@NotNull public D[] empty();

	@NotNull D[] create(final int size);
}
