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
import com.stormmq.llvm.domain.types.CanBePointedToType;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType.*;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.*;
import static com.stormmq.string.Formatting.format;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class PointerValueType<T extends CanBePointedToType> implements PrimitiveSingleValueType
{
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<IntegerValueType> i8Pointer = i8.pointerTo(0);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<IntegerValueType> i16Pointer = i16.pointerTo(0);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<IntegerValueType> i32Pointer = i32.pointerTo(0);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<IntegerValueType> i64Pointer = i64.pointerTo(0);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<IntegerValueType> i128Pointer = i128.pointerTo(0);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<FloatingPointValueType> halfPointer = half.pointerTo(0);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<FloatingPointValueType> floatPointer = _float.pointerTo(0);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<FloatingPointValueType> doublePointer = _double.pointerTo(0);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<FloatingPointValueType> fp128Pointer = fp128.pointerTo(0);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<FloatingPointValueType> x86_fp80Pointer = x86_fp80.pointerTo(0);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<FloatingPointValueType> ppc_fp128Pointer = ppc_fp128.pointerTo(0);

	@SuppressWarnings("SpellCheckingInspection") @NotNull private static final byte[] addrspaceOpenBracket = encodeUtf8BytesWithCertaintyValueIsValid("addrspace(");

	@NotNull public final T pointsTo;
	private final int addressSpace;

	public PointerValueType(@NotNull final T pointsTo, final int addressSpace)
	{
		if (addressSpace < 0)
		{
			throw new IllegalArgumentException(format("addressSpace can not be negative, ie not '%1$s'", addressSpace));
		}
		this.pointsTo = pointsTo;
		this.addressSpace = addressSpace;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		pointsTo.write(byteWriter);

		byteWriter.writeSpace();

		if (addressSpace != 0)
		{
			byteWriter.writeBytes(addrspaceOpenBracket);
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(addressSpace));
			byteWriter.writeCloseBracket();
		}
		byteWriter.writeAsterisk();
	}
}
