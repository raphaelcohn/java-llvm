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

package com.stormmq.llvm.domain.function.attributes;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.*;
import com.stormmq.llvm.domain.attributes.AttributeGroup;
import com.stormmq.llvm.domain.function.attributes.functionAttributes.FunctionAttribute;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class FunctionAttributeGroup extends AttributeGroup<FunctionAttribute> implements Reference
{
	@NotNull private static final byte[] attributesSpaceHash = encodeUtf8BytesWithCertaintyValueIsValid("attributes #");
	@NotNull private static final byte[] SpaceEqualsSpaceOpenBraceSpace = encodeUtf8BytesWithCertaintyValueIsValid(" = { ");
	@SuppressWarnings("HardcodedLineSeparator") @NotNull private static final byte[] SpaceCloseBraceLineFeedLineFeed = encodeUtf8BytesWithCertaintyValueIsValid(" }\n\n");

	public static <X extends Exception> void writeFunctionAttributes(@NotNull final ByteWriter<X> byteWriter, final int referenceIndex) throws X
	{
		byteWriter.writeSpace();
		byteWriter.writeHash();
		byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(referenceIndex));
	}

	@NotNull private final ReferenceTracker referenceTracker;

	public FunctionAttributeGroup(@NotNull final ReferenceTracker referenceTracker, @NotNull final FunctionAttribute... attributes)
	{
		super(attributes);
		this.referenceTracker = referenceTracker;
	}

	public <X extends Exception> int writeFunctionAttributesGroup(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		if (hasBeenWritten())
		{
			return referenceIndex();
		}

		final int referenceIndex = referenceIndex();
		// "attributes #0 = { "
		byteWriter.writeBytes(attributesSpaceHash);
		byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(referenceIndex));
		byteWriter.writeBytes(SpaceEqualsSpaceOpenBraceSpace);

		write(byteWriter);

		byteWriter.writeBytes(SpaceCloseBraceLineFeedLineFeed);

		return referenceIndex;
	}

	@Override
	public boolean hasBeenWritten()
	{
		return referenceTracker.hasBeenWritten(attributes);
	}

	@Override
	public int referenceIndex()
	{
		return referenceTracker.referenceIndex(attributes);
	}
}
