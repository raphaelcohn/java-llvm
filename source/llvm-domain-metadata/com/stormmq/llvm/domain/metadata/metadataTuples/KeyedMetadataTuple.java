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
import com.stormmq.llvm.domain.metadata.KeyWithMetadataField;
import com.stormmq.llvm.domain.metadata.Metadata;
import org.jetbrains.annotations.*;

import java.util.List;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;
import static java.util.Arrays.asList;
import static java.util.Collections.addAll;

public class KeyedMetadataTuple implements Metadata
{
	@NotNull private static final byte[] distinctSpace = encodeUtf8BytesWithCertaintyValueIsValid("distinct ");

	@NotNull protected final ReferenceTracker referenceTracker;
	private final boolean isDistinct;
	@NotNull private final String name;
	@NotNull private final List<KeyWithMetadataField> fields;

	@SuppressWarnings("OverloadedVarargsMethod")
	protected KeyedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, final boolean isDistinct, @NotNull @NonNls final String name, @NotNull final KeyWithMetadataField... fields)
	{
		this(referenceTracker, isDistinct, name, asList(fields));
	}

	protected KeyedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, final boolean isDistinct, @NotNull @NonNls final String name, @NotNull final List<KeyWithMetadataField> fields)
	{
		this.referenceTracker = referenceTracker;
		this.isDistinct = isDistinct;
		this.name = name;
		this.fields = fields;
	}

	@SuppressWarnings("OverloadedVarargsMethod")
	protected final void populate(@NotNull final KeyWithMetadataField... fields)
	{
		if (!this.fields.isEmpty())
		{
			throw new IllegalStateException("Already populated (varargs)");
		}
		addAll(this.fields, fields);
	}

	protected final void populateHackForCircularReferencesInMetadataModel(@NotNull @NonNls final KeyWithMetadataField field)
	{
		fields.add(field);
	}

	@Override
	public boolean isConstant()
	{
		return false;
	}

	@Override
	public boolean hasBeenWritten()
	{
		return referenceTracker.hasBeenWritten(this);
	}

	@Override
	public int referenceIndex()
	{
		return referenceTracker.referenceIndex(this);
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		if (hasBeenWritten())
		{
			byteWriter.writeExclamationMark();
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(referenceIndex()));
			return;
		}

		for (final KeyWithMetadataField field : fields)
		{
			field.writeIfNotAConstant(byteWriter);
		}

		byteWriter.writeExclamationMark();
		byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(referenceIndex()));

		byteWriter.writeBytes(SpaceEqualsSpace);
		if (isDistinct)
		{
			byteWriter.writeBytes(distinctSpace);
		}

		byteWriter.writeExclamationMark();
		byteWriter.writeUtf8EncodedStringWithCertainty(name);
		byteWriter.writeOpenBracket();

		boolean isAfterFirst = false;
		for (final KeyWithMetadataField field : fields)
		{
			if (isAfterFirst)
			{
				byteWriter.writeBytes(CommaSpace);
			}
			else
			{
				isAfterFirst = true;
			}

			field.writeKeyValue(byteWriter);
		}
		byteWriter.writeBytes(CloseBracketLineFeed);
		byteWriter.writeLineFeed();
	}

	@SuppressWarnings("SimplifiableIfStatement")
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

		final KeyedMetadataTuple that = (KeyedMetadataTuple) o;

		if (isDistinct != that.isDistinct)
		{
			return false;
		}
		if (!referenceTracker.equals(that.referenceTracker))
		{
			return false;
		}
		if (!name.equals(that.name))
		{
			return false;
		}
		return fields.equals(that.fields);
	}

	@Override
	public final int hashCode()
	{
		int result = referenceTracker.hashCode();
		result = 31 * result + (isDistinct ? 1 : 0);
		result = 31 * result + name.hashCode();
		result = 31 * result + fields.hashCode();
		return result;
	}
}
