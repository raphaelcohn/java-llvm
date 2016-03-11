package com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.methodVisitorAdaptors;

import com.stormmq.java.parsing.adaptors.asm.items.AsmMethodItem;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AbstractGatheringAnnotationVisitorAdaptor;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AnnotationVisitorAdaptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class AnnotationMemberDefaultValueMethodVisitorAdaptor implements MethodVisitorAdaptor
{
	@NotNull private final AsmMethodItem asmMethodItem;

	public AnnotationMemberDefaultValueMethodVisitorAdaptor(@NotNull final AsmMethodItem asmMethodItem)
	{
		this.asmMethodItem = asmMethodItem;
	}

	@Nullable
	@Override
	public AnnotationVisitorAdaptor annotationDefault()
	{
		return new AbstractGatheringAnnotationVisitorAdaptor()
		{
			@Override
			protected void visitEnd(@NotNull final Map<String, Object> definedValues)
			{
				if (definedValues.size() != 1)
				{
					throw new IllegalStateException("There should be only one defined value");
				}
				@Nullable final Object value = definedValues.get(com.stormmq.java.parsing.utilities.StringConstants.value);
				if (value == null)
				{
					throw new IllegalStateException("There should be only one defined value with the key 'value'");
				}
				asmMethodItem.setAnnotationDefaultValueIntermediate(value);
			}
		};
	}
}
