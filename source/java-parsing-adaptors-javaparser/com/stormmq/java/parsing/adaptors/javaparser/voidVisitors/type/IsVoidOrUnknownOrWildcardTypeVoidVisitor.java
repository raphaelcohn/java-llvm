package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.type;

import com.github.javaparser.ast.type.*;
import org.jetbrains.annotations.NotNull;

public final class IsVoidOrUnknownOrWildcardTypeVoidVisitor extends AbstractTypeVoidVisitor<Class<Void>>
{
	public static boolean isVoidOrUnknownOrWildcardType(@NotNull final Type type)
	{
		final IsVoidOrUnknownOrWildcardTypeVoidVisitor voidVisitor = new IsVoidOrUnknownOrWildcardTypeVoidVisitor();
		type.accept(voidVisitor, Void.class);
		return voidVisitor.isVoidOrUnknownOrWildcardType;
	}

	public boolean isVoidOrUnknownOrWildcardType;

	private IsVoidOrUnknownOrWildcardTypeVoidVisitor()
	{
		isVoidOrUnknownOrWildcardType = false;
	}

	@Override
	public void visit(@NotNull final UnknownType unknownType, @NotNull final Class<Void> state)
	{
		isVoidOrUnknownOrWildcardType = true;
	}

	@Override
	public void visit(@NotNull final WildcardType wildcardType, @NotNull final Class<Void> state)
	{
		isVoidOrUnknownOrWildcardType = true;
	}

	@Override
	public void visit(@NotNull final VoidType voidType, @NotNull final Class<Void> state)
	{
		isVoidOrUnknownOrWildcardType = true;
	}

	@Override
	public void visit(@NotNull final ReferenceType referenceType, @NotNull final Class<Void> state)
	{
	}

	@Override
	public void visit(@NotNull final PrimitiveType primitiveType, @NotNull final Class<Void> state)
	{
	}

	@Override
	public void visit(@NotNull final ClassOrInterfaceType classOrInterfaceType, @NotNull final Class<Void> state)
	{
	}
}
