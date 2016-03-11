package com.stormmq.java.parsing.ast.details.fieldDetails;

import com.stormmq.java.parsing.utilities.FieldFinality;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.fieldValueDetails.FieldValueDetail;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

public final class StaticClassFieldDetail extends AbstractClassFieldDetail
{
	public StaticClassFieldDetail(@NotNull final String fieldName, final TypeUsage typeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final Visibility visibility, @NotNull final FieldFinality fieldFinality, final boolean isTransient, @NotNull FieldValueDetail fieldValueDetail, @NotNull final AnnotationInstanceDetail[] annotationInstanceDetails)
	{
		super(fieldName, typeUsage, isDeprecatedInByteCode, isSynthetic, visibility, true, fieldFinality, isTransient, fieldValueDetail, annotationInstanceDetails);
	}
}
