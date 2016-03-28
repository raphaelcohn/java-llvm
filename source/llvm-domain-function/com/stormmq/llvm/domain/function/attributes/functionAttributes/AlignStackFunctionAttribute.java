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

package com.stormmq.llvm.domain.function.attributes.functionAttributes;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.attributes.AttributeKind;
import com.stormmq.string.Formatting;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.attributes.AttributeKind.Defined;

public final class AlignStackFunctionAttribute implements FunctionAttribute
{
	private final int stackAlignmentAsPowerOfTwo;

	public AlignStackFunctionAttribute(final int stackAlignmentAsPowerOfTwo)
	{
		this.stackAlignmentAsPowerOfTwo = stackAlignmentAsPowerOfTwo;
	}

	@NonNls
	@NotNull
	@Override
	public String name()
	{
		return "alignstack";
	}

	@NotNull
	@Override
	public AttributeKind attributeKind()
	{
		return Defined;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeUtf8EncodedStringWithCertainty(Formatting.format("alignstack(%1$s)", stackAlignmentAsPowerOfTwo));
	}
}
