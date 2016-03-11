package com.stormmq.java.parsing.ast.details.typeDetails;

import com.stormmq.java.parsing.ast.intermediates.Origin;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import org.jetbrains.annotations.NotNull;

public final class EnumTypeDetail extends AbstractClassOrInterfaceOrEnumTypeDetail
{
	public EnumTypeDetail(@NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourTypeReferenceName, @NotNull final Visibility visibility, final boolean isDeprecatedInByteCode, final boolean isStrictFloatingPoint, final boolean isSynthetic, @NotNull final AnnotationInstanceDetail[] annotationInstanceIntermediates, @NotNull final KnownReferenceTypeName[] interfaceTypeReferenceNames)
	{
		super(origin, ourTypeReferenceName, visibility, isDeprecatedInByteCode, isStrictFloatingPoint, isSynthetic, annotationInstanceIntermediates, interfaceTypeReferenceNames);
	}

}
