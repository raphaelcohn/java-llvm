package com.stormmq.java.parsing.ast.intermediateTypeArguments.wildcardIntermediateTypeArguments;

import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import org.jetbrains.annotations.NotNull;

// <?>
public final class AnyWildcardIntermediateTypeArgument extends AbstractWildcardIntermediateTypeArgument
{
	public AnyWildcardIntermediateTypeArgument(@NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		super(annotations);
	}
}
