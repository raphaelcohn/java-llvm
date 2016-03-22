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
import com.stormmq.llvm.domain.Writable;
import com.stormmq.llvm.metadata.Metadata;
import org.jetbrains.annotations.*;

import java.util.List;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public class NamedMetadataTuple implements Writable
{
	@NotNull public static final byte[] SpaceEqualsSpaceExclamationMarkOpenBrace = encodeUtf8BytesWithCertaintyValueIsValid(" = !{");
	@NotNull public static final byte[] SpaceEqualsSpace = encodeUtf8BytesWithCertaintyValueIsValid(" = ");
	@NotNull public static final byte[] CommaSpace = encodeUtf8BytesWithCertaintyValueIsValid(", ");
	@SuppressWarnings("HardcodedLineSeparator") @NotNull public static final byte[] CloseBraceLineFeed = encodeUtf8BytesWithCertaintyValueIsValid("}\n");

	@SuppressWarnings("FieldNotUsedInToString") @NotNull private final ReferenceTracker<String> referenceTracker;
	@NotNull private final String name;
	@NotNull private final List<? extends Metadata> tuple;
	@SuppressWarnings("FieldNotUsedInToString") @NotNull private final byte[] llAssemblyEncoding;

	public NamedMetadataTuple(@NotNull final ReferenceTracker<String> referenceTracker, @NotNull @NonNls final String name, @NotNull final List<? extends Metadata> tuple)
	{
		this.referenceTracker = referenceTracker;
		this.name = name;
		this.tuple = tuple;

		llAssemblyEncoding = encodeUtf8BytesWithCertaintyValueIsValid(name);
	}

	private boolean hasBeenWritten()
	{
		return referenceTracker.hasBeenWritten(name);
	}

	@Override
	public final <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		if (hasBeenWritten())
		{
			byteWriter.writeByte('!');
			byteWriter.writeBytes(llAssemblyEncoding);
			return;
		}

		writeTuple(byteWriter, tuple, llAssemblyEncoding);
	}

	public static <X extends Exception> void writeTuple(@NotNull final ByteWriter<X> byteWriter, @NotNull final List<? extends Metadata> tuple, @NotNull final byte... llAssemblyEncoding) throws X
	{
		final int size = tuple.size();
		final int[] referenceIndices = new int[size];
		for (int index = 0; index < size; index++)
		{
			final Metadata item = tuple.get(index);

			if (!item.isConstant())
			{
				final int referenceIndex;
				if (item.hasBeenWritten())
				{
					referenceIndex = item.referenceIndex();
				}
				else
				{
					referenceIndex = item.referenceIndex();
					byteWriter.writeByte('!');
					byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(referenceIndex));
					byteWriter.writeBytes(SpaceEqualsSpace);

					item.write(byteWriter);

					Writable.writeLineFeed(byteWriter);
					Writable.writeLineFeed(byteWriter);
				}
				referenceIndices[index] = referenceIndex;
			}
		}

		byteWriter.writeByte('!');
		byteWriter.writeBytes(llAssemblyEncoding);

		byteWriter.writeBytes(SpaceEqualsSpaceExclamationMarkOpenBrace);
		for (int index = 0; index < size; index++)
		{
			if (index != 0)
			{
				byteWriter.writeBytes(CommaSpace);
			}

			final Metadata item = tuple.get(index);
			if (item.isConstant())
			{
				item.write(byteWriter);
			}
			else
			{
				byteWriter.writeByte('!');
				byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(referenceIndices[index]));
			}
		}
		byteWriter.writeBytes(CloseBraceLineFeed);
	}

	@Override
	@NotNull
	public final String toString()
	{
		return format(ENGLISH, "%1$s(%2$s, %3$s)", getClass().getSimpleName(), name, tuple);
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

		final NamedMetadataTuple that = (NamedMetadataTuple) o;

		if (!referenceTracker.equals(that.referenceTracker))
		{
			return false;
		}
		if (!name.equals(that.name))
		{
			return false;
		}
		return tuple.equals(that.tuple);

	}

	@Override
	public final int hashCode()
	{
		int result = referenceTracker.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + tuple.hashCode();
		return result;
	}
}
