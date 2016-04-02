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

package com.stormmq.llvm.domain.typedValues.constantTypedValues.complexConstantExpressions;

import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.types.TypeWithSize;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.ArrayType;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.string.Formatting.format;

public final class ArrayConstantTypedValue<T extends TypeWithSize> extends AbstractComplexConstantTypedValue<ArrayType<T>, T>
{
	@SafeVarargs
	public ArrayConstantTypedValue(@NotNull final ArrayType<T> arrayType, @NotNull final ConstantTypedValue<T>... values)
	{
		super(arrayType, values);

		if (arrayType.isMultidimensional())
		{
			throw new IllegalArgumentException("Multidimensional array constants are not supported");
		}

		final int lengthOfZerothDimension = arrayType.lengthOfZerothDimension();
		final int numberOfElements = numberOfElements();
		if (lengthOfZerothDimension != numberOfElements)
		{
			throw new IllegalArgumentException(format("Array of type '%1$s' has a zeroth dimension length of '%2$s' but '%3$s' value%4$s supplied", arrayType, lengthOfZerothDimension, numberOfElements, numberOfElements == 1 ? " was" : "s were"));
		}
	}

	@Override
	protected byte[] open()
	{
		return OpenSquareBracketSpace;
	}

	@Override
	protected byte[] close()
	{
		return SpaceCloseSquareBracket;
	}
}
