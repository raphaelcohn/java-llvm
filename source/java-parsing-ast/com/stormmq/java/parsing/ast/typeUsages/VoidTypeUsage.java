package com.stormmq.java.parsing.ast.typeUsages;

import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import org.jetbrains.annotations.NotNull;

public final class VoidTypeUsage implements TypeUsage
{
	@NotNull private final AnnotationInstanceIntermediate[] annotations;

	public VoidTypeUsage(@NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		this.annotations = annotations;
	}

	@Override
	public int numberOfArrayDimensions()
	{
		return 0;
	}

	@NotNull
	@Override
	public TypeUsage resolve()
	{
		return this;
	}

	@Override
	public boolean isMultiDimensionalArray()
	{
		return false;
	}

	@Override
	public boolean isPrimitive()
	{
		return true;
	}

	@Override
	public boolean isArray()
	{
		return false;
	}

	@Override
	public boolean isVoid()
	{
		return false;
	}

	@Override
	public boolean couldBeString()
	{
		return false;
	}
}
