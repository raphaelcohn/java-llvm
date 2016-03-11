package com.stormmq.java.parsing.ast.details.fieldDetails;

import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.fieldValueDetails.FieldValueDetail;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.utilities.Visibility.Public;

public class AbstractConstantFieldDetail extends AbstractFieldDetail
{
	protected AbstractConstantFieldDetail(@NotNull final String fieldName, @NotNull final TypeUsage typeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final FieldValueDetail fieldValueDetail, final AnnotationInstanceDetail[] annotationInstanceDetails)
	{
		super(fieldName, typeUsage, isDeprecatedInByteCode, isSynthetic, fieldValueDetail, annotationInstanceDetails);
	}

	@NotNull
	public final Visibility visibility()
	{
		return Public;
	}

	public final boolean isStatic()
	{
		return true;
	}

	public final boolean isFinal()
	{
		return true;
	}
}
