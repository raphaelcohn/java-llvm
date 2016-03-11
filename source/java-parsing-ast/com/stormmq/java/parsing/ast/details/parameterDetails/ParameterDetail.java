package com.stormmq.java.parsing.ast.details.parameterDetails;

import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

public final class ParameterDetail
{
	@NotNull public static final ParameterDetail[] EmptyParameterDetails = {};

	@NotNull private final String parameterName;
	@NotNull private final TypeUsage resolvedTypeUsage;
	private final boolean isInferredParameterName;
	private final boolean isFinal;
	private final boolean isSynthetic;
	private final boolean isMandatory;
	@NotNull private final AnnotationInstanceDetail[] annotationInstanceDetails;

	public ParameterDetail(@NotNull final String parameterName, @NotNull final TypeUsage resolvedTypeUsage, final boolean isInferredParameterName, final boolean isFinal, final boolean isSynthetic, final boolean isMandatory, @NotNull final AnnotationInstanceDetail[] annotationInstanceDetails)
	{
		this.parameterName = parameterName;
		this.resolvedTypeUsage = resolvedTypeUsage;
		this.isInferredParameterName = isInferredParameterName;
		this.isFinal = isFinal;
		this.isSynthetic = isSynthetic;
		this.isMandatory = isMandatory;
		this.annotationInstanceDetails = annotationInstanceDetails;
	}
}
