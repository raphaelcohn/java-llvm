package com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.methodVisitorAdaptors;

import com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationItem;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AnnotationVisitorAdaptor;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.GatheringAsmAnnotationItemAnnotationVisitorAdaptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.RetentionPolicy;
import java.util.List;

public final class MethodAnnotationMethodVisitorAdaptor implements MethodVisitorAdaptor
{
	@NotNull private final List<AsmAnnotationItem> asmAnnotationItems;

	public MethodAnnotationMethodVisitorAdaptor(@NotNull final List<AsmAnnotationItem> asmAnnotationItems)
	{
		this.asmAnnotationItems = asmAnnotationItems;
	}

	@Nullable
	@Override
	public AnnotationVisitorAdaptor annotationOnMethod(@NotNull final String annotationTypeDescriptor, @NotNull final RetentionPolicy retentionPolicy)
	{
		return new GatheringAsmAnnotationItemAnnotationVisitorAdaptor(annotationTypeDescriptor, retentionPolicy, asmAnnotationItems::add);
	}
}
