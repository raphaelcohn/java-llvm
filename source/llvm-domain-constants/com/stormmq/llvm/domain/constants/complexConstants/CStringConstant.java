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

package com.stormmq.llvm.domain.constants.complexConstants;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.ArrayType;
import com.stormmq.string.InvalidUtf16StringException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.i8;
import static com.stormmq.string.StringUtilities.encodeUtf8Bytes;

public final class CStringConstant implements ComplexConstant<ArrayType<IntegerValueType>>
{
	@SuppressWarnings("HardcodedFileSeparator") private static final char Slash = '\\';
	@NotNull private final byte[] valueExcludingTerminalNul;
	@NotNull private static final byte[] Start = {'c', '"'};

	public CStringConstant(@NotNull @NonNls final String value)
	{
		try
		{
			valueExcludingTerminalNul = encodeUtf8Bytes(value);
		}
		catch (final InvalidUtf16StringException e)
		{
			throw new IllegalArgumentException("Invalid C Constant String", e);
		}
	}

	@Override
	@NotNull
	public ArrayType<IntegerValueType> type()
	{
		return new ArrayType<>(i8, valueExcludingTerminalNul.length + 1);
	}

	@Override
	public int alignment()
	{
		return 1;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		// c"hello world\0A\00"

		byteWriter.writeBytes(Start);

		for (final byte value : valueExcludingTerminalNul)
		{
			// 0 - 31 and > 0x80 (ie top bit set)
			if (value < 0x20)
			{
				byteWriter.writeByte(Slash);
				byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toHexString(value & 0xFF));
			}
			else
			{
				byteWriter.writeByte(value);
			}
		}

		// ASCII NUL terminator
		byteWriter.writeByte(0);

		byteWriter.writeByte('"');
	}
}
