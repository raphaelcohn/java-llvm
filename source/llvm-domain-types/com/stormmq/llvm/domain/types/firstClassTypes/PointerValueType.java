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
import com.stormmq.llvm.domain.AddressSpace;
import com.stormmq.llvm.domain.types.CanBePointedToType;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.AddressSpace.DefaultAddressSpace;
import static com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType.*;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.*;

public final class PointerValueType<T extends CanBePointedToType> implements PrimitiveSingleValueType
{
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<IntegerValueType> i8Pointer = i8.pointerTo(DefaultAddressSpace);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<IntegerValueType> i16Pointer = i16.pointerTo(DefaultAddressSpace);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<IntegerValueType> i32Pointer = i32.pointerTo(DefaultAddressSpace);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<IntegerValueType> i64Pointer = i64.pointerTo(DefaultAddressSpace);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<IntegerValueType> i128Pointer = i128.pointerTo(DefaultAddressSpace);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<FloatingPointValueType> halfPointer = half.pointerTo(DefaultAddressSpace);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<FloatingPointValueType> floatPointer = _float.pointerTo(DefaultAddressSpace);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<FloatingPointValueType> doublePointer = _double.pointerTo(DefaultAddressSpace);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<FloatingPointValueType> fp128Pointer = fp128.pointerTo(DefaultAddressSpace);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<FloatingPointValueType> x86_fp80Pointer = x86_fp80.pointerTo(DefaultAddressSpace);
	@SuppressWarnings("unused") @NotNull public static final PointerValueType<FloatingPointValueType> ppc_fp128Pointer = ppc_fp128.pointerTo(DefaultAddressSpace);

	@NotNull private final T pointsTo;
	private final AddressSpace addressSpace;

	public PointerValueType(@NotNull final T pointsTo, @NotNull final AddressSpace addressSpace)
	{
		this.pointsTo = pointsTo;
		this.addressSpace = addressSpace;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		pointsTo.write(byteWriter);

		if (addressSpace.isZero())
		{
			byteWriter.writeSpace();
		}
		else
		{
			addressSpace.write(byteWriter);
		}
		byteWriter.writeAsterisk();
	}

}
