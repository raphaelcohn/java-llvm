package com.stormmq.java.parsing.adaptors.javaparser.ast;

import com.github.javaparser.ast.stmt.Statement;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverContext;
import com.stormmq.java.parsing.ast.details.bodyDetails.BodyDetail;
import com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.BodyIntermediate;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class JavaparserBodyIntermediate implements BodyIntermediate
{
	@NotNull private final List<Statement> body;
	@NotNull private final TypeResolverContext typeResolverContext;

	public JavaparserBodyIntermediate(@NotNull final List<Statement> body, @NotNull final TypeResolverContext typeResolverContext)
	{
		this.body = body;
		this.typeResolverContext = typeResolverContext;
	}

	@Override
	public boolean isConcrete()
	{
		return true;
	}

	@Override
	public boolean isAbstract()
	{
		return false;
	}

	@Override
	public boolean isNative()
	{
		return false;
	}

	@NotNull
	@Override
	public BodyDetail resolve()
	{
		return new BodyDetail()
		{
			@Override
			public boolean isNative()
			{
				return false;
			}
		};
	}
}
