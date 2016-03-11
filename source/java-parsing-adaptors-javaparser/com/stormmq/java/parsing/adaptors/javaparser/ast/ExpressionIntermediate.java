package com.stormmq.java.parsing.adaptors.javaparser.ast;

import com.github.javaparser.ast.expr.Expression;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverContext;
import org.jetbrains.annotations.NotNull;

public final class ExpressionIntermediate
{
	@NotNull private final Expression expression;
	@NotNull private final TypeResolverContext typeResolverContext;

	public ExpressionIntermediate(@NotNull final Expression expression, @NotNull final TypeResolverContext typeResolverContext)
	{
		this.expression = expression;
		this.typeResolverContext = typeResolverContext;
	}
}
