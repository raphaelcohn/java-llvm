package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.utilities.Visibility;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractAnonymousClassMemberVoidVisitor<S extends BodyDeclarationState> extends AbstractInnerLocalOrAnonymousClassMemberVoidVisitor<S>
{
	protected AbstractAnonymousClassMemberVoidVisitor()
	{
	}

	@Override
	@NotNull
	protected final Visibility constructorVisibility(final int modifiers)
	{
		throw new IllegalArgumentException("Anonymous classes do not have constructors");
	}
}
