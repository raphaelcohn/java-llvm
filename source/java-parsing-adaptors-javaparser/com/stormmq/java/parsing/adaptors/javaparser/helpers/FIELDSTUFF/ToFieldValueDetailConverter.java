package com.stormmq.java.parsing.adaptors.javaparser.helpers.FIELDSTUFF;

import com.stormmq.java.parsing.ast.details.fieldValueDetails.*;
import com.stormmq.java.parsing.utilities.names.typeNames.PrimitiveTypeName;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public interface ToFieldValueDetailConverter
{
	@NotNull EnumMap<PrimitiveTypeName, ToFieldValueDetailConverter> Converters = new EnumMap<PrimitiveTypeName, ToFieldValueDetailConverter>(PrimitiveTypeName.class)
	{{
		put(PrimitiveTypeName._boolean, new ToFieldValueDetailConverter()
		{
			@NotNull
			@Override
			public InvariantStaticFieldValueDetail convert(@NotNull final Object value)
			{
				if (!(value instanceof Boolean))
				{
					throw new IllegalArgumentException("Can only convert boolean constants to BooleanInvariantStaticFieldValueDetail");
				}
				return new BooleanInvariantStaticFieldValueDetail((Boolean) value);
			}
		});
		put(PrimitiveTypeName._byte, new ToFieldValueDetailConverter()
		{
			@NotNull
			@Override
			public InvariantStaticFieldValueDetail convert(@NotNull final Object value)
			{
				if (!(value instanceof Integer))
				{
					throw new IllegalArgumentException("Can only convert int constants to ByteInvariantStaticFieldValueDetail");
				}
				return new ByteInvariantStaticFieldValueDetail((byte) (int) value);
			}
		});
		put(PrimitiveTypeName._short, new ToFieldValueDetailConverter()
		{
			@NotNull
			@Override
			public InvariantStaticFieldValueDetail convert(@NotNull final Object value)
			{
				if (!(value instanceof Integer))
				{
					throw new IllegalArgumentException("Can only convert int constants to ShortInvariantStaticFieldValueDetail");
				}
				return new ShortInvariantStaticFieldValueDetail((short) (int) value);
			}
		});
		put(PrimitiveTypeName._char, new ToFieldValueDetailConverter()
		{
			@NotNull
			@Override
			public InvariantStaticFieldValueDetail convert(@NotNull final Object value)
			{
				if (value instanceof Integer)
				{
					return new CharInvariantStaticFieldValueDetail((char) (int) value);
				}
				if (value instanceof Character)
				{
					return new CharInvariantStaticFieldValueDetail((char) value);
				}
				throw new IllegalArgumentException("Can only convert int and char constants to CharInvariantStaticFieldValueDetail");
			}
		});
		put(PrimitiveTypeName._int, new ToFieldValueDetailConverter()
		{
			@NotNull
			@Override
			public InvariantStaticFieldValueDetail convert(@NotNull final Object value)
			{
				if (!(value instanceof Integer))
				{
					throw new IllegalArgumentException("Can only convert int constants to IntegerInvariantStaticFieldValueDetail");
				}
				return new IntegerInvariantStaticFieldValueDetail((int) value);
			}
		});
		put(PrimitiveTypeName._long, new ToFieldValueDetailConverter()
		{
			@NotNull
			@Override
			public InvariantStaticFieldValueDetail convert(@NotNull final Object value)
			{
				if (value instanceof Integer)
				{
					return new LongInvariantStaticFieldValueDetail((long) (int) value);
				}
				if (value instanceof Long)
				{
					return new LongInvariantStaticFieldValueDetail((long) value);
				}
				throw new IllegalArgumentException("Can only convert int and long constants to LongInvariantStaticFieldValueDetail");
			}
		});
		put(PrimitiveTypeName._float, new ToFieldValueDetailConverter()
		{
			@NotNull
			@Override
			public InvariantStaticFieldValueDetail convert(@NotNull final Object value)
			{
				if (value instanceof Float)
				{
					return new FloatInvariantStaticFieldValueDetail((float) value);
				}
				if (value instanceof Integer)
				{
					return new FloatInvariantStaticFieldValueDetail((float) (int) value);
				}
				if (value instanceof Long)
				{
					return new FloatInvariantStaticFieldValueDetail((float) (long) value);
				}
				throw new IllegalArgumentException("Can only convert float, int and long constants to FloatInvariantStaticFieldValueDetail");
			}
		});
		put(PrimitiveTypeName._double, new ToFieldValueDetailConverter()
		{
			@NotNull
			@Override
			public InvariantStaticFieldValueDetail convert(@NotNull final Object value)
			{
				if (value instanceof Float)
				{
					return new DoubleInvariantStaticFieldValueDetail((float) value);
				}
				if (value instanceof Double)
				{
					return new DoubleInvariantStaticFieldValueDetail((double) value);
				}
				if (value instanceof Integer)
				{
					return new DoubleInvariantStaticFieldValueDetail((float) (int) value);
				}
				if (value instanceof Long)
				{
					return new DoubleInvariantStaticFieldValueDetail((float) (long) value);
				}
				throw new IllegalArgumentException("Can only convert float, double, int and long constants to DoubleInvariantStaticFieldValueDetail");
			}
		});
	}};

	@NotNull
	InvariantStaticFieldValueDetail convert(@NotNull final Object value);
}
