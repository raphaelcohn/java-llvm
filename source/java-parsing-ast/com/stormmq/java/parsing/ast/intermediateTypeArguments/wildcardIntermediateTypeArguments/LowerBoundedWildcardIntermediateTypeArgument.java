package com.stormmq.java.parsing.ast.intermediateTypeArguments.wildcardIntermediateTypeArguments;

import com.stormmq.java.parsing.ast.intermediateTypeArguments.ArrayPossibleIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import org.jetbrains.annotations.NotNull;

public final class LowerBoundedWildcardIntermediateTypeArgument extends AbstractWildcardIntermediateTypeArgument
{
	@NotNull private final ArrayPossibleIntermediateTypeArgument lowerBoundTypeArgument;

	// <? super Number> eg <? super lowerBoundTypeArgument>
	public LowerBoundedWildcardIntermediateTypeArgument(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final ArrayPossibleIntermediateTypeArgument lowerBoundTypeArgument)
	{
		super(annotations);
		this.lowerBoundTypeArgument = lowerBoundTypeArgument;
	}
}
