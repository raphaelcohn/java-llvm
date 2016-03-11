package com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates;

import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.MemberIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.MethodIntermediate;
import org.jetbrains.annotations.NotNull;

public interface AnnotationInstanceIntermediate
{
	@SuppressWarnings("unchecked") @NotNull AnnotationInstanceIntermediate[] EmptyAnnotations = {};
	@SuppressWarnings("unchecked") @NotNull AnnotationInstanceIntermediate[][] EmptyEmptyAnnotations = {};

	@NotNull
	AnnotationInstanceDetail resolve(@NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder);
}
