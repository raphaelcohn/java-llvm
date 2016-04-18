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
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.llvm.domain.types.SizedType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;

public final class SizedLocallyIdentifiedStructureType extends AbstractSizedStructureType implements LocallyIdentifiedStructureType
{
	@NotNull private static final byte[] typeSpace = encodeToUtf8ByteArrayWithCertaintyValueIsValid("type ");

	@NotNull private final LocalIdentifier localIdentifier;

	public SizedLocallyIdentifiedStructureType(@NotNull final LocalIdentifier localIdentifier, final boolean isPacked, @NotNull final SizedType... sizedTypes)
	{
		super(isPacked, sizedTypes);
		this.localIdentifier = localIdentifier;
	}

	@Override
	public boolean equals(@Nullable final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		if (!super.equals(o))
		{
			return false;
		}

		final SizedLocallyIdentifiedStructureType that = (SizedLocallyIdentifiedStructureType) o;

		return localIdentifier.equals(that.localIdentifier);
	}

	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + localIdentifier.hashCode();
		return result;
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(localIdentifier, isPacked, fields);
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		byteWriter.writeBytes(typeSpace);
		writeBody(byteWriter, dataLayoutSpecification);
	}

	@NotNull
	@Override
	public LocalIdentifier identifier()
	{
		return localIdentifier;
	}
}
