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
import org.jetbrains.annotations.*;

import static com.stormmq.llvm.domain.AddressSpace.GlobalAddressSpace;
import static com.stormmq.llvm.domain.target.Alignment.*;
import static com.stormmq.llvm.domain.target.Alignments.isDoubleAlignmentLlvmDefault;
import static com.stormmq.llvm.domain.target.CLongDoublePermutation.*;
import static com.stormmq.llvm.domain.target.PointerSizing.SixtyFourBitPointerSizing;
import static com.stormmq.llvm.domain.target.PointerSizing.ThirtyTwoBitPointerSizing;
import static com.stormmq.llvm.domain.target.Sizing.Sixteen;
import static com.stormmq.llvm.domain.target.Sizing.SixtyFour;
import static com.stormmq.llvm.domain.target.Sizing.ThirtyTwo;

public enum CommonCDataModel implements CDataModel
{
	LP32Windows(Sixteen, ThirtyTwo, ThirtyTwo, ThirtyTwoBitPointerSizing, Sixteen, SixtyFourBitLongDouble, SixtyFourBitAlignment), // Dead; partly guesswork
	ILP32LinuxX86(Sixteen, ThirtyTwo, ThirtyTwo, ThirtyTwoBitPointerSizing, ThirtyTwo, i686NinetySixBitLongDoubleClangAndGcc, ThirtyTwoBitAlignment),
	ILP32LinuxPowerPc(Sixteen, ThirtyTwo, ThirtyTwo, ThirtyTwoBitPointerSizing, ThirtyTwo, PowerPcDoubleDouble, ThirtyTwoBitAlignment),
	ILP32LinuxOther(Sixteen, ThirtyTwo, ThirtyTwo, ThirtyTwoBitPointerSizing, ThirtyTwo, QuadLongDouble, ThirtyTwoBitAlignment), // Not 100% sure of the long double settings
	ILP32Windows(Sixteen, ThirtyTwo, ThirtyTwo, ThirtyTwoBitPointerSizing, Sixteen, SixtyFourBitLongDouble, SixtyFourBitAlignment),

	IL32P64Windows(Sixteen, ThirtyTwo, SixtyFour, SixtyFourBitPointerSizing, Sixteen, SixtyFourBitLongDouble, SixtyFourBitAlignment),
	LP64LinuxNetBsdMacOsX86_64(Sixteen, ThirtyTwo, SixtyFour, SixtyFourBitPointerSizing, ThirtyTwo, X86_64LongDouble, SixtyFourBitAlignment), // aka I32LP64
	LP64LinuxMips64(Sixteen, ThirtyTwo, SixtyFour, SixtyFourBitPointerSizing, ThirtyTwo, Mips64LongDouble, SixtyFourBitAlignment), // aka I32LP64
	LP64LinuxOther(Sixteen, ThirtyTwo, SixtyFour, SixtyFourBitPointerSizing, ThirtyTwo, QuadLongDouble, SixtyFourBitAlignment), // aka I32LP64
	LP64LinuxPowerPcLegacy(Sixteen, ThirtyTwo, SixtyFour, SixtyFourBitPointerSizing, ThirtyTwo, PowerPcDoubleDouble, SixtyFourBitAlignment),
	ILP64(Sixteen, SixtyFour, SixtyFour, SixtyFourBitPointerSizing, ThirtyTwo, SixtyFourBitLongDouble, SixtyFourBitAlignment), // No known users, here for completeness.
	@SuppressWarnings("SpellCheckingInspection")SILP64(SixtyFour, SixtyFour, SixtyFour, SixtyFourBitPointerSizing, SixtyFour, SixtyFourBitLongDouble, SixtyFourBitAlignment), // wchar_t and long double are guesses. Old UNICOS model. Here for completeness.
	;

	@NotNull private final Sizing cShortSize;
	@NotNull private final Sizing cIntSize;
	@NotNull private final Sizing cLongSize;
	@NotNull private final PointerSizing pointerSizing;
	@NotNull public final Sizing cWideCharT;
	@NotNull private final CLongDoublePermutation cLongDoublePermutation;
	@NotNull public final Alignment doubleAlignment;

	CommonCDataModel(@NotNull final Sizing cShortSize, @NotNull final Sizing cIntSize, @NotNull final Sizing cLongSize, @NotNull final PointerSizing pointerSizing, @NotNull final Sizing cWideCharT, @NotNull final CLongDoublePermutation cLongDoublePermutation, @NotNull final Alignment doubleAlignment)
	{
		if (cLongSize.isLessThanThirtyTwo())
		{
			throw new IllegalArgumentException("cLongSize can not be less than ThirtyTwo");
		}

		this.cShortSize = cShortSize;
		this.cIntSize = cIntSize;
		this.cLongSize = cLongSize;
		this.pointerSizing = pointerSizing;
		this.cWideCharT = cWideCharT;
		this.cLongDoublePermutation = cLongDoublePermutation;
		this.doubleAlignment = doubleAlignment;
	}

	@NotNull
	public Alignment longDoubleAbiAlignment()
	{
		return cLongDoublePermutation.alignment;
	}

	@Override
	public int longDoubleStorageSizeInBits()
	{
		return cLongDoublePermutation.storageSizeInBits;
	}

	@Override
	public int pointerStorageSizeInBits()
	{
		return pointerSizing.storageSizeInBits();
	}

	public int pointerAbiAlignmentInBits(final int minimumAlignmentInBits)
	{
		return pointerSizing.abiAlignmentInBits(minimumAlignmentInBits);
	}

	@Override
	@NotNull
	public <T> T chooseLongDouble(@NotNull final T doubleType, @NotNull final T x86BitDoubleType, @NotNull final T powerPcDoubleDoubleType, @NotNull final T quadDoubleType, @NotNull final T mipsLongDoubleType)
	{
		return cLongDoublePermutation.choose(doubleType, x86BitDoubleType, powerPcDoubleDoubleType, quadDoubleType, mipsLongDoubleType);
	}

	@Override
	@NotNull
	public <T> T chooseShortSignedAndUnsigned(@NotNull final T sixteen, @NotNull final T thirtyTwo, @NotNull final T sixtyFour)
	{
		return choose(cShortSize, sixteen, thirtyTwo, sixtyFour);
	}

	@Override
	@NotNull
	public <T> T chooseIntSignedAndUnsigned(@NotNull final T sixteen, @NotNull final T thirtyTwo, @NotNull final T sixtyFour)
	{
		return choose(cIntSize, sixteen, thirtyTwo, sixtyFour);
	}

	@Override
	@NotNull
	public <T> T chooseLongIntSignedAndUnsigned(@NotNull final T sixteen, @NotNull final T thirtyTwo, @NotNull final T sixtyFour)
	{
		return choose(cLongSize, sixteen, thirtyTwo, sixtyFour);
	}

	@Override
	@NotNull
	public <T> T chooseWideCharT(@NotNull final T sixteen, @NotNull final T thirtyTwo, @NotNull final T sixtyFour)
	{
		return choose(cWideCharT, sixteen, thirtyTwo, sixtyFour);
	}

	@NotNull
	private static <T> T choose(final Sizing cShortSize, @NotNull final T sixteen, @NotNull final T thirtyTwo, @NotNull final T sixtyFour)
	{
		return cShortSize.choose(sixteen, thirtyTwo, sixtyFour);
	}

	@Override
	@NonNls
	@NotNull
	public String determineCNameThatClosestMatchesAnUnsigned16BitInteger(@NotNull @NonNls final String longIntName, @NotNull @NonNls final String intName)
	{
		if (isCIntSizeSixteenBits())
		{
			if (isCLongSizeNotThirtyTwoBits())
			{
				throw new UnsupportedOperationException("The data model does not support a signed 32-bit integer in a compatible way (it uses 16-bit integers)");
			}
			return longIntName;
		}
		if (isCIntSizeNotThirtyTwoBits())
		{
			throw new UnsupportedOperationException("The data model does not support a signed 32-bit integer in a compatible way (it uses 64-bit integers)");
		}
		return intName;
	}

	@Override
	public boolean isCShortSizeNotSixteenBits()
	{
		return cShortSize.isNotSixteen();
	}

	private boolean isCIntSizeNotThirtyTwoBits()
	{
		return cIntSize.isNotThirtyTwo();
	}

	private boolean isCIntSizeSixteenBits()
	{
		return cIntSize.isSixteen();
	}

	private boolean isCLongSizeNotThirtyTwoBits()
	{
		return cLongSize.isNotThirtyTwo();
	}

	public <X extends Exception> void writeDoubleAlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		if (isDoubleAlignmentLlvmDefault(doubleAlignment))
		{
			return;
		}
		doubleAlignment.writeAlignmentField(byteWriter, "f64");
	}

	public <X extends Exception> void writeLongDoubleAlignmentIfHasEightyBitPrecision(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		if (cLongDoublePermutation.doesDataModelSupportEightyBitPrecisionFloatingPoint())
		{
			longDoubleAbiAlignment().writeAlignmentField(byteWriter, "f80");
		}
	}

	public <X extends Exception> void writeGlobalAddressSpacePointerSizingIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		if (pointerSizing.isDefaultForGlobalAddressSpace())
		{
			return;
		}
		pointerSizing.write(byteWriter, GlobalAddressSpace);
	}
}
