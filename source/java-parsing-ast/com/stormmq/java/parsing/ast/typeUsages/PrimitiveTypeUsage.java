package com.stormmq.java.parsing.ast.typeUsages;

import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.PrimitiveTypeName;
import org.jetbrains.annotations.NotNull;

public final class PrimitiveTypeUsage implements TypeUsage
{
	@NotNull private final AnnotationInstanceIntermediate[] annotations;
	@NotNull private final PrimitiveTypeName primitiveTypeName;

	public PrimitiveTypeUsage(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final PrimitiveTypeName primitiveTypeName)
	{
		this.annotations = annotations;
		this.primitiveTypeName = primitiveTypeName;
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
