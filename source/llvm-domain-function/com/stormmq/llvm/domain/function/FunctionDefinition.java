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
import com.stormmq.llvm.domain.*;
import com.stormmq.llvm.domain.attributes.AttributeGroup;
import com.stormmq.llvm.domain.comdat.ComdatIdentifier;
import com.stormmq.llvm.domain.function.attributes.FunctionAttributeGroup;
import com.stormmq.llvm.domain.function.attributes.functionAttributes.FunctionAttribute;
import com.stormmq.llvm.domain.function.attributes.parameterAttributes.FixedParameterAttribute;
import com.stormmq.llvm.domain.function.attributes.parameterAttributes.ParameterAttribute;
import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import com.stormmq.llvm.domain.names.SectionName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.llvm.domain.Writable.writeSpace;
import static com.stormmq.llvm.domain.function.attributes.FunctionAttributeGroup.writeFunctionAttributes;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class FunctionDefinition implements Writable
{
	@NotNull public static final AttributeGroup<ParameterAttribute> EmptyParameterAttributes = parameterAttributes();
	@NotNull public static final AttributeGroup<ParameterAttribute> ZeroExtend = parameterAttributes(FixedParameterAttribute.zeroext);
	@NotNull public static final AttributeGroup<ParameterAttribute> SignExtend = parameterAttributes(FixedParameterAttribute.signext);

	@NotNull
	public static AttributeGroup<FunctionAttribute> functionAttributes(@NotNull final FunctionAttribute... functionAttributes)
	{
		return new AttributeGroup<>();
	}

	@NotNull
	public static AttributeGroup<ParameterAttribute> parameterAttributes(@NotNull final ParameterAttribute... parameterAttributes)
	{
		return new AttributeGroup<>();
	}

	@NotNull private static final byte[] defineSpace = encodeUtf8BytesWithCertaintyValueIsValid("define ");
	@NotNull private static final byte[] UnnamedAddress = encodeUtf8BytesWithCertaintyValueIsValid(" unnamed_addr");
	@NotNull private static final byte[] SpaceSection = encodeUtf8BytesWithCertaintyValueIsValid(" section \"");
	@NotNull private static final byte[] SpaceComdatSpaceOpenBracket = encodeUtf8BytesWithCertaintyValueIsValid(" comdat (");
	@NotNull private static final byte[] SpaceAlignSpace = encodeUtf8BytesWithCertaintyValueIsValid(" align ");
	@SuppressWarnings("HardcodedLineSeparator") @NotNull private static final byte[] NewLineOpenBrace = encodeUtf8BytesWithCertaintyValueIsValid("\n{");
	@SuppressWarnings("HardcodedLineSeparator") @NotNull private static final byte[] CloseBraceNewLine = encodeUtf8BytesWithCertaintyValueIsValid("}\n");

	@NotNull private final Linkage linkage;
	@NotNull private final Visibility visibility;
	@Nullable private final DllStorageClass dllStorageClass;
	@NotNull private final CallingConvention callingConvention;
	@NotNull private final AttributeGroup<ParameterAttribute> returnAttributes;
	@NotNull private final FormalParameter resultType;
	@NotNull private final GlobalIdentifier functionIdentifier;
	@NotNull private final FormalParameter[] parameters;
	private final boolean hasUnnamedAddress;
	@NotNull private final FunctionAttributeGroup functionAttributes;
	@Nullable private final SectionName sectionName;
	@Nullable private final ComdatIdentifier comdatIdentifier;
	private final int alignment;
	@Nullable private final GarbageCollectorStrategyName garbageCollectorStrategyName;
	// prefix
	// prologue
	// personality
	// metadata

	public FunctionDefinition(@NotNull final Linkage linkage, @NotNull final Visibility visibility, @Nullable final DllStorageClass dllStorageClass, @NotNull final CallingConvention callingConvention, @NotNull final AttributeGroup<ParameterAttribute> returnAttributes, @NotNull final FormalParameter resultType, @NotNull final GlobalIdentifier functionIdentifier, @NotNull final FormalParameter[] parameters, final boolean hasUnnamedAddress, @NotNull final FunctionAttributeGroup functionAttributes, @Nullable final SectionName sectionName, @Nullable final ComdatIdentifier comdatIdentifier, final int alignment, @Nullable final GarbageCollectorStrategyName garbageCollectorStrategyName)
	{
		if (alignment < AutomaticAlignment)
		{
			throw new IllegalArgumentException(format(ENGLISH, "Alignment ('%1$s') must not be negative", alignment));
		}
		this.linkage = linkage;
		this.visibility = visibility;
		this.dllStorageClass = dllStorageClass;
		this.callingConvention = callingConvention;
		this.returnAttributes = returnAttributes;
		this.functionIdentifier = functionIdentifier;
		this.resultType = resultType;
		this.parameters = parameters;
		this.hasUnnamedAddress = hasUnnamedAddress;
		this.functionAttributes = functionAttributes;
		this.sectionName = sectionName;
		this.comdatIdentifier = comdatIdentifier;
		this.alignment = alignment;
		this.garbageCollectorStrategyName = garbageCollectorStrategyName;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		final int referenceIndex = functionAttributes.writeFunctionAttributesGroup(byteWriter);

		byteWriter.writeBytes(defineSpace);
		byteWriter.writeBytes(linkage.llAssemblyValue);

		writeSpace(byteWriter);
		byteWriter.writeBytes(visibility.llAssemblyValue);

		if (dllStorageClass != null)
		{
			writeSpace(byteWriter);
			byteWriter.writeBytes(dllStorageClass.llAssemblyValue);
		}

		writeSpace(byteWriter);
		byteWriter.writeBytes(callingConvention.llAssemblyValue);

		writeSpace(byteWriter);
		returnAttributes.write(byteWriter);

		writeSpace(byteWriter);
		resultType.write(byteWriter);

		writeSpace(byteWriter);
		functionIdentifier.write(byteWriter);

		byteWriter.writeByte('(');
		for (final FormalParameter parameter : parameters)
		{
			parameter.write(byteWriter);
		}
		byteWriter.writeByte(')');

		if (hasUnnamedAddress)
		{
			byteWriter.writeBytes(UnnamedAddress);
		}

		writeFunctionAttributes(byteWriter, referenceIndex);

		if (sectionName != null)
		{
			byteWriter.writeBytes(SpaceSection);
			sectionName.write(byteWriter);
			byteWriter.writeByte('"');
		}

		if (comdatIdentifier != null)
		{
			byteWriter.writeBytes(SpaceComdatSpaceOpenBracket);
			comdatIdentifier.write(byteWriter);
			byteWriter.writeByte(')');
		}

		if (alignment != AutomaticAlignment)
		{
			byteWriter.writeBytes(SpaceAlignSpace);
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(alignment));
		}

		if (garbageCollectorStrategyName != null)
		{
			writeSpace(byteWriter);
			byteWriter.writeBytes(garbageCollectorStrategyName.llvmAssemblyEncoding);
		}

		byteWriter.writeBytes(NewLineOpenBrace);

		// write code

		byteWriter.writeBytes(CloseBraceNewLine);
	}

}
