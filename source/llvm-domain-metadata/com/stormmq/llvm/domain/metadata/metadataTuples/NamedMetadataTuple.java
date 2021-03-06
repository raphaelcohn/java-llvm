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

package com.stormmq.llvm.domain.metadata.metadataTuples;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.domain.LlvmWritable;
import com.stormmq.llvm.domain.metadata.Metadata;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.string.Formatting;
import org.jetbrains.annotations.*;

import java.util.List;

import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;

public class NamedMetadataTuple implements LlvmWritable
{
	@NotNull private static final byte[] SpaceEqualsSpaceExclamationMarkOpenBrace = encodeToUtf8ByteArrayWithCertaintyValueIsValid(" = !{");

	@SuppressWarnings("FieldNotUsedInToString") @NotNull private final ReferenceTracker referenceTracker;
	@NotNull private final String name;
	@NotNull private final List<? extends Metadata> tuple;
	@SuppressWarnings("FieldNotUsedInToString") @NotNull private final byte[] llAssemblyEncoding;

	protected NamedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @NotNull @NonNls final String name, @NotNull final List<? extends Metadata> tuple)
	{
		this.referenceTracker = referenceTracker;
		this.name = name;
		this.tuple = tuple;

		llAssemblyEncoding = encodeToUtf8ByteArrayWithCertaintyValueIsValid(name);
	}

	private boolean hasBeenWritten()
	{
		return referenceTracker.hasBeenWritten(name);
	}

	@Override
	public final <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		if (hasBeenWritten())
		{
			byteWriter.writeExclamationMark();
			byteWriter.writeBytes(llAssemblyEncoding);
			return;
		}

		writeTuple(byteWriter, tuple, dataLayoutSpecification, llAssemblyEncoding);
	}

	public static <X extends Exception> void writeTuple(@NotNull final ByteWriter<X> byteWriter, @NotNull final List<? extends Metadata> tuple, @NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final byte... llAssemblyEncoding) throws X
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
					item.write(byteWriter, dataLayoutSpecification);
					referenceIndex = item.referenceIndex();
				}
				referenceIndices[index] = referenceIndex;
			}
		}

		byteWriter.writeExclamationMark();
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
				item.write(byteWriter, dataLayoutSpecification);
			}
			else
			{
				byteWriter.writeExclamationMark();
				byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(referenceIndices[index]));
			}
		}
		byteWriter.writeBytes(CloseBraceLineFeed);
		byteWriter.writeLineFeed();
	}

	@Override
	@NotNull
	public final String toString()
	{
		return Formatting.format("%1$s(%2$s, %3$s)", getClass().getSimpleName(), name, tuple);
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
		//noinspection SimplifiableIfStatement
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
