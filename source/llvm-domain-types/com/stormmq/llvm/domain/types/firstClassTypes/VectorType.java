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
import com.stormmq.string.Api;
import com.stormmq.string.Formatting;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

@Api
public final class VectorType<T extends PrimitiveSingleValueType> implements SingleValueType
{
	@NotNull private static final byte[] SpaceXSpace = encodeUtf8BytesWithCertaintyValueIsValid(" x ");

	@NotNull private final T primitiveSingleValueType;
	public final int numberOfElements;

	public VectorType(@NotNull final T primitiveSingleValueType, final int numberOfElements)
	{
		if (numberOfElements < 1)
		{
			throw new IllegalArgumentException(Formatting.format("Number of element can not be '%1$s' - it must be greater than zero", numberOfElements));
		}
		this.primitiveSingleValueType = primitiveSingleValueType;
		this.numberOfElements = numberOfElements;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeByte('<');

		byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(numberOfElements));
		byteWriter.writeBytes(SpaceXSpace);
		primitiveSingleValueType.write(byteWriter);

		byteWriter.writeByte('>');
	}
}
