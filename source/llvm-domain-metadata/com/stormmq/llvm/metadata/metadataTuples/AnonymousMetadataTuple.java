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

package com.stormmq.llvm.metadata.metadataTuples;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.metadata.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.stormmq.llvm.metadata.metadataTuples.NamedMetadataTuple.writeTuple;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;
import static java.util.Collections.emptyList;

public class AnonymousMetadataTuple implements Metadata
{
	@NotNull private static final List<Metadata> EmptyList = emptyList();

	@NotNull
	public static AnonymousMetadataTuple emptyAnonymousMetadataTuple(@NotNull final ReferenceTracker referenceTracker)
	{
		return new AnonymousMetadataTuple(referenceTracker, EmptyList);
	}

	@NotNull private final ReferenceTracker referenceTracker;
	@NotNull private final List<? extends Metadata> tuple;

	public AnonymousMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @NotNull final List<? extends Metadata> tuple)
	{
		this.referenceTracker = referenceTracker;
		this.tuple = tuple;
	}

	@Override
	public final boolean isConstant()
	{
		return false;
	}

	@Override
	public final boolean hasBeenWritten()
	{
		return referenceTracker.hasBeenWritten(tuple);
	}

	@Override
	public final int referenceIndex()
	{
		return referenceTracker.referenceIndex(tuple);
	}

	@Override
	public final <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		if (hasBeenWritten())
		{
			byteWriter.writeExclamationMark();
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(referenceIndex()));
			return;
		}

		final byte[] llAssemblyEncoding = encodeUtf8BytesWithCertaintyValueIsValid(Integer.toString(referenceIndex()));
		writeTuple(byteWriter, tuple, llAssemblyEncoding);
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

		final AnonymousMetadataTuple that = (AnonymousMetadataTuple) o;

		return referenceTracker.equals(that.referenceTracker) && tuple.equals(that.tuple);
	}

	@Override
	public final int hashCode()
	{
		int result = referenceTracker.hashCode();
		result = 31 * result + tuple.hashCode();
		return result;
	}
}
