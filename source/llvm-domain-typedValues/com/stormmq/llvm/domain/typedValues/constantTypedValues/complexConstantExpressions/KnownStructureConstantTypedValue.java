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
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.IdentifiedStructureType;
import com.stormmq.string.Formatting;
import org.jetbrains.annotations.NotNull;

public final class KnownStructureConstantTypedValue extends AbstractComplexConstantTypedValue<IdentifiedStructureType, TypeWithSize>
{
	@SafeVarargs
	public KnownStructureConstantTypedValue(@NotNull final IdentifiedStructureType identifiedStructureType, @NotNull final ConstantTypedValue<TypeWithSize>... values)
	{
		super(identifiedStructureType, values);
		final int numberOfElements = numberOfElements();
		if (!identifiedStructureType.hasNumberOfElements(numberOfElements))
		{
			throw new IllegalArgumentException(Formatting.format("The number of elements in the known structure type '%1$s' mismatches the number (%2$s) of values supplied", identifiedStructureType, numberOfElements));
		}
	}

	@Override
	protected byte[] open()
	{
		return OpenBracketSpace;
	}

	@Override
	protected byte[] close()
	{
		return SpaceCloseBracket;
	}
}
