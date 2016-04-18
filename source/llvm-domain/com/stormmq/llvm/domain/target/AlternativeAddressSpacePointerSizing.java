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

package com.stormmq.llvm.domain.target;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.byteWriters.Writable;
import com.stormmq.llvm.domain.AddressSpace;
import com.stormmq.string.AbstractToString;
import org.jetbrains.annotations.*;

import java.util.Map;

import static java.util.Collections.emptyMap;

public final class AlternativeAddressSpacePointerSizing extends AbstractToString implements Writable
{
	@NotNull public static final AlternativeAddressSpacePointerSizing DefaultAlternativeAddressSpacePointerSizing = new AlternativeAddressSpacePointerSizing(emptyMap());

	@NotNull private final Map<AddressSpace, PointerSizing> alternativeAddressSpacePointerSizings;

	public AlternativeAddressSpacePointerSizing(@NotNull final Map<AddressSpace, PointerSizing> alternativeAddressSpacePointerSizings)
	{
		for (final AddressSpace addressSpace : alternativeAddressSpacePointerSizings.keySet())
		{
			if (addressSpace.isZero())
			{
				throw new IllegalArgumentException("Alternative address spaces can not be zero");
			}
		}
		this.alternativeAddressSpacePointerSizings = alternativeAddressSpacePointerSizings;
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(alternativeAddressSpacePointerSizings);
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

		final AlternativeAddressSpacePointerSizing that = (AlternativeAddressSpacePointerSizing) o;

		return alternativeAddressSpacePointerSizings.equals(that.alternativeAddressSpacePointerSizings);
	}

	@Override
	public int hashCode()
	{
		return alternativeAddressSpacePointerSizings.hashCode();
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		for (final Map.Entry<AddressSpace, PointerSizing> entry : alternativeAddressSpacePointerSizings.entrySet())
		{
			entry.getValue().write(byteWriter, entry.getKey());
		}
	}
}
