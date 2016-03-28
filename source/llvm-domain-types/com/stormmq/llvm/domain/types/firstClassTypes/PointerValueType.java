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

package com.stormmq.llvm.domain.types.firstClassTypes;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.Writable;
import com.stormmq.llvm.domain.types.CanBePointedToType;
import com.stormmq.string.Formatting;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType.*;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.*;
import static com.stormmq.llvm.domain.types.firstClassTypes.X86MmxFirstClassType.x86_mmx;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class PointerValueType implements PrimitiveSingleValueType
{
	@NotNull public static final PointerValueType i8Pointer = i8.pointerTo(0);
	@NotNull public static final PointerValueType i16Pointer = i16.pointerTo(0);
	@NotNull public static final PointerValueType i32Pointer = i32.pointerTo(0);
	@NotNull public static final PointerValueType i64Pointer = i64.pointerTo(0);
	@NotNull public static final PointerValueType i128Pointer = i128.pointerTo(0);
	@NotNull public static final PointerValueType halfPointer = half.pointerTo(0);
	@NotNull public static final PointerValueType floatPointer = _float.pointerTo(0);
	@NotNull public static final PointerValueType doublePointer = _double.pointerTo(0);
	@NotNull public static final PointerValueType fp128Pointer = fp128.pointerTo(0);
	@NotNull public static final PointerValueType x86_fp80Pointer = x86_fp80.pointerTo(0);
	@NotNull public static final PointerValueType ppc_fp128Pointer = ppc_fp128.pointerTo(0);
	@NotNull public static final PointerValueType x86_mmxPointer = x86_mmx.pointerTo(0);

	@NotNull private final CanBePointedToType canBePointedToType;
	private final int addressSpace;

	public PointerValueType(@NotNull final CanBePointedToType canBePointedToType, final int addressSpace)
	{
		if (addressSpace < 0)
		{
			throw new IllegalArgumentException(Formatting.format("addressSpace can not be negative, ie not '%1$s'", addressSpace));
		}
		this.canBePointedToType = canBePointedToType;
		this.addressSpace = addressSpace;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		canBePointedToType.write(byteWriter);

		Writable.writeSpace(byteWriter);

		if (addressSpace != 0)
		{
			byteWriter.writeBytes(encodeUtf8BytesWithCertaintyValueIsValid("addrspace("));
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(addressSpace));
			byteWriter.writeByte(')');
		}
		byteWriter.writeByte('*');
	}
}
