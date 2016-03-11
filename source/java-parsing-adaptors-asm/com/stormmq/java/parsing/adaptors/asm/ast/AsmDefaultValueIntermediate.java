package com.stormmq.java.parsing.adaptors.asm.ast;

import com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationValueItem;
import com.stormmq.java.parsing.adaptors.asm.items.AsmEnumConstantItem;
import com.stormmq.java.parsing.ast.details.valueDetails.*;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.DefaultValueIntermediate;
import com.stormmq.java.parsing.ast.intermediates.TypeIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.PrimitiveTypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.TypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.ImplementedTypeUsage;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static com.stormmq.java.parsing.utilities.PrimitiveTypeHelper.isBoxedPrimitiveExcludingVoid;

public final class AsmDefaultValueIntermediate implements DefaultValueIntermediate
{
	@NotNull private static final ArrayValueDetail<AnnotationSingleValueDetail> EmptyAnnotationArray = new ArrayValueDetail<>(new AnnotationSingleValueDetail[0]);
	@NotNull private static final ArrayValueDetail<EnumConstantSingleValueDetail> EmptyEnumArray = new ArrayValueDetail<>(new EnumConstantSingleValueDetail[0]);

	@NotNull private static final Map<Class<?>, Class<? extends SingleValueDetail>> Map = new HashMap<>(8);
	@NotNull private static final Map<TypeName, Class<?>> TypeNames = new HashMap<>(8);
	@NotNull private static final Map<TypeName, ArrayValueDetail> Empty = new HashMap<>(8);

	static
	{
		Map.put(boolean.class, BooleanSingleValueDetail.class);
		Map.put(byte.class, ByteSingleValueDetail.class);
		Map.put(short.class, ShortSingleValueDetail.class);
		Map.put(char.class, CharacterSingleValueDetail.class);
		Map.put(int.class, IntegerSingleValueDetail.class);
		Map.put(long.class, LongSingleValueDetail.class);
		Map.put(float.class, FloatSingleValueDetail.class);
		Map.put(double.class, DoubleSingleValueDetail.class);
		Map.put(String.class, StringSingleValueDetail.class);
		Map.put(KnownReferenceTypeName.class, KnownReferenceTypeNameSingleValueDetail.class);

		TypeNames.put(PrimitiveTypeName._boolean, boolean.class);
		TypeNames.put(PrimitiveTypeName._byte, byte.class);
		TypeNames.put(PrimitiveTypeName._short, short.class);
		TypeNames.put(PrimitiveTypeName._char, char.class);
		TypeNames.put(PrimitiveTypeName._int, int.class);
		TypeNames.put(PrimitiveTypeName._long, long.class);
		TypeNames.put(PrimitiveTypeName._float, float.class);
		TypeNames.put(PrimitiveTypeName._double, double.class);
		TypeNames.put(KnownReferenceTypeName.JavaLangString, String.class);
		TypeNames.put(new KnownReferenceTypeName(KnownReferenceTypeName.class.getName()), KnownReferenceTypeName.class);

		for (java.util.Map.Entry<Class<?>, Class<? extends SingleValueDetail>> entry : Map.entrySet())
		{
			for (java.util.Map.Entry<TypeName, Class<?>> classEntry : TypeNames.entrySet())
			{
				if (classEntry.getValue().equals(entry.getKey()))
				{
					//noinspection unchecked
					Empty.put(classEntry.getKey(), new ArrayValueDetail((SingleValueDetail[]) Array.newInstance(entry.getValue(), 0)));
				}
			}
		}
	}

	@NotNull private final Object value;

	public AsmDefaultValueIntermediate(@NotNull final Object value)
	{
		// Boxed primitive, String, KnownReferenceType, AsmEnumConstantItem, AsmAnnotationValueItem, array of unboxed primitive, array of String, array of KnownReferenceType, array of AsmEnumConstantItem, array of AsmAnnotationValueItem
		this.value = value;
	}

	@Override
	public boolean isAValue()
	{
		return true;
	}

	@NotNull
	@Override
	public ValueDetail resolve(@NotNull final TypeUsage resolvedReturnTypeUsage, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		return resolve(resolvedReturnTypeUsage, simpleTypeIntermediateRecorder, value);
	}

	@NotNull
	public static ValueDetail resolve(@NotNull final ImplementedTypeUsage resolvedReturnTypeUsage, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder, @NotNull final Object value)
	{
		final Class<?> valueClass = value.getClass();
		if (valueClass.isArray())
		{
			if (!resolvedReturnTypeUsage.isArray())
			{
				throw new IllegalArgumentException("resolvedReturnTypeUsage is not an array");
			}
			if (resolvedReturnTypeUsage.isMultiDimensionalArray())
			{
				throw new IllegalArgumentException("resolvedReturnTypeUsage is multidimensional");
			}

			final TypeName typeName = resolvedReturnTypeUsage.typeName;

			final int length = Array.getLength(value);
			if (length == 0)
			{
				@Nullable final ArrayValueDetail arrayDefaultValueDetail = Empty.get(typeName);
				if (arrayDefaultValueDetail != null)
				{
					return arrayDefaultValueDetail;
				}
				if (!(typeName instanceof KnownReferenceTypeName))
				{
					throw new IllegalArgumentException("resolvedReturnTypeUsage did not use a KnownReferenceTypeName");
				}
				final TypeIntermediate typeIntermediate = simpleTypeIntermediateRecorder.findMustBeExtant((KnownReferenceTypeName) typeName);
				final IntermediateCreator intermediateCreator = typeIntermediate.intermediateCreator();
				switch(intermediateCreator)
				{
					case Class:
					case Interface:
						throw new IllegalArgumentException("resolvedReturnTypeUsage was not an annotation or enum");

					case Annotation:
						return EmptyAnnotationArray;

					case Enum:
						return EmptyEnumArray;

					default:
						throw new IllegalStateException(StringConstants.ShouldBeImpossible);
				}
			}

			final Class<?> componentType = Array.get(value, 0).getClass();

			final Class<? extends SingleValueDetail> aClass = Map.get(componentType);
			if (aClass != null)
			{
				return array(aClass, length, value);
			}

			if (AsmEnumConstantItem.class.isAssignableFrom(componentType))
			{
				final EnumConstantSingleValueDetail[] values = new EnumConstantSingleValueDetail[length];
				for (int index = 0; index < length; index++)
				{
					values[index] = ((AsmEnumConstantItem) value).resolve();
				}
				return new ArrayValueDetail<>((SingleValueDetail[]) values);

			}
			else if (AsmAnnotationValueItem.class.isAssignableFrom(componentType))
			{
				final AnnotationSingleValueDetail[] values = new AnnotationSingleValueDetail[length];
				for (int index = 0; index < length; index++)
				{
					values[index] = ((AsmAnnotationValueItem) value).resolve(resolvedReturnTypeUsage, simpleTypeIntermediateRecorder);
				}
				return new ArrayValueDetail<>((SingleValueDetail[]) values);
			}
			else
			{
				throw new IllegalStateException("Can not resolve value array");
			}
		}
		else
		{
			if (resolvedReturnTypeUsage.isArray())
			{
				throw new IllegalArgumentException("resolvedReturnTypeUsage is an array");
			}

			if (isBoxedPrimitiveExcludingVoid(valueClass))
			{
				if (value instanceof Boolean)
				{
					return new BooleanSingleValueDetail((Boolean) value);
				}
				if (value instanceof Byte)
				{
					return new ByteSingleValueDetail((Byte) value);
				}
				if (value instanceof Short)
				{
					return new ShortSingleValueDetail((Short) value);
				}
				if (value instanceof Character)
				{
					return new CharacterSingleValueDetail((Character) value);
				}
				if (value instanceof Integer)
				{
					return new IntegerSingleValueDetail((Integer) value);
				}
				if (value instanceof Long)
				{
					return new LongSingleValueDetail((Long) value);
				}
				if (value instanceof Float)
				{
					return new FloatSingleValueDetail((Float) value);
				}
				if (value instanceof Double)
				{
					return new DoubleSingleValueDetail((Double) value);
				}
				throw new IllegalStateException("Unknown primitive");
			}
			else if (String.class.isAssignableFrom(valueClass))
			{
				return new StringSingleValueDetail((String) value);
			}
			else if (KnownReferenceTypeName.class.isAssignableFrom(valueClass))
			{
				return new KnownReferenceTypeNameSingleValueDetail((KnownReferenceTypeName) value);
			}
			else if (AsmEnumConstantItem.class.isAssignableFrom(valueClass))
			{
				final AsmEnumConstantItem asmEnumConstantItem = (AsmEnumConstantItem) value;
				return asmEnumConstantItem.resolve();
			}
			else if (AsmAnnotationValueItem.class.isAssignableFrom(valueClass))
			{
				final AsmAnnotationValueItem asmAnnotationValueItem = (AsmAnnotationValueItem) value;
				return asmAnnotationValueItem.resolve(resolvedReturnTypeUsage, simpleTypeIntermediateRecorder);
			}
			else
			{
				throw new IllegalStateException("Can not resolve value");
			}
		}
	}

	@NotNull
	private static ValueDetail array(@NotNull final Class<? extends SingleValueDetail> arrayComponent, final int length, @NotNull final Object value)
	{
		final Object values = Array.newInstance(arrayComponent, length);
		for(int index = 0; index < length; index++)
		{
			final Constructor<?> constructor = arrayComponent.getConstructors()[0];
			final Object o;
			try
			{
				o = constructor.newInstance(Array.get(value, index));
			}
			catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
			{
				throw new IllegalStateException(e);
			}
			Array.set(values, index, o);
		}
		return new ArrayValueDetail<>((SingleValueDetail[]) values);
	}
}
