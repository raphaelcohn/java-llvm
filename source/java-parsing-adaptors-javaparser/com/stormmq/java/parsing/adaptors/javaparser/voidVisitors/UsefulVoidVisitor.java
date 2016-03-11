package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.jetbrains.annotations.NotNull;

public interface UsefulVoidVisitor<S> extends VoidVisitor<S>
{
	static void throwShouldNotBeVisited()
	{
		throw new UnsupportedOperationException("Should not be visited");
	}

	static void throwUselessVisit()
	{
		throw new UnsupportedOperationException("Useless to visit as context always known");
	}

	@Override
	void visit(@NotNull final NormalAnnotationExpr normalAnnotationExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final SingleMemberAnnotationExpr singleMemberAnnotationExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final MarkerAnnotationExpr markerAnnotationExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final LambdaExpr lambdaExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final MethodReferenceExpr methodReferenceExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final TypeExpr typeExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final VariableDeclarationExpr variableDeclarationExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final UnaryExpr unaryExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final SuperExpr superExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final ThisExpr thisExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final NameExpr nameExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final QualifiedNameExpr qualifiedNameExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final ObjectCreationExpr objectCreationExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final MethodCallExpr methodCallExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final InstanceOfExpr instanceOfExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final FieldAccessExpr fieldAccessExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final EnclosedExpr enclosedExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final ConditionalExpr conditionalExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final ClassExpr classExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final CastExpr castExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final BinaryExpr binaryExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final AssignExpr assignExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final ArrayInitializerExpr arrayInitializerExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final ArrayCreationExpr arrayCreationExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final ArrayAccessExpr arrayAccessExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final BlockComment blockComment, @NotNull final S state);

	@Override
	void visit(@NotNull final JavadocComment javadocComment, @NotNull final S state);

	@Override
	void visit(@NotNull final LineComment lineComment, @NotNull final S state);

	@Override
	void visit(@NotNull final NullLiteralExpr nullLiteralExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final BooleanLiteralExpr booleanLiteralExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final DoubleLiteralExpr doubleLiteralExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final CharLiteralExpr charLiteralExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final LongLiteralMinValueExpr longLiteralMinValueExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final IntegerLiteralMinValueExpr integerLiteralMinValueExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final LongLiteralExpr longLiteralExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final IntegerLiteralExpr integerLiteralExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final StringLiteralExpr stringLiteralExpression, @NotNull final S state);

	@Override
	void visit(@NotNull final EnumConstantDeclaration enumConstantDeclaration, @NotNull final S state);

	@Override
	void visit(@NotNull final FieldDeclaration fieldDeclaration, @NotNull final S state);

	@Override
	void visit(@NotNull final InitializerDeclaration initializerDeclaration, @NotNull final S state);

	@Override
	void visit(@NotNull final ConstructorDeclaration constructorDeclaration, @NotNull final S state);

	@Override
	void visit(@NotNull final MethodDeclaration methodDeclaration, @NotNull final S state);

	@Override
	void visit(@NotNull final AnnotationMemberDeclaration annotationMemberDeclaration, @NotNull final S state);

	@Override
	void visit(@NotNull final EmptyMemberDeclaration emptyMemberDeclaration, @NotNull final S state);

	@Override
	void visit(@NotNull final ClassOrInterfaceType classOrInterfaceType, @NotNull final S state);

	@Override
	void visit(@NotNull final PrimitiveType primitiveType, @NotNull final S state);

	@Override
	void visit(@NotNull final ReferenceType referenceType, @NotNull final S state);

	@Override
	void visit(@NotNull final VoidType voidType, @NotNull final S state);

	@Override
	void visit(@NotNull final WildcardType wildcardType, @NotNull final S state);

	@Override
	void visit(@NotNull final UnknownType unknownType, @NotNull final S state);
}
