package com.stormmq.java.parsing.ast.intermediateTypeArguments.wildcardIntermediateTypeArguments;

import com.stormmq.java.parsing.ast.intermediateTypeArguments.AbstractIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractWildcardIntermediateTypeArgument extends AbstractIntermediateTypeArgument
{
	protected AbstractWildcardIntermediateTypeArgument(@NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		super(annotations);
	}
}
