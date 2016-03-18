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

package com.stormmq.llvm.domain.parameterTypes;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.attributes.Attribute;
import com.stormmq.llvm.domain.attributes.AttributeGroup;
import com.stormmq.llvm.domain.attributes.parameterAttributes.ParameterAttribute;
import com.stormmq.llvm.domain.attributes.writers.ByteWriterFunctionAttributeGroupWriter;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class ArrayParameterType implements ParameterType
{
	@NotNull private static final byte[] Middle = encodeUtf8BytesWithCertaintyValueIsValid(" x ");

	@NotNull private final SingleValueParameterType singleValueParameterType;
	@NotNull private final AttributeGroup<ParameterAttribute> parameterAttributesGroup;
	private final int[] dimensionLengths;
	private final int length;

	public ArrayParameterType(@NotNull final SingleValueParameterType singleValueParameterType, @NotNull final AttributeGroup<ParameterAttribute> parameterAttributesGroup, final int... dimensionLengths)
	{
		this.singleValueParameterType = singleValueParameterType;
		this.parameterAttributesGroup = parameterAttributesGroup;
		this.dimensionLengths = dimensionLengths;
		length = dimensionLengths.length;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		for (int index = 0; index < length; index++)
		{
			final int dimensionLength = dimensionLengths[index];
			byteWriter.writeByte('[');

			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(dimensionLength));

			byteWriter.writeBytes(Middle);
		}

		singleValueParameterType.write(byteWriter);

		for(int index = 0; index < length; index++)
		{
			byteWriter.writeByte(']');
		}

		final ByteWriterFunctionAttributeGroupWriter<X> attributeWriter = new ByteWriterFunctionAttributeGroupWriter<>(byteWriter);
		parameterAttributesGroup.write(attributes ->
		{
			for (final Attribute attribute : attributes)
			{
				byteWriter.writeByte(' ');
				attribute.write(attributeWriter);
			}
		});
	}
}
