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

package com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.identifiers.LocalIdentifier;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractLocallyIdentifiedStructureType implements LocallyIdentifiedStructureType
{
	@NotNull private final LocalIdentifier localIdentifier;

	protected AbstractLocallyIdentifiedStructureType(@NotNull final LocalIdentifier localIdentifier)
	{
		this.localIdentifier = localIdentifier;
	}

	@Override
	public final <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(typePrefix());
		writeBody(byteWriter);
	}

	@NotNull
	protected abstract byte[] typePrefix();

	protected abstract <X extends Exception> void writeBody(@NotNull final ByteWriter<X> byteWriter) throws X;

	@NotNull
	@Override
	public final LocalIdentifier localIdentifier()
	{
		return localIdentifier;
	}

	@Override
	@NotNull
	@NonNls
	public final String name()
	{
		return localIdentifier.name();
	}
}