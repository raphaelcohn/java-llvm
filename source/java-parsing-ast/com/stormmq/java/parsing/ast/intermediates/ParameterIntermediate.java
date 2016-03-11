package com.stormmq.java.parsing.ast.intermediates;

import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.details.parameterDetails.ParameterDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.MemberIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AbstractAnnotationInstanceIntermediate.resolveAnnotationInstanceIntermediates;

public final class ParameterIntermediate
{
	@SuppressWarnings("unchecked") @NotNull public static final ParameterIntermediate[] EmptyParameterIntermediates = {};

	@NotNull private final String parameterName;
	@NotNull private final TypeUsage typeUsage;
	private final boolean isFinal;
	private final boolean isInferredParameterName;
	private final boolean isSynthetic;
	private final boolean isMandatory;
	@NotNull private final AnnotationInstanceIntermediate[] annotations;

	public ParameterIntermediate(@NotNull final String parameterName, @NotNull final TypeUsage typeUsage, final boolean isFinal, final boolean isInferredParameterName, final boolean isSynthetic, final boolean isMandatory, @NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		this.parameterName = parameterName;
		this.typeUsage = typeUsage;
		this.isFinal = isFinal;
		this.isInferredParameterName = isInferredParameterName;
		this.isSynthetic = isSynthetic;
		this.isMandatory = isMandatory;
		this.annotations = annotations;
	}

	@NotNull
	public ParameterDetail resolve(@NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		return new ParameterDetail(parameterName, typeUsage.resolve(), isInferredParameterName, isFinal, isSynthetic, isMandatory, resolveAnnotationInstanceIntermediates(annotations, memberIntermediateRecorder, simpleTypeIntermediateRecorder));
	}

	@NotNull
	public String parameterName()
	{
		return parameterName;
	}
}
