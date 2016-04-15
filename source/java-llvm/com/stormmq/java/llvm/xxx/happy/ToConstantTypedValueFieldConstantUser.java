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

package com.stormmq.java.llvm.xxx.happy;

import com.stormmq.java.classfile.domain.RawDouble;
import com.stormmq.java.classfile.domain.fieldConstants.FieldConstant;
import com.stormmq.java.classfile.domain.fieldConstants.FieldConstantUser;
import com.stormmq.java.classfile.domain.information.FieldInformation;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.IntegerConstantTypedValue;
import com.stormmq.llvm.domain.types.CanBePointedToType;
import com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType;
import com.stormmq.string.AbstractToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.llvm.domain.typedValues.constantTypedValues.complexConstantExpressions.CStringConstantTypedValue.cStringValueTypeConstant;
import static com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.FloatingPointConstantTypedValue._double;
import static com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.FloatingPointConstantTypedValue._float;
import static com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.IntegerConstantTypedValue.i64;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.i32;

public final class ToConstantTypedValueFieldConstantUser<T extends CanBePointedToType> extends AbstractToString implements FieldConstantUser<ConstantTypedValue<T>>
{
	@Nullable
	public static <T extends CanBePointedToType> ConstantTypedValue<T> toLlvmConstant(@NotNull final FieldInformation fieldInformation, @NotNull final T llvmType)
	{
		@Nullable final FieldConstant constantValue = fieldInformation.constantValue;
		return new ToConstantTypedValueFieldConstantUser<T>(llvmType instanceof IntegerValueType ? (IntegerValueType) llvmType : i32).initializerConstant(constantValue);
	}

	@NotNull private final IntegerValueType integerValueType;

	private ToConstantTypedValueFieldConstantUser(@NotNull final IntegerValueType integerValueType)
	{
		this.integerValueType = integerValueType;
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(integerValueType);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	private ConstantTypedValue<T> initializerConstant(@Nullable final FieldConstant constantValue)
	{
		return constantValue != null ? constantValue.use(this) : null;
	}

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	public ConstantTypedValue<T> useString(@NotNull final String value)
	{
		return (ConstantTypedValue<T>) cStringValueTypeConstant(value, false);
	}

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	public ConstantTypedValue<T> useFloat(final float value)
	{
		return (ConstantTypedValue<T>) _float(value);
	}

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	public ConstantTypedValue<T> useDouble(@NotNull final RawDouble value)
	{
		return (ConstantTypedValue<T>) _double(value.asLong());
	}

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	public ConstantTypedValue<T> useInteger(final int value)
	{
		return (ConstantTypedValue<T>) new IntegerConstantTypedValue(integerValueType, value);
	}

	@SuppressWarnings("unchecked")
	@NotNull
	@Override
	public ConstantTypedValue<T> useLong(final long value)
	{
		return (ConstantTypedValue<T>) i64(value);
	}
}
