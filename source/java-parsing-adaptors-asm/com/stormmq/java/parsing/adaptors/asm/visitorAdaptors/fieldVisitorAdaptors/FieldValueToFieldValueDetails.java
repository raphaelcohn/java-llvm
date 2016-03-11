package com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.fieldVisitorAdaptors;

import com.stormmq.java.parsing.ast.details.fieldValueDetails.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Type.getDescriptor;

public interface FieldValueToFieldValueDetails
{
	@NotNull
	FieldValueDetail convert(@NotNull final Object value, @NotNull final String descriptor);

	@NotNull Map<Class<?>, FieldValueToFieldValueDetails> Converters = new HashMap<Class<?>, FieldValueToFieldValueDetails>()
	{{
		final String stringDescriptor = getDescriptor(String.class);
		final String booleanDescriptor = getDescriptor(boolean.class);
		final String byteDescriptor = getDescriptor(byte.class);
		final String shortDescriptor = getDescriptor(short.class);
		final String charDescriptor = getDescriptor(char.class);
		final String intDescriptor = getDescriptor(int.class);
		final String longDescriptor = getDescriptor(long.class);
		final String floatDescriptor = getDescriptor(float.class);
		final String doubleDescriptor = getDescriptor(double.class);

		put(String.class, new FieldValueToFieldValueDetails()
		{
			@NotNull
			@Override
			public FieldValueDetail convert(@NotNull final Object value, @NotNull final String descriptor)
			{
				if (descriptor.equals(stringDescriptor))
				{
					return new StringInvariantStaticFieldValueDetail((String) value);
				}
				throw new IllegalArgumentException("value does not match descriptor");
			}
		});

		put(Integer.class, new FieldValueToFieldValueDetails()
		{
			@NotNull
			@Override
			public FieldValueDetail convert(@NotNull final Object value, @NotNull final String descriptor)
			{
				final int integer = (int) value;

				if (descriptor.equals(booleanDescriptor))
				{
					return new BooleanInvariantStaticFieldValueDetail(integer != 0);
				}

				if (descriptor.equals(byteDescriptor))
				{
					return new ShortInvariantStaticFieldValueDetail((byte) integer);
				}

				if (descriptor.equals(shortDescriptor))
				{
					return new ShortInvariantStaticFieldValueDetail((short) integer);
				}

				if (descriptor.equals(charDescriptor))
				{
					return new CharInvariantStaticFieldValueDetail((char) integer);
				}

				if (descriptor.equals(intDescriptor))
				{
					return new IntegerInvariantStaticFieldValueDetail(integer);
				}

				throw new IllegalArgumentException("value does not match descriptor");
			}
		});

		put(Long.class, new FieldValueToFieldValueDetails()
		{
			@NotNull
			@Override
			public FieldValueDetail convert(@NotNull final Object value, @NotNull final String descriptor)
			{
				if (descriptor.equals(longDescriptor))
				{
					return new LongInvariantStaticFieldValueDetail((long) value);
				}
				throw new IllegalArgumentException("value does not match descriptor");
			}
		});

		put(Float.class, new FieldValueToFieldValueDetails()
		{
			@NotNull
			@Override
			public FieldValueDetail convert(@NotNull final Object value, @NotNull final String descriptor)
			{
				if (descriptor.equals(floatDescriptor))
				{
					return new FloatInvariantStaticFieldValueDetail((float) value);
				}
				throw new IllegalArgumentException("value does not match descriptor");
			}
		});

		put(Double.class, new FieldValueToFieldValueDetails()
		{
			@NotNull
			@Override
			public FieldValueDetail convert(@NotNull final Object value, @NotNull final String descriptor)
			{
				if (descriptor.equals(doubleDescriptor))
				{
					return new DoubleInvariantStaticFieldValueDetail((double) value);
				}
				throw new IllegalArgumentException("value does not match descriptor");
			}
		});
	}};
}
