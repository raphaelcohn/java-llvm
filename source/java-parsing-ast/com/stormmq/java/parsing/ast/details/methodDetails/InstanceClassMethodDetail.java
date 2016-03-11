package com.stormmq.java.parsing.ast.details.methodDetails;

import com.stormmq.java.parsing.ast.details.bodyDetails.BodyDetail;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.parameterDetails.ParameterDetail;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

public final class InstanceClassMethodDetail extends AbstractClassMethodDetail
{
	@NotNull private final Completeness completeness;

	public InstanceClassMethodDetail(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ParameterDetail[] parameterDetails, @NotNull final KnownReferenceTypeName[] exceptionTypeReferenceNames, final boolean isVarArgs, @NotNull final Visibility visibility, final boolean isBridge, final boolean isSynchronized, @NotNull final BodyDetail bodyDetail, @NotNull final Completeness completeness, @NotNull final AnnotationInstanceDetail[] annotationInstanceDetails)
	{
		super(methodName, returnTypeUsage, isDeprecatedInByteCode, isSynthetic, parameterDetails, exceptionTypeReferenceNames, isVarArgs, visibility, isBridge, isSynchronized, bodyDetail, annotationInstanceDetails);
		this.completeness = completeness;
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
		return completeness;
	}
}
