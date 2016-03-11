package com.stormmq.java.parsing.ast.details.fieldDetails;

import com.stormmq.java.parsing.utilities.FieldFinality;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.fieldValueDetails.FieldValueDetail;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractClassFieldDetail extends AbstractFieldDetail
{
	@NotNull private final Visibility visibility;
	private final boolean isStatic;
	private final FieldFinality fieldFinality;
	private final boolean isTransient;

	protected AbstractClassFieldDetail(@NotNull final String fieldName, @NotNull final TypeUsage typeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final Visibility visibility, final boolean isStatic, @NotNull final FieldFinality fieldFinality, final boolean isTransient, @NotNull final FieldValueDetail fieldValueDetail, @NotNull final AnnotationInstanceDetail[] annotationInstanceDetails)
	{
		super(fieldName, typeUsage, isDeprecatedInByteCode, isSynthetic, fieldValueDetail, annotationInstanceDetails);
		this.fieldFinality = fieldFinality;
		this.isTransient = isTransient;
		this.visibility = visibility;
		this.isStatic = isStatic;
	}

	@NotNull
	public final Visibility visibility()
	{
		return visibility;
	}

	public final boolean isStatic()
	{
		return isStatic;
	}

	public final boolean isFinal()
	{
		return fieldFinality == FieldFinality.Final;
	}
}
