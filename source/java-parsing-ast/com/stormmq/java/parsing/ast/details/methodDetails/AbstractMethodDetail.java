package com.stormmq.java.parsing.ast.details.methodDetails;

import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMethodDetail implements MethodDetail
{
	@NotNull private final String methodName;
	@NotNull private final TypeUsage returnTypeUsage;
	private final boolean isDeprecatedInByteCode;
	private final boolean isSynthetic;
	@NotNull private final AnnotationInstanceDetail[] annotationInstanceIntermediates;

	protected AbstractMethodDetail(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final AnnotationInstanceDetail[] annotationInstanceDetails)
	{
		this.methodName = methodName;
		this.returnTypeUsage = returnTypeUsage;
		this.isDeprecatedInByteCode = isDeprecatedInByteCode;
		this.isSynthetic = isSynthetic;
		this.annotationInstanceIntermediates = annotationInstanceDetails;
	}

	@Override
	public final boolean isNamed(@NotNull final String name)
	{
		return methodName.equals(name);
	}

	@NotNull
	@Override
	public final TypeUsage returnTypeUsage()
	{
		return returnTypeUsage;
	}
}
