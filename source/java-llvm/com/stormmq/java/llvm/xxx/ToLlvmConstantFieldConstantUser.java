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

package com.stormmq.java.llvm.xxx;

import com.stormmq.java.classfile.domain.RawDouble;
import com.stormmq.java.classfile.domain.fieldConstants.FieldConstant;
import com.stormmq.java.classfile.domain.fieldConstants.FieldConstantUser;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.FloatingPointConstantTypedValue;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.IntegerConstantTypedValue;
import com.stormmq.llvm.domain.types.Type;
import com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.llvm.domain.typedValues.constantTypedValues.complexConstantExpressions.CStringConstantTypedValue.cStringValueTypeConstant;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.i32;

public final class ToLlvmConstantFieldConstantUser implements FieldConstantUser<ConstantTypedValue<?>>
{
	@Nullable
	public static ConstantTypedValue<Type> toLlvmConstant(@NotNull final Type llvmType, @Nullable final FieldConstant constantValue)
	{
		return new ToLlvmConstantFieldConstantUser(llvmType instanceof IntegerValueType ? (IntegerValueType) llvmType : i32).initializerConstant(constantValue);
	}

	@NotNull private final IntegerValueType integerValueType;

	private ToLlvmConstantFieldConstantUser(@NotNull final IntegerValueType integerValueType)
	{
		this.integerValueType = integerValueType;
	}

	@SuppressWarnings("unchecked")
	@Nullable
	private ConstantTypedValue<Type> initializerConstant(@Nullable final FieldConstant constantValue)
	{
		return constantValue != null ? (ConstantTypedValue<Type>) constantValue.use(this) : null;
	}

	@NotNull
	@Override
	public ConstantTypedValue<?> useString(@NotNull final String value)
	{
		return cStringValueTypeConstant(value, true);
	}

	@NotNull
	@Override
	public ConstantTypedValue<?> useFloat(final float value)
	{
		return FloatingPointConstantTypedValue._float(value);
	}

	@NotNull
	@Override
	public ConstantTypedValue<?> useDouble(@NotNull final RawDouble value)
	{
		return FloatingPointConstantTypedValue._double(value.asLong());
	}

	@NotNull
	@Override
	public ConstantTypedValue<?> useInteger(final int value)
	{
		return new IntegerConstantTypedValue(integerValueType, value);
	}

	@NotNull
	@Override
	public ConstantTypedValue<?> useLong(final long value)
	{
		return IntegerConstantTypedValue.i64(value);
	}
}
