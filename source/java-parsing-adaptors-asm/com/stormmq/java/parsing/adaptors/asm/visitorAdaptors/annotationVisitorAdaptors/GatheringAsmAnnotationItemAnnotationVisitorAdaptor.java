package com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors;

import com.stormmq.java.parsing.adaptors.asm.helpers.AsmHelper;
import com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationItem;
import com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationItemUser;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.RetentionPolicy;
import java.util.Map;

public final class GatheringAsmAnnotationItemAnnotationVisitorAdaptor extends AbstractGatheringAnnotationVisitorAdaptor
{
	@NotNull private final KnownReferenceTypeName annotationReferenceTypeName;
	@NotNull private final RetentionPolicy retentionPolicy;
	@NotNull private final AsmAnnotationItemUser asmAnnotationItemUser;

	public GatheringAsmAnnotationItemAnnotationVisitorAdaptor(@NotNull final String typeDescriptor, @NotNull final RetentionPolicy retentionPolicy, @NotNull final AsmAnnotationItemUser asmAnnotationItemUser)
	{
		this.asmAnnotationItemUser = asmAnnotationItemUser;

		annotationReferenceTypeName = AsmHelper.knownReferenceTypeName(typeDescriptor);

		// default is CLASS
		this.retentionPolicy = retentionPolicy;
	}

	@Override
	public void visitEnd(@NotNull final Map<String, Object> definedValues)
	{
		asmAnnotationItemUser.use(new AsmAnnotationItem(annotationReferenceTypeName, retentionPolicy, definedValues));
	}
}
