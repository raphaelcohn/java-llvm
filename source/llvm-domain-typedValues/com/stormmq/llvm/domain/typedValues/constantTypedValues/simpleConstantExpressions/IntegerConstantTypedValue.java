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
import com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.string.StringConstants._false;
import static com.stormmq.string.StringConstants._true;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.*;
import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;

public final class IntegerConstantTypedValue extends AbstractTypedValue<IntegerValueType> implements ConstantTypedValue<IntegerValueType>
{
	@NotNull public static final IntegerConstantTypedValue Int64Zero = new IntegerConstantTypedValue(i64, 0L);
	@NotNull public static final IntegerConstantTypedValue Int64One = new IntegerConstantTypedValue(i64, 1L);

	@NotNull
	public static IntegerConstantTypedValue i1(final boolean value)
	{
		return new IntegerConstantTypedValue(i1, value ? 1 : 0);
	}

	@NotNull
	public static IntegerConstantTypedValue i8(final char value)
	{
		return new IntegerConstantTypedValue(i8, value);
	}

	@NotNull
	public static IntegerConstantTypedValue i16(final char value)
	{
		return new IntegerConstantTypedValue(i16, value);
	}

	@NotNull
	public static IntegerConstantTypedValue i32(final int value)
	{
		return new IntegerConstantTypedValue(i32, value);
	}

	@NotNull
	public static IntegerConstantTypedValue i64(final long value)
	{
		return new IntegerConstantTypedValue(i64, value);
	}

	@NotNull private static final byte[] llAssemblyEncodingTrue = encodeToUtf8ByteArrayWithCertaintyValueIsValid(_true);
	@NotNull private static final byte[] llAssemblyEncodingFalse = encodeToUtf8ByteArrayWithCertaintyValueIsValid(_false);

	private final long value;

	public IntegerConstantTypedValue(@NotNull final IntegerValueType integerValueType, final long value)
	{
		super(integerValueType);
		this.value = value;
	}

	@Override
	protected <X extends Exception> void writeValue(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		if (type.equals(i1))
		{
			final byte[] llAssemblyEncoding = value == 0 ? llAssemblyEncodingFalse : llAssemblyEncodingTrue;
			byteWriter.writeBytes(llAssemblyEncoding);
		}
		else
		{
			byteWriter.writeUtf8EncodedStringWithCertainty(Long.toString(value));
		}
	}
}
