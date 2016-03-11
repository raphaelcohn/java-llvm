package com.stormmq.java.parsing.adaptors.javaparser.ast;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulFieldValueDetail;
import com.stormmq.java.parsing.ast.details.valueDetails.ValueDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.DefaultValueIntermediate;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

public final class JavaparserDefaultValueIntermediate implements DefaultValueIntermediate
{
	@NotNull private final UsefulFieldValueDetail constantValue;

	public JavaparserDefaultValueIntermediate(@NotNull final UsefulFieldValueDetail constantValue)
	{
		this.constantValue = constantValue;
	}

	@Override
	public boolean isAValue()
	{
		return true;
	}

	@NotNull
	@Override
	public ValueDetail resolve(@NotNull final TypeUsage resolvedReturnTypeUsage, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		throw new UnsupportedOperationException();
	}
}
