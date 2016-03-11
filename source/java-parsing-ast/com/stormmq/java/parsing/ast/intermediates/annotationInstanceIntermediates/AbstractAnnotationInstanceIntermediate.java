package com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates;

import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.MemberIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.MethodIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.ReferenceTypeName;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail.EmptyAnnotationInstanceDetails;

public abstract class AbstractAnnotationInstanceIntermediate<R extends ReferenceTypeName> implements AnnotationInstanceIntermediate
{
	@NotNull
	public static AnnotationInstanceDetail[] resolveAnnotationInstanceIntermediates(@NotNull final AnnotationInstanceIntermediate[] annotationInstanceIntermediates, @NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		final int length = annotationInstanceIntermediates.length;
		if (length == 0)
		{
			return EmptyAnnotationInstanceDetails;
		}

		final AnnotationInstanceDetail[] resolved = new AnnotationInstanceDetail[length];
		//noinspection ForLoopReplaceableByForEach
		for (int index = 0; index < length; index++)
		{
			resolved[index] = annotationInstanceIntermediates[index].resolve(memberIntermediateRecorder, simpleTypeIntermediateRecorder);
		}
		return resolved;
	}

	@NotNull protected final R referenceTypeName;
	@NotNull protected final Map<String, Object> values;

	protected AbstractAnnotationInstanceIntermediate(@NotNull final R referenceTypeName, @NotNull final Map<String, Object> values)
	{
		this.referenceTypeName = referenceTypeName;
		this.values = values;
	}
}
