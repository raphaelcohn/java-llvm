package com.stormmq.java.parsing.adaptors.javaparser.genericVisitors;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.ast.visitor.GenericVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface UsefulGenericVisitor<R, S> extends GenericVisitor<R, S>
{
	@Override
	@NotNull
	R visit(@NotNull final NormalAnnotationExpr normalAnnotationExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final SingleMemberAnnotationExpr singleMemberAnnotationExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final MarkerAnnotationExpr markerAnnotationExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final LambdaExpr lambdaExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final MethodReferenceExpr methodReferenceExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final TypeExpr typeExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final VariableDeclarationExpr variableDeclarationExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final UnaryExpr unaryExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final SuperExpr superExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ThisExpr thisExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final NameExpr nameExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final QualifiedNameExpr qualifiedNameExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ObjectCreationExpr objectCreationExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final MethodCallExpr methodCallExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final InstanceOfExpr instanceOfExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final FieldAccessExpr fieldAccessExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final EnclosedExpr enclosedExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ConditionalExpr conditionalExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ClassExpr classExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final CastExpr castExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final BinaryExpr binaryExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final AssignExpr assignExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ArrayInitializerExpr arrayInitializerExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ArrayCreationExpr arrayCreationExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ArrayAccessExpr arrayAccessExpression, @NotNull final S state);

	@Override
	@Nullable
	R visit(@NotNull final BlockComment blockComment, @NotNull final S state);

	@Override
	@Nullable
	R visit(@NotNull final JavadocComment javadocComment, @NotNull final S state);

	@Override
	@Nullable
	R visit(@NotNull final LineComment lineComment, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final NullLiteralExpr nullLiteralExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final BooleanLiteralExpr booleanLiteralExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final DoubleLiteralExpr doubleLiteralExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final CharLiteralExpr charLiteralExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final LongLiteralMinValueExpr longLiteralMinValueExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final IntegerLiteralMinValueExpr integerLiteralMinValueExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final LongLiteralExpr longLiteralExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final IntegerLiteralExpr integerLiteralExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final StringLiteralExpr stringLiteralExpression, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final EnumConstantDeclaration enumConstantDeclaration, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final FieldDeclaration fieldDeclaration, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final InitializerDeclaration initializerDeclaration, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ConstructorDeclaration constructorDeclaration, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final MethodDeclaration methodDeclaration, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final AnnotationMemberDeclaration annotationMemberDeclaration, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final EmptyMemberDeclaration emptyMemberDeclaration, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ClassOrInterfaceType classOrInterfaceType, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final PrimitiveType primitiveType, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ReferenceType referenceType, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final VoidType voidType, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final WildcardType wildcardType, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final UnknownType unknownType, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final CompilationUnit compilationUnit, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final PackageDeclaration packageDeclaration, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ImportDeclaration importDeclaration, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final TypeParameter typeParameter, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ClassOrInterfaceDeclaration classOrInterfaceDeclaration, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final EnumDeclaration enumDeclaration, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final EmptyTypeDeclaration emptyTypeDeclaration, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final AnnotationDeclaration annotationDeclaration, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final VariableDeclarator variableDeclarator, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final VariableDeclaratorId variableDeclaratorId, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final Parameter parameter, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final MultiTypeParameter multiTypeParameter, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final MemberValuePair memberValuePair, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ExplicitConstructorInvocationStmt explicitConstructorInvocationStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final TypeDeclarationStmt typeDeclarationStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final AssertStmt assertStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final BlockStmt blockStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final LabeledStmt labeledStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final EmptyStmt emptyStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ExpressionStmt expressionStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final SwitchStmt switchStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final SwitchEntryStmt switchEntryStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final BreakStmt breakStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ReturnStmt returnStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final IfStmt ifStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final WhileStmt whileStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ContinueStmt continueStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final DoStmt doStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ForeachStmt forEachStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ForStmt forStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final ThrowStmt throwStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final SynchronizedStmt synchronizedStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final TryStmt tryStatement, @NotNull final S state);

	@Override
	@NotNull
	R visit(@NotNull final CatchClause catchClause, @NotNull final S state);
}
