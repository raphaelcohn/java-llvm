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
import com.stormmq.llvm.domain.function.attributes.parameterAttributes.ParameterAttribute;
import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.llvm.domain.Writable.writeSpace;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class FunctionDeclaration implements Writable
{
	@NotNull private static final byte[] declareSpace = encodeUtf8BytesWithCertaintyValueIsValid("declare ");
	@NotNull private static final byte[] UnnamedAddress = encodeUtf8BytesWithCertaintyValueIsValid(" unnamed_addr");
	@NotNull private static final byte[] SpaceAlignSpace = encodeUtf8BytesWithCertaintyValueIsValid(" align ");

	@NotNull private final Linkage linkage;
	@NotNull private final Visibility visibility;
	@Nullable private final DllStorageClass dllStorageClass;
	@NotNull private final CallingConvention callingConvention;
	@NotNull private final AttributeGroup<ParameterAttribute> returnAttributes;
	@NotNull private final FormalParameter resultType;
	@NotNull private final GlobalIdentifier functionIdentifier;
	@NotNull private final FormalParameter[] parameters;
	private final boolean hasUnnamedAddress;
	private final int alignment;
	@Nullable private final GarbageCollectorStrategyName garbageCollectorStrategyName;

	// also prefix, prologue
	public FunctionDeclaration(@NotNull final Linkage linkage, @NotNull final Visibility visibility, @Nullable final DllStorageClass dllStorageClass, @NotNull final CallingConvention callingConvention, @NotNull final AttributeGroup<ParameterAttribute> returnAttributes, @NotNull final FormalParameter resultType, @NotNull final GlobalIdentifier functionIdentifier, @NotNull final FormalParameter[] parameters, final boolean hasUnnamedAddress, final int alignment, @Nullable final GarbageCollectorStrategyName garbageCollectorStrategyName)
	{

		this.linkage = linkage;
		this.visibility = visibility;
		this.dllStorageClass = dllStorageClass;
		this.callingConvention = callingConvention;
		this.returnAttributes = returnAttributes;
		this.resultType = resultType;
		this.functionIdentifier = functionIdentifier;
		this.parameters = parameters;
		this.hasUnnamedAddress = hasUnnamedAddress;
		this.alignment = alignment;
		this.garbageCollectorStrategyName = garbageCollectorStrategyName;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(declareSpace);
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

		Writable.writeLineFeed(byteWriter);
	}
}
