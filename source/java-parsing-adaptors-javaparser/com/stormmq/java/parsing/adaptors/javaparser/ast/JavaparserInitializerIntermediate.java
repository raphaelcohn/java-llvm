package com.stormmq.java.parsing.adaptors.javaparser.ast;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.stormmq.java.parsing.ast.intermediates.InitializerIntermediate;
import org.jetbrains.annotations.NotNull;

public final class JavaparserInitializerIntermediate implements InitializerIntermediate
{
	@NotNull private final BlockStmt block;

	public JavaparserInitializerIntermediate(@NotNull final BlockStmt block)
	{
		this.block = block;
	}
}
