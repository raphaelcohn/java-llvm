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

package com.stormmq.llvm.domain.names;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.Writable;
import com.stormmq.string.*;
import org.jetbrains.annotations.*;

import static com.stormmq.string.StringUtilities.iterateOverStringCodePoints;

public abstract class AbstractName implements Writable
{
	@NotNull private final String name;

	protected AbstractName(@NonNls @NotNull final String name)
	{
		this.name = validateName(name);
	}

	@NotNull
	private static String validateName(@NotNull @NonNls final String name)
	{
		try
		{
			iterateOverStringCodePoints(name, (index, codePoint) ->
			{
				if (codePoint == 0)
				{
					throw new IllegalArgumentException("Names can not contain ASCII NUL");
				}
			});
		}
		catch (final InvalidUtf16StringException e)
		{
			throw new IllegalArgumentException("A name must be a valid UTF-16 string", e);
		}
		return name;
	}

	@Override
	public final <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(start());
		byteWriter.writeUtf8EncodedStringWithCertainty(name);
		byteWriter.writeBytes(end());
	}

	@NotNull
	protected abstract byte[] start();

	@NotNull
	protected abstract byte[] end();

	@Override
	@NotNull
	public final String toString()
	{
		return Formatting.format("%1$s(%2$s)". getClass().getSimpleName(), name);
	}

	@Override
	public final boolean equals(@Nullable final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		final AbstractName sectionName = (AbstractName) o;

		return name.equals(sectionName.name);

	}

	@Override
	public final int hashCode()
	{
		return name.hashCode();
	}

	@NotNull
	@NonNls
	public final String name()
	{
		return name;
	}
}
