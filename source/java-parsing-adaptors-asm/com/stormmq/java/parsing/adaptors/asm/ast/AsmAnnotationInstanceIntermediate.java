package com.stormmq.java.parsing.adaptors.asm.ast;

import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.details.valueDetails.ValueDetail;
import com.stormmq.java.parsing.ast.details.valueDetails.ValueDetailAnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.MemberIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.MethodIntermediate;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AbstractAnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.ImplementedTypeUsage;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AsmAnnotationInstanceIntermediate extends AbstractAnnotationInstanceIntermediate<KnownReferenceTypeName>
{
	@NotNull private final RetentionPolicy retentionPolicy;

	public AsmAnnotationInstanceIntermediate(@NotNull final KnownReferenceTypeName referenceTypeName, @NotNull final Map<String, Object> values, @NotNull final RetentionPolicy retentionPolicy)
	{
		super(referenceTypeName, values);
		this.retentionPolicy = retentionPolicy;
	}

	@NotNull
	@Override
	public AnnotationInstanceDetail resolve(@NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		final int size = values.size();
		if (size == 0)
		{
			return new AnnotationInstanceDetail()
			{
			};
		}

		final Map<String, ValueDetail> valuesDetails = new HashMap<>(size);

		for (Map.Entry<String, Object> entry : values.entrySet())
		{
			final String name = entry.getKey();
			final Object value = entry.getValue();
			final ImplementedTypeUsage resolvedTypeUsage = getResolvedTypeUsage(memberIntermediateRecorder, name);
			final ValueDetail valueDetail = AsmDefaultValueIntermediate.resolve(resolvedTypeUsage, simpleTypeIntermediateRecorder, value);
			valuesDetails.put(name, valueDetail);
		}

		return new ValueDetailAnnotationInstanceDetail(referenceTypeName, retentionPolicy, valuesDetails);
	}

	@NotNull
	private ImplementedTypeUsage getResolvedTypeUsage(@NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final String name)
	{
		final List<MethodIntermediate> methodIntermediates = memberIntermediateRecorder.findMustBeExtant(referenceTypeName);
		for (MethodIntermediate methodIntermediate : methodIntermediates)
		{
			if (methodIntermediate.isNamed(name))
			{
				return methodIntermediate.resolvedReturnTypeUsage();
			}
		}
		throw new IllegalStateException("No matching method name for annotation");
	}

}
