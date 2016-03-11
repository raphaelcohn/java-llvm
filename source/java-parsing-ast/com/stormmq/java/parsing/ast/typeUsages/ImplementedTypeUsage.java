package com.stormmq.java.parsing.ast.typeUsages;

import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.TypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.VoidTypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.ReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class ImplementedTypeUsage implements TypeUsage
{
	@NotNull public final TypeName typeName;
	@NotNull private final AnnotationInstanceIntermediate[] annotations;
	@NotNull private final AnnotationInstanceIntermediate[][] arrayAnnotations;
	private final int numberOfArrayDimensions;

	public ImplementedTypeUsage(@NotNull final TypeName typeName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final AnnotationInstanceIntermediate[][] arrayAnnotations)
	{
		this.annotations = annotations;
		this.arrayAnnotations = arrayAnnotations;
		final int arrayCount = arrayAnnotations.length;
		if (typeName instanceof VoidTypeName && arrayCount != 0)
		{
			throw new IllegalArgumentException(format(ENGLISH, "numberOfArrayDimensions ('%1$s') must be zero for a VoidTypeName", arrayCount));
		}
		this.typeName = typeName;
		this.numberOfArrayDimensions = arrayCount;
	}

	@Override
	public int numberOfArrayDimensions()
	{
		return numberOfArrayDimensions;
	}

	@Override
	@NotNull
	public TypeUsage resolve()
	{
		if (typeName instanceof ReferenceTypeName)
		{
			if (typeName instanceof KnownReferenceTypeName)
			{
				return this;
			}
			return new ImplementedTypeUsage(((ReferenceTypeName) typeName).resolve(), annotations, arrayAnnotations);
		}
		return this;
	}

	@Override
	public boolean equals(@Nullable final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		final ImplementedTypeUsage typeUsage = (ImplementedTypeUsage) o;

		if (numberOfArrayDimensions != typeUsage.numberOfArrayDimensions)
		{
			return false;
		}
		if (!typeName.equals(typeUsage.typeName))
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = typeName.hashCode();
		result = 31 * result + numberOfArrayDimensions;
		return result;
	}

	@Override
	public boolean isMultiDimensionalArray()
	{
		return numberOfArrayDimensions > 1;
	}

	@Override
	public boolean isPrimitive()
	{
		return typeName.isPrimitive();
	}

	@Override
	public boolean isArray()
	{
		return numberOfArrayDimensions != 0;
	}

	@Override
	public boolean isVoid()
	{
		return typeName.isVoid();
	}

	@Override
	public boolean couldBeString()
	{
		throw new UnsupportedOperationException();
	}
}
