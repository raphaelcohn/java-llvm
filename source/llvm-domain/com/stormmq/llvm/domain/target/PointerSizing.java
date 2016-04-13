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

import static com.stormmq.llvm.domain.target.Alignment.SixtyFourBitAlignment;
import static com.stormmq.llvm.domain.target.Alignment.ThirtyTwoBitAlignment;
import static com.stormmq.llvm.domain.target.Sizing.SixtyFour;
import static com.stormmq.llvm.domain.target.Sizing.ThirtyTwo;

public final class PointerSizing
{
	@NotNull public static final PointerSizing ThirtyTwoBitPointerSizing = new PointerSizing(ThirtyTwo, ThirtyTwoBitAlignment);
	@NotNull public static final PointerSizing SixtyFourBitPointerSizing = new PointerSizing(SixtyFour, SixtyFourBitAlignment);

	@NotNull private final Sizing sizing;
	@NotNull private final Alignment alignment;

	private PointerSizing(@NotNull final Sizing sizing, @NotNull final Alignment alignment)
	{
		this.sizing = sizing;
		this.alignment = alignment;
	}

	public int storageSizeInBits()
	{
		return sizing.storageSizeInBits();
	}

	public int abiAlignmentInBits(final int minimumAlignmentInBits)
	{
		return alignment.abiAlignmentInBits(minimumAlignmentInBits);
	}

	public <X extends Exception> void writeAlignmentField(final ByteWriter<X> byteWriter, @NonNls @NotNull final String abbreviatedName) throws X
	{
		alignment.writeAlignmentField(byteWriter, abbreviatedName);
	}
}
