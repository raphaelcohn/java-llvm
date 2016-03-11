package com.stormmq.java.parsing.ast.details.valueDetails;

import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.RetentionPolicy;
import java.util.Map;

public final class ValueDetailAnnotationInstanceDetail implements AnnotationInstanceDetail
{
	@NotNull private final KnownReferenceTypeName knownReferenceTypeName;
	@NotNull private final RetentionPolicy retentionPolicy;
	@NotNull private final Map<String, ValueDetail> valuesDetails;

	public ValueDetailAnnotationInstanceDetail(@NotNull final KnownReferenceTypeName knownReferenceTypeName, @NotNull final RetentionPolicy retentionPolicy, @NotNull final Map<String, ValueDetail> valuesDetails)
	{
		this.knownReferenceTypeName = knownReferenceTypeName;
		this.retentionPolicy = retentionPolicy;
		this.valuesDetails = valuesDetails;
	}
}
