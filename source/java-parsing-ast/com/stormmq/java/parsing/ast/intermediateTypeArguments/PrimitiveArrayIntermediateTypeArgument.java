package com.stormmq.java.parsing.ast.intermediateTypeArguments;

import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.PrimitiveTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

public final class PrimitiveArrayIntermediateTypeArgument extends AbstractArrayPossibleIntermediateTypeArgument
{
	@NotNull private final PrimitiveTypeName primitiveTypeName;

	public PrimitiveArrayIntermediateTypeArgument(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final AnnotationInstanceIntermediate[][] arrayAnnotations, @NotNull final PrimitiveTypeName primitiveTypeName)
	{
		super(annotations, arrayAnnotations);
		this.primitiveTypeName = primitiveTypeName;
		if (!isArray())
		{
			throw new IllegalArgumentException("Must be an array");
		}
	}

	@NotNull
	@Override
	public TypeUsage resolve()
	{
		throw new UnsupportedOperationException("Resolution not yet supported");
	}

	@Override
	public boolean isPrimitive()
	{
		return true;
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
