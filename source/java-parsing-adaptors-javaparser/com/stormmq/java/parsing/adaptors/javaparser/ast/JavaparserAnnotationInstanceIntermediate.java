package com.stormmq.java.parsing.adaptors.javaparser.ast;

import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.UnresolvedReferenceTypeName;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.MemberIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.MethodIntermediate;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AbstractAnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.ReferenceTypeName;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class JavaparserAnnotationInstanceIntermediate extends AbstractAnnotationInstanceIntermediate<ReferenceTypeName>
{
	@SuppressWarnings("unchecked")
	public JavaparserAnnotationInstanceIntermediate(@NotNull final UnresolvedReferenceTypeName referenceTypeName, @NotNull final Map<String, ExpressionIntermediate> values)
	{
		super(referenceTypeName, (Map<String, Object>)((Object) values));
	}

	@NotNull
	@Override
	public AnnotationInstanceDetail resolve(@NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		throw new UnsupportedOperationException("Implement me!");
	}
}
