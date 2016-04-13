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
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import org.jetbrains.annotations.NotNull;

import java.util.function.ToIntFunction;

import static com.stormmq.string.Formatting.format;
import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;

public final class IntegerValueType implements PrimitiveSingleValueType
{
	private static final int MaximumNumberOfBits = 2 << 23 - 1;

	@NotNull public static final IntegerValueType i1 = new IntegerValueType(1, DataLayoutSpecification::int1AbiAlignmentInBits);
	@NotNull public static final IntegerValueType i8 = new IntegerValueType(8, DataLayoutSpecification::int8AbiAlignmentInBits);
	@NotNull public static final IntegerValueType i16 = new IntegerValueType(16, DataLayoutSpecification::int16AbiAlignmentInBits);
	@NotNull public static final IntegerValueType i32 = new IntegerValueType(32, DataLayoutSpecification::int32AbiAlignmentInBits);
	@NotNull public static final IntegerValueType i64 = new IntegerValueType(64, DataLayoutSpecification::int64AbiAlignmentInBits);
	@NotNull public static final IntegerValueType i128 = new IntegerValueType(128, DataLayoutSpecification::int128AbiAlignmentInBits);

	@SuppressWarnings("FieldNotUsedInToString") @NotNull private final ToIntFunction<DataLayoutSpecification> abiAlignmentInBits;
	@SuppressWarnings("FieldNotUsedInToString") private final int storageSizeInBits;
	@NotNull private final String stringValue;
	@SuppressWarnings("FieldNotUsedInToString") @NotNull private final byte[] llvmAssemblyEncoding;

	private IntegerValueType(final int numberOfBits, @NotNull final ToIntFunction<DataLayoutSpecification> abiAlignmentInBits)
	{
		if (numberOfBits < 1)
		{
			throw new IllegalArgumentException(format("Number of bits must be 1 or more, not '%1$s'", numberOfBits));
		}

		if (numberOfBits > MaximumNumberOfBits)
		{
			throw new IllegalArgumentException(format("Number of bits must not exceed 2^23 - 1, not '%1$s'", numberOfBits));
		}

		this.abiAlignmentInBits = abiAlignmentInBits;

		storageSizeInBits = numberOfBitsRoundedUpToNearestPowerOfTwo(numberOfBits);

		stringValue = 'i' + Integer.toString(numberOfBits);
		llvmAssemblyEncoding = encodeToUtf8ByteArrayWithCertaintyValueIsValid(stringValue);
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		byteWriter.writeBytes(llvmAssemblyEncoding);
	}

	@Override
	@NotNull
	public String toString()
	{
		return stringValue;
	}

	private static int numberOfBitsRoundedUpToNearestPowerOfTwo(final int numberOfBits)
	{
		if (numberOfBits % 8 != 0)
		{
			//noinspection MultiplyOrDivideByPowerOfTwo
			return ((numberOfBits / 8) + 1) * 8;
		}
		return numberOfBits;
	}

	@Override
	public int storageSizeInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
	{
		return storageSizeInBits;
	}

	@Override
	public int abiAlignmentInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
	{
		return abiAlignmentInBits.applyAsInt(dataLayoutSpecification);
	}
}
