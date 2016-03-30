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

package com.stormmq.llvm.domain.constants.complexConstants;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.types.TypeWithSize;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.ArrayType;
import com.stormmq.llvm.domain.constants.Constant;
import org.jetbrains.annotations.NotNull;

public final class ArrayConstant<T extends TypeWithSize> implements ComplexConstant<ArrayType<T>>
{
	@NotNull private static final byte[] Start = {'[', ' '};
	@NotNull private static final byte[] End = {' ', ']'};

	@NotNull private final ArrayType<T> arrayType;
	@NotNull private final Constant<T>[] values;

	@SafeVarargs
	public ArrayConstant(@NotNull final ArrayType<T> arrayType, @NotNull final Constant<T>... values)
	{
		this.arrayType = arrayType;
		this.values = values;
	}

	@Override
	@NotNull
	public ArrayType<T> type()
	{
		return arrayType;
	}

	@Override
	public int alignment()
	{
		return AutomaticAlignment;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(Start);

		final int length = values.length;
		for(int index = 0; index < length; index++)
		{
			if (index != 0)
			{
				byteWriter.writeBytes(CommaSpace);
			}
			values[index].write(byteWriter);
		}

		byteWriter.writeBytes(End);
	}
}
