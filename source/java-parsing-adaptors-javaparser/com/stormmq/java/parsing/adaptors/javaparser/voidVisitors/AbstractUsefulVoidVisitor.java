package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors;

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

import static com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.UsefulVoidVisitor.throwUselessVisit;

/*
	This captures stuff that would never be usefully visited
 */
public abstract class AbstractUsefulVoidVisitor<S> implements UsefulVoidVisitor<S>
{
	protected AbstractUsefulVoidVisitor()
	{
	}

	@Override
	public final void visit(@NotNull final BlockComment blockComment, @NotNull final S state)
	{
	}

	@Override
	public final void visit(@NotNull final JavadocComment javadocComment, @NotNull final S state)
	{
	}

	@Override
	public final void visit(@NotNull final LineComment lineComment, @NotNull final S state)
	{
	}

	@Override
	public final void visit(@NotNull final CompilationUnit compilationUnit, @NotNull final S state)
	{
		throwUselessVisit();
	}

	@Override
	public final void visit(@NotNull final PackageDeclaration packageDeclaration, @NotNull final S state)
	{
		throwUselessVisit();
	}

	@Override
	public final void visit(@NotNull final ImportDeclaration importDeclaration, @NotNull final S state)
	{
		throwUselessVisit();
	}

	@Override
	public final void visit(@NotNull final CatchClause catchClause, @NotNull final S state)
	{
		throwUselessVisit();
	}

	@Override
	public final void visit(@NotNull final MultiTypeParameter multiTypeParameter, @NotNull final S state)
	{
		throwUselessVisit();
	}

	@Override
	public final void visit(@NotNull final MemberValuePair memberValuePair, @NotNull final S state)
	{
		throwUselessVisit();
	}

	@Override
	public final void visit(@NotNull final TypeParameter typeParameter, @NotNull final S state)
	{
		throwUselessVisit();
	}

	@Override
	public final void visit(@NotNull final Parameter parameter, @NotNull final S state)
	{
		throwUselessVisit();
	}

	@Override
	public final void visit(@NotNull final VariableDeclarator variableDeclarator, @NotNull final S state)
	{
		throwUselessVisit();
	}

	@Override
	public final void visit(@NotNull final VariableDeclaratorId variableDeclaratorId, @NotNull final S state)
	{
		throwUselessVisit();
	}
}
