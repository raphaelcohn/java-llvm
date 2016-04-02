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
import com.stormmq.string.Formatting;
import org.jetbrains.annotations.*;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class ArrayType<T extends TypeWithSize> implements AggregateType
{
	@NotNull private static final byte[] SpaceXSpace = encodeUtf8BytesWithCertaintyValueIsValid(" x ");

	@NotNull private final T elementType;
	private final int[] lengthsOfDimensions;

	public ArrayType(@NotNull final T elementType, final int... lengthsOfDimensions)
	{
		final int length = lengthsOfDimensions.length;
		if (length == 0)
		{
			throw new IllegalArgumentException("dimensionLengths can not be empty");
		}

		for (int index = 0; index < length; index++)
		{
			final int dimensionLength = lengthsOfDimensions[index];
			if (dimensionLength < 0)
			{
				throw new IllegalArgumentException(Formatting.format("Dimension '%1$s' (zero-based) can not be negative ('%2$s)", index, dimensionLength));
			}
		}

		this.elementType = elementType;
		this.lengthsOfDimensions = lengthsOfDimensions;
	}

	public boolean isMultidimensional()
	{
		return lengthsOfDimensions.length > 1;
	}

	public int lengthOfZerothDimension()
	{
		return lengthOfDimension(0);
	}

	private int lengthOfDimension(final int zeroBasedDimensionIndex)
	{
		return lengthsOfDimensions[zeroBasedDimensionIndex];
	}

	@SuppressWarnings("ForLoopReplaceableByForEach")
	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		final int length = lengthsOfDimensions.length;

		for (int zeroBasedDimensionIndex = 0; zeroBasedDimensionIndex < length; zeroBasedDimensionIndex++)
		{
			byteWriter.writeOpenSquareBracket();
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(lengthOfDimension(zeroBasedDimensionIndex)));
			byteWriter.writeBytes(SpaceXSpace);
		}

		elementType.write(byteWriter);

		for (int index = 0; index < length; index++)
		{
			byteWriter.writeCloseSquareBracket();
		}
	}
}
