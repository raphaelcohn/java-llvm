package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.utilities.Visibility;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.adaptors.javaparser.StringConstants.constructor;

public abstract class AbstractInnerOrLocalClassMemberVoidVisitor<S extends BodyDeclarationState> extends AbstractInnerLocalOrAnonymousClassMemberVoidVisitor<S>
{
	protected AbstractInnerOrLocalClassMemberVoidVisitor()
	{
	}

	@Override
	@NotNull
	protected final Visibility constructorVisibility(final int modifiers)
	{
		return visibility(modifiers, constructor);
	}
}
