package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.*;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractUsefulTypeDeclarationVoidVisitor<S> extends AbstractUsefulVoidVisitor<S>
{
	protected AbstractUsefulTypeDeclarationVoidVisitor()
	{
	}

	@Override
	public final void visit(@NotNull final NormalAnnotationExpr normalAnnotationExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final SingleMemberAnnotationExpr singleMemberAnnotationExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final MarkerAnnotationExpr markerAnnotationExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final LambdaExpr lambdaExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final MethodReferenceExpr methodReferenceExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final TypeExpr typeExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final VariableDeclarationExpr variableDeclarationExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final UnaryExpr unaryExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final SuperExpr superExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ThisExpr thisExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final NameExpr nameExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final QualifiedNameExpr qualifiedNameExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ObjectCreationExpr objectCreationExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final MethodCallExpr methodCallExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final InstanceOfExpr instanceOfExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final FieldAccessExpr fieldAccessExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final EnclosedExpr enclosedExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ConditionalExpr conditionalExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ClassExpr classExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final CastExpr castExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final BinaryExpr binaryExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final AssignExpr assignExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ArrayInitializerExpr arrayInitializerExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ArrayCreationExpr arrayCreationExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ArrayAccessExpr arrayAccessExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final NullLiteralExpr nullLiteralExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final BooleanLiteralExpr booleanLiteralExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final DoubleLiteralExpr doubleLiteralExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final CharLiteralExpr charLiteralExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final LongLiteralMinValueExpr longLiteralMinValueExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final IntegerLiteralMinValueExpr integerLiteralMinValueExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final LongLiteralExpr longLiteralExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final IntegerLiteralExpr integerLiteralExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final StringLiteralExpr stringLiteralExpression, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ExplicitConstructorInvocationStmt explicitConstructorInvocationStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final TypeDeclarationStmt typeDeclarationStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final AssertStmt assertStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final BlockStmt blockStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final LabeledStmt labeledStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final EmptyStmt emptyStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ExpressionStmt expressionStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final SwitchStmt switchStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final SwitchEntryStmt switchEntryStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final BreakStmt breakStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ReturnStmt returnStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final IfStmt ifStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final WhileStmt whileStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ContinueStmt continueStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final DoStmt doStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ForeachStmt forEachStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ForStmt forStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ThrowStmt throwStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final SynchronizedStmt synchronizedStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final TryStmt tryStatement, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ClassOrInterfaceType classOrInterfaceType, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final PrimitiveType primitiveType, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ReferenceType referenceType, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final VoidType voidType, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final WildcardType wildcardType, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final UnknownType unknownType, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}
}
