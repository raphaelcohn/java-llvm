package com.stormmq.java.parsing.ast.details.fieldDetails;

import com.stormmq.java.parsing.utilities.FieldFinality;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.ast.details.fieldValueDetails.UnassignedFieldValueDetail.Unassigned;

public final class InstanceClassFieldDetail extends AbstractClassFieldDetail
{
	public InstanceClassFieldDetail(@NotNull final String fieldName, @NotNull final TypeUsage typeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final Visibility visibility, @NotNull final FieldFinality fieldFinality, final boolean isTransient, @NotNull final AnnotationInstanceDetail[] annotationInstanceDetails)
	{
		super(fieldName, typeUsage, isDeprecatedInByteCode, isSynthetic, visibility, false, fieldFinality, isTransient, Unassigned, annotationInstanceDetails);
	}
}
