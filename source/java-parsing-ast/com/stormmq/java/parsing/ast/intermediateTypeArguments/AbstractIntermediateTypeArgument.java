package com.stormmq.java.parsing.ast.intermediateTypeArguments;

import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractIntermediateTypeArgument implements IntermediateTypeArgument
{
	@NotNull private final AnnotationInstanceIntermediate[] annotations;

	protected AbstractIntermediateTypeArgument(@NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		this.annotations = annotations;
	}
}
