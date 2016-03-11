package com.stormmq.java.parsing.ast.details.methodDetails;

import com.stormmq.java.parsing.ast.details.valueDetails.ValueDetail;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.utilities.Completeness.Final;
import static com.stormmq.java.parsing.utilities.Visibility.Public;

public final class AnnotationMethodDetail extends AbstractMethodDetail
{
	@NotNull private final ValueDetail annotationValueDetail;

	public AnnotationMethodDetail(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final AnnotationInstanceDetail[] annotationInstanceDetails, @NotNull final ValueDetail annotationValueDetail)
	{
		super(methodName, returnTypeUsage, isDeprecatedInByteCode, isSynthetic, annotationInstanceDetails);
		this.annotationValueDetail = annotationValueDetail;
	}

	@NotNull
	@Override
	public Visibility visibility()
	{
		return Public;
	}

	@Override
	public boolean isStatic()
	{
		return false;
	}

	@NotNull
	@Override
	public Completeness completeness()
	{
		return Final;
	}
}
