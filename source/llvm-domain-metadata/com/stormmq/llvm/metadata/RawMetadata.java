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

package com.stormmq.llvm.metadata;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.identifiers.Identifier;
import org.jetbrains.annotations.*;

import java.util.Arrays;
import java.util.Set;

import static com.stormmq.java.parsing.utilities.ReservedIdentifiers._false;
import static com.stormmq.java.parsing.utilities.ReservedIdentifiers._true;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class RawMetadata implements Metadata
{
	@NotNull private final byte[] llAssemblyEncoding;

	public RawMetadata(@NotNull final Enum<?> value)
	{
		this(value.name());
	}

	@SuppressWarnings("TypeMayBeWeakened")
	public <E extends Enum<E>> RawMetadata(@NotNull final Set<E> values)
	{
		this(convertSetOfEnumFlags(values));
	}

	@NotNull
	private static <E extends Enum<E>> String convertSetOfEnumFlags(@NotNull final Iterable<E> values)
	{
		final StringBuilder stringBuilder = new StringBuilder(1024);
		for (final E value : values)
		{
			if (stringBuilder.length() != 0)
			{
				stringBuilder.append(' ');
			}
			stringBuilder.append(value.name());
		}
		return stringBuilder.toString();
	}

	public RawMetadata(@NotNull final Identifier identifier)
	{
		this(identifier.name());
	}

	public RawMetadata(final int value)
	{
		this(Integer.toString(value));
	}

	public RawMetadata(final boolean value)
	{
		this(value ? _true : _false);
	}

	public RawMetadata(@NonNls @NotNull final String value)
	{
		this(encodeUtf8BytesWithCertaintyValueIsValid(value));
	}

	@SuppressWarnings("OverloadedVarargsMethod")
	private RawMetadata(@NotNull final byte... llAssemblyEncoding)
	{
		this.llAssemblyEncoding = llAssemblyEncoding;
	}

	@Override
	public boolean isConstant()
	{
		return true;
	}

	@Override
	public boolean hasBeenWritten()
	{
		return false;
	}

	@Override
	public int referenceIndex()
	{
		throw new UnsupportedOperationException("Raw metadata does not have a reference");
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(llAssemblyEncoding);
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

		final RawMetadata that = (RawMetadata) o;

		return Arrays.equals(llAssemblyEncoding, that.llAssemblyEncoding);

	}

	@Override
	public int hashCode()
	{
		return Arrays.hashCode(llAssemblyEncoding);
	}
}
