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
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.llvm.domain.types.SizedType;
import com.stormmq.string.AbstractToString;
import org.jetbrains.annotations.*;

import java.util.Arrays;

public abstract class AbstractSizedStructureType extends AbstractToString implements SizedStructureSizedType
{
	protected final boolean isPacked;
	@NotNull protected final SizedType[] fields;

	protected AbstractSizedStructureType(final boolean isPacked, @NotNull final SizedType... fields)
	{
		this.isPacked = isPacked;
		this.fields = fields;
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

		final AbstractSizedStructureType that = (AbstractSizedStructureType) o;

		if (isPacked != that.isPacked)
		{
			return false;
		}
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		return Arrays.equals(fields, that.fields);

	}

	@Override
	public int hashCode()
	{
		int result = (isPacked ? 1 : 0);
		result = 31 * result + Arrays.hashCode(fields);
		return result;
	}

	@Override
	public final <X extends Exception> void writeBody(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		structureDataLayout(dataLayoutSpecification).writeBody(byteWriter, dataLayoutSpecification, fields);
	}

	@Override
	public final boolean isPacked()
	{
		return isPacked;
	}

	@NotNull
	@Override
	public final int[] offsets(@NotNull final DataLayoutSpecification dataLayoutSpecification)
	{
		return structureDataLayout(dataLayoutSpecification).offsets(dataLayoutSpecification, fields);
	}

	@Override
	public final int storageSizeInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
	{
		return structureDataLayout(dataLayoutSpecification).storageSizeInBits(dataLayoutSpecification, fields);
	}

	@Override
	public final int abiAlignmentInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
	{
		return structureDataLayout(dataLayoutSpecification).abiAlignmentInBits(dataLayoutSpecification, fields);
	}

	@NotNull
	private StructureDataLayout structureDataLayout(@NotNull final DataLayoutSpecification dataLayoutSpecification)
	{
		return StructureDataLayout.structureDataLayout(isPacked, dataLayoutSpecification);
	}
}
