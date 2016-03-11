package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.type;

import com.github.javaparser.ast.type.*;
import org.jetbrains.annotations.NotNull;

public final class IsUnknownWildcardVoidOrArrayOrNotLikelyToBeStringVoidVisitor extends AbstractTypeVoidVisitor<Class<Void>>
{
	public static boolean isUnknownWildcardVoidOrArrayOrNotLikelyToBeString(@NotNull final Type type)
	{
		final IsUnknownWildcardVoidOrArrayOrNotLikelyToBeStringVoidVisitor voidVisitor = new IsUnknownWildcardVoidOrArrayOrNotLikelyToBeStringVoidVisitor();
		type.accept(voidVisitor, Void.class);
		return voidVisitor.isUnknownWildcardVoidOrArrayOrNotLikelyToBeString;
	}

	public boolean isUnknownWildcardVoidOrArrayOrNotLikelyToBeString;

	private IsUnknownWildcardVoidOrArrayOrNotLikelyToBeStringVoidVisitor()
	{
		isUnknownWildcardVoidOrArrayOrNotLikelyToBeString = false;
	}

	@Override
	public void visit(@NotNull final UnknownType unknownType, @NotNull final Class<Void> state)
	{
		isUnknownWildcardVoidOrArrayOrNotLikelyToBeString = true;
	}

	@Override
	public void visit(@NotNull final WildcardType wildcardType, @NotNull final Class<Void> state)
	{
		isUnknownWildcardVoidOrArrayOrNotLikelyToBeString = true;
	}

	@Override
	public void visit(@NotNull final VoidType voidType, @NotNull final Class<Void> state)
	{
		isUnknownWildcardVoidOrArrayOrNotLikelyToBeString = true;
	}

	@Override
	public void visit(@NotNull final ReferenceType referenceType, @NotNull final Class<Void> state)
	{
		if (referenceType.getArrayCount() != 0)
		{
			isUnknownWildcardVoidOrArrayOrNotLikelyToBeString = true;
		}
	}

	@Override
	public void visit(@NotNull final PrimitiveType primitiveType, @NotNull final Class<Void> state)
	{
	}

	@Override
	public void visit(@NotNull final ClassOrInterfaceType classOrInterfaceType, @NotNull final Class<Void> state)
	{
		final String name = classOrInterfaceType.getName();
		if (name.equals("String") || name.equals("java.lang.String"))
		{
			return;
		}
		isUnknownWildcardVoidOrArrayOrNotLikelyToBeString = true;
	}
}
