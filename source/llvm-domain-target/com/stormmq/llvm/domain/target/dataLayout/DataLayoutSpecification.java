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
import com.stormmq.llvm.domain.Writable;
import com.stormmq.llvm.domain.target.triple.Architecture;
import org.jetbrains.annotations.*;

import static com.stormmq.llvm.domain.target.dataLayout.Endianness.BigEndian;
import static com.stormmq.llvm.domain.target.dataLayout.Endianness.LittleEndian;
import static com.stormmq.llvm.domain.target.dataLayout.Mangling.MachO;
import static com.stormmq.llvm.domain.target.triple.Architecture.x86_64;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class DataLayoutSpecification implements Writable
{
	@NotNull private static final byte[] TargetDataLayoutStart = encodeUtf8BytesWithCertaintyValueIsValid("target datalayout = \"");
	@SuppressWarnings("HardcodedLineSeparator") @NotNull private static final byte[] End = encodeUtf8BytesWithCertaintyValueIsValid("\"\n");
	private static final byte Hyphen = '-';

	@NotNull public static final Endianness DefaultEndianness = BigEndian; // Mach OS X is LittleEndian
	public static final int DefaultStackAlignmentSizeInBytes = 0; // Mac OS X is 16
	@NotNull public static final Sizing DefaultPointer64Sizing = new Sizing(64);
	@NotNull public static final Sizing DefaultBooleanSizing = new Sizing(8);
	@NotNull public static final Sizing DefaultByteSizing = new Sizing(8);
	@NotNull public static final Sizing DefaultShortSizing = new Sizing(16);
	@NotNull public static final Sizing DefaultIntSizing = new Sizing(32);
	@NotNull public static final Sizing DefaultLongSizing = new Sizing(32, 64); // Mac OS X is 64, 64
	@NotNull public static final Sizing DefaultHalfSizing = new Sizing(16);
	@NotNull public static final Sizing DefaultFloatSizing = new Sizing(32);
	@NotNull public static final Sizing DefaultDoubleSizing = new Sizing(64);
	// @NotNull public static final Sizing DefaultLongDoubleSizing = new Sizing(128); // Mac OS X is 128
	@NotNull public static final Sizing DefaultQuadSizing = new Sizing(128);
	@NotNull public static final Sizing DefaultVector64Sizing = new Sizing(64);
	@NotNull public static final Sizing DefaultVector128Sizing = new Sizing(128);
	@NotNull public static final Sizing DefaultAggregateSizing = new Sizing(0, 64);
	@Nullable public static final Mangling DefaultMangling = null; // Mac OS X is MachO

	@NotNull public static final DataLayoutSpecification DarwinOnX86_64 = new DataLayoutSpecification(LittleEndian, 18, DefaultPointer64Sizing, DefaultBooleanSizing, DefaultByteSizing, DefaultShortSizing, DefaultIntSizing, new Sizing(64, 64), DefaultHalfSizing, DefaultFloatSizing, DefaultDoubleSizing, new Sizing(128), DefaultQuadSizing, DefaultVector64Sizing, DefaultVector128Sizing, DefaultAggregateSizing, MachO, x86_64);

	@NotNull private final Endianness endianness;
	@NotNull private final String stackAlignmentSizeInBits;
	@NotNull private final Sizing pointer64Sizing;
	@NotNull private final Sizing booleanSizing;
	@NotNull private final Sizing byteSizing;
	@NotNull private final Sizing shortSizing;
	@NotNull private final Sizing intSizing;
	@NotNull private final Sizing longSizing;
	@NotNull private final Sizing halfSizing;
	@NotNull private final Sizing floatSizing;
	@NotNull private final Sizing doubleSizing;
	@NotNull private final Sizing longDoubleSizing;
	@NotNull private final Sizing quadSizing;
	@NotNull private final Sizing vector64Sizing;
	@NotNull private final Sizing vector128Sizing;
	@NotNull private final Sizing aggregateTypeAlignment;
	@NotNull private final Mangling mangling;
	@NotNull private final Architecture architecture;

	public DataLayoutSpecification(@NotNull final Endianness endianness, final int stackAlignmentSizeInBytes, @NotNull final Sizing pointer64Sizing, @NotNull final Sizing booleanSizing, @NotNull final Sizing byteSizing, @NotNull final Sizing shortSizing, @NotNull final Sizing intSizing, @NotNull final Sizing longSizing, @NotNull final Sizing halfSizing, @NotNull final Sizing floatSizing, @NotNull final Sizing doubleSizing, @NotNull final Sizing longDoubleSizing, @NotNull final Sizing quadSizing, @NotNull final Sizing vector64Sizing, @NotNull final Sizing vector128Sizing, @NotNull final Sizing aggregateTypeAlignment, @NotNull final Mangling mangling, @NotNull final Architecture architecture)
	{
		if (stackAlignmentSizeInBytes < 0)
		{
			throw new IllegalArgumentException("stackAlignmentSizeInBytes can not be negative");
		}
		this.endianness = endianness;
		stackAlignmentSizeInBits = Integer.toString(stackAlignmentSizeInBytes << 3);
		this.pointer64Sizing = pointer64Sizing;
		this.booleanSizing = booleanSizing;
		this.byteSizing = byteSizing;
		this.shortSizing = shortSizing;
		this.intSizing = intSizing;
		this.longSizing = longSizing;
		this.halfSizing = halfSizing;
		this.floatSizing = floatSizing;
		this.doubleSizing = doubleSizing;
		this.longDoubleSizing = longDoubleSizing;
		this.quadSizing = quadSizing;
		this.vector64Sizing = vector64Sizing;
		this.vector128Sizing = vector128Sizing;
		this.aggregateTypeAlignment = aggregateTypeAlignment;
		this.mangling = mangling;
		this.architecture = architecture;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(TargetDataLayoutStart);

		byteWriter.writeUtf8EncodedStringWithCertainty(endianness.dataLayoutEncoding);
		writeField(byteWriter, "S", stackAlignmentSizeInBits);
		writeField(byteWriter, "n:", architecture.nativeIntegerWidths);
		writeField(byteWriter, "p:64", pointer64Sizing.dataLayoutEncoding);
		writeField(byteWriter, "i1", booleanSizing.dataLayoutEncoding);
		writeField(byteWriter, "i8", byteSizing.dataLayoutEncoding);
		writeField(byteWriter, "i16", shortSizing.dataLayoutEncoding);
		writeField(byteWriter, "i32", intSizing.dataLayoutEncoding);
		writeField(byteWriter, "i64", longSizing.dataLayoutEncoding);
		writeField(byteWriter, "f16", halfSizing.dataLayoutEncoding);
		writeField(byteWriter, "f32", floatSizing.dataLayoutEncoding);
		writeField(byteWriter, "f64", doubleSizing.dataLayoutEncoding);
		writeField(byteWriter, "f80", longDoubleSizing.dataLayoutEncoding);
		writeField(byteWriter, "f128", quadSizing.dataLayoutEncoding);
		writeField(byteWriter, "v64", vector64Sizing.dataLayoutEncoding);
		writeField(byteWriter, "v128", vector128Sizing.dataLayoutEncoding);
		writeField(byteWriter, "a", aggregateTypeAlignment.dataLayoutEncoding);
		writeField(byteWriter, "m:", mangling.dataLayoutEncoding);
		writeField(byteWriter, "n:", architecture.nativeIntegerWidths);

		byteWriter.writeBytes(End);
	}

	private static <X extends Exception> void writeField(@NotNull final ByteWriter<X> byteWriter, @NotNull @NonNls final String dataLayoutEncodingA, @NotNull @NonNls final String dataLayoutEncodingB) throws X
	{
		byteWriter.writeByte(Hyphen);
		byteWriter.writeUtf8EncodedStringWithCertainty(dataLayoutEncodingA);
		byteWriter.writeUtf8EncodedStringWithCertainty(dataLayoutEncodingB);
	}
}
