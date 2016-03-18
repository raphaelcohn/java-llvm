// The MIT License (MIT)
//
// Copyright © 2016, Raphael Cohn <raphael.cohn@stormmq.com>
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package com.stormmq.llvm.domain.parameterTypes;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.attributes.Attribute;
import com.stormmq.llvm.attributes.AttributeGroup;
import com.stormmq.llvm.attributes.parameterAttributes.ParameterAttribute;
import com.stormmq.llvm.attributes.writers.ByteWriterFunctionAttributeGroupWriter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.attributes.AttributeGroup.*;

public final class SingleValueParameterType implements ParameterType
{
	@NotNull public static final SingleValueParameterType Boolean = new SingleValueParameterType("i1", ZeroExtend); // boolean
	@NotNull public static final SingleValueParameterType BooleanPointer = Boolean.pointTo(); // Boolean
	@NotNull public static final SingleValueParameterType SignedByte = new SingleValueParameterType("i8", SignExtend); // byte
	@NotNull public static final SingleValueParameterType SignedBytePointer = SignedByte.pointTo(); // Byte
	@NotNull public static final SingleValueParameterType UnsignedByte = new SingleValueParameterType("i8", ZeroExtend);
	@NotNull public static final SingleValueParameterType UnsignedBytePointer = UnsignedByte.pointTo();
	@NotNull public static final SingleValueParameterType SignedShort = new SingleValueParameterType("i16", SignExtend); // short
	@NotNull public static final SingleValueParameterType SignedShortPointer = SignedShort.pointTo(); // Short
	@NotNull public static final SingleValueParameterType UnsignedShort = new SingleValueParameterType("i16", ZeroExtend); // char
	@NotNull public static final SingleValueParameterType UnsignedShortPointer = UnsignedShort.pointTo(); // Character
	@NotNull public static final SingleValueParameterType SignedInteger = new SingleValueParameterType("i32", SignExtend); // int
	@NotNull public static final SingleValueParameterType SignedIntegerPointer = SignedInteger.pointTo(); // Integer
	@NotNull public static final SingleValueParameterType UnsignedInteger = new SingleValueParameterType("i32", ZeroExtend); // Integer
	@NotNull public static final SingleValueParameterType UnsignedIntegerPointer = UnsignedInteger.pointTo();
	@NotNull public static final SingleValueParameterType SignedLong = new SingleValueParameterType("i32", SignExtend); // long
	@NotNull public static final SingleValueParameterType SignedLongPointer = SignedLong.pointTo(); // Long
	@NotNull public static final SingleValueParameterType UnsignedLong = new SingleValueParameterType("i32", ZeroExtend);
	@NotNull public static final SingleValueParameterType UnsignedLongPointer = UnsignedLong.pointTo();
	@NotNull public static final SingleValueParameterType Half = new SingleValueParameterType("half");
	@NotNull public static final SingleValueParameterType HalfPointer = Half.pointTo();
	@NotNull public static final SingleValueParameterType Float = new SingleValueParameterType("float"); // float
	@NotNull public static final SingleValueParameterType FloatPointer = Float.pointTo(); // Float
	@NotNull public static final SingleValueParameterType Double = new SingleValueParameterType("double"); // double
	@NotNull public static final SingleValueParameterType DoublePointer = Double.pointTo(); // Double
	@NotNull public static final SingleValueParameterType Quad = new SingleValueParameterType("fp128");
	@NotNull public static final SingleValueParameterType QuadPointer = Quad.pointTo();

	@NotNull public static final SingleValueParameterType LongDoubleIsh = new SingleValueParameterType("x86_fp80"); // annoyingly common, when quad is a better option
	@NotNull public static final SingleValueParameterType IbmDoubleDouble = new SingleValueParameterType("ppc_fp128"); // defunct in PowerPC64 ELFv2 ABI

	@NotNull public static final ParameterType MMX = new SingleValueParameterType("x86_mmx"); // One on its own. Not particularly useful.

	@NotNull public static final ParameterType _void = new SingleValueParameterType("void");

	@NotNull private final String typeName;
	@NotNull private final AttributeGroup<ParameterAttribute> parameterAttributesGroup;
	private final int pointerIndirections;

	public SingleValueParameterType(@NonNls @NotNull final String typeName)
	{
		this(typeName, EmptyParameterAttibutes);
	}

	public SingleValueParameterType(@NonNls @NotNull final String typeName, @NotNull final AttributeGroup<ParameterAttribute> parameterAttributesGroup)
	{
		this(typeName, parameterAttributesGroup, 0);
	}

	public SingleValueParameterType(@NonNls @NotNull final String typeName, @NotNull final AttributeGroup<ParameterAttribute> parameterAttributesGroup, final int pointerIndirections)
	{
		if (typeName.equals("void") && pointerIndirections > 0)
		{
			throw new IllegalArgumentException("Pointers to void are not allowed");
		}
		this.typeName = typeName;
		this.parameterAttributesGroup = parameterAttributesGroup;
		this.pointerIndirections = pointerIndirections;
	}

	@NotNull
	public SingleValueParameterType pointTo()
	{
		return new SingleValueParameterType(typeName, parameterAttributesGroup, pointerIndirections + 1);
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeUtf8EncodedStringWithCertainty(typeName);

		for(int index = 0; index < pointerIndirections; index++)
		{
			byteWriter.writeByte('*');
		}

		parameterAttributesGroup.write(attributes1 ->
		{
			for (final Attribute attribute : attributes1)
			{
				byteWriter.writeByte(' ');
				attribute.write(new ByteWriterFunctionAttributeGroupWriter<>(byteWriter));
			}
		});
	}
}
