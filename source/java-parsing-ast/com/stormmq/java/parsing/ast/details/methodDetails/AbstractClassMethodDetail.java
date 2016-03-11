package com.stormmq.java.parsing.ast.details.methodDetails;

import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.bodyDetails.BodyDetail;
import com.stormmq.java.parsing.ast.details.parameterDetails.ParameterDetail;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractClassMethodDetail extends AbstractInterfaceOrClassMethodDetail implements ClassMethodDetail
{
	@NotNull private final Visibility visibility;
	private final boolean isBridge;
	private final boolean isSynchronized;
	@NotNull private final BodyDetail bodyDetail;
	private final boolean isNative;

	protected AbstractClassMethodDetail(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ParameterDetail[] parameterDetails, @NotNull final KnownReferenceTypeName[] exceptionTypeReferenceNames, final boolean isVarArgs, @NotNull final Visibility visibility, final boolean isBridge, final boolean isSynchronized, @NotNull final BodyDetail bodyDetail, @NotNull final AnnotationInstanceDetail[] annotationInstanceDetails)
	{
		super(methodName, returnTypeUsage, isDeprecatedInByteCode, isSynthetic, parameterDetails, exceptionTypeReferenceNames, isVarArgs, annotationInstanceDetails);
		this.visibility = visibility;
		this.isBridge = isBridge;
		this.isSynchronized = isSynchronized;
		this.bodyDetail = bodyDetail;
		this.isNative = bodyDetail.isNative();
	}

	@NotNull
	public final Visibility visibility()
	{
		return visibility;
	}
}
