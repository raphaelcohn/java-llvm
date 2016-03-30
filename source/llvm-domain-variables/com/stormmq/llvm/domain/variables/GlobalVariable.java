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

package com.stormmq.llvm.domain.variables;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.*;
import com.stormmq.llvm.domain.comdat.ComdatDefinition;
import com.stormmq.llvm.domain.comdat.MayHaveComdatDefinition;
import com.stormmq.llvm.domain.constants.Constant;
import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import com.stormmq.llvm.domain.names.SectionName;
import com.stormmq.llvm.domain.target.triple.TargetTriple;
import com.stormmq.llvm.domain.types.Type;
import com.stormmq.string.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class GlobalVariable<T extends Type> extends AbstractVariable implements MayHaveComdatDefinition
{
	private static final int MaximumPowerOfTwo = 29;
	@SuppressWarnings("SpellCheckingInspection") @NotNull private static final byte[] SpaceAddressSpaceStart = encodeUtf8BytesWithCertaintyValueIsValid(" addrspace(");
	@NotNull private static final byte[] SpaceExternallyInitialized = encodeUtf8BytesWithCertaintyValueIsValid(" externally_initialized");
	@NotNull private static final byte[] SpaceGlobal = encodeUtf8BytesWithCertaintyValueIsValid(" global");
	@NotNull private static final byte[] SpaceConstant = encodeUtf8BytesWithCertaintyValueIsValid(" constant");
	@NotNull private static final byte[] CommaSpaceAlignSpace = encodeUtf8BytesWithCertaintyValueIsValid(", align ");

	private final int addressSpace;
	private final boolean isExternallyInitialized;
	private final boolean isConstant;
	@NotNull private final T type;
	@Nullable private final Constant<T> initializerConstant;
	@Nullable private final SectionName sectionName;
	@Nullable private ComdatDefinition comdatDefinition;
	private final int alignmentAsPowerOfTwo;

	public GlobalVariable(@NotNull final GlobalIdentifier globalIdentifier, @NotNull final Linkage linkage, @NotNull final Visibility visibility, @Nullable final DllStorageClass dllStorageClass, @Nullable final ThreadLocalStorageModel threadLocalStorageModel, final boolean hasUnnamedAddress, final int addressSpace, final boolean isExternallyInitialized, final boolean isConstant, @NotNull final T type, @Nullable final Constant<T> initializerConstant, @Nullable final SectionName sectionName, @Nullable final ComdatDefinition comdatDefinition, final int alignmentAsPowerOfTwo)
	{
		super(globalIdentifier, linkage, visibility, dllStorageClass, threadLocalStorageModel, hasUnnamedAddress);
		this.isExternallyInitialized = isExternallyInitialized;
		if (alignmentAsPowerOfTwo < AutomaticAlignment)
		{
			throw new IllegalArgumentException(Formatting.format("alignmentAsPowerOfTwo ('%1$s') can not be negative", alignmentAsPowerOfTwo));
		}

		if (alignmentAsPowerOfTwo > MaximumPowerOfTwo)
		{
			throw new IllegalArgumentException(Formatting.format("alignmentAsPowerOfTwo ('%1$s') can not be more than %2$s", alignmentAsPowerOfTwo, MaximumPowerOfTwo));
		}

		if (isConstant && initializerConstant == null)
		{
			throw new IllegalArgumentException("Constants must have a non-null initializerConstant");
		}

		this.addressSpace = addressSpace;
		this.isConstant = isConstant;
		this.type = type;
		this.initializerConstant = initializerConstant;
		this.sectionName = sectionName;
		this.comdatDefinition = comdatDefinition;
		this.alignmentAsPowerOfTwo = alignmentAsPowerOfTwo;
	}

	@SuppressWarnings("SpellCheckingInspection")
	// [@<GlobalVarName> =] [Linkage] [Visibility] [DLLStorageClass] [ThreadLocal] [unnamed_addr] [AddrSpace] [ExternallyInitialized] <global | constant> <Type> [<InitializerConstant>] [, section "name"] [, comdat [($name)]] [, align <Alignment>]
	@Override
	protected <X extends Exception> void writeVariable(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		if (addressSpace != 0)
		{
			byteWriter.writeBytes(SpaceAddressSpaceStart);
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(addressSpace));
			byteWriter.writeCloseBracket();
		}

		if (isExternallyInitialized)
		{
			byteWriter.writeBytes(SpaceExternallyInitialized);
		}

		final byte[] globalOrConstant = isConstant ? SpaceConstant : SpaceGlobal;
		byteWriter.writeBytes(globalOrConstant);

		byteWriter.writeSpace();
		type.write(byteWriter);

		if (initializerConstant != null)
		{
			initializerConstant.write(byteWriter);
		}

		if (sectionName != null)
		{
			writeCommaSpace(byteWriter);
			sectionName.write(byteWriter);
		}

		if (comdatDefinition != null)
		{
			writeCommaSpace(byteWriter);
			comdatDefinition.writeComdatIdentifier(byteWriter);
		}

		if (alignmentAsPowerOfTwo != AutomaticAlignment)
		{
			final int alignmentInBytes = 1 << alignmentAsPowerOfTwo;
			byteWriter.writeBytes(CommaSpaceAlignSpace);
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(alignmentInBytes));
		}
	}

	@Override
	public void adjustComdatDefinition(@NotNull final Set<ComdatDefinition> comdatDefinitions, @NotNull final TargetTriple targetTriple)
	{
		comdatDefinition = MayHaveComdatDefinition.adjustComdatDefinition(comdatDefinitions, comdatDefinition, targetTriple);
	}
}
