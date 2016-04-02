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

package com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.typedValues.AbstractTypedValue;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType;
import org.jetbrains.annotations.NotNull;

public final class FloatingPointConstantTypedValue extends AbstractTypedValue<FloatingPointValueType> implements ConstantTypedValue<FloatingPointValueType>
{
	@NotNull private static final byte[] _0xH = {'0', 'x', 'H'}; // Half
	@NotNull private static final byte[] _0x = {'0', 'x'}; // Float and Double
	@NotNull private static final byte[] _0xL = {'0', 'x', 'L'}; // Quad
	@NotNull private static final byte[] _0xM = {'0', 'x', 'M'}; // PPC
	@NotNull private static final byte[] _0xK = {'0', 'x', 'K'}; // X86 long double

	@NotNull
	public static FloatingPointConstantTypedValue half(final int value)
	{
		return new FloatingPointConstantTypedValue(FloatingPointValueType.half, 0L, value, _0xH);
	}

	@NotNull
	public static FloatingPointConstantTypedValue _float(final float value)
	{
		return new FloatingPointConstantTypedValue(FloatingPointValueType._float, 0L, Float.floatToIntBits(value), _0x);
	}

	@NotNull
	public static FloatingPointConstantTypedValue _double(final long doubleValueAsRawLongBitsAsJavaLosesNaNInformationOnConversion)
	{
		return new FloatingPointConstantTypedValue(FloatingPointValueType._double, 0L, doubleValueAsRawLongBitsAsJavaLosesNaNInformationOnConversion, _0x);
	}

	@NotNull
	public static FloatingPointConstantTypedValue quad(final long left, final long right)
	{
		return new FloatingPointConstantTypedValue(FloatingPointValueType.fp128, left, right, _0xL);
	}

	@NotNull
	public static FloatingPointConstantTypedValue powerPcDoubleDouble(final long left, final long right)
	{
		return new FloatingPointConstantTypedValue(FloatingPointValueType.ppc_fp128, left, right, _0xM);
	}

	@NotNull
	public static FloatingPointConstantTypedValue x86LongDouble(final char left, final long right)
	{
		return new FloatingPointConstantTypedValue(FloatingPointValueType.x86_fp80, left, right, _0xK);
	}

	private final long left;
	private final long right;
	@NotNull private final byte[] prefix;

	private FloatingPointConstantTypedValue(@NotNull final FloatingPointValueType floatingPointValueType, final long left, final long right, @NotNull final byte[] prefix)
	{
		super(floatingPointValueType);
		this.left = left;
		this.right = right;
		this.prefix = prefix;
	}

	@Override
	protected <X extends Exception> void writeValue(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(prefix);
		byteWriter.writeUtf8EncodedStringWithCertainty(Long.toHexString(left));
		byteWriter.writeUtf8EncodedStringWithCertainty(Long.toHexString(right));
	}
}
