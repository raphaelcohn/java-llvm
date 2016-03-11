package com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors;

import com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationValueItem;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;

import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import java.util.function.Consumer;

import static com.stormmq.java.parsing.adaptors.asm.helpers.AsmHelper.typeUsageFromAsmType;
import static com.stormmq.java.parsing.utilities.PrimitiveTypeHelper.isBoxedPrimitiveExcludingVoid;
import static com.stormmq.java.parsing.utilities.PrimitiveTypeHelper.isUnboxedPrimitiveExcludingVoid;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public interface AnnotationVisitorAdaptor
{
	@NotNull
	static RetentionPolicy toRetentionPolicy(final boolean visible)
	{
		return visible ? RUNTIME : CLASS;
	}

	@NotNull
	static Object actualValue(@NotNull final Object value, final boolean arraysArePermitted)
	{
		@NotNull final Class<?> valueClass = value.getClass();

		if (valueClass.equals(Type.class))
		{
			return typeUsageFromAsmType((Type) value);
		}

		if (valueClass.equals(String.class) || valueClass.equals(AsmAnnotationValueItem.class) || isBoxedPrimitiveExcludingVoid(valueClass))
		{
			return value;
		}

		if (valueClass.isArray())
		{
			if (!arraysArePermitted)
			{
				throw new IllegalStateException("Arrays are not permitted");
			}

			@Nullable final Class<?> componentClass = valueClass.getComponentType();
			assert componentClass != null;

			if (componentClass.equals(String.class) || isUnboxedPrimitiveExcludingVoid(componentClass))
			{
				return value;
			}

			// Created by use using AbstractArrayAnnotationVisitor
			if (componentClass.equals(Object.class))
			{
				final Type[] values = (Type[]) value;
				final int length = values.length;
				final TypeUsage[] typeUsages = new TypeUsage[length];
				for (int index = 0; index < length; index++)
				{
					typeUsages[index] = typeUsageFromAsmType(values[index]);
				}
				return typeUsages;
			}

			throw new IllegalArgumentException("Unsupported array value type");
		}

		throw new IllegalArgumentException("Unsupported value type");
	}

	static void record(@NotNull final Map<String, Object> definedValues, @NotNull final String name, @NotNull final Object value)
	{
		@Nullable final Object extant = definedValues.put(name, value);
		if (extant != null)
		{
			throw new IllegalStateException("Same name visited more than once");
		}
	}

	default void keyValuePrimitiveStringOrArrayOfThem(@NotNull final String memberName, @NotNull final Object value)
	{
	}

	default void keyValueEnum(@NotNull final String memberName, @NotNull final String enumTypeDescriptor, @NotNull final String enumConstantName)
	{
	}

	@Nullable
	default AnnotationVisitorAdaptor keyValueArray(@Nullable final String memberName)
	{
		return null;
	}

	@Nullable
	default AnnotationVisitorAdaptor keyValueAnnotationInstance(@NotNull final String name, @NotNull final String annotationTypeDescriptor)
	{
		return null;
	}

	default void defaultKeyOrArrayValuePrimitiveStringOrArrayOfThem(@NotNull final Object value)
	{
	}

	default void defaultKeyOrArrayValueEnum(@NotNull final String enumTypeDescriptor, @NotNull final String enumConstantName)
	{
	}

	@Nullable
	default AnnotationVisitorAdaptor defaultKeyOrArrayValueAnnotationInstance(@NotNull final String annotationTypeDescriptor)
	{
		return null;
	}

	default void end()
	{
	}
	@NotNull Consumer<AnnotationVisitorAdaptor> end = AnnotationVisitorAdaptor::end;
}
