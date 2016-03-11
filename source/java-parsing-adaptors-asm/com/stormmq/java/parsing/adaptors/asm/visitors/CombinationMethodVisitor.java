package com.stormmq.java.parsing.adaptors.asm.visitors;

import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.methodVisitorAdaptors.MethodVisitorAdaptor;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AnnotationVisitorAdaptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.*;

import java.util.*;
import java.util.function.*;

import static com.stormmq.java.parsing.adaptors.asm.helpers.AsmHelper.AsmVersion;
import static com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AnnotationVisitorAdaptor.toRetentionPolicy;
import static com.stormmq.java.parsing.adaptors.asm.visitors.CombinationAnnotationVisitor.AnnotationCombinationConstructor;
import static com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.methodVisitorAdaptors.MethodVisitorAdaptor.code;
import static com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.methodVisitorAdaptors.MethodVisitorAdaptor.end;
import static java.util.Arrays.asList;

public final class CombinationMethodVisitor extends MethodVisitor
{
	@NotNull public static final BiFunction<MethodVisitor, List<MethodVisitorAdaptor>, CombinationMethodVisitor> MethodCombinationConstructor = CombinationMethodVisitor::new;

	@NotNull private final Streamable<MethodVisitorAdaptor> streamable;

	@NotNull
	public static CombinationMethodVisitor combinationMethodVisitor(@NotNull final MethodVisitorAdaptor... methodVisitorAdaptors)
	{
		return new CombinationMethodVisitor(null, asList(methodVisitorAdaptors));
	}

	public CombinationMethodVisitor(@Nullable final MethodVisitor superVisitor, @NotNull final List<MethodVisitorAdaptor> methodVisitorAdaptors)
	{
		super(AsmVersion, superVisitor);
		streamable = new Streamable<>(methodVisitorAdaptors);
	}

	private void forEach(@NotNull final Consumer<MethodVisitorAdaptor> methodVisitorAdaptorConsumer)
	{
		streamable.forEach(methodVisitorAdaptorConsumer);
	}

	@NotNull
	private <V> List<V> map(@NotNull final Function<MethodVisitorAdaptor, V> action)
	{
		return streamable.map(action);
	}

	@NotNull
	private CombinationAnnotationVisitor annotations(@Nullable final AnnotationVisitor superVisitor, @NotNull final Function<MethodVisitorAdaptor, AnnotationVisitorAdaptor> lambda)
	{
		return streamable.combination(superVisitor, lambda, AnnotationCombinationConstructor);
	}

	@Override
	public void visitParameter(@Nullable final String name, final int access)
	{
		super.visitParameter(name, access);
		forEach(visitor -> visitor.parameter(name, access));
	}

	@Nullable
	@Override
	public AnnotationVisitor visitAnnotationDefault()
	{
		return annotations(super.visitAnnotationDefault(), MethodVisitorAdaptor.annotationDefault);
	}

	@Nullable
	@Override
	public AnnotationVisitor visitAnnotation(@NotNull final String desc, final boolean visible)
	{
		return annotations(super.visitAnnotation(desc, visible), visitor -> visitor.annotationOnMethod(desc, toRetentionPolicy(visible)));
	}

	@Nullable
	@Override
	public AnnotationVisitor visitTypeAnnotation(final int typeRef, @Nullable final TypePath typePath, @NotNull final String desc, final boolean visible)
	{
		return annotations(super.visitTypeAnnotation(typeRef, typePath, desc, visible), visitor -> visitor.annotationOnType(typeRef, typePath, desc, toRetentionPolicy(visible)));
	}

	@Nullable
	@Override
	public AnnotationVisitor visitParameterAnnotation(final int parameter, @NotNull final String desc, final boolean visible)
	{
		return annotations(super.visitParameterAnnotation(parameter, desc, visible), visitor -> visitor.annotationOnParameter(parameter, desc, toRetentionPolicy(visible)));
	}

	@Override
	public void visitAttribute(@NotNull final Attribute attr)
	{
		super.visitAttribute(attr);
		forEach(visitor -> visitor.unknownAttribute(attr));
	}

	@Override
	public void visitCode()
	{
		super.visitCode();
		forEach(code);
	}

	@Override
	public void visitFrame(final int type, final int nLocal, @NotNull final Object[] local, final int nStack, @NotNull final Object[] stack)
	{
		super.visitFrame(type, nLocal, local, nStack, stack);
		forEach(visitor -> visitor.frame(type, nLocal, local, nStack, stack));
	}

	@Override
	public void visitInsn(final int opcode)
	{
		super.visitInsn(opcode);
		forEach(visitor -> visitor.zeroOperandInstruction(opcode));
	}

	@Override
	public void visitIntInsn(final int opcode, final int operand)
	{
		super.visitIntInsn(opcode, operand);
		forEach(visitor -> visitor.singleIntegerOperandInstruction(opcode, operand));
	}

	@Override
	public void visitVarInsn(final int opcode, @SuppressWarnings("QuestionableName") final int var)
	{
		super.visitVarInsn(opcode, var);
		forEach(visitor -> visitor.localVariableInstruction(opcode, var));
	}

	@Override
	public void visitTypeInsn(final int opcode, @NotNull final String type)
	{
		super.visitTypeInsn(opcode, type);
		forEach(visitor -> visitor.typeInstruction(opcode, type));
	}

	@Override
	public void visitFieldInsn(final int opcode, @NotNull final String owner, @NotNull final String name, @NotNull final String desc)
	{
		super.visitFieldInsn(opcode, owner, name, desc);
		forEach(visitor -> visitor.fieldInstruction(opcode, owner, name, desc));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc)
	{
		super.visitMethodInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitMethodInsn(final int opcode, @NotNull final String owner, @NotNull final String name, @NotNull final String desc, final boolean itf)
	{
		super.visitMethodInsn(opcode, owner, name, desc, itf);
		forEach(visitor -> visitor.methodInstruction(opcode, owner, name, desc, itf));
	}

	@Override
	public void visitInvokeDynamicInsn(@NotNull final String name, @NotNull final String desc, @NotNull final Handle bsm, @NotNull final Object... bsmArgs)
	{
		super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
		forEach(visitor -> visitor.invokeDynamicInstruction(name, desc, bsm, bsmArgs));
	}

	@Override
	public void visitJumpInsn(final int opcode, @NotNull final Label label)
	{
		super.visitJumpInsn(opcode, label);
		forEach(visitor -> visitor.jumpInstruction(opcode, label));
	}

	@Override
	public void visitLabel(@NotNull final Label label)
	{
		super.visitLabel(label);
		forEach(visitor -> visitor.label(label));
	}

	@Override
	public void visitLdcInsn(final Object cst)
	{
		super.visitLdcInsn(cst);
		forEach(visitor -> visitor.loadConstantInstruction(cst));
	}

	@Override
	public void visitIincInsn(@SuppressWarnings("QuestionableName") final int var, final int increment)
	{
		super.visitIincInsn(var, increment);
		forEach(visitor -> visitor.incrementLocalVariableInstruction(var, increment));
	}

	@Override
	public void visitTableSwitchInsn(final int min, final int max, @NotNull final Label dflt, @NotNull final Label... labels)
	{
		super.visitTableSwitchInsn(min, max, dflt, labels);
		forEach(visitor -> visitor.tableSwitchInstruction(min, max, labels, dflt));
	}

	@Override
	public void visitLookupSwitchInsn(@NotNull final Label dflt, @NotNull final int[] keys, @NotNull final Label[] labels)
	{
		super.visitLookupSwitchInsn(dflt, keys, labels);
		forEach(visitor -> visitor.lookUpSwitchInstruction(keys, labels, dflt));
	}

	@Override
	public void visitMultiANewArrayInsn(@NotNull final String desc, final int dims)
	{
		super.visitMultiANewArrayInsn(desc, dims);
		forEach(visitor -> visitor.newMultidimensionalArray(desc, dims));
	}

	@Nullable
	@Override
	public AnnotationVisitor visitInsnAnnotation(final int typeRef, @Nullable final TypePath typePath, @NotNull final String desc, final boolean visible)
	{
		return annotations(super.visitInsnAnnotation(typeRef, typePath, desc, visible), visitor -> visitor.annotationOnInstruction(typeRef, typePath, desc, toRetentionPolicy(visible)));
	}

	@Override
	public void visitTryCatchBlock(@NotNull final Label start, @NotNull final Label end, @NotNull final Label handler, @Nullable final String type)
	{
		super.visitTryCatchBlock(start, end, handler, type);
		forEach(visitor -> visitor.visitTryCatchBlock(start, end, handler, type));
	}

	@Nullable
	@Override
	public AnnotationVisitor visitTryCatchAnnotation(final int typeRef, @Nullable final TypePath typePath, @NotNull final String desc, final boolean visible)
	{
		return annotations(super.visitTryCatchAnnotation(typeRef, typePath, desc, visible), visitor -> visitor.annotationOnTryCatch(typeRef, typePath, desc, toRetentionPolicy(visible)));
	}

	@Override
	public void visitLocalVariable(@NotNull final String name, @NotNull final String desc, @Nullable final String signature, @NotNull final Label start, @NotNull final Label end, final int index)
	{
		super.visitLocalVariable(name, desc, signature, start, end, index);
		forEach(visitor -> visitor.visitLocalVariable(name, desc, signature, start, end, index));
	}

	@Nullable
	@Override
	public AnnotationVisitor visitLocalVariableAnnotation(final int typeRef, @Nullable final TypePath typePath, @NotNull final Label[] start, @NotNull final Label[] end, final int[] index, @NotNull final String desc, final boolean visible)
	{
		return annotations(super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible), visitor -> visitor.annotationOnLocalVariable(typeRef, typePath, start, end, index, desc, toRetentionPolicy(visible)));
	}

	@Override
	public void visitLineNumber(final int line, @NotNull final Label start)
	{
		super.visitLineNumber(line, start);
	}

	@Override
	public void visitMaxs(final int maxStack, final int maxLocals)
	{
		super.visitMaxs(maxStack, maxLocals);
		forEach(visitor -> visitor.maximumMethodLocalAndStackSize(maxLocals, maxStack));
	}

	@Override
	public void visitEnd()
	{
		super.visitEnd();
		forEach(end);
	}
}
