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
import com.stormmq.llvm.domain.ObjectFileFormat;
import com.stormmq.llvm.domain.Writable;
import com.stormmq.llvm.domain.target.triple.TargetTriple;
import com.stormmq.string.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.llvm.domain.comdat.ComdatSelectionKind.any;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class ComdatDefinition implements Writable
{
	@NotNull private static final byte[] Middle = encodeUtf8BytesWithCertaintyValueIsValid(" = comdat ");

	@NotNull private final ComdatIdentifier comdatIdentifier;
	@NotNull private final ComdatSelectionKind comdatSelectionKind;

	public ComdatDefinition(@NotNull final ComdatIdentifier comdatIdentifier, @NotNull final ComdatSelectionKind comdatSelectionKind)
	{
		this.comdatIdentifier = comdatIdentifier;
		this.comdatSelectionKind = comdatSelectionKind;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		// $foo = comdat largest
		writeComdatIdentifier(byteWriter);
		byteWriter.writeBytes(Middle);
		byteWriter.writeBytes(comdatSelectionKind.llAssemblyValueWithLineFeed);
	}

	@Override
	@NotNull
	public String toString()
	{
		return Formatting.format("%1$s(%2$s, %3$s)". getClass().getSimpleName(), comdatIdentifier, comdatSelectionKind);
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

		final ComdatDefinition that = (ComdatDefinition) o;

		return comdatIdentifier.equals(that.comdatIdentifier);
	}

	@Override
	public int hashCode()
	{
		return comdatIdentifier.hashCode();
	}

	public <X extends Exception> void writeComdatIdentifier(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		comdatIdentifier.write(byteWriter);
	}

	@NotNull
	public ComdatIdentifier comdatIdentifier()
	{
		return comdatIdentifier;
	}

	@Nullable
	public ComdatDefinition adjustComdatDefinition(@NotNull final TargetTriple targetTriple)
	{
		final ObjectFileFormat objectFileFormat = targetTriple.objectFileFormat();

		if (comdatSelectionKind.isSupportedByObjectFileFormat(objectFileFormat))
		{
			return this;
		}

		if (any.isSupportedByObjectFileFormat(objectFileFormat))
		{
			return new ComdatDefinition(comdatIdentifier, any);
		}

		return null;
	}
}
