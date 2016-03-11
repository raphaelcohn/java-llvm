package com.stormmq.java.parsing.adaptors.javaparser.helpers.FIELDSTUFF;

import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.stormmq.java.parsing.adaptors.javaparser.ast.JavaparserInitializerIntermediate;
import com.stormmq.java.parsing.adaptors.javaparser.helpers.NameExprHelper;
import com.stormmq.java.parsing.ast.intermediates.InitializerIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import static com.github.javaparser.ast.expr.AssignExpr.Operator.assign;

public final class FieldInit
{
	private FieldInit()
	{
	}

	@NotNull
	public static InitializerIntermediate fieldInitializer(@NotNull final Expression init, @NotNull final String fieldName, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName)
	{
		final Expression target = new FieldAccessExpr(NameExprHelper.parentNameToNameExpr(ourKnownReferenceTypeName), fieldName);
		return new JavaparserInitializerIntermediate(new BlockStmt(Collections.<Statement>singletonList(new ExpressionStmt(new AssignExpr(target, init, assign)))));
	}

}
