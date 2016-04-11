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

package com.stormmq.llvm.domain.target.dataLayout;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.AddressSpace;
import com.stormmq.llvm.domain.Writable;
import com.stormmq.llvm.domain.target.triple.Architecture;
import org.jetbrains.annotations.*;

import java.util.Map;
import java.util.Map.Entry;

import static com.stormmq.llvm.domain.AddressSpace.GlobalAddressSpace;
import static com.stormmq.llvm.domain.target.dataLayout.Endianness.BigEndian;
import static com.stormmq.llvm.domain.target.dataLayout.Endianness.LittleEndian;
import static com.stormmq.llvm.domain.target.dataLayout.SymbolMangling.MachO;
import static com.stormmq.llvm.domain.target.triple.Architecture.x86_64;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;
import static java.util.Collections.emptyMap;

public final class DataLayoutSpecification implements Writable
{
	public static final int SixteenBits = 16;
	public static final int ThirtyTwoBits = 32;
	public static final int SixtyFourBits = 64;
	public static final int EightyBits = 80;
	public static final int OneHundredAndTwentyEightBits = 128;

	@SuppressWarnings("SpellCheckingInspection") @NotNull private static final byte[] TargetDataLayoutStart = encodeUtf8BytesWithCertaintyValueIsValid("target datalayout = \"");
	@SuppressWarnings("HardcodedLineSeparator") @NotNull private static final byte[] End = encodeUtf8BytesWithCertaintyValueIsValid("\"\n");

	@NotNull private static final Endianness DefaultEndianness = BigEndian; // Mach OS X is LittleEndian
	private static final int DefaultStackAlignmentSizeInBytes = 0; // Mac OS X is 16
	@NotNull private static final PointerSizing DefaultPointerSizing = new PointerSizing(64, new Alignment(64));
	@NotNull private static final Map<AddressSpace, PointerSizing> DefaultAlternativeAddressSpacePointerSizings = emptyMap();
	@NotNull private static final Alignment DefaultBooleanAlignment = new Alignment(8);
	@NotNull private static final Alignment DefaultByteAlignment = new Alignment(8);
	@NotNull private static final Alignment DefaultShortAlignment = new Alignment(16);
	@NotNull private static final Alignment DefaultIntAlignment = new Alignment(32);
	@NotNull private static final Alignment DefaultLongAlignment = new Alignment(32, 64); // Mac OS X is 64, 64
	@NotNull private static final Alignment DefaultHalfAlignment = new Alignment(16);
	@NotNull private static final Alignment DefaultFloatAlignment = new Alignment(32);
	@NotNull private static final Alignment DefaultDoubleAlignment = new Alignment(64);
	@NotNull private static final Alignment DefaultQuadAlignment = new Alignment(128);
	@NotNull private static final Alignment DefaultVector64Alignment = new Alignment(64);
	@NotNull private static final Alignment DefaultVector128Alignment = new Alignment(128);
	@NotNull private static final Alignment DefaultAggregateAlignment = new Alignment(0, 64);
	@Nullable public static final SymbolMangling DefaultSymbolMangling = null; // Mac OS X is MachO

	@NotNull public static final DataLayoutSpecification DarwinOnX86_64 = new DataLayoutSpecification(LittleEndian, 16, DefaultPointerSizing, DefaultAlternativeAddressSpacePointerSizings, DefaultBooleanAlignment, DefaultByteAlignment, DefaultShortAlignment, DefaultIntAlignment, new Alignment(64, 64), DefaultHalfAlignment, DefaultFloatAlignment, DefaultDoubleAlignment, new Alignment(128), DefaultQuadAlignment, DefaultVector64Alignment, DefaultVector128Alignment, DefaultAggregateAlignment, MachO, x86_64, false);

	@NotNull private final Endianness endianness;
	@NotNull private final String stackAlignmentSizeInBits;
	@NotNull private final PointerSizing globalAddressSpacePointerSizing;
	@NotNull private final Map<AddressSpace, PointerSizing> alternativeAddressSpacePointerSizings;
	@NotNull private final Alignment booleanAlignment;
	@NotNull private final Alignment byteAlignment;
	@NotNull private final Alignment shortAlignment;
	@NotNull private final Alignment intAlignment;
	@NotNull private final Alignment longAlignment;
	@NotNull private final Alignment halfAlignment;
	@NotNull private final Alignment floatAlignment;
	@NotNull private final Alignment doubleAlignment;
	@NotNull private final Alignment longDoubleAlignment;
	@NotNull private final Alignment quadAlignment;
	@NotNull private final Alignment vector64Alignment;
	@NotNull private final Alignment vector128Alignment;
	@NotNull private final Alignment aggregateTypeAlignment;
	@NotNull private final SymbolMangling symbolMangling;
	@NotNull private final Architecture architecture;
	private final boolean wideCharIsWindows;

	private DataLayoutSpecification(@NotNull final Endianness endianness, final int stackAlignmentSizeInBytes, @NotNull final PointerSizing globalAddressSpacePointerSizing, @NotNull final Map<AddressSpace, PointerSizing> alternativeAddressSpacePointerSizings, @NotNull final Alignment booleanAlignment, @NotNull final Alignment byteAlignment, @NotNull final Alignment shortAlignment, @NotNull final Alignment intAlignment, @NotNull final Alignment longAlignment, @NotNull final Alignment halfAlignment, @NotNull final Alignment floatAlignment, @NotNull final Alignment doubleAlignment, @NotNull final Alignment longDoubleAlignment, @NotNull final Alignment quadAlignment, @NotNull final Alignment vector64Alignment, @NotNull final Alignment vector128Alignment, @NotNull final Alignment aggregateTypeAlignment, @NotNull final SymbolMangling symbolMangling, @NotNull final Architecture architecture, final boolean wideCharIsWindows)
	{
		if (stackAlignmentSizeInBytes < 0)
		{
			throw new IllegalArgumentException("stackAlignmentSizeInBytes can not be negative");
		}

		for (final AddressSpace addressSpace : alternativeAddressSpacePointerSizings.keySet())
		{
			if (addressSpace.isZero())
			{
				throw new IllegalArgumentException("Alternative address spaces can not be zero");
			}
		}

		this.endianness = endianness;
		stackAlignmentSizeInBits = Integer.toString(stackAlignmentSizeInBytes << 3);
		this.globalAddressSpacePointerSizing = globalAddressSpacePointerSizing;
		this.alternativeAddressSpacePointerSizings = alternativeAddressSpacePointerSizings;
		this.booleanAlignment = booleanAlignment;
		this.byteAlignment = byteAlignment;
		this.shortAlignment = shortAlignment;
		this.intAlignment = intAlignment;
		this.longAlignment = longAlignment;
		this.halfAlignment = halfAlignment;
		this.floatAlignment = floatAlignment;
		this.doubleAlignment = doubleAlignment;
		this.longDoubleAlignment = longDoubleAlignment;
		this.quadAlignment = quadAlignment;
		this.vector64Alignment = vector64Alignment;
		this.vector128Alignment = vector128Alignment;
		this.aggregateTypeAlignment = aggregateTypeAlignment;
		this.symbolMangling = symbolMangling;
		this.architecture = architecture;
		this.wideCharIsWindows = wideCharIsWindows;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(TargetDataLayoutStart);

		byteWriter.writeUtf8EncodedStringWithCertainty(endianness.dataLayoutEncoding);
		writeField(byteWriter, "S", stackAlignmentSizeInBits);
		writeField(byteWriter, "n", architecture.nativeIntegerWidths);
		writePointerSizingField(byteWriter, GlobalAddressSpace, globalAddressSpacePointerSizing);
		for (final Entry<AddressSpace, PointerSizing> entry : alternativeAddressSpacePointerSizings.entrySet())
		{
			writePointerSizingField(byteWriter, entry.getKey(), entry.getValue());
		}
		writeField(byteWriter, "i1", booleanAlignment.dataLayoutEncoding);
		writeField(byteWriter, "i8", byteAlignment.dataLayoutEncoding);
		writeField(byteWriter, "i16", shortAlignment.dataLayoutEncoding);
		writeField(byteWriter, "i32", intAlignment.dataLayoutEncoding);
		writeField(byteWriter, "i64", longAlignment.dataLayoutEncoding);
		// i128 ?
		writeField(byteWriter, "f16", halfAlignment.dataLayoutEncoding);
		writeField(byteWriter, "f32", floatAlignment.dataLayoutEncoding);
		writeField(byteWriter, "f64", doubleAlignment.dataLayoutEncoding);
		writeField(byteWriter, "f80", longDoubleAlignment.dataLayoutEncoding);
		writeField(byteWriter, "f128", quadAlignment.dataLayoutEncoding);
		writeField(byteWriter, "v64", vector64Alignment.dataLayoutEncoding);
		writeField(byteWriter, "v128", vector128Alignment.dataLayoutEncoding);
		writeField(byteWriter, "a", aggregateTypeAlignment.dataLayoutEncoding);
		writeField(byteWriter, "m:", symbolMangling.dataLayoutEncoding);

		byteWriter.writeBytes(End);
	}

	private static <X extends Exception> void writePointerSizingField(@NotNull final ByteWriter<X> byteWriter, @NotNull final AddressSpace addressSpace, @NotNull final PointerSizing pointerSizing) throws X
	{
		writeField(byteWriter, addressSpace.pointerTargetLayoutPrefix(pointerSizing.sizeInBits), pointerSizing.alignment.dataLayoutEncoding);
	}

	private static <X extends Exception> void writeField(@NotNull final ByteWriter<X> byteWriter, @NotNull @NonNls final String dataLayoutEncodingA, @NotNull @NonNls final String dataLayoutEncodingB) throws X
	{
		byteWriter.writeHyphen();
		byteWriter.writeUtf8EncodedStringWithCertainty(dataLayoutEncodingA);
		byteWriter.writeUtf8EncodedStringWithCertainty(dataLayoutEncodingB);
	}

	public int booleanSizeInBitsRoundedUpToNearestEightBits()
	{
		return 8;
	}

	public int booleanAlignmentInBits()
	{
		return booleanAlignment.abiAlignmentInBits;
	}

	public int byteSizeInBits()
	{
		return 8;
	}

	public int byteAlignmentInBits()
	{
		return byteAlignment.abiAlignmentInBits;
	}

	public int shortSizeInBits()
	{
		return SixteenBits;
	}
	
	public int shortAlignmentInBits()
	{
		return shortAlignment.abiAlignmentInBits;
	}

	public int intSizeInBits()
	{
		return ThirtyTwoBits;
	}
	
	public int intAlignmentInBits()
	{
		return intAlignment.abiAlignmentInBits;
	}
	
	public int longAlignmentInBits()
	{
		return longAlignment.abiAlignmentInBits;
	}

	public int int128SizeInBits()
	{
		return OneHundredAndTwentyEightBits;
	}

	public int int128AlignmentInBits()
	{
		return OneHundredAndTwentyEightBits;
	}

	public int halfSizeInBits()
	{
		return SixteenBits;
	}
	
	public int halfAlignmentInBits()
	{
		return halfAlignment.abiAlignmentInBits;
	}

	public int floatSizeInBits()
	{
		return ThirtyTwoBits;
	}
	
	public int floatAlignmentInBits()
	{
		return floatAlignment.abiAlignmentInBits;
	}

	public int doubleSizeInBits()
	{
		return SixtyFourBits;
	}
	
	public int doubleAlignmentInBits()
	{
		return doubleAlignment.abiAlignmentInBits;
	}

	// In clang, long double is debugged as 128 bits NOT 80 bits, even on x86; see also https://stackoverflow.com/questions/13525774/clang-and-float128-bug-error/17056045
	public int longDoubleSizeInBits()
	{
		return OneHundredAndTwentyEightBits;
	}
	
	public int longDoubleAlignmentInBits()
	{
		return longDoubleAlignment.abiAlignmentInBits;
	}
	
	public int quadAlignmentInBits()
	{
		return quadAlignment.abiAlignmentInBits;
	}

	@NotNull
	public PointerSizing globalAddressSpacePointerSizing()
	{
		return globalAddressSpacePointerSizing;
	}

	public int wideCharSizeInBits()
	{
		return wideCharIsWindows ? SixteenBits : ThirtyTwoBits;
	}

	public int wideCharAlignmentInBits()
	{
		return wideCharIsWindows ? SixteenBits : ThirtyTwoBits;
	}
}
