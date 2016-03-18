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

package com.stormmq.llvm.attributes.writers;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.attributes.Attribute;
import org.jetbrains.annotations.*;

import java.util.*;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class ByteWriterFunctionAttributeGroupWriter<X extends Exception> implements AttributeGroupWriter<X>, AttributeWriter<X>
{
	@NotNull private static final byte[] AttributesStart = encodeUtf8BytesWithCertaintyValueIsValid("attributes #");
	@NotNull private static final byte[] AttributesMiddle = encodeUtf8BytesWithCertaintyValueIsValid(" = {");
	@SuppressWarnings("HardcodedLineSeparator") @NotNull private static final byte[] AttributesEnd = encodeUtf8BytesWithCertaintyValueIsValid(" }\n");

	@NotNull private final ByteWriter<X> byteWriter;
	private int nextGroupIdentifier;
	@NotNull private final Map<SortedSet<? extends Attribute>, Integer> assignedGroupIdentifiers;

	public ByteWriterFunctionAttributeGroupWriter(@NotNull final ByteWriter<X> byteWriter)
	{
		this.byteWriter = byteWriter;
		nextGroupIdentifier = 0;
		assignedGroupIdentifiers = new IdentityHashMap<>(64);
	}

	@Override
	public void write(@NotNull final SortedSet<? extends Attribute> attributes) throws X
	{
		@Nullable final Integer existing = assignedGroupIdentifiers.get(attributes);
		if (existing != null)
		{
			return;
		}

		final int groupIdentifier = nextGroupIdentifier;
		assignedGroupIdentifiers.put(attributes, nextGroupIdentifier);
		nextGroupIdentifier++;

		byteWriter.writeBytes(AttributesStart);
		byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(groupIdentifier));
		byteWriter.writeBytes(AttributesMiddle);

		for (final Attribute attribute : attributes)
		{
			byteWriter.writeByte(' ');
			attribute.write(this);
		}
		byteWriter.writeBytes(AttributesEnd);
	}

	@Override
	public void write(@NonNls @NotNull final String value) throws X
	{
		byteWriter.writeUtf8EncodedStringWithCertainty(value);
	}
}
