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

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.llvm.domain.typedValues.AbstractTypedValue;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.types.*;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractComplexConstantTypedValue<T extends CanBePointedToType, V extends CanBePointedToType> extends AbstractTypedValue<T> implements ConstantTypedValue<T>
{
	@NotNull private final ConstantTypedValue<V>[] values;

	protected AbstractComplexConstantTypedValue(@NotNull final T knownStructureType, @NotNull final ConstantTypedValue<V>[] values)
	{
		super(knownStructureType);
		this.values = values;
	}

	protected final int numberOfElements()
	{
		return values.length;
	}

	protected abstract byte[] open();

	protected abstract byte[] close();

	@Override
	protected final <X extends Exception> void writeValue(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		byteWriter.writeBytes(open());

		final int length = values.length;
		for(int index = 0; index < length; index++)
		{
			if (index != 0)
			{
				byteWriter.writeBytes(CommaSpace);
			}
			values[index].write(byteWriter, dataLayoutSpecification);
		}

		byteWriter.writeBytes(close());
	}
}
