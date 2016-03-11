package com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.stormmq.java.parsing.adaptors.javaparser.dead.AbstractHasBody;
import com.stormmq.java.parsing.adaptors.javaparser.referenceTypeNameMakers.ReferenceTypeNameMaker;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.nullToEmptyList;

public final class EnumConstantDeclarationWrapper extends AbstractHasBody
{
	@NotNull private final EnumConstantDeclaration enumConstantDeclaration;

	public EnumConstantDeclarationWrapper(@NotNull final EnumConstantDeclaration enumConstantDeclaration)
	{
		this.enumConstantDeclaration = enumConstantDeclaration;
	}

	@Override
	@NotNull
	protected List<BodyDeclaration> typeDeclarationBody()
	{
		return enumConstantDeclaration.getClassBody();
	}

	@Override
	public boolean hasEmptyBody()
	{
		final List<BodyDeclaration> classBody = enumConstantDeclaration.getClassBody();
		return classBody == null || classBody.isEmpty();
	}

	@NotNull
	public List<Expression> arguments()
	{
		return nullToEmptyList(enumConstantDeclaration.getArgs());
	}

	@NotNull
	public List<Expression> withImplicitArguments(final int ordinal)
	{
		@Nullable final List<Expression> arguments = enumConstantDeclaration.getArgs();

		final boolean isEmpty = arguments == null || arguments.isEmpty();
		final int size = isEmpty ? 0 : arguments.size();
		@NotNull final List<Expression> withImplicitArguments = new ArrayList<>(size + 2);

		final String name = enumConstantDeclaration.getName();
		withImplicitArguments.add(new StringLiteralExpr(name));
		withImplicitArguments.add(new IntegerLiteralExpr(Integer.toString(ordinal, 10)));
		if (isEmpty)
		{
			return withImplicitArguments;
		}
		for (Expression argument : arguments)
		{
			withImplicitArguments.add(argument);
		}
		return withImplicitArguments;
	}

	@NotNull
	public String constantName()
	{
		return enumConstantDeclaration.getName();
	}

	@NotNull
	public AnnotationInstanceIntermediate[] parseAnnotationInstanceDetails(@NotNull final ReferenceTypeNameMaker referenceTypeNameMaker)
	{
		return referenceTypeNameMaker.convertToAnnotationInstanceIntermediates(enumConstantDeclaration.getAnnotations());
	}
}
