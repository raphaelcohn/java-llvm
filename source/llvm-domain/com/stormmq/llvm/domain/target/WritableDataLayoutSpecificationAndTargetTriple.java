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
import java.util.function.*;

import static com.stormmq.llvm.domain.target.Alignments.*;
import static com.stormmq.llvm.domain.target.AlternativeAddressSpacePointerSizing.*;
import static com.stormmq.llvm.domain.target.StackAlignment.OneHundredAndTwentyEightBitsStackAlignment;
import static com.stormmq.llvm.domain.target.StackAlignment.SixtyFourBitsStackAlignment;
import static com.stormmq.llvm.domain.target.StackAlignment.Unspecified;
import static com.stormmq.llvm.domain.target.Architecture.*;
import static com.stormmq.llvm.domain.target.CommonCDataModel.*;
import static com.stormmq.llvm.domain.target.CxxNameMangling.Intel;
import static com.stormmq.llvm.domain.target.Endianness.BigEndian;
import static com.stormmq.llvm.domain.target.Endianness.LittleEndian;
import static com.stormmq.llvm.domain.target.SymbolMangling.ELF;
import static com.stormmq.llvm.domain.target.SymbolMangling.MIPS;
import static com.stormmq.llvm.domain.target.SymbolMangling.MachO;
import static com.stormmq.llvm.domain.target.TargetOperatingSystem.*;
import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;

public final class WritableDataLayoutSpecificationAndTargetTriple extends AbstractToString implements Writable, DataLayoutSpecification
{
	@SuppressWarnings("SpellCheckingInspection") @NotNull private static final byte[] TargetDataLayoutStart = encodeToUtf8ByteArrayWithCertaintyValueIsValid("target datalayout = \"");
	@SuppressWarnings("HardcodedLineSeparator") @NotNull private static final byte[] TargetDataLayoutEnd = encodeToUtf8ByteArrayWithCertaintyValueIsValid("\"\n");

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple MacOsXMavericksOnX86_64 = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, OneHundredAndTwentyEightBitsStackAlignment, DefaultAlternativeAddressSpacePointerSizing, x86_64Alignment, MachO, x86_64, LP64LinuxNetBsdMacOsX86_64, MacOsXMavericks, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple MacOsXYosemiteOnX86_64 = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, OneHundredAndTwentyEightBitsStackAlignment, DefaultAlternativeAddressSpacePointerSizing, x86_64Alignment, MachO, x86_64, LP64LinuxNetBsdMacOsX86_64, MacOsXYosemite, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple MacOsXElCapitanOnX86_64 = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, OneHundredAndTwentyEightBitsStackAlignment, DefaultAlternativeAddressSpacePointerSizing,x86_64Alignment, MachO, x86_64, LP64LinuxNetBsdMacOsX86_64, MacOsXElCapitan, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnX86_64 = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, OneHundredAndTwentyEightBitsStackAlignment, DefaultAlternativeAddressSpacePointerSizing, x86_64Alignment, ELF, x86_64, LP64LinuxNetBsdMacOsX86_64, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnMips64LittleEndian = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, OneHundredAndTwentyEightBitsStackAlignment, DefaultAlternativeAddressSpacePointerSizing, Mips64Alignment, MIPS, mips64el, LP64LinuxMips64, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnMips64BigEndian = new WritableDataLayoutSpecificationAndTargetTriple(BigEndian, OneHundredAndTwentyEightBitsStackAlignment, DefaultAlternativeAddressSpacePointerSizing, Mips64Alignment, MIPS, mips64, LP64LinuxMips64, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnAArch64LittleEndian = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, OneHundredAndTwentyEightBitsStackAlignment, DefaultAlternativeAddressSpacePointerSizing, AArch64Alignment, ELF, aarch64, LP64LinuxOther, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnAArch64BigEndian = new WritableDataLayoutSpecificationAndTargetTriple(BigEndian, OneHundredAndTwentyEightBitsStackAlignment, DefaultAlternativeAddressSpacePointerSizing, AArch64Alignment, ELF, aarch64_be, LP64LinuxOther, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnPowerPC64LittleEndian = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, Unspecified, DefaultAlternativeAddressSpacePointerSizing, PowerPc64Alignment, ELF, ppc64le, LP64LinuxOther, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnPowerPC64BigEndian = new WritableDataLayoutSpecificationAndTargetTriple(BigEndian, Unspecified, DefaultAlternativeAddressSpacePointerSizing, PowerPc64Alignment, ELF, ppc64, LP64LinuxPowerPcLegacy, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnI686 = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, OneHundredAndTwentyEightBitsStackAlignment, DefaultAlternativeAddressSpacePointerSizing, i866Alignment, ELF, i686, ILP32LinuxX86, Linux, Intel, null);

	@NotNull public static final WritableDataLayoutSpecificationAndTargetTriple LinuxOnArmV7Eabi = new WritableDataLayoutSpecificationAndTargetTriple(LittleEndian, SixtyFourBitsStackAlignment, DefaultAlternativeAddressSpacePointerSizing, ArmV7Alignment, ELF, armv7, ILP32LinuxOther, Linux, Intel, "eabi");

	@SuppressWarnings("MethodCanBeVariableArityMethod")
	public static <X extends Exception> void writeField(@NotNull final ByteWriter<X> byteWriter, @NotNull @NonNls final String dataLayoutEncodingA, @NotNull @NonNls final byte[] dataLayoutEncodingB) throws X
	{
		byteWriter.writeHyphen();
		byteWriter.writeUtf8EncodedStringWithCertainty(dataLayoutEncodingA);
		byteWriter.writeBytes(dataLayoutEncodingB);
	}

	@NotNull private final Endianness endianness;
	@NotNull private final StackAlignment stackAlignment;
	@NotNull private final AlternativeAddressSpacePointerSizing alternativeAddressSpacePointerSizings;
	@NotNull private final Alignments alignments;
	@NotNull private final SymbolMangling symbolMangling;
	@NotNull private final Architecture architecture;
	@NotNull private final CommonCDataModel commonCDataModel;
	@NotNull private final CxxNameMangling cxxNameMangling;
	private final int minimumAlignmentInBits;
	@NotNull private final TargetTriple targetTriple;
	@NotNull private final ObjectFileFormat objectFileFormat;

	private WritableDataLayoutSpecificationAndTargetTriple(@NotNull final Endianness endianness, @NotNull final StackAlignment stackAlignment, @NotNull final AlternativeAddressSpacePointerSizing alternativeAddressSpacePointerSizings, @NotNull final Alignments alignments, @NotNull final SymbolMangling symbolMangling, @NotNull final Architecture architecture, @NotNull final CommonCDataModel commonCDataModel, @NotNull final TargetOperatingSystem operatingSystem, @NotNull final CxxNameMangling cxxNameMangling, @NonNls @Nullable final String environment)
	{
		this.endianness = endianness;
		this.stackAlignment = stackAlignment;
		this.alternativeAddressSpacePointerSizings = alternativeAddressSpacePointerSizings;
		this.alignments = alignments;
		this.symbolMangling = symbolMangling;
		this.architecture = architecture;
		this.commonCDataModel = commonCDataModel;
		this.cxxNameMangling = cxxNameMangling;
		minimumAlignmentInBits = architecture.minimumAlignmentInBits();
		targetTriple = new TargetTriple(architecture, operatingSystem, environment);
		objectFileFormat = symbolMangling.objectFileFormat(operatingSystem.objectFileFormat());
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(endianness, stackAlignment, alternativeAddressSpacePointerSizings, alignments, symbolMangling, architecture, commonCDataModel, cxxNameMangling);
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
		endianness.write(byteWriter);
		stackAlignment.writeIfNotDefault(byteWriter);
		architecture.writeNativeIntegerWidths(byteWriter);
		commonCDataModel.writeGlobalAddressSpacePointerSizingIfNotDefault(byteWriter);
		alternativeAddressSpacePointerSizings.write(byteWriter);
		alignments.writeInt1AlignmentIfNotDefault(byteWriter);
		alignments.writeInt8AlignmentIfNotDefault(byteWriter);
		alignments.writeInt16AlignmentIfNotDefault(byteWriter);
		alignments.writeInt32AlignmentIfNotDefault(byteWriter);
		alignments.writeInt64AlignmentIfNotDefault(byteWriter);
		alignments.writeInt128AlignmentIfNotDefault(byteWriter);
		alignments.writeHalfAlignmentIfNotDefault(byteWriter);
		alignments.writeFloatAlignmentIfNotDefault(byteWriter);
		commonCDataModel.writeDoubleAlignmentIfNotDefault(byteWriter);
		commonCDataModel.writeLongDoubleAlignmentIfHasEightyBitPrecision(byteWriter);
		alignments.writeQuadAlignmentIfNotDefault(byteWriter);
		alignments.writeVector64AlignmentIfNotDefault(byteWriter);
		alignments.writeVector128AlignmentIfNotDefault(byteWriter);
		alignments.writeAggregateAlignmentIfNotDefault(byteWriter);
		symbolMangling.writeIfNotDefault(byteWriter);
		byteWriter.writeBytes(TargetDataLayoutEnd);

		targetTriple.write(byteWriter);
	}

	@Override
	public int pointerAbiAlignmentInBits()
	{
		return commonCDataModel.pointerAbiAlignmentInBits(minimumAlignmentInBits);
	}

	@Override
	public int int1AbiAlignmentInBits()
	{
		return abiAlignment(alignments::int1Alignment);
	}

	@Override
	public int int8AbiAlignmentInBits()
	{
		return abiAlignment(alignments::int8Alignment);
	}

	@Override
	public int int16AbiAlignmentInBits()
	{
		return abiAlignment(alignments::int16Alignment);
	}

	@Override
	public int int32AbiAlignmentInBits()
	{
		return abiAlignment(alignments::int32Alignment);
	}
	
	@Override
	public int int64AbiAlignmentInBits()
	{
		return abiAlignment(alignments::int64Alignment);
	}

	@Override
	public int int128AbiAlignmentInBits()
	{
		return abiAlignment(alignments::int128Alignment);
	}

	@Override
	public int halfAbiAlignmentInBits()
	{
		return abiAlignment(alignments::halfAlignment);
	}

	@Override
	public int floatAbiAlignmentInBits()
	{
		return abiAlignment(alignments::floatAlignment);
	}

	@Override
	public int doubleAbiAlignmentInBits()
	{
		return abiAlignment(alignments::doubleAlignment);
	}

	@Override
	public int longDoubleAbiAlignmentInBits()
	{
		return abiAlignment(commonCDataModel::longDoubleAbiAlignment);
	}
	
	@Override
	public int quadAbiAlignmentInBits()
	{
		return abiAlignment(alignments::quadAlignment);
	}

	@Override
	public int vector64AbiAlignmentInBits()
	{
		return abiAlignment(alignments::vector64Alignment);
	}

	@Override
	public int vector128AbiAlignmentInBits()
	{
		return abiAlignment(alignments::vector128Alignment);
	}

	@Override
	public int aggregateAbiAlignmentInBits()
	{
		return abiAlignment(alignments::aggregateAlignment);
	}

	private int abiAlignment(@NotNull final Supplier<Alignment> supplier)
	{
		return supplier.get().abiAlignmentInBits(minimumAlignmentInBits);
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
		return alignments.isInt128AlignmentUnspecified();
	}

	@Override
	public int pointerStorageSizeInBits()
	{
		return commonCDataModel.pointerStorageSizeInBits();
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
