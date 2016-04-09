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
import com.stormmq.llvm.domain.types.Type;
import org.jetbrains.annotations.NotNull;

import java.sql.Struct;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class IdentifiedLocallyIdentifiedStructureType extends AbstractLocallyIdentifiedStructureType
{
	@NotNull private static final byte[] typeSpace = encodeUtf8BytesWithCertaintyValueIsValid("type ");

	private final boolean isPacked;
	@NotNull private final Type[] types;

	public IdentifiedLocallyIdentifiedStructureType(@NotNull final LocalIdentifier localIdentifier, final boolean isPacked, @NotNull final Type... types)
	{
		super(localIdentifier);
		this.isPacked = isPacked;
		this.types = types;
	}

	public boolean hasNumberOfElements(final int numberOfElements)
	{
		return types.length == numberOfElements;
	}

	@NotNull
	@Override
	protected byte[] typePrefix()
	{
		return typeSpace;
	}

	@Override
	protected <X extends Exception> void writeBody(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		StructureType.writeBracedContent(byteWriter, isPacked, types);
	}
}