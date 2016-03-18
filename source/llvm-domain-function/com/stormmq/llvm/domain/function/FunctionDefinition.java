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

package com.stormmq.llvm.domain.function;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.attributes.Attribute;
import com.stormmq.llvm.domain.attributes.AttributeGroup;
import com.stormmq.llvm.domain.attributes.functionAttributes.FunctionAttribute;
import com.stormmq.llvm.domain.attributes.writers.ByteWriterFunctionAttributeGroupWriter;
import com.stormmq.llvm.domain.parameterTypes.ParameterType;
import org.jetbrains.annotations.*;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class FunctionDefinition
{
	public static final int NoAlignment = -1;

	@NotNull private static final byte[] defineSpace = encodeUtf8BytesWithCertaintyValueIsValid("define ");
	@NotNull private static final byte[] SpaceAt = encodeUtf8BytesWithCertaintyValueIsValid(" @");
	@NotNull private static final byte[] UnnamedAddress = encodeUtf8BytesWithCertaintyValueIsValid(" unnamed_addr");
	@NotNull private static final byte[] SpaceSection = encodeUtf8BytesWithCertaintyValueIsValid(" section \"");
	@NotNull private static final byte[] SpaceComdatDollar = encodeUtf8BytesWithCertaintyValueIsValid(" comdat ($");
	@NotNull private static final byte[] SpaceAlignSpace = encodeUtf8BytesWithCertaintyValueIsValid(" align ");
	@NotNull private static final byte[] SpaceGcSpaceDoubleQuote = encodeUtf8BytesWithCertaintyValueIsValid(" gc \"");
	@SuppressWarnings("HardcodedLineSeparator") @NotNull private static final byte[] NewLineOpenBrace = encodeUtf8BytesWithCertaintyValueIsValid("\n{");
	@SuppressWarnings("HardcodedLineSeparator") @NotNull private static final byte[] CloseBraceNewLine = encodeUtf8BytesWithCertaintyValueIsValid("}\n");

	@NotNull private final LinkageType linkageType;
	@NotNull private final VisibilityStyle visibilityStyle;
	@Nullable private final DllStorageClass dllStorageClass;
	@NotNull private final CallingConvention callingConvention;
	@NotNull private final ParameterType resultType;
	@NotNull private final String functionName;
	@NotNull private final ParameterType[] parameters;
	private final boolean isUnnamedAddress;
	@NotNull private final AttributeGroup<FunctionAttribute> attributes;
	@Nullable @NonNls private final String sectionName;
	@Nullable @NonNls private final String comdatName;
	private final int alignment;
	@Nullable private final String garbageCollectorStrategyName;
	// prefix
	// prologue
	// personality
	// metadata

	public FunctionDefinition(@NotNull final LinkageType linkageType, @NotNull final VisibilityStyle visibilityStyle, @Nullable final DllStorageClass dllStorageClass, @NotNull final CallingConvention callingConvention, @NotNull final ParameterType resultType, @NonNls @NotNull final String functionName, @NotNull final ParameterType[] parameters, final boolean isUnnamedAddress, @NotNull final AttributeGroup<FunctionAttribute> attributes, @Nullable @NonNls final String sectionName, @Nullable @NonNls final String comdatName, final int alignment, @Nullable @NonNls final String garbageCollectorStrategyName)
	{
		this.linkageType = linkageType;
		this.visibilityStyle = visibilityStyle;
		this.dllStorageClass = dllStorageClass;
		this.callingConvention = callingConvention;
		// We deliberately prefix with '.' to prevent collisions with the 'llvm.' namespace of predefined functions
		this.functionName = '.' + functionName;
		this.resultType = resultType;
		this.parameters = parameters;
		this.isUnnamedAddress = isUnnamedAddress;
		this.attributes = attributes;
		this.sectionName = sectionName;
		this.comdatName = comdatName;
		this.alignment = alignment;
		this.garbageCollectorStrategyName = garbageCollectorStrategyName;
	}

	public <X extends Exception> void writeDefine(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(defineSpace);

		byteWriter.writeUtf8EncodedStringWithCertainty(linkageType.name);
		byteWriter.writeByte(' ');

		byteWriter.writeUtf8EncodedStringWithCertainty(visibilityStyle.name);
		byteWriter.writeByte(' ');

		if (dllStorageClass != null)
		{
			byteWriter.writeUtf8EncodedStringWithCertainty(dllStorageClass.name());
			byteWriter.writeByte(' ');
		}

		byteWriter.writeUtf8EncodedStringWithCertainty(callingConvention.name);
		byteWriter.writeByte(' ');

		resultType.write(byteWriter);
		byteWriter.writeBytes(SpaceAt);
		byteWriter.writeUtf8EncodedStringWithCertainty(functionName);

		byteWriter.writeByte('(');
		for (final ParameterType parameter : parameters)
		{
			parameter.write(byteWriter);
		}
		byteWriter.writeByte(')');

		if (isUnnamedAddress)
		{
			byteWriter.writeBytes(UnnamedAddress);
		}

		final ByteWriterFunctionAttributeGroupWriter<X> attributeWriter = new ByteWriterFunctionAttributeGroupWriter<>(byteWriter);
		attributes.write(attributes1 ->
		{
			for (final Attribute attribute : attributes1)
			{
				byteWriter.writeByte(' ');
				attribute.write(attributeWriter);
			}
		});

		if (sectionName != null)
		{
			byteWriter.writeBytes(SpaceSection);
			byteWriter.writeByte('"');
		}

		if (comdatName != null)
		{
			byteWriter.writeBytes(SpaceComdatDollar);
			byteWriter.writeUtf8EncodedStringWithCertainty(comdatName);
			byteWriter.writeByte(')');
		}

		if (alignment != NoAlignment)
		{
			byteWriter.writeBytes(SpaceAlignSpace);
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(alignment));
		}

		if (garbageCollectorStrategyName != null)
		{
			byteWriter.writeBytes(SpaceGcSpaceDoubleQuote);
			byteWriter.writeUtf8EncodedStringWithCertainty(garbageCollectorStrategyName);
			byteWriter.writeByte('"');
		}

		byteWriter.writeBytes(NewLineOpenBrace);

		// write code

		byteWriter.writeBytes(CloseBraceNewLine);


		byteWriter.writeByte(')');
	}
}
