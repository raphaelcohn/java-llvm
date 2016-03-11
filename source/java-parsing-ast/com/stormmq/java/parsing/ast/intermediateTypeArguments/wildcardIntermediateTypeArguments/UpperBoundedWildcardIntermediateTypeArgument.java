package com.stormmq.java.parsing.ast.intermediateTypeArguments.wildcardIntermediateTypeArguments;

import com.stormmq.java.parsing.ast.intermediateTypeArguments.ArrayPossibleIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import org.jetbrains.annotations.NotNull;

public final class UpperBoundedWildcardIntermediateTypeArgument extends AbstractWildcardIntermediateTypeArgument
{
	@NotNull private final ArrayPossibleIntermediateTypeArgument upperBoundTypeArgument;

	// <? extends Number> eg <? extends upperBoundTypeArgument>
	public UpperBoundedWildcardIntermediateTypeArgument(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final ArrayPossibleIntermediateTypeArgument upperBoundTypeArgument)
	{
		super(annotations);
		this.upperBoundTypeArgument = upperBoundTypeArgument;
	}
}
