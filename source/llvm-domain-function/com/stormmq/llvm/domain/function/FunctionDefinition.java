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
import com.stormmq.llvm.domain.comdat.ComdatDefinition;
import com.stormmq.llvm.domain.comdat.MayHaveComdatDefinition;
import com.stormmq.llvm.domain.function.attributes.FunctionAttributeGroup;
import com.stormmq.llvm.domain.function.attributes.parameterAttributes.ParameterAttribute;
import com.stormmq.llvm.domain.identifiers.AbstractGloballyIdentified;
import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import com.stormmq.llvm.domain.names.SectionName;
import com.stormmq.llvm.domain.target.triple.TargetTriple;
import com.stormmq.llvm.metadata.debugging.DISubprogramKeyedMetadataTuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static com.stormmq.llvm.domain.function.attributes.FunctionAttributeGroup.writeFunctionAttributes;
import static com.stormmq.string.Formatting.format;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class FunctionDefinition extends AbstractGloballyIdentified implements MayHaveComdatDefinition
{
	@NotNull private static final byte[] defineSpace = encodeUtf8BytesWithCertaintyValueIsValid("define ");
	@NotNull private static final byte[] SpaceSection = encodeUtf8BytesWithCertaintyValueIsValid(" section \"");
	@NotNull private static final byte[] SpaceComdatSpaceOpenBracket = encodeUtf8BytesWithCertaintyValueIsValid(" comdat (");
	@NotNull private static final byte[] SpaceExclamationMarkDbgSpace = encodeUtf8BytesWithCertaintyValueIsValid(" !dbg ");
	@SuppressWarnings("HardcodedLineSeparator") @NotNull private static final byte[] NewLineOpenBrace = encodeUtf8BytesWithCertaintyValueIsValid("\n{");

	@NotNull private final Linkage linkage;
	@NotNull private final Visibility visibility;
	@Nullable private final DllStorageClass dllStorageClass;
	@NotNull private final CallingConvention callingConvention;
	@NotNull private final AttributeGroup<ParameterAttribute> returnAttributes;
	@NotNull private final FormalParameter resultType;
	@NotNull private final FormalParameter[] parameters;
	private final boolean hasUnnamedAddress;
	@NotNull private final FunctionAttributeGroup functionAttributes;
	@Nullable private final SectionName sectionName;
	@Nullable private ComdatDefinition comdatDefinition;
	private final int alignment;
	@Nullable private final GarbageCollectorStrategyName garbageCollectorStrategyName;
	@NotNull private final DISubprogramKeyedMetadataTuple debuggingInformation;

	// prefix
	// prologue
	// personality

	public FunctionDefinition(@NotNull final Linkage linkage, @NotNull final Visibility visibility, @Nullable final DllStorageClass dllStorageClass, @NotNull final CallingConvention callingConvention, @NotNull final AttributeGroup<ParameterAttribute> returnAttributes, @NotNull final FormalParameter resultType, @NotNull final GlobalIdentifier globalIdentifier, @NotNull final FormalParameter[] parameters, final boolean hasUnnamedAddress, @NotNull final FunctionAttributeGroup functionAttributes, @Nullable final SectionName sectionName, @Nullable final ComdatDefinition comdatDefinition, final int alignment, @Nullable final GarbageCollectorStrategyName garbageCollectorStrategyName, @NotNull final DISubprogramKeyedMetadataTuple debuggingInformation)
	{
		super(globalIdentifier);

		if (alignment < AutomaticAlignment)
		{
			throw new IllegalArgumentException(format("Alignment ('%1$s') must not be negative", alignment));
		}

		this.linkage = linkage;
		this.visibility = visibility;
		this.dllStorageClass = dllStorageClass;
		this.callingConvention = callingConvention;
		this.returnAttributes = returnAttributes;
		this.resultType = resultType;
		this.parameters = parameters;
		this.hasUnnamedAddress = hasUnnamedAddress;
		this.functionAttributes = functionAttributes;
		this.sectionName = sectionName;
		this.comdatDefinition = comdatDefinition;
		this.alignment = alignment;
		this.garbageCollectorStrategyName = garbageCollectorStrategyName;
		this.debuggingInformation = debuggingInformation;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		final int referenceIndex = functionAttributes.writeFunctionAttributesGroup(byteWriter);

		byteWriter.writeBytes(defineSpace);
		byteWriter.writeBytes(linkage.llAssemblyValue);

		byteWriter.writeSpace();
		byteWriter.writeBytes(visibility.llAssemblyValue);

		if (dllStorageClass != null)
		{
			byteWriter.writeSpace();
			byteWriter.writeBytes(dllStorageClass.llAssemblyValue);
		}

		byteWriter.writeSpace();
		byteWriter.writeBytes(callingConvention.llAssemblyValue);

		byteWriter.writeSpace();
		returnAttributes.write(byteWriter);

		byteWriter.writeSpace();
		resultType.write(byteWriter);

		byteWriter.writeSpace();
		globalIdentifier().write(byteWriter);

		byteWriter.writeOpenBracket();
		for (final FormalParameter parameter : parameters)
		{
			parameter.write(byteWriter);
		}
		byteWriter.writeCloseBracket();

		if (hasUnnamedAddress)
		{
			byteWriter.writeBytes(SpaceUnnamedAddress);
		}

		writeFunctionAttributes(byteWriter, referenceIndex);

		if (sectionName != null)
		{
			byteWriter.writeBytes(SpaceSection);
			sectionName.write(byteWriter);
			byteWriter.writeDoubleQuote();
		}

		if (comdatDefinition != null)
		{
			byteWriter.writeBytes(SpaceComdatSpaceOpenBracket);
			comdatDefinition.writeComdatIdentifier(byteWriter);
			byteWriter.writeCloseBracket();
		}

		if (alignment != AutomaticAlignment)
		{
			byteWriter.writeBytes(SpaceAlignSpace);
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(alignment));
		}

		if (garbageCollectorStrategyName != null)
		{
			byteWriter.writeSpace();
			byteWriter.writeBytes(garbageCollectorStrategyName.llvmAssemblyEncoding);
		}

		byteWriter.writeBytes(SpaceExclamationMarkDbgSpace);
		debuggingInformation.write(byteWriter);

		byteWriter.writeBytes(NewLineOpenBrace);

		// write code

		byteWriter.writeBytes(Writable.CloseBraceLineFeed);
	}

	@Override
	public void adjustComdatDefinition(@NotNull final Set<ComdatDefinition> comdatDefinitions, @NotNull final TargetTriple targetTriple)
	{
		comdatDefinition = MayHaveComdatDefinition.adjustComdatDefinition(comdatDefinitions, comdatDefinition, targetTriple);
	}
}
