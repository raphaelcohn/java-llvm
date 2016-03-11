package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.stormmq.java.parsing.adaptors.javaparser.helpers.FieldDeclarationHelper;
import com.stormmq.java.parsing.adaptors.javaparser.helpers.VariableDeclaratorIdHelper;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.FieldFinality;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.github.javaparser.ast.body.ModifierSet.*;
import static com.stormmq.java.parsing.adaptors.javaparser.StringConstants.A_field_can_not_be_both_volatile_and_final;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.assertNotNull;
import static com.stormmq.java.parsing.utilities.FieldFinality.*;

public abstract class AbstractMemberVoidVisitor<S extends BodyDeclarationState> extends AbstractTypeDeclarationVoidVisitor<S>
{
	protected AbstractMemberVoidVisitor()
	{
	}

	protected abstract void visitStaticInitializer(@NotNull final List<Statement> body, @NotNull final S state);

	protected abstract void visitInstanceInitializer(@NotNull final List<Statement> body, @NotNull final S state);

	protected abstract void visitStaticField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldTypeUsage, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations);

	protected abstract void visitNonStaticField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldTypeUsage, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations);

	protected abstract void useStaticField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldType, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility);

	protected abstract void useInstanceField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldType, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility);

	@Override
	public final void visit(@NotNull final EmptyMemberDeclaration emptyMemberDeclaration, @NotNull final S state)
	{
		guardShouldNotHaveAnnotations(emptyMemberDeclaration, "emptyMemberDeclaration");
	}

	@Override
	public final void visit(@NotNull final InitializerDeclaration initializerDeclaration, @NotNull final S state)
	{
		guardShouldNotHaveAnnotations(initializerDeclaration, "initializerDeclaration");

		final BlockStmt body = validateInitializerAndGetBody(initializerDeclaration);

		final List<Statement> statements = assertNotNull(body.getStmts());
		if (initializerDeclaration.isStatic())
		{
			visitStaticInitializer(statements, state);
		}
		else
		{
			visitInstanceInitializer(statements, state);
		}
	}

	@NotNull
	private static BlockStmt validateInitializerAndGetBody(@NotNull final InitializerDeclaration initializerDeclaration)
	{
		@Nullable final List<AnnotationExpr> annotations = initializerDeclaration.getAnnotations();
		if ((annotations != null) && !annotations.isEmpty())
		{
			throw new IllegalArgumentException("initializers should not have annotations");
		}

		final BlockStmt body = initializerDeclaration.getBlock();
		if (body == null)
		{
			throw new IllegalArgumentException("initializers should always have a body");
		}
		return body;
	}

	@Override
	public final void visit(@NotNull final FieldDeclaration fieldDeclaration, @NotNull final S state)
	{
		@Nullable final List<VariableDeclarator> variableDeclarators = fieldDeclaration.getVariables();
		if ((variableDeclarators == null) || variableDeclarators.isEmpty())
		{
			throw new IllegalStateException("There should always be at least one variableDeclarator");
		}

		final int modifiers = FieldDeclarationHelper.fieldModifiers(fieldDeclaration);
		final boolean isStatic = isStatic(modifiers);
		final boolean isTransient = isTransient(modifiers);
		final boolean isVolatile = isVolatile(modifiers);
		final boolean isFinal = isFinal(modifiers);

		final Visibility visibility = visibility(modifiers, "field");

		final FieldFinality fieldFinality;
		if (isVolatile)
		{
			if (isFinal)
			{
				throw new IllegalArgumentException(A_field_can_not_be_both_volatile_and_final);
			}
			fieldFinality = Volatile;
		}
		else if (isFinal)
		{
			fieldFinality = Final;
		}
		else
		{
			fieldFinality = Normal;
		}

		final Type fieldType = assertNotNull(fieldDeclaration.getType());

		final KnownReferenceTypeName typeName = state.currentTypeName();

		for (final VariableDeclarator variableDeclarator : variableDeclarators)
		{
			final VariableDeclaratorId variableDeclaratorId = assertNotNull(variableDeclarator.getId());
			final String fieldName = state.fieldName(variableDeclaratorId);
			final int cStyleArrayCount = VariableDeclaratorIdHelper.guardCStyleArrayCount(variableDeclaratorId);
			@Nullable final Expression init = variableDeclarator.getInit();

			final TypeUsage fieldTypeUsage = state.fieldTypeUsage(fieldType, cStyleArrayCount);

			final AnnotationInstanceIntermediate[] annotations = annotations(fieldDeclaration, state);

			if (isStatic)
			{
				visitStaticField(typeName, fieldName, fieldTypeUsage, init, isTransient, fieldFinality, visibility, annotations);
			}
			else
			{
				visitNonStaticField(typeName, fieldName, fieldTypeUsage, init, isTransient, fieldFinality, visibility, annotations);
			}
		}
	}

}
