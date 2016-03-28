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

package com.stormmq.llvm.domain.constants.simpleConstants.floatingPointConstants;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType;
import com.stormmq.string.Api;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType.x86_fp80;

@Api
public final class X86LongDoubleConstant implements FloatingPointConstant
{
	@NotNull private static final byte[] _0xK = {'0', 'x', 'K'};
	@NotNull private final String twentyHexadecimalCharacters;
	private static final int SixteenBytes = 16;

	public X86LongDoubleConstant(@NotNull final String twentyHexadecimalCharacters)
	{
		this.twentyHexadecimalCharacters = twentyHexadecimalCharacters;
	}

	@Override
	@NotNull
	public FloatingPointValueType type()
	{
		return x86_fp80;
	}

	@Override
	public int alignment()
	{
		return SixteenBytes;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(_0xK);
		byteWriter.writeUtf8EncodedStringWithCertainty(twentyHexadecimalCharacters);
	}
}
