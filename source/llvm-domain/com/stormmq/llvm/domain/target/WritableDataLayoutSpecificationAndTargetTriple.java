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

package com.stormmq.llvm.domain.target;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.byteWriters.Writable;
import com.stormmq.llvm.domain.*;
import com.stormmq.string.AbstractToString;
import org.jetbrains.annotations.*;

import java.util.Map;
import java.util.Map.Entry;

import static com.stormmq.primitives.IntHelper.isNotAPowerOfTwoIfGreaterThanZero;
import static com.stormmq.llvm.domain.AddressSpace.GlobalAddressSpace;
import static com.stormmq.llvm.domain.target.Alignment.*;
import static com.stormmq.llvm.domain.target.Architecture.*;
import static com.stormmq.llvm.domain.target.CommonCDataModel.*;
import static com.stormmq.llvm.domain.target.CxxNameMangling.Intel;
import static com.stormmq.llvm.domain.target.Endianness.BigEndian;
import static com.stormmq.llvm.domain.target.Endianness.LittleEndian;
import static com.stormmq.llvm.domain.target.SymbolMangling.ELF;
import static com.stormmq.llvm.domain.target.SymbolMangling.MIPS;
import static com.stormmq.llvm.domain.target.SymbolMangling.MachO;
import static com.stormmq.llvm.domain.target.TargetOperatingSystem.*;
import static com.stormmq.string.Formatting.format;
import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;
import static java.util.Collections.emptyMap;

public final class WritableDataLayoutSpecificationAndTargetTriple extends AbstractToString implements Writable, DataLayoutSpecification
{
	private static final int EightBits = 8;
	private static final int SixteenBits = 16;
	private static final int ThirtyTwoBits = 32;
	private static final int SixtyFourBits = 64;
	private static final int OneHundredAndTwentyEightBits = 128;

	@SuppressWarnings("SpellCheckingInspection") @NotNull private static final byte[] TargetDataLayoutStart = encodeToUtf8ByteArrayWithCertaintyValueIsValid("target datalayout = \"");
	@SuppressWarnings("HardcodedLineSeparator") @NotNull private static final byte[] TargetDataLayoutEnd = encodeToUtf8ByteArrayWithCertaintyValueIsValid("\"\n");

	@NotNull private static final Endianness DefaultEndianness = BigEndian;
	private static final int DefaultStackAlignmentSize = 0;
	private static final int OneHundredAndTwentyEightBitStackAlignment = OneHundredAndTwentyEightBits;
	@NotNull private static final Map<AddressSpace, PointerSizing> DefaultAlternativeAddressSpacePointerSizings = emptyMap();
	@NotNull private static final Alignment DefaultInt1Alignment = EightBitAlignment;
	@NotNull private static final Alignment DefaultInt8Alignment = EightBitAlignment;
	@NotNull private static final Alignment DefaultInt16Alignment = SixteenBitAlignment;
	@NotNull private static final Alignment DefaultInt32Alignment = ThirtyTwoBitAlignment;
	@NotNull private static final Alignment DefaultInt64Alignment = new Alignment(ThirtyTwoBits, SixtyFourBits);
	@NotNull private static final Alignment DefaultHalfAlignment = SixteenBitAlignment;
	@NotNull private static final Alignment DefaultFloatAlignment = ThirtyTwoBitAlignment;
	@NotNull private static final Alignment DefaultQuadAlignment = OneHundredAndTwentyBitAlignment;
	@NotNull private static final Alignment DefaultVector64Alignment = SixtyFourBitAlignment;
	@NotNull private static final Alignment DefaultVector128Alignment = OneHundredAndTwentyBitAlignment;
	@NotNull private static final Alignment DefaultAggregateAlignment = new Alignment(0, SixtyFourBits);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple MacOsXMavericksOnX86_64 = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, OneHundredAndTwentyEightBitStackAlignment, DefaultAlternativeAddressSpacePointerSizings, DefaultInt1Alignment, DefaultInt8Alignment, DefaultInt16Alignment, DefaultInt32Alignment, SixtyFourBitAlignment, null, DefaultHalfAlignment, DefaultFloatAlignment, DefaultQuadAlignment, DefaultVector64Alignment, DefaultVector128Alignment, DefaultAggregateAlignment, MachO, x86_64, LP64LinuxNetBsdMacOsX86_64, MacOsXMavericks, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple MacOsXYosemiteOnX86_64 = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, OneHundredAndTwentyEightBitStackAlignment, DefaultAlternativeAddressSpacePointerSizings, DefaultInt1Alignment, DefaultInt8Alignment, DefaultInt16Alignment, DefaultInt32Alignment, SixtyFourBitAlignment, null, DefaultHalfAlignment, DefaultFloatAlignment, DefaultQuadAlignment, DefaultVector64Alignment, DefaultVector128Alignment, DefaultAggregateAlignment, MachO, x86_64, LP64LinuxNetBsdMacOsX86_64, MacOsXYosemite, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple MacOsXElCapitanOnX86_64 = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, OneHundredAndTwentyEightBitStackAlignment, DefaultAlternativeAddressSpacePointerSizings, DefaultInt1Alignment, DefaultInt8Alignment, DefaultInt16Alignment, DefaultInt32Alignment, SixtyFourBitAlignment, null, DefaultHalfAlignment, DefaultFloatAlignment, DefaultQuadAlignment, DefaultVector64Alignment, DefaultVector128Alignment, DefaultAggregateAlignment, MachO, x86_64, LP64LinuxNetBsdMacOsX86_64, MacOsXElCapitan, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnX86_64 = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, OneHundredAndTwentyEightBitStackAlignment, DefaultAlternativeAddressSpacePointerSizings, DefaultInt1Alignment, DefaultInt8Alignment, DefaultInt16Alignment, DefaultInt32Alignment, SixtyFourBitAlignment, null, DefaultHalfAlignment, DefaultFloatAlignment, DefaultQuadAlignment, DefaultVector64Alignment, DefaultVector128Alignment, DefaultAggregateAlignment, ELF, x86_64, LP64LinuxNetBsdMacOsX86_64, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnMips64LittleEndian = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, OneHundredAndTwentyEightBitStackAlignment, DefaultAlternativeAddressSpacePointerSizings, DefaultInt1Alignment, new Alignment(EightBits, ThirtyTwoBits), new Alignment(SixteenBits, ThirtyTwoBits), DefaultInt32Alignment, SixtyFourBitAlignment, null, DefaultHalfAlignment, DefaultFloatAlignment, DefaultQuadAlignment, DefaultVector64Alignment, DefaultVector128Alignment, DefaultAggregateAlignment, MIPS, mips64el, LP64LinuxMips64, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnMips64BigEndian = new WritableDataLayoutSpecificationAndTargetTriple(DefaultEndianness, OneHundredAndTwentyEightBitStackAlignment, DefaultAlternativeAddressSpacePointerSizings, DefaultInt1Alignment, new Alignment(EightBits, ThirtyTwoBits), new Alignment(SixteenBits, ThirtyTwoBits), DefaultInt32Alignment, SixtyFourBitAlignment, null, DefaultHalfAlignment, DefaultFloatAlignment, DefaultQuadAlignment, DefaultVector64Alignment, DefaultVector128Alignment, DefaultAggregateAlignment, MIPS, mips64, LP64LinuxMips64, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnAArch64LittleEndian = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, OneHundredAndTwentyEightBitStackAlignment, DefaultAlternativeAddressSpacePointerSizings, DefaultInt1Alignment, DefaultInt8Alignment, DefaultInt16Alignment, DefaultInt32Alignment, SixtyFourBitAlignment, OneHundredAndTwentyBitAlignment, DefaultHalfAlignment, DefaultFloatAlignment, DefaultQuadAlignment, DefaultVector64Alignment, DefaultVector128Alignment, DefaultAggregateAlignment, ELF, aarch64, LP64LinuxOther, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnAArch64BigEndian = new WritableDataLayoutSpecificationAndTargetTriple(DefaultEndianness, OneHundredAndTwentyEightBitStackAlignment, DefaultAlternativeAddressSpacePointerSizings, DefaultInt1Alignment, DefaultInt8Alignment, DefaultInt16Alignment, DefaultInt32Alignment, SixtyFourBitAlignment, OneHundredAndTwentyBitAlignment, DefaultHalfAlignment, DefaultFloatAlignment, DefaultQuadAlignment, DefaultVector64Alignment, DefaultVector128Alignment, DefaultAggregateAlignment, ELF, aarch64_be, LP64LinuxOther, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnPowerPC64LittleEndian = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, DefaultStackAlignmentSize, DefaultAlternativeAddressSpacePointerSizings, DefaultInt1Alignment, DefaultInt8Alignment, DefaultInt16Alignment, DefaultInt32Alignment, SixtyFourBitAlignment, null, DefaultHalfAlignment, DefaultFloatAlignment, DefaultQuadAlignment, DefaultVector64Alignment, DefaultVector128Alignment, DefaultAggregateAlignment, ELF, ppc64le, LP64LinuxOther, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnPowerPC64BigEndian = new WritableDataLayoutSpecificationAndTargetTriple(DefaultEndianness, DefaultStackAlignmentSize, DefaultAlternativeAddressSpacePointerSizings, DefaultInt1Alignment, DefaultInt8Alignment, DefaultInt16Alignment, DefaultInt32Alignment, SixtyFourBitAlignment, null, DefaultHalfAlignment, DefaultFloatAlignment, DefaultQuadAlignment, DefaultVector64Alignment, DefaultVector128Alignment, DefaultAggregateAlignment, ELF, ppc64, LP64LinuxPowerPcLegacy, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnI686 = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, OneHundredAndTwentyEightBitStackAlignment, DefaultAlternativeAddressSpacePointerSizings, DefaultInt1Alignment, DefaultInt8Alignment, DefaultInt16Alignment, DefaultInt32Alignment, SixtyFourBitAlignment, null, DefaultHalfAlignment, DefaultFloatAlignment, DefaultQuadAlignment, DefaultVector64Alignment, DefaultVector128Alignment, DefaultAggregateAlignment, ELF, i686, ILP32LinuxX86, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnArmV7Eabi = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, SixtyFourBits, DefaultAlternativeAddressSpacePointerSizings, DefaultInt1Alignment, DefaultInt8Alignment, DefaultInt16Alignment, DefaultInt32Alignment, SixtyFourBitAlignment, null, DefaultHalfAlignment, DefaultFloatAlignment, DefaultQuadAlignment, DefaultVector64Alignment, new Alignment(SixtyFourBits, OneHundredAndTwentyEightBits), new Alignment(0, ThirtyTwoBits), ELF, armv7, ILP32LinuxOther, Linux, Intel, "eabi");

	@NotNull private final Endianness endianness;
	@NotNull private final byte[] stackAlignmentSizeInBits;
	@NotNull private final Map<AddressSpace, PointerSizing> alternativeAddressSpacePointerSizings;
	@NotNull private final Alignment int1Alignment;
	@NotNull private final Alignment int8Alignment;
	@NotNull private final Alignment int16Alignment;
	@NotNull private final Alignment int32Alignment;
	@NotNull private final Alignment int64Alignment;
	@Nullable private final Alignment int128Alignment;
	@NotNull private final Alignment halfAlignment;
	@NotNull private final Alignment floatAlignment;
	@NotNull private final Alignment quadAlignment;
	@NotNull private final Alignment vector64Alignment;
	@NotNull private final Alignment vector128Alignment;
	@NotNull private final Alignment aggregateTypeAlignment;
	@NotNull private final SymbolMangling symbolMangling;
	@NotNull private final Architecture architecture;
	@NotNull private final CommonCDataModel commonCDataModel;
	@NotNull private final CxxNameMangling cxxNameMangling;
	private final int minimumAlignmentInBits;
	@NotNull private final TargetTriple targetTriple;
	@NotNull private final ObjectFileFormat objectFileFormat;

	private WritableDataLayoutSpecificationAndTargetTriple(@NotNull final Endianness endianness, final int stackAlignmentSizeInBits, @NotNull final Map<AddressSpace, PointerSizing> alternativeAddressSpacePointerSizings, @NotNull final Alignment int1Alignment, @NotNull final Alignment int8Alignment, @NotNull final Alignment int16Alignment, @NotNull final Alignment int32Alignment, @NotNull final Alignment int64Alignment, @Nullable final Alignment int128Alignment, @NotNull final Alignment halfAlignment, @NotNull final Alignment floatAlignment, @NotNull final Alignment quadAlignment, @NotNull final Alignment vector64Alignment, @NotNull final Alignment vector128Alignment, @NotNull final Alignment aggregateTypeAlignment, @NotNull final SymbolMangling symbolMangling, @NotNull final Architecture architecture, @NotNull final CommonCDataModel commonCDataModel, @NotNull final TargetOperatingSystem operatingSystem, @NotNull final CxxNameMangling cxxNameMangling, @NonNls @Nullable final String environment)
	{
		if (stackAlignmentSizeInBits < 0)
		{
			throw new IllegalArgumentException(format("stackAlignmentSizeInBits '%1$s' can not be negative", stackAlignmentSizeInBits));
		}

		if (stackAlignmentSizeInBits > 0)
		{
			if (isNotAPowerOfTwoIfGreaterThanZero(stackAlignmentSizeInBits))
			{
				throw new IllegalStateException(format("stackAlignmentSizeInBits '%1$s' is not a power of two or zero", stackAlignmentSizeInBits));
			}
		}

		for (final AddressSpace addressSpace : alternativeAddressSpacePointerSizings.keySet())
		{
			if (addressSpace.isZero())
			{
				throw new IllegalArgumentException("Alternative address spaces can not be zero");
			}
		}

		this.endianness = endianness;
		this.stackAlignmentSizeInBits = encodeToUtf8ByteArrayWithCertaintyValueIsValid(Integer.toString(stackAlignmentSizeInBits));
		this.alternativeAddressSpacePointerSizings = alternativeAddressSpacePointerSizings;
		this.int1Alignment = int1Alignment;
		this.int8Alignment = int8Alignment;
		this.int16Alignment = int16Alignment;
		this.int32Alignment = int32Alignment;
		this.int64Alignment = int64Alignment;
		this.int128Alignment = int128Alignment;
		this.halfAlignment = halfAlignment;
		this.floatAlignment = floatAlignment;
		this.quadAlignment = quadAlignment;
		this.vector64Alignment = vector64Alignment;
		this.vector128Alignment = vector128Alignment;
		this.aggregateTypeAlignment = aggregateTypeAlignment;
		this.symbolMangling = symbolMangling;
		this.architecture = architecture;
		this.commonCDataModel = commonCDataModel;
		this.cxxNameMangling = cxxNameMangling;
		minimumAlignmentInBits = commonCDataModel.minimumAlignmentInBits();
		targetTriple = new TargetTriple(architecture, operatingSystem, environment);
		objectFileFormat = symbolMangling.objectFileFormat;
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(endianness, stackAlignmentSizeInBits, alternativeAddressSpacePointerSizings, int1Alignment, int8Alignment, int16Alignment, int32Alignment, int64Alignment, int128Alignment, halfAlignment, floatAlignment, quadAlignment, vector64Alignment, vector128Alignment, aggregateTypeAlignment, symbolMangling, architecture, commonCDataModel, cxxNameMangling, minimumAlignmentInBits, targetTriple, objectFileFormat);
	}

	@NotNull
	public CxxNameMangling cxxNameMangling()
	{
		return cxxNameMangling;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(TargetDataLayoutStart);

		byteWriter.writeUtf8EncodedStringWithCertainty(endianness.dataLayoutEncoding);
		writeField(byteWriter, "S", stackAlignmentSizeInBits);
		architecture.write(byteWriter, this);
		writePointerSizingField(byteWriter, GlobalAddressSpace, commonCDataModel.pointerSizing);
		for (final Entry<AddressSpace, PointerSizing> entry : alternativeAddressSpacePointerSizings.entrySet())
		{
			writePointerSizingField(byteWriter, entry.getKey(), entry.getValue());
		}
		int1Alignment.writeAlignmentField(byteWriter, "i1");
		int8Alignment.writeAlignmentField(byteWriter, "i8");
		int16Alignment.writeAlignmentField(byteWriter, "i16");
		int32Alignment.writeAlignmentField(byteWriter, "i32");
		int64Alignment.writeAlignmentField(byteWriter, "i64");
		if (int128Alignment != null)
		{
			int128Alignment.writeAlignmentField(byteWriter, "i128");
		}
		halfAlignment.writeAlignmentField(byteWriter, "f16");
		floatAlignment.writeAlignmentField(byteWriter, "f32");
		commonCDataModel.doubleAlignment.writeAlignmentField(byteWriter, "f64");
		if (commonCDataModel.doesDataModelSupportEightyBitPrecisionFloatingPoint())
		{
			commonCDataModel.longDoubleAlignment().writeAlignmentField(byteWriter, "f80");
		}
		quadAlignment.writeAlignmentField(byteWriter, "f128");
		vector64Alignment.writeAlignmentField(byteWriter, "v64");
		vector128Alignment.writeAlignmentField(byteWriter, "v128");
		aggregateTypeAlignment.writeAlignmentField(byteWriter, "a");
		symbolMangling.write(byteWriter, this);

		byteWriter.writeBytes(TargetDataLayoutEnd);

		targetTriple.write(byteWriter);
	}

	private static <X extends Exception> void writePointerSizingField(@NotNull final ByteWriter<X> byteWriter, @NotNull final AddressSpace addressSpace, @NotNull final PointerSizing pointerSizing) throws X
	{
		pointerSizing.writeAlignmentField(byteWriter, addressSpace.pointerTargetLayoutPrefix(pointerSizing.storageSizeInBits()));
	}

	@Override
	public int aggregateTypeAbiAlignmentInBits()
	{
		return aggregateTypeAlignment.abiAlignmentInBits(minimumAlignmentInBits);
	}

	@Override
	public int int1AbiAlignmentInBits()
	{
		return int1Alignment.abiAlignmentInBits(minimumAlignmentInBits);
	}

	@Override
	public int int8AbiAlignmentInBits()
	{
		return int8Alignment.abiAlignmentInBits(minimumAlignmentInBits);
	}

	@Override
	public int int16AbiAlignmentInBits()
	{
		return int16Alignment.abiAlignmentInBits(minimumAlignmentInBits);
	}

	@Override
	public int int32AbiAlignmentInBits()
	{
		return int32Alignment.abiAlignmentInBits(minimumAlignmentInBits);
	}
	
	@Override
	public int int64AbiAlignmentInBits()
	{
		return int64Alignment.abiAlignmentInBits(minimumAlignmentInBits);
	}

	@Override
	public int int128AbiAlignmentInBits()
	{
		return int128Alignment == null ? OneHundredAndTwentyEightBits : int128Alignment.abiAlignmentInBits(minimumAlignmentInBits);
	}

	@Override
	public int halfAbiAlignmentInBits()
	{
		return halfAlignment.abiAlignmentInBits(minimumAlignmentInBits);
	}

	@Override
	public int floatAbiAlignmentInBits()
	{
		return floatAlignment.abiAlignmentInBits(minimumAlignmentInBits);
	}

	@Override
	public int doubleAbiAlignmentInBits()
	{
		return commonCDataModel.doubleAlignment.abiAlignmentInBits(minimumAlignmentInBits);
	}

	@Override
	public int longDoubleAbiAlignmentInBits()
	{
		return commonCDataModel.longDoubleAlignment().abiAlignmentInBits(minimumAlignmentInBits);
	}
	
	@Override
	public int quadAbiAlignmentInBits()
	{
		return quadAlignment.abiAlignmentInBits(minimumAlignmentInBits);
	}

	@Override
	@NotNull
	public ObjectFileFormat objectFileFormat()
	{
		return objectFileFormat;
	}

	@Override
	public boolean doesInt128RequireExplicitPadding()
	{
		return int128Alignment == null;
	}

	@Override
	public int pointerStorageSizeInBits()
	{
		return commonCDataModel.pointerStorageSizeInBits();
	}

	@Override
	public int pointerAbiAlignmentInBits()
	{
		return commonCDataModel.pointerAbiAlignmentInBits(minimumAlignmentInBits);
	}

	@NotNull
	@Override
	public <T> T chooseForArchitecture(@NotNull final Map<Architecture, T> knownValues, @NotNull final T defaultValue)
	{
		return knownValues.getOrDefault(architecture, defaultValue);
	}

	@Override
	public int longDoubleStorageSizeInBits()
	{
		return commonCDataModel.longDoubleStorageSizeInBits();
	}

	@NotNull
	@Override
	public Alignment longDoubleAlignment()
	{
		return commonCDataModel.longDoubleAlignment();
	}

	@Override
	public boolean doesDataModelSupportEightyBitPrecisionFloatingPoint()
	{
		return commonCDataModel.doesDataModelSupportEightyBitPrecisionFloatingPoint();
	}

	@NotNull
	@Override
	public <T> T chooseLongDouble(@NotNull final T doubleType, @NotNull final T x86BitDoubleType, @NotNull final T powerPcDoubleDoubleType, @NotNull final T quadDoubleType, @NotNull final T mipsLongDoubleType)
	{
		return commonCDataModel.chooseLongDouble(doubleType, x86BitDoubleType, powerPcDoubleDoubleType, quadDoubleType, mipsLongDoubleType);
	}

	@NotNull
	@Override
	public <T> T chooseShortSignedAndUnsigned(@NotNull final T sixteen, @NotNull final T thirtyTwo, @NotNull final T sixtyFour)
	{
		return commonCDataModel.chooseShortSignedAndUnsigned(sixteen, thirtyTwo, sixtyFour);
	}

	@NotNull
	@Override
	public <T> T chooseIntSignedAndUnsigned(@NotNull final T sixteen, @NotNull final T thirtyTwo, @NotNull final T sixtyFour)
	{
		return commonCDataModel.chooseIntSignedAndUnsigned(sixteen, thirtyTwo, sixtyFour);
	}

	@NotNull
	@Override
	public <T> T chooseLongIntSignedAndUnsigned(@NotNull final T sixteen, @NotNull final T thirtyTwo, @NotNull final T sixtyFour)
	{
		return commonCDataModel.chooseLongIntSignedAndUnsigned(sixteen, thirtyTwo, sixtyFour);
	}

	@NotNull
	@Override
	public <T> T chooseWideCharT(@NotNull final T sixteen, @NotNull final T thirtyTwo, @NotNull final T sixtyFour)
	{
		return commonCDataModel.chooseWideCharT(sixteen, thirtyTwo, sixtyFour);
	}

	@NotNull
	@Override
	public String determineCNameThatClosestMatchesAnUnsigned16BitInteger(@NotNull @NonNls final String longIntName, @NotNull @NonNls final String intName)
	{
		return commonCDataModel.determineCNameThatClosestMatchesAnUnsigned16BitInteger(longIntName, intName);
	}

	@Override
	public boolean isCShortSizeNotSixteenBits()
	{
		return commonCDataModel.isCShortSizeNotSixteenBits();
	}
}
