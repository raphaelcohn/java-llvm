package com.stormmq.java.parsing.ast.details.fieldDetails;

import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.fieldValueDetails.FieldValueDetail;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

public final class AnnotationFieldDetail extends AbstractConstantFieldDetail
{
	public AnnotationFieldDetail(@NotNull final String fieldName, @NotNull final TypeUsage typeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final FieldValueDetail fieldValueDetail, @NotNull final AnnotationInstanceDetail[] annotationInstanceDetails)
	{
		super(fieldName, typeUsage, isDeprecatedInByteCode, isSynthetic, fieldValueDetail, annotationInstanceDetails);
	}
}
