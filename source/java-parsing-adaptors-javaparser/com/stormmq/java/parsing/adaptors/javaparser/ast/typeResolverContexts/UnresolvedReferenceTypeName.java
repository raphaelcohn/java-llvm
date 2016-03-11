package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.github.javaparser.ast.expr.NameExpr;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.ReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.assertNotNull;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.NameExprHelper.qualifiedName;

public final class UnresolvedReferenceTypeName implements ReferenceTypeName
{
	@NotNull public static final UnresolvedReferenceTypeName[] Empty = {};

	@NotNull
	public static UnresolvedReferenceTypeName[] convertNameExprToUnresolvedReferenceTypeNames(@Nullable final List<NameExpr> nameExprs, @NotNull final TypeResolverContext typeResolverContext)
	{
		if (nameExprs == null)
		{
			return Empty;
		}

		final int size = nameExprs.size();
		if (size == 0)
		{
			return Empty;
		}

		final UnresolvedReferenceTypeName[] unresolvedReferenceTypeNames = new UnresolvedReferenceTypeName[size];
		for (int index = 0; index < size; index++)
		{
			unresolvedReferenceTypeNames[index] = convertNameExprToUnresolveReferenceTypeName(assertNotNull(nameExprs.get(index)), typeResolverContext);
		}
		return unresolvedReferenceTypeNames;
	}

	@NotNull
	public static UnresolvedReferenceTypeName convertNameExprToUnresolveReferenceTypeName(@NotNull final NameExpr nameExpr, @NotNull final TypeResolverContext typeResolverContext)
	{
		return new UnresolvedReferenceTypeName(qualifiedName(nameExpr), typeResolverContext);
	}

	private UnresolvedReferenceTypeName(@NotNull final String qualifiedName, @NotNull final TypeResolverContext typeResolverContext)
	{
	}

	@Override
	public boolean isVoid()
	{
		return false;
	}

	@Override
	public boolean isPrimitive()
	{
		return false;
	}

	@Override
	public boolean isJavaLangObject()
	{
		throw new UnsupportedOperationException("Can not be sure");
	}

	@NotNull
	@Override
	public KnownReferenceTypeName resolve()
	{
		throw new UnsupportedOperationException("Resolve");
	}
}
