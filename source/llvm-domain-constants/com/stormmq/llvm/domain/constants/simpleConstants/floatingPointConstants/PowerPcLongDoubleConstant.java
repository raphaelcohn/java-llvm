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
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType.ppc_fp128;

public final class PowerPcLongDoubleConstant implements FloatingPointConstant
{
	@NotNull private static final byte[] _0xM = {'0', 'x', 'M'};
	private static final int SixteenBytes = 16;

	private final long valueLeft;
	private final long valueRight;

	public PowerPcLongDoubleConstant(final long valueLeft, final long valueRight)
	{
		this.valueLeft = valueLeft;
		this.valueRight = valueRight;
	}

	@Override
	@NotNull
	public FloatingPointValueType type()
	{
		return ppc_fp128;
	}

	@Override
	public int alignment()
	{
		return SixteenBytes;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(_0xM);
		byteWriter.writeUtf8EncodedStringWithCertainty(Long.toHexString(valueLeft));
		byteWriter.writeUtf8EncodedStringWithCertainty(Long.toHexString(valueRight));
	}
}
