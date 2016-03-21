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

package com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.types.TypeWithSize;
import org.jetbrains.annotations.*;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class ArrayType implements AggregateType
{
	@NotNull private static final byte[] SpaceXSpace = encodeUtf8BytesWithCertaintyValueIsValid(" x ");

	@NotNull private final TypeWithSize elementType;
	private final int[] dimensionLengths;

	public ArrayType(@NotNull final TypeWithSize elementType, final int... dimensionLengths)
	{
		final int length = dimensionLengths.length;
		if (length == 0)
		{
			throw new IllegalArgumentException("dimensionLengths can not be empty");
		}

		for (int index = 0; index < length; index++)
		{
			final int dimensionLength = dimensionLengths[index];
			if (dimensionLength < 0)
			{
				throw new IllegalArgumentException(format(ENGLISH, "Dimension '%1$s' (zero-based) can not be negative ('%2$s)", index, dimensionLength));
			}
		}

		this.elementType = elementType;
		this.dimensionLengths = dimensionLengths;
	}

	@SuppressWarnings("ForLoopReplaceableByForEach")
	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		final int length = dimensionLengths.length;

		for (int index = 0; index < length; index++)
		{
			byteWriter.writeByte('[');
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(dimensionLengths[index]));
			byteWriter.writeBytes(SpaceXSpace);
		}

		elementType.write(byteWriter);

		for (int index = 0; index < length; index++)
		{
			byteWriter.writeByte(']');
		}
	}
}
