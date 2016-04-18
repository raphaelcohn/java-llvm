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

package com.stormmq.llvm.domain.target;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.string.*;
import org.jetbrains.annotations.*;

import static com.stormmq.llvm.domain.target.WritableDataLayoutSpecificationAndTargetTriple.writeField;
import static com.stormmq.string.Formatting.format;
import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;

public final class Alignment extends AbstractToString
{
	@NotNull public static final Alignment EightBitAlignment = new Alignment(8);
	@NotNull public static final Alignment SixteenBitAlignment = new Alignment(16);
	@NotNull public static final Alignment ThirtyTwoBitAlignment = new Alignment(32);
	@NotNull public static final Alignment SixtyFourBitAlignment = new Alignment(64);
	@NotNull public static final Alignment OneHundredAndTwentyBitAlignment = new Alignment(128);
	@NotNull public static final Alignment ThirtyTwoSixtyFourBitAlignment = new Alignment(32, 64);
	@NotNull public static final Alignment ZeroSixtyFourBitAlignment = new Alignment(0, 64);

	private final int abiAlignmentInBits;
	private final int preferredAbiAlignmentInBits;
	@NonNls @NotNull private final byte[] dataLayoutEncoding;

	private Alignment(final int abi)
	{
		this(abi, abi);
	}

	public Alignment(final int abi, final int pref)
	{
		if (abi < 0)
		{
			throw new IllegalArgumentException("abi can not be negative");
		}
		if (pref < 1)
		{
			throw new IllegalArgumentException("pref must be greater than zero");
		}
		if (pref < abi)
		{
			throw new IllegalArgumentException("abi can not be less than abi");
		}
		abiAlignmentInBits = abi;
		preferredAbiAlignmentInBits = pref;
		dataLayoutEncoding = encodeToUtf8ByteArrayWithCertaintyValueIsValid(format(":%1$s:%2$s", abi, pref));
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(abiAlignmentInBits, preferredAbiAlignmentInBits);
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

		final Alignment alignment = (Alignment) o;

		return abiAlignmentInBits == alignment.abiAlignmentInBits && preferredAbiAlignmentInBits == alignment.preferredAbiAlignmentInBits;
	}

	@Override
	public int hashCode()
	{
		int result = abiAlignmentInBits;
		result = 31 * result + preferredAbiAlignmentInBits;
		return result;
	}

	public int abiAlignmentInBits(final int minimumAlignmentInBits)
	{
		if (abiAlignmentInBits == 0)
		{
			if (preferredAbiAlignmentInBits != 0)
			{
				return preferredAbiAlignmentInBits;
			}
			return minimumAlignmentInBits;
		}
		return abiAlignmentInBits;
	}

	public <X extends Exception> void writeAlignmentField(@NotNull final ByteWriter<X> byteWriter, @NonNls @NotNull final String abbreviatedName) throws X
	{
		writeField(byteWriter, abbreviatedName, dataLayoutEncoding);
	}
}
