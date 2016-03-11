package com.stormmq.java.parsing.adaptors.javaparser.genericVisitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.MultiTypeParameter;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.stmt.CatchClause;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractUsefulGenericVisitor<R, S> implements UsefulGenericVisitor<R, S>
{
	protected AbstractUsefulGenericVisitor()
	{
	}

	@Override
	@Nullable
	public final R visit(@NotNull final BlockComment blockComment, @NotNull final S state)
	{
		return null;
	}

	@Override
	@Nullable
	public final R visit(@NotNull final JavadocComment javadocComment, @NotNull final S state)
	{
		return null;
	}

	@Override
	@Nullable
	public final R visit(@NotNull final LineComment lineComment, @NotNull final S state)
	{
		return null;
	}

	@Override
	@NotNull
	public final R visit(@NotNull final CompilationUnit compilationUnit, @NotNull final S state)
	{
		throw new UnsupportedOperationException("Useless to visit as context always known");
	}

	@Override
	@NotNull
	public final R visit(@NotNull final PackageDeclaration packageDeclaration, @NotNull final S state)
	{
		throw new UnsupportedOperationException("Useless to visit as context always known");
	}

	@Override
	@NotNull
	public final R visit(@NotNull final ImportDeclaration importDeclaration, @NotNull final S state)
	{
		throw new UnsupportedOperationException("Useless to visit as context always known");
	}

	@Override
	@NotNull
	public final R visit(@NotNull final CatchClause catchClause, @NotNull final S state)
	{
		throw new UnsupportedOperationException("Useless to visit as context always known");
	}

	@Override
	@NotNull
	public final R visit(@NotNull final MultiTypeParameter multiTypeParameter, @NotNull final S state)
	{
		throw new UnsupportedOperationException("Useless to visit as context always known");
	}

	@Override
	@NotNull
	public final R visit(@NotNull final MemberValuePair memberValuePair, @NotNull final S state)
	{
		throw new UnsupportedOperationException("Useless to visit as context always known");
	}

	@Override
	@NotNull
	public final R visit(@NotNull final TypeParameter typeParameter, @NotNull final S state)
	{
		throw new UnsupportedOperationException("Useless to visit as context always known");
	}

	@Override
	@NotNull
	public final R visit(@NotNull final Parameter parameter, @NotNull final S state)
	{
		throw new UnsupportedOperationException("Useless to visit as context always known");
	}

	@Override
	@NotNull
	public final R visit(@NotNull final VariableDeclarator variableDeclarator, @NotNull final S state)
	{
		throw new UnsupportedOperationException("Useless to visit as context always known");
	}

	@Override
	@NotNull
	public final R visit(@NotNull final VariableDeclaratorId variableDeclaratorId, @NotNull final S state)
	{
		throw new UnsupportedOperationException("Useless to visit as context always known");
	}
}
