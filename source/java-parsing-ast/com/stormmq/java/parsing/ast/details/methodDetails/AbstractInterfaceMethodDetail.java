package com.stormmq.java.parsing.ast.details.methodDetails;

import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.parameterDetails.ParameterDetail;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.utilities.Visibility.Public;

public abstract class AbstractInterfaceMethodDetail extends AbstractInterfaceOrClassMethodDetail implements InterfaceMethodDetail
{
	protected AbstractInterfaceMethodDetail(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ParameterDetail[] parameterDetails, @NotNull final KnownReferenceTypeName[] exceptionTypeNames, final boolean isVarArgs, @NotNull final AnnotationInstanceDetail[] annotationInstanceDetails)
	{
		super(methodName, returnTypeUsage, isDeprecatedInByteCode, isSynthetic, parameterDetails, exceptionTypeNames, isVarArgs, annotationInstanceDetails);
	}

	@NotNull
	public final Visibility visibility()
	{
		return Public;
	}
}
