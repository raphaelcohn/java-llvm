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
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.llvm.domain.typedValues.AbstractTypedValue;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.FloatingPointBitsWriter.Double;
import static com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.FloatingPointBitsWriter.PowerPcDoubleDouble;
import static com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.FloatingPointBitsWriter.X86LongDouble;
import static com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType.*;

public final class FloatingPointConstantTypedValue extends AbstractTypedValue<FloatingPointValueType> implements ConstantTypedValue<FloatingPointValueType>
{
	@NotNull
	public static FloatingPointConstantTypedValue half(final short value)
	{
		return new FloatingPointConstantTypedValue(half, 0L, value, FloatingPointBitsWriter.Half);
	}

	@NotNull
	public static FloatingPointConstantTypedValue _float(final float value)
	{
		return new FloatingPointConstantTypedValue(_float, 0L, Float.floatToIntBits(value), FloatingPointBitsWriter.Float);
	}

	@NotNull
	public static FloatingPointConstantTypedValue _double(final long doubleValueAsRawLongBitsAsJavaLosesNaNInformationOnConversion)
	{
		return new FloatingPointConstantTypedValue(_double, 0L, doubleValueAsRawLongBitsAsJavaLosesNaNInformationOnConversion, Double);
	}

	@NotNull
	public static FloatingPointConstantTypedValue quad(final long left, final long right)
	{
		return new FloatingPointConstantTypedValue(fp128, left, right, FloatingPointBitsWriter.Quad);
	}

	@NotNull
	public static FloatingPointConstantTypedValue powerPcDoubleDouble(final long left, final long right)
	{
		return new FloatingPointConstantTypedValue(ppc_fp128, left, right, PowerPcDoubleDouble);
	}

	@NotNull
	public static FloatingPointConstantTypedValue x86LongDouble(final char left, final long right)
	{
		return new FloatingPointConstantTypedValue(x86_fp80, left, right, X86LongDouble);
	}

	private final long left;
	private final long right;
	@NotNull private final FloatingPointBitsWriter floatingPointBitsWriter;

	private FloatingPointConstantTypedValue(@NotNull final FloatingPointValueType floatingPointValueType, final long left, final long right, @NotNull final FloatingPointBitsWriter floatingPointBitsWriter)
	{
		super(floatingPointValueType);
		this.left = left;
		this.right = right;
		this.floatingPointBitsWriter = floatingPointBitsWriter;
	}

	@Override
	protected <X extends Exception> void writeValue(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		floatingPointBitsWriter.write(byteWriter, left, right);
	}
}
