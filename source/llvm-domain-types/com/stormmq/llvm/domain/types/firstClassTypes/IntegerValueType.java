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
import org.jetbrains.annotations.NotNull;

import static com.stormmq.string.Formatting.format;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class IntegerValueType implements PrimitiveSingleValueType
{
	@NotNull public static final IntegerValueType i1 = new IntegerValueType(1, 1);
	@NotNull public static final IntegerValueType i8 = new IntegerValueType(8, 1);
	@NotNull public static final IntegerValueType i16 = new IntegerValueType(16, 2);
	@NotNull public static final IntegerValueType i32 = new IntegerValueType(32, 4);
	@NotNull public static final IntegerValueType i64 = new IntegerValueType(64, 8);
	@NotNull public static final IntegerValueType i128 = new IntegerValueType(128, 16);

	@SuppressWarnings("MagicNumber")
	@NotNull
	public static IntegerValueType integerValueTypeFromSizeInBits(final int sizeInBits)
	{
		if (sizeInBits < 1)
		{
			throw new IllegalArgumentException(format("integer sizeInBits can not be '%1$s'", sizeInBits));
		}
		switch (sizeInBits)
		{
			case 1:
				return i1;

			case 8:
				return i8;

			case 16:
				return i16;

			case 32:
				return i32;

			case 64:
				return i64;

			case 128:
				return i128;

			default:
				final int divided = sizeInBits >> 3;
				final int remainder = sizeInBits % 8;
				final int toScale = remainder == 0 ? 0 : 1;
				return new IntegerValueType(sizeInBits, divided + toScale);
		}
	}

	private static final int MaximumNumberOfBits = 2 << 23 - 1;
	@NotNull private final String stringValue;
	@SuppressWarnings("FieldNotUsedInToString") @NotNull private final byte[] llvmAssemblyEncoding;
	@SuppressWarnings("FieldNotUsedInToString") public final int alignment;

	private IntegerValueType(final int numberOfBits, final int alignment)
	{
		if (numberOfBits < 1)
		{
			throw new IllegalArgumentException(format("Number of bits must be 1 or more, not '%1$s'", numberOfBits));
		}

		if (numberOfBits > MaximumNumberOfBits)
		{
			throw new IllegalArgumentException(format("Number of bits must not exceed 2^23 - 1, not '%1$s'", numberOfBits));
		}

		stringValue = 'i' + Integer.toString(numberOfBits);
		llvmAssemblyEncoding = encodeUtf8BytesWithCertaintyValueIsValid(stringValue);

		this.alignment = alignment;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(llvmAssemblyEncoding);
	}

	@Override
	@NotNull
	public String toString()
	{
		return stringValue;
	}
}
