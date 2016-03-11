package com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.fieldVisitorAdaptors;

import com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationItemUser;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AnnotationVisitorAdaptor;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.GatheringAsmAnnotationItemAnnotationVisitorAdaptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.RetentionPolicy;

public final class AnnotationGatheringFieldVisitorAdaptor implements FieldVisitorAdaptor
{
	@NotNull private final AsmAnnotationItemUser asmAnnotationItemUser;

	public AnnotationGatheringFieldVisitorAdaptor(@NotNull final AsmAnnotationItemUser asmAnnotationItemUser)
	{
		this.asmAnnotationItemUser = asmAnnotationItemUser;
	}

	@Nullable
	@Override
	public AnnotationVisitorAdaptor annotationOnField(@NotNull final String annotationTypeDescriptor, @NotNull final RetentionPolicy retentionPolicy)
	{
		return new GatheringAsmAnnotationItemAnnotationVisitorAdaptor(annotationTypeDescriptor, retentionPolicy, asmAnnotationItemUser);
	}
}
