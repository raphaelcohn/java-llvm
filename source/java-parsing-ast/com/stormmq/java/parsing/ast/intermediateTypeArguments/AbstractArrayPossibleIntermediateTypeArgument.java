package com.stormmq.java.parsing.ast.intermediateTypeArguments;

import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractArrayPossibleIntermediateTypeArgument extends AbstractIntermediateTypeArgument implements ArrayPossibleIntermediateTypeArgument
{
	@NotNull private final AnnotationInstanceIntermediate[][] arrayAnnotations;
	private final int arrayDimensions;

	protected AbstractArrayPossibleIntermediateTypeArgument(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final AnnotationInstanceIntermediate[][] arrayAnnotations)
	{
		super(annotations);
		this.arrayAnnotations = arrayAnnotations;
		this.arrayDimensions = arrayAnnotations.length;
	}

	@Override
	public final int numberOfArrayDimensions()
	{
		return arrayDimensions;
	}

	@Override
	public final boolean isArray()
	{
		return arrayDimensions != 0;
	}

	@Override
	public final boolean isMultiDimensionalArray()
	{
		return arrayDimensions > 1;
	}
}
