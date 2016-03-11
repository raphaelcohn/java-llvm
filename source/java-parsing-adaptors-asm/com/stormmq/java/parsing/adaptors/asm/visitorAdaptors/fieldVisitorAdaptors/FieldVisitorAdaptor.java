package com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.fieldVisitorAdaptors;

import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AnnotationVisitorAdaptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.TypePath;

import java.lang.annotation.RetentionPolicy;
import java.util.function.Consumer;

public interface FieldVisitorAdaptor
{
	default void unknownAttribute(@NotNull final Attribute unknownAttribute)
	{
		throw new IllegalArgumentException("Unknown attribute");
	}

	@Nullable
	default AnnotationVisitorAdaptor annotationOnField(@NotNull final String annotationTypeDescriptor, @NotNull final RetentionPolicy retentionPolicy)
	{
		return null;
	}

	@Nullable
	default AnnotationVisitorAdaptor annotationOnType(final int typeRef, @Nullable final TypePath typePath, @NotNull final String annotationTypeDescriptor, @NotNull final RetentionPolicy retentionPolicy)
	{
		return null;
	}

	default void end()
	{
	}
	@NotNull Consumer<FieldVisitorAdaptor> end = FieldVisitorAdaptor::end;
}
