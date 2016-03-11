package com.stormmq.java.parsing.adaptors.asm.visitors;

import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.fieldVisitorAdaptors.FieldVisitorAdaptor;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AnnotationVisitorAdaptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.*;

import java.util.List;
import java.util.function.*;

import static com.stormmq.java.parsing.adaptors.asm.helpers.AsmHelper.AsmVersion;
import static com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AnnotationVisitorAdaptor.toRetentionPolicy;
import static com.stormmq.java.parsing.adaptors.asm.visitors.CombinationAnnotationVisitor.AnnotationCombinationConstructor;
import static com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.fieldVisitorAdaptors.FieldVisitorAdaptor.end;

public final class CombinationFieldVisitor extends FieldVisitor
{
	@NotNull public static final BiFunction<FieldVisitor, List<FieldVisitorAdaptor>, CombinationFieldVisitor> FieldCombinationConstructor = CombinationFieldVisitor::new;

	@NotNull private final Streamable<FieldVisitorAdaptor> streamable;

	public CombinationFieldVisitor(@Nullable final FieldVisitor fieldVisitor, @NotNull final List<FieldVisitorAdaptor> fieldVisitorAdaptors)
	{
		super(AsmVersion, fieldVisitor);
		streamable = new Streamable<>(fieldVisitorAdaptors);
	}

	private void forEach(@NotNull final Consumer<FieldVisitorAdaptor> fieldVisitorAdaptorConsumer)
	{
		streamable.forEach(fieldVisitorAdaptorConsumer);
	}

	@NotNull
	private <V> List<V> map(@NotNull final Function<FieldVisitorAdaptor, V> action)
	{
		return streamable.map(action);
	}

	@NotNull
	private CombinationAnnotationVisitor annotations(@Nullable final AnnotationVisitor superVisitor, @NotNull final Function<FieldVisitorAdaptor, AnnotationVisitorAdaptor> lambda)
	{
		return streamable.combination(superVisitor, lambda, AnnotationCombinationConstructor);
	}

	@Override
	public AnnotationVisitor visitAnnotation(@NotNull final String desc, final boolean visible)
	{
		return annotations(super.visitAnnotation(desc, visible), visitor -> visitor.annotationOnField(desc, toRetentionPolicy(visible)));
	}

	@Override
	public AnnotationVisitor visitTypeAnnotation(final int typeRef, @Nullable final TypePath typePath, @NotNull final String desc, final boolean visible)
	{
		return annotations(super.visitTypeAnnotation(typeRef, typePath, desc, visible), visitor -> visitor.annotationOnType(typeRef, typePath, desc, toRetentionPolicy(visible)));
	}

	@Override
	public void visitAttribute(@NotNull final Attribute attr)
	{
		super.visitAttribute(attr);
		forEach(annotationVisitorAdaptor -> annotationVisitorAdaptor.unknownAttribute(attr));
	}

	@Override
	public void visitEnd()
	{
		super.visitEnd();
		forEach(end);
	}
}
