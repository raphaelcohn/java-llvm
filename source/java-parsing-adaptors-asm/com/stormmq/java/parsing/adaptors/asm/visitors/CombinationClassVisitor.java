package com.stormmq.java.parsing.adaptors.asm.visitors;

import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AnnotationVisitorAdaptor;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.fieldVisitorAdaptors.FieldVisitorAdaptor;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.methodVisitorAdaptors.MethodVisitorAdaptor;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.typeVisitorAdaptors.MethodContainer;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.typeVisitorAdaptors.TypeVisitorAdaptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.stormmq.java.parsing.adaptors.asm.helpers.AsmHelper.AsmVersion;
import static com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AnnotationVisitorAdaptor.toRetentionPolicy;
import static com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.typeVisitorAdaptors.TypeVisitorAdaptor.end;
import static com.stormmq.java.parsing.adaptors.asm.visitors.CombinationAnnotationVisitor.AnnotationCombinationConstructor;
import static com.stormmq.java.parsing.adaptors.asm.visitors.CombinationFieldVisitor.FieldCombinationConstructor;
import static com.stormmq.java.parsing.adaptors.asm.visitors.CombinationMethodVisitor.MethodCombinationConstructor;
import static com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.typeVisitorAdaptors.MethodContainer.NoMethodContainer;
import static com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.typeVisitorAdaptors.MethodContainer.localOrAnonymousClassInMethod;
import static java.util.Arrays.asList;

/*
	Order is normally:-
		visit()
		visitSource() [only if not SKIP_DEBUG and has some debug data]
		visitOuterClass() [only if there is an outer class / method]
		annotationOnMethod() [if any]
		annotationOnType() [if any]
		visitAttribute() [if any unrecognised]
		visitInnerClass() [if any]
		visitField()
			annotationOnMethod() [if any]
			annotationOnType() [if any]
			visitAttribute() [if any unrecognised]
			visitEnd()
		visitMethod()
			parameter()
			annotationDefault()
				visit() / visitEnum() / visitArray() / annotationOnMethod() [ visit a nested annotation ]
				visitEnd()
			annotationOnMethod()
			annotationOnType()
			readParameterAnnotations()
			visitAttribute()
			visitCode() [only if SKIP_CODE]
			visitEnd()
		visitEnd()

	However, visitMethod() can occur before visit() for <clinit> and <init>; there really isn't any g'tee
	Hence this class exists to create a very simple anaemic model which can be used later on
 */
public final class CombinationClassVisitor extends ClassVisitor
{
	@NotNull private static final String[] Empty = new String[0];

	@NotNull private final Streamable<TypeVisitorAdaptor> streamable;

	@NotNull
	public static ClassVisitor combinationClassVisitor(@NotNull final TypeVisitorAdaptor... typeVisitorAdaptors)
	{
		return new CombinationClassVisitor(null, asList(typeVisitorAdaptors));
	}

	public CombinationClassVisitor(@Nullable final ClassVisitor classVisitor, @NotNull final List<TypeVisitorAdaptor> typeVisitorAdaptors)
	{
		super(AsmVersion, classVisitor);
		streamable = new Streamable<>(typeVisitorAdaptors);
	}

	private void forEach(@NotNull final Consumer<TypeVisitorAdaptor> classVisitorAdaptorConsumer)
	{
		streamable.forEach(classVisitorAdaptorConsumer);
	}

	@NotNull
	private CombinationAnnotationVisitor annotations(@Nullable final AnnotationVisitor superVisitor, @NotNull final Function<TypeVisitorAdaptor, AnnotationVisitorAdaptor> lambda)
	{
		return streamable.combination(superVisitor, lambda, AnnotationCombinationConstructor);
	}

	@NotNull
	private CombinationFieldVisitor fields(@Nullable final FieldVisitor superVisitor, @NotNull final Function<TypeVisitorAdaptor, FieldVisitorAdaptor[]> lambda)
	{
		return streamable.combinationOfCombination(superVisitor, lambda, FieldCombinationConstructor);
	}

	@NotNull
	private CombinationMethodVisitor methods(@Nullable final MethodVisitor superVisitor, @NotNull final Function<TypeVisitorAdaptor, MethodVisitorAdaptor[]> lambda)
	{
		return streamable.combinationOfCombination(superVisitor, lambda, MethodCombinationConstructor);
	}

	@Override
	public AnnotationVisitor visitAnnotation(@NotNull final String desc, final boolean visible)
	{
		return annotations(super.visitAnnotation(desc, visible), visitor -> visitor.annotationOnType(desc, toRetentionPolicy(visible)));
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
	public void visit(final int version, final int access, @NotNull final String name, @Nullable final String signature, @Nullable final String superName, @Nullable final String[] interfaces)
	{
		super.visit(version, access, name, signature, superName, interfaces);
		forEach(annotationVisitorAdaptor -> annotationVisitorAdaptor.type(version, access, name, signature, superName, (interfaces == null) ? Empty : interfaces));
	}

	@Override
	public void visitSource(@Nullable final String source, @Nullable final String debug)
	{
		super.visitSource(source, debug);
		forEach(annotationVisitorAdaptor -> annotationVisitorAdaptor.source(source, debug));
	}

	@Override
	public void visitOuterClass(@NotNull final String owner, @Nullable final String name, @Nullable final String desc)
	{
		super.visitOuterClass(owner, name, desc);
		final MethodContainer methodContainer;
		if (name == null)
		{
			if (desc != null)
			{
				throw newIllegalCombination();
			}
			methodContainer = NoMethodContainer;
		}
		else
		{
			if (desc == null)
			{
				throw newIllegalCombination();
			}
			methodContainer = localOrAnonymousClassInMethod(name, desc);
		}
		forEach(annotationVisitorAdaptor -> annotationVisitorAdaptor.outerType(owner, methodContainer));
	}

	@NotNull
	private static IllegalArgumentException newIllegalCombination()
	{
		return new IllegalArgumentException("name and desc must both be null or both be a value");
	}

	@Override
	public void visitInnerClass(@NotNull final String name, @Nullable final String outerName, @Nullable final String innerName, final int access)
	{
		super.visitInnerClass(name, outerName, innerName, access);
		forEach(annotationVisitorAdaptor -> annotationVisitorAdaptor.innerType(name, outerName, innerName, access));
	}

	@Override
	public FieldVisitor visitField(final int access, @NotNull final String name, @NotNull final String desc, @Nullable final String signature, @Nullable final Object value)
	{
		return fields(super.visitField(access, name, desc, signature, value), classVisitorAdaptor -> classVisitorAdaptor.field(access, name, desc, signature, value));
	}

	@Override
	public MethodVisitor visitMethod(final int access, @NotNull final String name, @NotNull final String desc, @Nullable final String signature, @Nullable final String[] exceptions)
	{
		return methods(super.visitMethod(access, name, desc, signature, exceptions), classVisitorAdaptor -> classVisitorAdaptor.method(access, name, desc, signature, (exceptions == null) ? Empty : exceptions));
	}

	@Override
	public void visitEnd()
	{
		super.visitEnd();
		forEach(end);
	}
}
