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

package com.stormmq.llvm.domain.types.firstClassTypes;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.AddressSpace;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.llvm.domain.types.CanBePointedToType;
import com.stormmq.string.AbstractToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.llvm.domain.AddressSpace.GlobalAddressSpace;

public final class PointerValueType<T extends CanBePointedToType> extends AbstractToString implements PrimitiveSingleValueType
{
	@NotNull
	public static <T extends CanBePointedToType> PointerValueType<T> pointedToInGlobalAddressSpace(@NotNull final T pointsTo)
	{
		return new PointerValueType<>(pointsTo, GlobalAddressSpace);
	}

	@NotNull private final T pointsTo;
	@NotNull private final AddressSpace addressSpace;

	public PointerValueType(@NotNull final T pointsTo, @NotNull final AddressSpace addressSpace)
	{
		this.pointsTo = pointsTo;
		this.addressSpace = addressSpace;
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(pointsTo, addressSpace);
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

		final PointerValueType<?> that = (PointerValueType<?>) o;

		return pointsTo.equals(that.pointsTo) && addressSpace.equals(that.addressSpace);
	}

	@Override
	public int hashCode()
	{
		int result = pointsTo.hashCode();
		result = 31 * result + addressSpace.hashCode();
		return result;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		pointsTo.writeForPointedTo(byteWriter, dataLayoutSpecification);

		if (addressSpace.isZero())
		{
			byteWriter.writeSpace();
		}
		else
		{
			addressSpace.write(byteWriter, dataLayoutSpecification);
		}

		byteWriter.writeAsterisk();
	}

	@Override
	public int storageSizeInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
	{
		return dataLayoutSpecification.pointerStorageSizeInBits();
	}

	@Override
	public int abiAlignmentInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
	{
		return dataLayoutSpecification.pointerAbiAlignmentInBits();
	}
}
