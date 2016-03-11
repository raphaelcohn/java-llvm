package com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.typeVisitorAdaptors;

import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.fieldVisitorAdaptors.FieldVisitorAdaptor;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AnnotationVisitorAdaptor;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.methodVisitorAdaptors.MethodVisitorAdaptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.TypePath;

import java.lang.annotation.RetentionPolicy;
import java.util.function.Consumer;

public interface TypeVisitorAdaptor
{
	@NotNull FieldVisitorAdaptor[] EmptyFieldVisitorAdaptors = {};

	@NotNull MethodVisitorAdaptor[] EmptyMethodVisitorAdaptors = {};

	@Nullable
	default AnnotationVisitorAdaptor annotationOnType(@NotNull final String annotationTypeDescriptor, @NotNull final RetentionPolicy retentionPolicy)
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

	default void end()
	{
	}
	@NotNull Consumer<TypeVisitorAdaptor> end = TypeVisitorAdaptor::end;

	@SuppressWarnings("MethodCanBeVariableArityMethod")
	default void type(final int version, final int modifiers, @NotNull final String typeName, @Nullable final String typeParameters, @Nullable final String superTypeName, @NotNull final String[] interfaceTypeNames)
	{
	}

	default void source(@Nullable final String sourceFileName, @Nullable final String anyAdditionalDebugInformation)
	{
	}

	default void outerType(@NotNull final String outerTypeInternalTypeName, @NotNull final MethodContainer methodContainer)
	{
	}

	default void innerType(@NotNull final String internalTypeName, @Nullable final String outerInternalTypeName, @Nullable final String innerInternalTypeName, final int modifiers)
	{
	}

	@NotNull
	default FieldVisitorAdaptor[] field(final int modifiers, @NotNull final String fieldName, @NotNull final String fieldTypeDescriptor, @Nullable final String signature, @Nullable final Object fieldValueIfAny)
	{
		return EmptyFieldVisitorAdaptors;
	}

	@SuppressWarnings("MethodCanBeVariableArityMethod")
	@NotNull
	default MethodVisitorAdaptor[] method(final int modifiers, @NotNull final String methodName, @NotNull final String methodDescriptor, @Nullable final String signature, @NotNull final String[] exceptionInternalTypeNames)
	{
		return EmptyMethodVisitorAdaptors;
	}

}
