package com.stormmq.java.parsing.ast.details.packageDetails;

import com.stormmq.java.parsing.ast.details.Detail;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.utilities.names.PackageName;
import org.jetbrains.annotations.NotNull;

public final class PackageDetail implements Detail
{
	@NotNull private final PackageName packageName;
	@NotNull private final AnnotationInstanceDetail[] annotationInstanceDetails;

	public PackageDetail(@NotNull final PackageName packageName, @NotNull final AnnotationInstanceDetail[] annotationInstanceDetails)
	{
		this.packageName = packageName;
		this.annotationInstanceDetails = annotationInstanceDetails;
	}
}
