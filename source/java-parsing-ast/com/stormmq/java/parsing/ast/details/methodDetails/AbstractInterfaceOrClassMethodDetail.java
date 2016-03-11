package com.stormmq.java.parsing.ast.details.methodDetails;

import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.parameterDetails.ParameterDetail;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractInterfaceOrClassMethodDetail extends AbstractMethodDetail
{
	@NotNull private final ParameterDetail[] parameterDetails;
	@NotNull private final KnownReferenceTypeName[] exceptionTypeReferenceNames;
	private final boolean isVarArgs;

	protected AbstractInterfaceOrClassMethodDetail(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ParameterDetail[] parameterDetails, @NotNull final KnownReferenceTypeName[] exceptionTypeReferenceNames, final boolean isVarArgs, @NotNull final AnnotationInstanceDetail[] annotationInstanceDetails)
	{
		super(methodName, returnTypeUsage, isDeprecatedInByteCode, isSynthetic, annotationInstanceDetails);
		this.parameterDetails = parameterDetails;
		this.exceptionTypeReferenceNames = exceptionTypeReferenceNames;
		this.isVarArgs = isVarArgs;
	}
}
