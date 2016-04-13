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
import com.stormmq.llvm.domain.types.SizedType;
import com.stormmq.string.Api;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.string.Formatting.format;
import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;

@Api
public final class VectorType<T extends PrimitiveSingleValueType> implements SingleValueType, SizedType
{
	@NotNull private static final byte[] SpaceXSpace = encodeToUtf8ByteArrayWithCertaintyValueIsValid(" x ");

	@NotNull private final T elementType;
	private final int numberOfElements;

	public VectorType(@NotNull final T elementType, final int numberOfElements)
	{
		if (numberOfElements < 1)
		{
			throw new IllegalArgumentException(format("Number of element can not be '%1$s' - it must be greater than zero", numberOfElements));
		}
		this.elementType = elementType;
		this.numberOfElements = numberOfElements;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		byteWriter.writeOpenAngleBracket();

		byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(numberOfElements));
		byteWriter.writeBytes(SpaceXSpace);
		elementType.write(byteWriter, dataLayoutSpecification);

		byteWriter.writeCloseAngleBracket();
	}

	public boolean hasNumberOfElements(final int numberOfElements)
	{
		return this.numberOfElements == numberOfElements;
	}

	@Override
	public int storageSizeInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
	{
		return elementType.storageSizeInBits(dataLayoutSpecification) * numberOfElements;
	}

	@Override
	public int abiAlignmentInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
	{
		return elementType.abiAlignmentInBits(dataLayoutSpecification);
	}
}
