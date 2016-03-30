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

package com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.types.Type;
import org.jetbrains.annotations.NotNull;

public final class KnownStructureType implements StructureType
{
	@NotNull private static final byte[] OpenAngleBracketOpenBracketSpace = {'<', '{', ' '};
	@NotNull private static final byte[] SpaceCloseBracketCloseAngleBracket = {' ', '}', '>'};

	private final boolean isPacked;
	@NotNull private final Type[] types;

	public KnownStructureType(final boolean isPacked, @NotNull final Type... types)
	{
		this.isPacked = isPacked;
		this.types = types;
	}

	// when defining, needs to have 'type '
	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		final byte[] open;
		final byte[] close;
		if (isPacked)
		{
			open = OpenAngleBracketOpenBracketSpace;
			close = SpaceCloseBracketCloseAngleBracket;
		}
		else
		{
			open = OpenBracketSpace;
			close = SpaceCloseBracket;
		}

		byteWriter.writeBytes(open);

		final int length = types.length;
		for (int index = 0; index < length; index++)
		{
			final Type type = types[index];
			if (index != 0)
			{
				byteWriter.writeBytes(CommaSpace);
			}
			type.write(byteWriter);
		}

		byteWriter.writeBytes(close);
	}
}
