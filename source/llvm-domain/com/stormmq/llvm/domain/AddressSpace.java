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

package com.stormmq.llvm.domain;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.string.Formatting;
import org.jetbrains.annotations.*;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class AddressSpace implements Writable
{
	@SuppressWarnings("SpellCheckingInspection") @NotNull private static final byte[] SpaceAddressSpaceStart = encodeUtf8BytesWithCertaintyValueIsValid(" addrspace(");
	@NotNull public static final AddressSpace GlobalAddressSpace = new AddressSpace(0);
	private static final int MaximumAddressSpace = 8388608; // 2^23

	public final int value;

	private AddressSpace(final int addressSpace)
	{
		if (addressSpace < 0)
		{
			throw new IllegalArgumentException(Formatting.format("addressSpace can not be negative (it is '%1$s')", addressSpace));
		}
		if (addressSpace > MaximumAddressSpace)
		{
			throw new IllegalArgumentException(Formatting.format("addressSpace can not exceed the maximum of '%1$s' (it is '%2$s')", MaximumAddressSpace, addressSpace));
		}
		value = addressSpace;
	}

	public boolean isZero()
	{
		return value == 0;
	}

	public boolean isNotZero()
	{
		return value != 0;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(SpaceAddressSpaceStart);
		byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(value));
		byteWriter.writeCloseBracket();
	}

	@NotNull
	@NonNls
	public String pointerTargetLayoutPrefix(final int sizeInBits)
	{
		if (isZero())
		{
			return "p:" + sizeInBits + ':';
		}
		return "p" + value + ':' + sizeInBits + ':';
	}

	@Override
	public boolean equals(@Nullable final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		final AddressSpace that = (AddressSpace) o;

		return value == that.value;

	}

	@Override
	public int hashCode()
	{
		return value;
	}

	@Override
	@NotNull
	@NonNls
	public String toString()
	{
		return Integer.toString(value);
	}
}
