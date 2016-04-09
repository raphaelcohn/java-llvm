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

package com.stormmq.llvm.domain.instructions;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.typedValues.TypedValue;
import com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.Writable.SpaceToSpace;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class TruncateIntegerInstruction extends AbstractInstruction<IntegerValueType>
{
	// <result> = trunc <ty> <value> to <ty2>             ; yields ty2

	@NotNull private static final byte[] truncSpace = encodeUtf8BytesWithCertaintyValueIsValid("trunc ");

	@NotNull private final TypedValue<IntegerValueType> integerValue;
	@NotNull private final IntegerValueType to;

	private TruncateIntegerInstruction(@NotNull final TypedValue<IntegerValueType> integerValue, @NotNull final IntegerValueType to)
	{
		this.integerValue = integerValue;
		this.to = to;
	}

	@NotNull
	@Override
	public IntegerValueType to()
	{
		return to;
	}

	@NotNull
	@Override
	protected byte[] nameWithSpaceAsLlAssemblyValue()
	{
		return truncSpace;
	}

	@Override
	protected <X extends Exception> void writeBody(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		integerValue.write(byteWriter);
		byteWriter.writeBytes(SpaceToSpace);
		to.write(byteWriter);
	}
}
