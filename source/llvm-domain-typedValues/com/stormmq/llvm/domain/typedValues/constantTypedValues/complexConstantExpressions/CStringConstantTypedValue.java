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

package com.stormmq.llvm.domain.typedValues.constantTypedValues.complexConstantExpressions;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.llvm.domain.typedValues.AbstractTypedValue;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.ArrayType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.i8;
import static java.lang.Integer.toHexString;

public final class CStringConstantTypedValue extends AbstractTypedValue<ArrayType<IntegerValueType>> implements ConstantTypedValue<ArrayType<IntegerValueType>>
{
	private static final int LessThan32OrTopBitSet = 0x20;
	private static final int ByteMask = 0xFF;
	@NotNull private static final byte[] Open = {'c', '"'};

	@NotNull private final byte[] valueIncludingTerminalAsciiNullIfAppropriate;

	private static final int CharMask = 0x00_FF;
	@SuppressWarnings("NumericCastThatLosesPrecision")
	@NotNull
	public static CStringConstantTypedValue cStringValueTypeConstant(@NotNull @NonNls final String value, final boolean addTerminalAsciiNull)
	{
		// Sadly, some of the strings in Java's charsets.jar are invalid UTF-16 strings. We have to permit them.

		final int length = value.length();
		final int i = (length << 1) + (addTerminalAsciiNull ? 1 : 0);
		if (i < 0)
		{
			throw new IllegalArgumentException("String is too big to convert");
		}
		final byte[] valueIncludingTerminalAsciiNullIfAppropriate = new byte[i];
		for(int index = 0; index < length; index++)
		{
			final int character = value.charAt(index);
			valueIncludingTerminalAsciiNullIfAppropriate[(index << 1)] = (byte)((character >> 8));
			//noinspection MagicNumber
			valueIncludingTerminalAsciiNullIfAppropriate[(index << 1) + 1] = (byte)((character & 0xFF));
		}

		if (addTerminalAsciiNull)
		{
			valueIncludingTerminalAsciiNullIfAppropriate[i - 1] = 0;
		}

		return new CStringConstantTypedValue(valueIncludingTerminalAsciiNullIfAppropriate);
	}

	private CStringConstantTypedValue(@NotNull final byte[] valueIncludingTerminalAsciiNullIfAppropriate)
	{
		super(new ArrayType<>(i8, valueIncludingTerminalAsciiNullIfAppropriate.length));
		this.valueIncludingTerminalAsciiNullIfAppropriate = valueIncludingTerminalAsciiNullIfAppropriate;
	}

	@Override
	protected <X extends Exception> void writeValue(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		// eg  c"hello world\0A\00"

		byteWriter.writeBytes(Open);

		for (final byte value : valueIncludingTerminalAsciiNullIfAppropriate)
		{
			if (value < LessThan32OrTopBitSet)
			{
				byteWriter.writeSlash();
				byteWriter.writeUtf8EncodedStringWithCertainty(toHexString(value & ByteMask));
			}
			else
			{
				byteWriter.writeByte(value);
			}
		}

		byteWriter.writeDoubleQuote();
	}
}
