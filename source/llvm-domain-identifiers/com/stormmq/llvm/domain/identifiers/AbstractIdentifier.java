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

package com.stormmq.llvm.domain.identifiers;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.string.AbstractToString;
import org.jetbrains.annotations.*;

public abstract class AbstractIdentifier extends AbstractToString implements Identifier
{
	@NotNull private final LlvmString identifier;

	protected AbstractIdentifier(@NotNull @NonNls final String identifier)
	{
		this.identifier = new LlvmString(identifier);
	}

	@Override
	public final <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		byteWriter.writeByte(prefix());
		identifier.write(byteWriter, dataLayoutSpecification);
	}

	protected abstract byte prefix();

	@NotNull
	@Override
	public final String name()
	{
		return identifier.name();
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(identifier);
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

		final AbstractIdentifier that = (AbstractIdentifier) o;

		return identifier.equals(that.identifier);
	}

	@Override
	public final int hashCode()
	{
		return identifier.hashCode();
	}
}
