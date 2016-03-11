package com.stormmq.java.parsing.adaptors.asm.items;

import com.stormmq.java.parsing.adaptors.asm.ast.AsmAnnotationInstanceIntermediate;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Map;

import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate.EmptyAnnotations;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Locale.ENGLISH;

public final class AsmAnnotationItem
{
	@SuppressWarnings("unchecked") @NotNull public static final List<AsmAnnotationItem>[] EmptyAsmAnnotationItemsArray = new List[0];
	@NotNull public static final List<AsmAnnotationItem> EmptyAsmAnnotationItems = emptyList();

	@NotNull private final KnownReferenceTypeName annotationReferenceTypeName;
	@NotNull private final RetentionPolicy retentionPolicy;
	@NotNull private final Map<String, Object> definedValues;

	public AsmAnnotationItem(@NotNull final KnownReferenceTypeName annotationReferenceTypeName, @NotNull final RetentionPolicy retentionPolicy, @NotNull final Map<String, Object> definedValues)
	{
		this.annotationReferenceTypeName = annotationReferenceTypeName;
		this.retentionPolicy = retentionPolicy;
		this.definedValues = definedValues;
	}

	@NotNull
	public static AnnotationInstanceIntermediate[] parseAnnotationInstanceDetails(@NotNull final List<AsmAnnotationItem> asmAnnotationItems)
	{
		final int size = asmAnnotationItems.size();
		if (size == 0)
		{
			return EmptyAnnotations;
		}
		@SuppressWarnings("unchecked") final AnnotationInstanceIntermediate[] annotationInstanceIntermediates = new AnnotationInstanceIntermediate[size];
		for(int index = 0; index < size; index++)
		{
			final AsmAnnotationItem asmAnnotationItem = asmAnnotationItems.get(index);
			annotationInstanceIntermediates[index] = asmAnnotationItem.toAnnotationInstanceDetail();
		}
		return annotationInstanceIntermediates;
	}

	@NotNull
	public AnnotationInstanceIntermediate toAnnotationInstanceDetail()
	{
		return new AsmAnnotationInstanceIntermediate(annotationReferenceTypeName, definedValues, retentionPolicy);
	}

	@Override
	@NotNull
	public String toString()
	{
		return format(ENGLISH, "%1$s(%2$s, %3$s, %4$s)", getClass().getSimpleName(), annotationReferenceTypeName, retentionPolicy, definedValues);
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

		final AsmAnnotationItem that = (AsmAnnotationItem) o;

		if (!annotationReferenceTypeName.equals(that.annotationReferenceTypeName))
		{
			return false;
		}
		if (!definedValues.equals(that.definedValues))
		{
			return false;
		}
		if (retentionPolicy != that.retentionPolicy)
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = annotationReferenceTypeName.hashCode();
		result = 31 * result + retentionPolicy.hashCode();
		result = 31 * result + definedValues.hashCode();
		return result;
	}
}
