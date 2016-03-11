package com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.methodVisitorAdaptors;

import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AnnotationVisitorAdaptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.*;

import java.lang.annotation.RetentionPolicy;
import java.util.function.Consumer;
import java.util.function.Function;

public interface MethodVisitorAdaptor
{
	@Nullable
	default AnnotationVisitorAdaptor annotationOnMethod(@NotNull final String annotationTypeDescriptor, @NotNull final RetentionPolicy retentionPolicy)
	{
		return null;
	}

	@Nullable
	default AnnotationVisitorAdaptor annotationOnType(final int typeRef, @Nullable final TypePath typePath, @NotNull final String annotationTypeDescriptor, @NotNull final RetentionPolicy retentionPolicy)
	{
		return null;
	}

	default void unknownAttribute(@NotNull final Attribute unknownAttribute)
	{
		throw new IllegalArgumentException("Unknown attribute");
	}

	@Nullable
	default AnnotationVisitorAdaptor annotationOnParameter(final int parameter, @NotNull final String annotationTypeDescriptor, @NotNull final RetentionPolicy retentionPolicy)
	{
		return null;
	}

	@Nullable
	default AnnotationVisitorAdaptor annotationOnInstruction(final int typeRef, @Nullable final TypePath typePath, @NotNull final String annotationTypeDescriptor, @NotNull final RetentionPolicy retentionPolicy)
	{
		return null;
	}

	default AnnotationVisitorAdaptor annotationOnTryCatch(final int typeRef, @Nullable final TypePath typePath, @NotNull final String annotationTypeDescriptor, @NotNull final RetentionPolicy retentionPolicy)
	{
		return null;
	}

	@Nullable
	default AnnotationVisitorAdaptor annotationOnLocalVariable(final int typeRef, @Nullable final TypePath typePath, @NotNull final Label[] start, @NotNull final Label[] end, final int[] index, @NotNull final String annotationTypeDescriptor, @NotNull final RetentionPolicy retentionPolicy)
	{
		return null;
	}

	@Nullable
	default AnnotationVisitorAdaptor annotationDefault()
	{
		return null;
	}
	@NotNull Function<MethodVisitorAdaptor, AnnotationVisitorAdaptor> annotationDefault = MethodVisitorAdaptor::annotationDefault;

	default void parameter(@Nullable final String parameterName, final int modifiers)
	{
	}

	default void maximumMethodLocalAndStackSize(final int maxLocals, final int maxStack)
	{
	}

	default void code()
	{
	}
	@NotNull Consumer<MethodVisitorAdaptor> code = MethodVisitorAdaptor::code;

	default void frame(final int frameType, final int nLocal, @NotNull final Object[] local, final int nStack, @NotNull final Object[] stack)
	{
	}

	default void zeroOperandInstruction(final int opCode)
	{
	}

	default void singleIntegerOperandInstruction(final int opCode, final int operand)
	{
	}

	default void localVariableInstruction(final int opCode, final int localVariableIndex)
	{
	}

	default void typeInstruction(final int opCode, @NotNull final String internalTypeName)
	{
	}

	default void fieldInstruction(final int opCode, @NotNull final String fieldOwnerInternalTypeName, @NotNull final String fieldName, @NotNull final String fieldTypeDescriptor)
	{
	}

	default void methodInstruction(final int opCode, @NotNull final String fieldOwnerInternalTypeName, @NotNull final String methodName, @NotNull final String methodDescriptor, final boolean methodOwnerIsInterface)
	{
	}

	default void invokeDynamicInstruction(@NotNull final String methodName, @NotNull final String methodDescriptor, @NotNull final Handle bootstrapMethodHandle, @NotNull final Object... bootstrapMethodArguments)
	{
	}

	default void jumpInstruction(final int opCode, @NotNull final Label label)
	{
	}

	default void label(@NotNull final Label label)
	{
	}

	// As well as primitive, constantValue can be String, a Handle, a Type, etc
	default void loadConstantInstruction(@NotNull final Object constantValue)
	{
	}

	default void incrementLocalVariableInstruction(final int localVariableIndex, final int increment)
	{
	}

	default void tableSwitchInstruction(final int minimumKeyValue, final int maximumKeyValue, @NotNull final Label[] labels, @NotNull final Label beginningOfDefaultHandlerBlock)
	{
	}

	default void lookUpSwitchInstruction(@NotNull final int[] keyValues, @NotNull final Label[] beginningOfEachHandlerBlock, @NotNull final Label beginningOfDefaultHandlerBlock)
	{
	}

	default void newMultidimensionalArray(@NotNull final String arrayTypeDescriptor, final int numberOfDimensionsToAllocate)
	{
	}

	default void visitTryCatchBlock(@NotNull final Label start, @NotNull final Label end, @NotNull final Label handler, @Nullable final String type)
	{
	}

	default void visitLocalVariable(@NotNull final String localVariableName, @NotNull final String internalTypeName, @Nullable final String signature, @NotNull final Label start, @NotNull final Label end, final int index)
	{
	}

	default void end()
	{
	}
	@NotNull Consumer<MethodVisitorAdaptor> end = MethodVisitorAdaptor::end;
}
