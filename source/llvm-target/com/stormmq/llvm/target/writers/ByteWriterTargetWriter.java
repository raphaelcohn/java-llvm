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

package com.stormmq.llvm.target.writers;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.target.triple.TargetTriple;
import com.stormmq.llvm.target.dataLayout.DataLayoutSpecification;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class ByteWriterTargetWriter<X extends Exception> implements TargetWriter<X>
{
	@NotNull private static final byte[] TargetDataLayoutStart = encodeUtf8BytesWithCertaintyValueIsValid("target datalayout = \"");
	@NotNull private static final byte[] TargetTripleStart = encodeUtf8BytesWithCertaintyValueIsValid("target triple = \"");
	@SuppressWarnings("HardcodedLineSeparator") @NotNull private static final byte[] End = encodeUtf8BytesWithCertaintyValueIsValid("\"\n");

	@NotNull private final ByteWriter<X> byteWriter;

	public ByteWriterTargetWriter(@NotNull final ByteWriter<X> byteWriter)
	{
		this.byteWriter = byteWriter;
	}

	@Override
	public void writeTarget(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final TargetTriple targetTriple) throws X
	{
		final DataLayoutSpecificationFieldWriter<X> dataLayoutSpecificationFieldWriter = new ByteWriterDataLayoutSpecificationFieldWriter<>(byteWriter);
		byteWriter.writeBytes(TargetDataLayoutStart);
		dataLayoutSpecification.write(dataLayoutSpecificationFieldWriter);
		byteWriter.writeBytes(End);

		byteWriter.writeBytes(TargetTripleStart);
		final TargetTripleWriter<X> targetTripleWriter = new ByteWriterTargetTripleWriter<>(byteWriter);
		targetTriple.write(targetTripleWriter);
		byteWriter.writeBytes(End);
	}
}
