package com.stormmq.java.parsing.ast.details.fieldDetails;

import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.fieldValueDetails.FieldValueDetail;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractFieldDetail implements FieldDetail
{
	@NotNull private final String fieldName;
	@NotNull private final TypeUsage typeUsage;
	private final boolean isDeprecatedInByteCode;
	private final boolean isSynthetic;
	@NotNull private final FieldValueDetail fieldValueDetail;
	@NotNull private final AnnotationInstanceDetail[] annotationInstanceDetails;

	protected AbstractFieldDetail(@NotNull final String fieldName, @NotNull final TypeUsage typeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final FieldValueDetail fieldValueDetail, @NotNull final AnnotationInstanceDetail[] annotationInstanceDetails)
	{
		this.fieldName = fieldName;
		this.typeUsage = typeUsage;
		this.isDeprecatedInByteCode = isDeprecatedInByteCode;
		this.isSynthetic = isSynthetic;
		this.fieldValueDetail = fieldValueDetail;
		this.annotationInstanceDetails = annotationInstanceDetails;
	}

	@Override
	public final boolean isNamed(@NotNull final String name)
	{
		return fieldName.equals(name);
	}

	@NotNull
	@Override
	public final TypeUsage fieldTypeUsage()
	{
		return typeUsage;
	}
}
