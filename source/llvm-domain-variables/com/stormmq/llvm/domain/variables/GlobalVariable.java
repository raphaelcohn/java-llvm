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
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.llvm.domain.typedValues.AddressableIdentifierTypedValue;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import com.stormmq.llvm.domain.names.SectionName;
import com.stormmq.llvm.domain.types.SizedType;
import com.stormmq.llvm.domain.types.firstClassTypes.PointerValueType;
import com.stormmq.string.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static com.stormmq.primitives.IntHelper.*;
import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;

public final class GlobalVariable<T extends SizedType> extends AbstractVariable implements MayHaveComdatDefinition
{
	private static final int MaximumAlignmentPowerOfTwo = 29;
	@NotNull private static final byte[] SpaceExternallyInitialized = encodeToUtf8ByteArrayWithCertaintyValueIsValid(" externally_initialized");
	@NotNull private static final byte[] SpaceGlobal = encodeToUtf8ByteArrayWithCertaintyValueIsValid(" global");
	@NotNull private static final byte[] SpaceConstant = encodeToUtf8ByteArrayWithCertaintyValueIsValid(" constant");
	@NotNull private static final byte[] CommaSpaceAlignSpace = encodeToUtf8ByteArrayWithCertaintyValueIsValid(", align ");

	@NotNull private final AddressSpace addressSpace;
	private final boolean isExternallyInitialized;
	private final boolean isConstant;
	@NotNull private final T type;
	@Nullable private final ConstantTypedValue<T> initializerConstantTypedValue;
	@Nullable private final SectionName sectionName;
	@Nullable private final ComdatDefinition comdatDefinition;
	private final int alignmentInBytesMustBeAPowerOfTwo;

	public GlobalVariable(@NotNull final GlobalIdentifier globalIdentifier, @NotNull final Linkage linkage, @NotNull final Visibility visibility, @NotNull final DllStorageClass dllStorageClass, @NotNull final ThreadLocalStorageModel threadLocalStorageModel, final boolean hasUnnamedAddress, @NotNull final AddressSpace addressSpace, final boolean isExternallyInitialized, @NotNull final T type, @Nullable final ConstantTypedValue<T> initializerConstantTypedValue, @Nullable final SectionName sectionName, @Nullable final ComdatDefinition comdatDefinition, final int alignmentInBytesMustBeAPowerOfTwo)
	{
		super(globalIdentifier, linkage, visibility, dllStorageClass, threadLocalStorageModel, hasUnnamedAddress);

		if (alignmentInBytesMustBeAPowerOfTwo < AutomaticAlignment)
		{
			throw new IllegalArgumentException(Formatting.format("alignmentInBytesMustBeAPowerOfTwo ('%1$s') can not be negative", alignmentInBytesMustBeAPowerOfTwo));
		}

		if (alignmentInBytesMustBeAPowerOfTwo > 0)
		{
			if (alignmentInBytesMustBeAPowerOfTwo > MaximumAlignmentPowerOfTwo)
			{
				throw new IllegalArgumentException(Formatting.format("alignmentInBytesMustBeAPowerOfTwo ('%1$s') can not be more than %2$s", alignmentInBytesMustBeAPowerOfTwo, MaximumAlignmentPowerOfTwo));
			}

			if (isNotAPowerOfTwoIfGreaterThanZero(alignmentInBytesMustBeAPowerOfTwo))
			{
				throw new IllegalArgumentException(Formatting.format("alignmentInBytesMustBeAPowerOfTwo ('%1$s') is not a power of 2", alignmentInBytesMustBeAPowerOfTwo));
			}
		}

		this.addressSpace = addressSpace;
		this.isExternallyInitialized = isExternallyInitialized;
		isConstant = initializerConstantTypedValue != null;
		this.type = type;
		this.initializerConstantTypedValue = initializerConstantTypedValue;
		this.sectionName = sectionName;
		this.comdatDefinition = comdatDefinition;
		this.alignmentInBytesMustBeAPowerOfTwo = alignmentInBytesMustBeAPowerOfTwo;
	}

	@NotNull
	public T type()
	{
		return type;
	}

	@NotNull
	public AddressableIdentifierTypedValue<PointerValueType<T>, GlobalIdentifier> pointer()
	{
		final PointerValueType<T> pointerTo = type.pointerTo(addressSpace);
		return new AddressableIdentifierTypedValue<>(pointerTo, globalIdentifier);
	}

	@SuppressWarnings("SpellCheckingInspection")
	// [@<GlobalVarName> =] [Linkage] [Visibility] [DLLStorageClass] [ThreadLocal] [unnamed_addr] [AddrSpace] [ExternallyInitialized] <global | constant> <Type> [<InitializerConstant>] [, section "name"] [, comdat [($name)]] [, align <Alignment>]
	@Override
	protected <X extends Exception> void writeVariable(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		if (addressSpace.isNotZero())
		{
			addressSpace.write(byteWriter, dataLayoutSpecification);
		}

		if (isExternallyInitialized)
		{
			byteWriter.writeBytes(SpaceExternallyInitialized);
		}

		final byte[] globalOrConstant = isConstant ? SpaceConstant : SpaceGlobal;
		byteWriter.writeBytes(globalOrConstant);

		byteWriter.writeSpace();
		type.write(byteWriter, dataLayoutSpecification);

		if (initializerConstantTypedValue != null)
		{
			byteWriter.writeSpace();
			initializerConstantTypedValue.write(byteWriter, dataLayoutSpecification);
		}

		if (sectionName != null)
		{
			writeCommaSpace(byteWriter);
			sectionName.write(byteWriter, dataLayoutSpecification);
		}

		if (comdatDefinition != null)
		{
			@Nullable final ComdatDefinition actualComdatDefinition = comdatDefinition.adjustComdatDefinition(dataLayoutSpecification.objectFileFormat());
			if (actualComdatDefinition != null)
			{
				writeCommaSpace(byteWriter);
				comdatDefinition.writeComdatIdentifier(byteWriter, dataLayoutSpecification);
			}
		}

		if (alignmentInBytesMustBeAPowerOfTwo != AutomaticAlignment)
		{
			final int alignmentInBytes = 1 << alignmentInBytesMustBeAPowerOfTwo;
			byteWriter.writeBytes(CommaSpaceAlignSpace);
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(alignmentInBytes));
		}
	}

	@Override
	public void useComdatDefinition(@NotNull final Consumer<ComdatDefinition> consumer)
	{
		consumer.accept(comdatDefinition);
	}

	public boolean isConstant()
	{
		return isConstant;
	}
}
