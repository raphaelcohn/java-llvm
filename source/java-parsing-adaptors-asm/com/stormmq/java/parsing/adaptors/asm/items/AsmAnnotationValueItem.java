package com.stormmq.java.parsing.adaptors.asm.items;

import com.stormmq.java.parsing.ast.details.valueDetails.AnnotationSingleValueDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

// Occurs as a default value for complex annotations and when an annotations has a value() with a subtype of Annotation
public final class AsmAnnotationValueItem
{
	@NotNull private final KnownReferenceTypeName annotationReferenceTypeName;
	@NotNull private final Map<String, Object> definedValues;

	public AsmAnnotationValueItem(@NotNull final KnownReferenceTypeName annotationReferenceTypeName, @NotNull final Map<String, Object> definedValues)
	{
		this.annotationReferenceTypeName = annotationReferenceTypeName;
		this.definedValues = definedValues;
	}

	@NotNull
	public AnnotationSingleValueDetail resolve(@NotNull final TypeUsage resolvedReturnTypeUsage, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		System.err.println("TODO: Implement nested complex annotations support with AnnotationSingleDefaultValueDetail");
		return new AnnotationSingleValueDetail();
	}

	@Override
	@NotNull
	public String toString()
	{
		return format(ENGLISH, "%1$s(%2$s, %3$s)", getClass().getSimpleName(), annotationReferenceTypeName, definedValues);
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

		final AsmAnnotationValueItem that = (AsmAnnotationValueItem) o;

		if (!annotationReferenceTypeName.equals(that.annotationReferenceTypeName))
		{
			return false;
		}
		if (!definedValues.equals(that.definedValues))
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = annotationReferenceTypeName.hashCode();
		result = 31 * result + definedValues.hashCode();
		return result;
	}
}
