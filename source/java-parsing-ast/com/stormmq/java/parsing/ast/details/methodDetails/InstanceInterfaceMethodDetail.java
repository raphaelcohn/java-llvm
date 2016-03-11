package com.stormmq.java.parsing.ast.details.methodDetails;

import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.ReferenceTypeName;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.parameterDetails.ParameterDetail;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.utilities.Completeness.Abstract;

public final class InstanceInterfaceMethodDetail<R extends ReferenceTypeName> extends AbstractInterfaceMethodDetail
{
	public InstanceInterfaceMethodDetail(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ParameterDetail[] parameterDetails, @NotNull final KnownReferenceTypeName[] exceptionTypeReferenceNames, final boolean isVarArgs, @NotNull final AnnotationInstanceDetail[] annotationInstanceDetails)
	{
		super(methodName, returnTypeUsage, isDeprecatedInByteCode, isSynthetic, parameterDetails, exceptionTypeReferenceNames, isVarArgs, annotationInstanceDetails);
	}

	public final boolean isStatic()
	{
		return false;
	}

	@NotNull
	public final Completeness completeness()
	{
		return Abstract;
	}
}
