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
import com.stormmq.string.StringUtilities;
import com.stormmq.string.Utf8ByteUser;
import org.jetbrains.annotations.*;

import static com.stormmq.string.Formatting.format;
import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;

public final class Alignment
{
	@NotNull public static final Alignment EightBitAlignment = new Alignment(8);
	@NotNull public static final Alignment SixteenBitAlignment = new Alignment(16);
	@NotNull public static final Alignment ThirtyTwoBitAlignment = new Alignment(32);
	@NotNull public static final Alignment SixtyFourBitAlignment = new Alignment(64);
	@NotNull public static final Alignment OneHundredAndTwentyBitAlignment = new Alignment(128);

	public final int abiAlignmentInBits;
	@NonNls @NotNull private final byte[] dataLayoutEncoding;

	private Alignment(final int abi)
	{
		this(abi, abi);
	}

	public Alignment(final int abi, final int pref)
	{
		abiAlignmentInBits = abi;
		dataLayoutEncoding = encodeToUtf8ByteArrayWithCertaintyValueIsValid(format(":%1$s:%2$s", abi, pref));
	}

	public int abiAlignmentInBits(final int minimumAlignmentInBits)
	{
		if (abiAlignmentInBits == 0)
		{
			return minimumAlignmentInBits;
		}
		return abiAlignmentInBits;
	}

	public <X extends Exception> void writeAlignmentField(@NotNull final ByteWriter<X> byteWriter, @NonNls @NotNull final String abbreviatedName) throws X
	{
		writeField(byteWriter, abbreviatedName, dataLayoutEncoding);
	}

	@SuppressWarnings("MethodCanBeVariableArityMethod")
	public static <X extends Exception> void writeField(@NotNull final ByteWriter<X> byteWriter, @NotNull @NonNls final String dataLayoutEncodingA, @NotNull @NonNls final byte[] dataLayoutEncodingB) throws X
	{
		byteWriter.writeHyphen();
		byteWriter.writeUtf8EncodedStringWithCertainty(dataLayoutEncodingA);
		byteWriter.writeBytes(dataLayoutEncodingB);
	}
}
