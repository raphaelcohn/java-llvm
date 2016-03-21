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

package com.stormmq.llvm.domain.types;

import com.stormmq.byteWriters.ByteWriter;
import org.jetbrains.annotations.*;

public final class FunctionType implements TypeExcludingVoid, CanBePointedToType
{
	@NotNull private static final byte[] SpaceOpenBracket = {' ', '('};
	@NotNull private static final byte[] CommaSpace = {',', ' '};

	@NotNull private final VoidOrFirstClassTypeExcludingLabelAndMetadata returnType;
	@NotNull private final TypeExcludingVoid[] formalParameterTypes;

	public FunctionType(@NotNull final VoidOrFirstClassTypeExcludingLabelAndMetadata returnType, @NotNull final TypeExcludingVoid... formalParameterTypes)
	{
		this.returnType = returnType;
		this.formalParameterTypes = formalParameterTypes;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		returnType.write(byteWriter);
		byteWriter.writeBytes(SpaceOpenBracket);

		final int length = formalParameterTypes.length;
		for(int index = 0; index < length; index++)
		{
			if (index != 0)
			{
				byteWriter.writeBytes(CommaSpace);
			}

			formalParameterTypes[index].write(byteWriter);
		}

		byteWriter.writeByte(')');
	}
}
