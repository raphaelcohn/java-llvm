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
import com.stormmq.llvm.domain.comdat.ComdatIdentifier;
import com.stormmq.llvm.domain.names.SectionName;
import com.stormmq.llvm.domain.types.Type;
import org.jetbrains.annotations.*;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class GlobalVariable extends AbstractVariable
{
	@NotNull private static final byte[] SpaceAddressSpaceStart = encodeUtf8BytesWithCertaintyValueIsValid(" addrspace(");
	@NotNull private static final byte[] SpaceGlobal = encodeUtf8BytesWithCertaintyValueIsValid(" global");
	@NotNull private static final byte[] SpaceConstant = encodeUtf8BytesWithCertaintyValueIsValid(" constant");
	@NotNull private static final byte[] CommaSpaceAlignSpace = encodeUtf8BytesWithCertaintyValueIsValid(", align ");

	private final int addressSpace;
	private final boolean isConstant;
	@NotNull private final Type type;
	@Nullable private final Object initializerConstant;
	@Nullable private final SectionName sectionName;
	@Nullable private final ComdatIdentifier comdatIdentifier;
	private final int alignmentAsPowerOfTwo;

	public GlobalVariable(@NotNull @NonNls final String name, @NotNull final Linkage linkage, @NotNull final Visibility visibility, @Nullable final DllStorageClass dllStorageClass, @Nullable final ThreadLocalStorageModel threadLocalStorageModel, final boolean hasUnnamedAddress, final int addressSpace, final boolean isConstant, @NotNull final Type type, @Nullable final Object initializerConstant, @Nullable final SectionName sectionName, @Nullable final ComdatIdentifier comdatIdentifier, final int alignmentAsPowerOfTwo)
	{
		super(name, linkage, visibility, dllStorageClass, threadLocalStorageModel, hasUnnamedAddress);
		if (alignmentAsPowerOfTwo < AutomaticAlignment)
		{
			throw new IllegalArgumentException(format(ENGLISH, "alignmentAsPowerOfTwo ('%1$s') can not be negative", alignmentAsPowerOfTwo));
		}

		if (alignmentAsPowerOfTwo > 29)
		{
			throw new IllegalArgumentException(format(ENGLISH, "alignmentAsPowerOfTwo ('%1$s') can not be more than 29", alignmentAsPowerOfTwo));
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
		this.comdatIdentifier = comdatIdentifier;
		this.alignmentAsPowerOfTwo = alignmentAsPowerOfTwo;
	}

	// [@<GlobalVarName> =] [Linkage] [Visibility] [DLLStorageClass] [ThreadLocal] [unnamed_addr] [AddrSpace] [ExternallyInitialized] <global | constant> <Type> [<InitializerConstant>] [, section "name"] [, comdat [($name)]] [, align <Alignment>]

	@Override
	protected <X extends Exception> void writeVariable(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		if (addressSpace != 0)
		{
			byteWriter.writeBytes(SpaceAddressSpaceStart);
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(addressSpace));
			byteWriter.writeByte(')');
		}

		// TODO: ExternallyInitialized

		final byte[] globalOrConstant = isConstant ? SpaceConstant : SpaceGlobal;
		byteWriter.writeBytes(globalOrConstant);

		Writable.writeSpace(byteWriter);
		type.write(byteWriter);

		if (initializerConstant != null)
		{
			throw new UnsupportedOperationException("initializer constants are not yet supported");
		}

		if (sectionName != null)
		{
			writeCommaSpace(byteWriter);
			sectionName.write(byteWriter);
		}

		if (comdatIdentifier != null)
		{
			writeCommaSpace(byteWriter);
			comdatIdentifier.write(byteWriter);
		}

		if (alignmentAsPowerOfTwo != AutomaticAlignment)
		{
			final int alignmentInBytes = 1 << alignmentAsPowerOfTwo;
			byteWriter.writeBytes(CommaSpaceAlignSpace);
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(alignmentInBytes));
		}
	}
}
