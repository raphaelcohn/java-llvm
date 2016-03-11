package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.github.javaparser.ast.body.ModifierSet.isProtected;
import static com.github.javaparser.ast.body.ModifierSet.isPublic;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.*;
import static com.stormmq.java.parsing.utilities.Visibility.Private;

public abstract class AbstractEnumClassMemberVoidVisitor<S extends BodyDeclarationState> extends AbstractStaticOrEnumClassMemberVoidVisitor<S>
{
	protected AbstractEnumClassMemberVoidVisitor()
	{
	}

	protected abstract void useEnumConstant(@NotNull final String enumConstantName, @Nullable final KnownReferenceTypeName enumConstantClassName, @NotNull final List<Expression> arguments, final int ordinal, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final List<BodyDeclaration> classBody);

	@Override
	protected final void visitEnumConstant(@NotNull final EnumConstantDeclaration enumConstantDeclaration, @NotNull final S state, @NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		final String enumConstantName = enumConstantName(enumConstantDeclaration);

		final List<Expression> arguments = nullToEmptyList(enumConstantDeclaration.getArgs());
		for (Expression argument : arguments)
		{
			assertNotNull(argument);
		}

		final List<BodyDeclaration> classBody = nullToEmptyList(enumConstantDeclaration.getClassBody());
		for (BodyDeclaration bodyDeclaration : classBody)
		{
			assertNotNull(bodyDeclaration);
		}

		@Nullable final KnownReferenceTypeName enumConstantClassName = classBody.isEmpty() ? null : state.enumConstantAsInnerClassName(enumConstantDeclaration);

		final int ordinal = state.nextEnumConstantOrdinal(enumConstantDeclaration);

		useEnumConstant(enumConstantName, enumConstantClassName, arguments, ordinal, annotations, classBody);
	}

	@Override
	@NotNull
	protected final Visibility constructorVisibility(final int modifiers)
	{
		if (isPublic(modifiers))
		{
			throw new IllegalArgumentException("A constructor in an enum can not be public");
		}

		if (isProtected(modifiers))
		{
			throw new IllegalArgumentException("A constructor in an enum can not be protected");
		}

		return Private;
	}

	@NotNull
	private static String enumConstantName(@NotNull final EnumConstantDeclaration enumConstantDeclaration)
	{
		return validateName(enumConstantDeclaration.getName(), false);
	}
}
