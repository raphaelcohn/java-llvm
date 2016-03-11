package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.Visibility;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.adaptors.javaparser.StringConstants.constructor;

public abstract class AbstractStaticClassMemberVoidVisitor<S extends BodyDeclarationState> extends AbstractStaticOrEnumClassMemberVoidVisitor<S>
{
	protected AbstractStaticClassMemberVoidVisitor()
	{
	}

	@Override
	protected final void visitEnumConstant(@NotNull final EnumConstantDeclaration enumConstantDeclaration, @NotNull final S state, @NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		throw new IllegalStateException("Enum constant declarations are not allowed in static classes");
	}

	@Override
	@NotNull
	protected final Visibility constructorVisibility(final int modifiers)
	{
		return visibility(modifiers, constructor);
	}
}
