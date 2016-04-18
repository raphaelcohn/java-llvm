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
import com.stormmq.string.AbstractToString;
import org.jetbrains.annotations.*;

import static com.stormmq.llvm.domain.target.Alignment.*;

public final class Alignments extends AbstractToString
{
	@NotNull static final Alignment DefaultInt1Alignment = EightBitAlignment;
	@NotNull static final Alignment DefaultInt8Alignment = EightBitAlignment;
	@NotNull static final Alignment DefaultInt16Alignment = SixteenBitAlignment;
	@NotNull static final Alignment DefaultInt32Alignment = ThirtyTwoBitAlignment;
	@NotNull private static final Alignment DefaultInt64Alignment = ThirtyTwoSixtyFourBitAlignment;
	@NotNull private static final Alignment DefaultInt128Alignment = OneHundredAndTwentyBitAlignment;
	@NotNull static final Alignment DefaultHalfAlignment = SixteenBitAlignment;
	@NotNull static final Alignment DefaultFloatAlignment = ThirtyTwoBitAlignment;
	@NotNull static final Alignment DefaultDoubleAlignment = SixtyFourBitAlignment;
	@NotNull static final Alignment DefaultQuadAlignment = OneHundredAndTwentyBitAlignment;
	@NotNull static final Alignment DefaultVector64Alignment = SixtyFourBitAlignment;
	@NotNull static final Alignment DefaultVector128Alignment = OneHundredAndTwentyBitAlignment;
	@NotNull static final Alignment DefaultAggregateAlignment = ZeroSixtyFourBitAlignment;

	@NotNull private static final Alignments LlvmDefaultAlignment = new Alignments(DefaultInt1Alignment, DefaultInt8Alignment, DefaultInt16Alignment, DefaultInt32Alignment, DefaultInt64Alignment, DefaultInt128Alignment, DefaultHalfAlignment, DefaultFloatAlignment, DefaultDoubleAlignment, DefaultQuadAlignment, DefaultVector64Alignment, DefaultVector128Alignment, DefaultAggregateAlignment);
	@NotNull public static final Alignments x86_64Alignment = LlvmDefaultAlignment.adjustInt64Alignment(SixtyFourBitAlignment);
	@NotNull public static final Alignments PowerPc64Alignment = LlvmDefaultAlignment.adjustInt64Alignment(SixtyFourBitAlignment);
	@NotNull public static final Alignments Sparc64Alignment = LlvmDefaultAlignment.adjustInt64Alignment(SixtyFourBitAlignment);
	@NotNull public static final Alignments i866Alignment = LlvmDefaultAlignment.i686(ThirtyTwoSixtyFourBitAlignment);
	@NotNull public static final Alignments Mips64Alignment = LlvmDefaultAlignment.mips64(ThirtyTwoBitAlignment, ThirtyTwoBitAlignment, SixtyFourBitAlignment);
	@NotNull public static final Alignments ArmV7Alignment = LlvmDefaultAlignment.armV7(SixtyFourBitAlignment, new Alignment(64, 128), new Alignment(0, 32));
	@NotNull public static final Alignments AArch64Alignment = LlvmDefaultAlignment.aarch64(SixtyFourBitAlignment, OneHundredAndTwentyBitAlignment);

	@Nullable private final Alignment int1Alignment;
	@Nullable private final Alignment int8Alignment;
	@Nullable private final Alignment int16Alignment;
	@Nullable private final Alignment int32Alignment;
	@Nullable private final Alignment int64Alignment;
	@Nullable private final Alignment int128Alignment;
	@Nullable private final Alignment halfAlignment;
	@Nullable private final Alignment floatAlignment;
	@Nullable private final Alignment doubleAlignment;
	@Nullable private final Alignment quadAlignment;
	@Nullable private final Alignment vector64Alignment;
	@Nullable private final Alignment vector128Alignment;
	@Nullable private final Alignment aggregateAlignment;

	public Alignments(@Nullable final Alignment int1Alignment, @Nullable final Alignment int8Alignment, @Nullable final Alignment int16Alignment, @Nullable final Alignment int32Alignment, @Nullable final Alignment int64Alignment, @Nullable final Alignment int128Alignment, @Nullable final Alignment halfAlignment, @Nullable final Alignment floatAlignment, @Nullable final Alignment doubleAlignment, @Nullable final Alignment quadAlignment, @Nullable final Alignment vector64Alignment, @Nullable final Alignment vector128Alignment, @Nullable final Alignment aggregateAlignment)
	{
		this.int1Alignment = int1Alignment;
		this.int8Alignment = int8Alignment;
		this.int16Alignment = int16Alignment;
		this.int32Alignment = int32Alignment;
		this.int64Alignment = int64Alignment;
		this.int128Alignment = int128Alignment;
		this.halfAlignment = halfAlignment;
		this.floatAlignment = floatAlignment;
		this.doubleAlignment = doubleAlignment;
		this.quadAlignment = quadAlignment;
		this.vector64Alignment = vector64Alignment;
		this.vector128Alignment = vector128Alignment;
		this.aggregateAlignment = aggregateAlignment;
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(int1Alignment, int8Alignment, int16Alignment, int32Alignment, int64Alignment, int128Alignment, halfAlignment, floatAlignment, doubleAlignment, quadAlignment, vector64Alignment, vector128Alignment, aggregateAlignment);
	}

	@SuppressWarnings("SimplifiableIfStatement")
	@Override
	public boolean equals(@Nullable final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		final Alignments that = (Alignments) o;

		if (int1Alignment != null ? !int1Alignment.equals(that.int1Alignment) : that.int1Alignment != null)
		{
			return false;
		}
		if (int8Alignment != null ? !int8Alignment.equals(that.int8Alignment) : that.int8Alignment != null)
		{
			return false;
		}
		if (int16Alignment != null ? !int16Alignment.equals(that.int16Alignment) : that.int16Alignment != null)
		{
			return false;
		}
		if (int32Alignment != null ? !int32Alignment.equals(that.int32Alignment) : that.int32Alignment != null)
		{
			return false;
		}
		if (int64Alignment != null ? !int64Alignment.equals(that.int64Alignment) : that.int64Alignment != null)
		{
			return false;
		}
		if (int128Alignment != null ? !int128Alignment.equals(that.int128Alignment) : that.int128Alignment != null)
		{
			return false;
		}
		if (halfAlignment != null ? !halfAlignment.equals(that.halfAlignment) : that.halfAlignment != null)
		{
			return false;
		}
		if (floatAlignment != null ? !floatAlignment.equals(that.floatAlignment) : that.floatAlignment != null)
		{
			return false;
		}
		if (doubleAlignment != null ? !doubleAlignment.equals(that.doubleAlignment) : that.doubleAlignment != null)
		{
			return false;
		}
		if (quadAlignment != null ? !quadAlignment.equals(that.quadAlignment) : that.quadAlignment != null)
		{
			return false;
		}
		if (vector64Alignment != null ? !vector64Alignment.equals(that.vector64Alignment) : that.vector64Alignment != null)
		{
			return false;
		}
		if (vector128Alignment != null ? !vector128Alignment.equals(that.vector128Alignment) : that.vector128Alignment != null)
		{
			return false;
		}
		return aggregateAlignment != null ? aggregateAlignment.equals(that.aggregateAlignment) : that.aggregateAlignment == null;
	}

	@Override
	public int hashCode()
	{
		int result = int1Alignment != null ? int1Alignment.hashCode() : 0;
		result = 31 * result + (int8Alignment != null ? int8Alignment.hashCode() : 0);
		result = 31 * result + (int16Alignment != null ? int16Alignment.hashCode() : 0);
		result = 31 * result + (int32Alignment != null ? int32Alignment.hashCode() : 0);
		result = 31 * result + (int64Alignment != null ? int64Alignment.hashCode() : 0);
		result = 31 * result + (int128Alignment != null ? int128Alignment.hashCode() : 0);
		result = 31 * result + (halfAlignment != null ? halfAlignment.hashCode() : 0);
		result = 31 * result + (floatAlignment != null ? floatAlignment.hashCode() : 0);
		result = 31 * result + (doubleAlignment != null ? doubleAlignment.hashCode() : 0);
		result = 31 * result + (quadAlignment != null ? quadAlignment.hashCode() : 0);
		result = 31 * result + (vector64Alignment != null ? vector64Alignment.hashCode() : 0);
		result = 31 * result + (vector128Alignment != null ? vector128Alignment.hashCode() : 0);
		result = 31 * result + (aggregateAlignment != null ? aggregateAlignment.hashCode() : 0);
		return result;
	}

	@NotNull
	private Alignments adjustInt64Alignment(@NotNull final Alignment int64Alignment)
	{
		return new Alignments(int1Alignment, int8Alignment, int16Alignment, int32Alignment, int64Alignment, int128Alignment, halfAlignment, floatAlignment, doubleAlignment, quadAlignment, vector64Alignment, vector128Alignment, aggregateAlignment);
	}

	@NotNull
	private Alignments aarch64(@NotNull final Alignment int64Alignment, @NotNull final Alignment int128Alignment)
	{
		return new Alignments(int1Alignment, int8Alignment, int16Alignment, int32Alignment, int64Alignment, int128Alignment, halfAlignment, floatAlignment, doubleAlignment, quadAlignment, vector64Alignment, vector128Alignment, aggregateAlignment);
	}

	@NotNull
	private Alignments i686(@NotNull final Alignment doubleAlignment)
	{
		return new Alignments(int1Alignment, int8Alignment, int16Alignment, int32Alignment, int64Alignment, int128Alignment, halfAlignment, floatAlignment, doubleAlignment, quadAlignment, vector64Alignment, vector128Alignment, aggregateAlignment);
	}

	@NotNull
	private Alignments mips64(@NotNull final Alignment int8Alignment, @NotNull final Alignment int16Alignment, @NotNull final Alignment int64Alignment)
	{
		return new Alignments(int1Alignment, int8Alignment, int16Alignment, int32Alignment, int64Alignment, int128Alignment, halfAlignment, floatAlignment, doubleAlignment, quadAlignment, vector64Alignment, vector128Alignment, aggregateAlignment);
	}

	@NotNull
	private Alignments armV7(@NotNull final Alignment int64Alignment, @NotNull final Alignment vector128Alignment, @NotNull final Alignment aggregateAlignment)
	{
		return new Alignments(int1Alignment, int8Alignment, int16Alignment, int32Alignment, int64Alignment, int128Alignment, halfAlignment, floatAlignment, doubleAlignment, quadAlignment, vector64Alignment, vector128Alignment, aggregateAlignment);
	}

	public static boolean isDoubleAlignmentLlvmDefault(@NotNull final Alignment doubleAlignment)
	{
		return DefaultDoubleAlignment.equals(doubleAlignment);
	}

	public boolean isInt1AlignmentDefault()
	{
		return int1Alignment == null;
	}

	public boolean isInt8AlignmentDefault()
	{
		return int8Alignment == null;
	}

	public boolean isInt16AlignmentDefault()
	{
		return int16Alignment == null;
	}

	public boolean isInt32AlignmentDefault()
	{
		return int32Alignment == null;
	}

	public boolean isInt64AlignmentDefault()
	{
		return int64Alignment == null;
	}

	public boolean isInt128AlignmentDefault()
	{
		return int128Alignment == null;
	}

	public boolean isHalfAlignmentDefault()
	{
		return halfAlignment == null;
	}

	public boolean isFloatAlignmentDefault()
	{
		return floatAlignment == null;
	}

	public boolean isDoubleAlignmentDefault()
	{
		return doubleAlignment == null;
	}

	public boolean isQuadAlignmentDefault()
	{
		return quadAlignment == null;
	}

	public boolean isVector64AlignmentDefault()
	{
		return vector64Alignment == null;
	}

	public boolean isVector128AlignmentDefault()
	{
		return vector128Alignment == null;
	}

	public boolean isAggregateAlignmentDefault()
	{
		return aggregateAlignment == null;
	}

	@NotNull
	public Alignment int1Alignment()
	{
		return alignment(int1Alignment, DefaultInt1Alignment);
	}

	@NotNull
	public Alignment int8Alignment()
	{
		return alignment(int8Alignment, DefaultInt8Alignment);
	}

	@NotNull
	public Alignment int16Alignment()
	{
		return alignment(int16Alignment, DefaultInt16Alignment);
	}

	@NotNull
	public Alignment int32Alignment()
	{
		return alignment(int32Alignment, DefaultInt32Alignment);
	}

	@NotNull
	public Alignment int64Alignment()
	{
		return alignment(int64Alignment, DefaultInt64Alignment);
	}

	@NotNull
	public Alignment int128Alignment()
	{
		return alignmentWhereLlvmDefaultAlignmentIsUnknown(int128Alignment, DefaultInt128Alignment, OneHundredAndTwentyBitAlignment);
	}

	@NotNull
	public Alignment halfAlignment()
	{
		return alignment(halfAlignment, DefaultHalfAlignment);
	}

	@NotNull
	public Alignment floatAlignment()
	{
		return alignment(floatAlignment, DefaultFloatAlignment);
	}

	@NotNull
	public Alignment doubleAlignment()
	{
		return alignment(doubleAlignment, DefaultDoubleAlignment);
	}

	@NotNull
	public Alignment quadAlignment()
	{
		return alignment(quadAlignment, DefaultQuadAlignment);
	}

	@NotNull
	public Alignment vector64Alignment()
	{
		return alignment(vector64Alignment, DefaultVector64Alignment);
	}

	@NotNull
	public Alignment vector128Alignment()
	{
		return alignment(vector128Alignment, DefaultVector128Alignment);
	}

	@NotNull
	public Alignment aggregateAlignment()
	{
		return alignment(aggregateAlignment, DefaultAggregateAlignment);
	}
	
	public <X extends Exception> void writeInt1AlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		writeAlignmentIfNotDefault(byteWriter, int1Alignment, "int1");
	}
	
	public <X extends Exception> void writeInt8AlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		writeAlignmentIfNotDefault(byteWriter, int8Alignment, "int8");
	}
	
	public <X extends Exception> void writeInt16AlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		writeAlignmentIfNotDefault(byteWriter, int16Alignment, "int16");
	}
	
	public <X extends Exception> void writeInt32AlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		writeAlignmentIfNotDefault(byteWriter, int32Alignment, "int32");
	}
	
	public <X extends Exception> void writeInt64AlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		writeAlignmentIfNotDefault(byteWriter, int64Alignment, "int64");
	}
	
	public <X extends Exception> void writeInt128AlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		writeAlignmentIfNotDefault(byteWriter, int128Alignment, "int128");
	}
	
	public <X extends Exception> void writeHalfAlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		writeAlignmentIfNotDefault(byteWriter, halfAlignment, "f16");
	}
	
	public <X extends Exception> void writeFloatAlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		writeAlignmentIfNotDefault(byteWriter, floatAlignment, "f32");
	}

	public <X extends Exception> void writeDoubleAlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		writeAlignmentIfNotDefault(byteWriter, doubleAlignment, "f64");
	}
	
	public <X extends Exception> void writeQuadAlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		writeAlignmentIfNotDefault(byteWriter, quadAlignment, "f128");
	}
	
	public <X extends Exception> void writeVector64AlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		writeAlignmentIfNotDefault(byteWriter, vector64Alignment, "v64");
	}
	
	public <X extends Exception> void writeVector128AlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		writeAlignmentIfNotDefault(byteWriter, vector128Alignment, "v128");
	}

	public <X extends Exception> void writeAggregateAlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		writeAlignmentIfNotDefault(byteWriter, aggregateAlignment, "a");
	}

	public boolean isInt128AlignmentUnspecified()
	{
		return isAlignmentUnspecified(int128Alignment, DefaultInt128Alignment);
	}

	private static <X extends Exception> void writeAlignmentIfNotDefault(@NotNull final ByteWriter<X> byteWriter, @Nullable final Alignment alignment, @NonNls @NotNull final String abbreviatedName) throws X
	{
		if (alignment != null)
		{
			alignment.writeAlignmentField(byteWriter, abbreviatedName);
		}
	}
	
	@NotNull
	private static Alignment alignment(@Nullable final Alignment alignment, @NotNull final Alignment llvmDefaultAlignment)
	{
		return alignment == null ? llvmDefaultAlignment : alignment;
	}

	private static boolean isAlignmentUnspecified(@Nullable final Alignment alignment, @Nullable final Alignment llvmDefaultAlignment)
	{
		return alignment == null && llvmDefaultAlignment == null;
	}

	@NotNull
	private static Alignment alignmentWhereLlvmDefaultAlignmentIsUnknown(@Nullable final Alignment alignment, @Nullable final Alignment llvmDefaultAlignment, @NotNull final Alignment fallbackAlignment)
	{
		if (alignment == null)
		{
			if (llvmDefaultAlignment == null)
			{
				return fallbackAlignment;
			}
			return llvmDefaultAlignment;
		}
		return alignment;
	}
}
