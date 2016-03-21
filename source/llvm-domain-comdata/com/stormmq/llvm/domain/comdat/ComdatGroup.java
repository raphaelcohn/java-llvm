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

package com.stormmq.llvm.domain.comdat;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.Writable;
import org.jetbrains.annotations.*;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class ComdatGroup implements Writable
{
	@NotNull private static final byte[] Middle = encodeUtf8BytesWithCertaintyValueIsValid(" = comdat ");
	@NotNull private static final byte[] End = encodeUtf8BytesWithCertaintyValueIsValid(")");

	@NotNull private final ComdatName comdatName;
	@NotNull private final ComdatSelectionKind comdatSelectionKind;

	public ComdatGroup(@NotNull final ComdatName comdatName, @NotNull final ComdatSelectionKind comdatSelectionKind)
	{
		this.comdatName = comdatName;
		this.comdatSelectionKind = comdatSelectionKind;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		// $foo = comdat largest
		final String name = comdatName.name();
		byteWriter.writeByte('$');
		byteWriter.writeUtf8EncodedStringWithCertainty(name);
		byteWriter.writeBytes(Middle);
		byteWriter.writeBytes(comdatSelectionKind.llAssemblyValueWithLineFeed);
	}

	@Override
	@NotNull
	public String toString()
	{
		return format(ENGLISH, "%1$s(%2$s, %3$s)". getClass().getSimpleName(), comdatName, comdatSelectionKind);
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

		final ComdatGroup that = (ComdatGroup) o;

		return comdatName.equals(that.comdatName) && comdatSelectionKind == that.comdatSelectionKind;
	}

	@Override
	public int hashCode()
	{
		int result = comdatName.hashCode();
		result = 31 * result + comdatSelectionKind.hashCode();
		return result;
	}
}
