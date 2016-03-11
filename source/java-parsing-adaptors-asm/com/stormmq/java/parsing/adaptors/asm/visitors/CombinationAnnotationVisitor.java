package com.stormmq.java.parsing.adaptors.asm.visitors;

import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AnnotationVisitorAdaptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.AnnotationVisitor;

import java.util.List;
import java.util.function.*;

import static com.stormmq.java.parsing.adaptors.asm.helpers.AsmHelper.AsmVersion;

public final class CombinationAnnotationVisitor extends AnnotationVisitor
{
	@NotNull public static final BiFunction<AnnotationVisitor, List<AnnotationVisitorAdaptor>, CombinationAnnotationVisitor> AnnotationCombinationConstructor = CombinationAnnotationVisitor::new;
	@NotNull private final Streamable<AnnotationVisitorAdaptor> streamable;

	public CombinationAnnotationVisitor(@Nullable final AnnotationVisitor annotationVisitor, @NotNull final List<AnnotationVisitorAdaptor> annotationVisitorAdaptors)
	{
		super(AsmVersion, annotationVisitor);
		streamable = new Streamable<>(annotationVisitorAdaptors);
	}

	private void forEach(@NotNull final Consumer<AnnotationVisitorAdaptor> annotationVisitorAdaptorConsumer)
	{
		streamable.forEach(annotationVisitorAdaptorConsumer);
	}

	@NotNull
	private <V> List<V> map(@NotNull final Function<AnnotationVisitorAdaptor, V> action)
	{
		return streamable.map(action);
	}

	@NotNull
	private CombinationAnnotationVisitor combination(@Nullable final AnnotationVisitor superVisitor, @NotNull final Function<AnnotationVisitorAdaptor, AnnotationVisitorAdaptor> lambda)
	{
		return streamable.combination(superVisitor, lambda, AnnotationCombinationConstructor);
	}

	@Override
	public void visit(@Nullable final String name, @NotNull final Object value)
	{
		super.visit(name, value);
		final Consumer<AnnotationVisitorAdaptor> annotationVisitorAdaptorConsumer;
		if (name == null)
		{
			annotationVisitorAdaptorConsumer = annotationVisitorAdaptor -> annotationVisitorAdaptor.defaultKeyOrArrayValuePrimitiveStringOrArrayOfThem(value);
		}
		else
		{
			annotationVisitorAdaptorConsumer = annotationVisitorAdaptor -> annotationVisitorAdaptor.keyValuePrimitiveStringOrArrayOfThem(name, value);
		}
		forEach(annotationVisitorAdaptorConsumer);
	}

	@Override
	public void visitEnum(@Nullable final String name, @NotNull final String desc, @NotNull final String value)
	{
		super.visitEnum(name, desc, value);
		final Consumer<AnnotationVisitorAdaptor> annotationVisitorAdaptorConsumer;
		if (name == null)
		{
			annotationVisitorAdaptorConsumer = annotationVisitorAdaptor -> annotationVisitorAdaptor.defaultKeyOrArrayValueEnum(desc, value);
		}
		else
		{
			annotationVisitorAdaptorConsumer = annotationVisitorAdaptor -> annotationVisitorAdaptor.keyValueEnum(name, desc, value);
		}
		forEach(annotationVisitorAdaptorConsumer);
	}

	@Override
	public AnnotationVisitor visitAnnotation(@Nullable final String name, @NotNull final String desc)
	{
		final Function<AnnotationVisitorAdaptor, AnnotationVisitorAdaptor> annotationVisitorAdaptorFunction;
		if (name == null)
		{
			annotationVisitorAdaptorFunction = annotationVisitorAdaptor -> annotationVisitorAdaptor.defaultKeyOrArrayValueAnnotationInstance(desc);
		}
		else
		{
			annotationVisitorAdaptorFunction = annotationVisitorAdaptor -> annotationVisitorAdaptor.keyValueAnnotationInstance(name, desc);
		}
		return combination(super.visitAnnotation(name, desc), annotationVisitorAdaptorFunction);
	}

	@Override
	public AnnotationVisitor visitArray(@Nullable final String name)
	{
		return combination(super.visitArray(name), annotationVisitorAdaptor -> annotationVisitorAdaptor.keyValueArray(name));
	}

	@Override
	public void visitEnd()
	{
		super.visitEnd();
		forEach(AnnotationVisitorAdaptor.end);
	}
}
