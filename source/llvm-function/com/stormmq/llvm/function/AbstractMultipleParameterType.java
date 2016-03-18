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

package com.stormmq.llvm.function;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.attributes.Attribute;
import com.stormmq.llvm.attributes.AttributeGroup;
import com.stormmq.llvm.attributes.parameterAttributes.ParameterAttribute;
import com.stormmq.llvm.attributes.writers.ByteWriterFunctionAttributeGroupWriter;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public abstract class AbstractMultipleParameterType implements ParameterType
{
	@NotNull private static final byte[] Middle = encodeUtf8BytesWithCertaintyValueIsValid(" x ");

	private final int length;
	@NotNull private final ParameterType parameterType;
	@NotNull private final AttributeGroup<ParameterAttribute> parameterAttributesGroup;

	protected AbstractMultipleParameterType(final int length, @NotNull final ParameterType parameterType, @NotNull final AttributeGroup<ParameterAttribute> parameterAttributesGroup)
	{
		this.length = length;
		this.parameterType = parameterType;
		this.parameterAttributesGroup = parameterAttributesGroup;
	}

	protected abstract byte start();
	protected abstract byte end();

	@Override
	public final <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeByte(start());

		byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(length));

		byteWriter.writeBytes(Middle);

		parameterType.write(byteWriter);

		byteWriter.writeByte(end());

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
