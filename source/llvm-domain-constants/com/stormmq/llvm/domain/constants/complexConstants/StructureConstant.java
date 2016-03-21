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

package com.stormmq.llvm.domain.variables.constants.complexConstants;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.StructureType;
import com.stormmq.llvm.domain.variables.constants.Constant;
import org.jetbrains.annotations.NotNull;

public final class StructureConstant implements ComplexConstant<StructureType>
{
	@NotNull private static final byte[] Start = {'{', ' '};
	@NotNull private static final byte[] CommaSpace = {',', ' '};
	@NotNull private static final byte[] End = {' ', '}'};

	@NotNull private final StructureType structureType;
	@NotNull private final Constant<?>[] values;

	public StructureConstant(@NotNull final StructureType structureType, @NotNull final Constant<?>... values)
	{
		this.structureType = structureType;
		this.values = values;
	}

	@NotNull
	@Override
	public StructureType type()
	{
		return structureType;
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
		for (int index = 0; index < length; index++)
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
