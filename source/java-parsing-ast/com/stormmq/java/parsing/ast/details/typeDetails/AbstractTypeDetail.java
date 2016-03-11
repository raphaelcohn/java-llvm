package com.stormmq.java.parsing.ast.details.typeDetails;

import com.stormmq.java.parsing.ast.intermediates.Origin;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractTypeDetail implements TypeDetail
{
	@NotNull protected final Origin origin;
	@NotNull protected final KnownReferenceTypeName ourTypeReferenceName;
	@NotNull protected final Visibility visibility;
	protected final boolean isDeprecatedInByteCode;
	protected final boolean isStrictFloatingPoint;
	protected final boolean isSynthetic;
	@NotNull protected final AnnotationInstanceDetail[] annotationInstanceIntermediates;

	protected AbstractTypeDetail(@NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourTypeReferenceName, @NotNull final Visibility visibility, final boolean isDeprecatedInByteCode, final boolean isStrictFloatingPoint, final boolean isSynthetic, @NotNull final AnnotationInstanceDetail[] annotationInstanceIntermediates)
	{
		this.origin = origin;
		this.ourTypeReferenceName = ourTypeReferenceName;
		this.visibility = visibility;
		this.isDeprecatedInByteCode = isDeprecatedInByteCode;
		this.isStrictFloatingPoint = isStrictFloatingPoint;
		this.isSynthetic = isSynthetic;
		this.annotationInstanceIntermediates = annotationInstanceIntermediates;
	}

	@NotNull
	public final KnownReferenceTypeName ourTypeReferenceName()
	{
		return ourTypeReferenceName;
	}

	@NotNull
	@Override
	public final Origin origin()
	{
		return origin;
	}
}
