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

package com.stormmq.llvm.domain.metadata.debugging.structAlignmentAndSizeCounters;

import com.stormmq.llvm.domain.metadata.debugging.DIDerivedTypeKeyedMetadataTuple;
import org.jetbrains.annotations.*;

// See: http://www.catb.org/esr/structure-packing/
public final class UnpackedStructAlignmentAndSizeCounter implements StructAlignmentAndSizeCounter
{
	private int currentSize;
	private int largestAlignmentInBits;

	public UnpackedStructAlignmentAndSizeCounter()
	{
		currentSize = 0;
		largestAlignmentInBits = 0;
	}

	@Override
	public void accept(@NotNull final DIDerivedTypeKeyedMetadataTuple element)
	{
		final int elementSizeInBits = element.sizeInBits();
		final int elementAlignmentInBits = element.alignmentInBits();

		// For aggregate types, the X86-64 ABI does not require any alignment
		final int remainder = elementAlignmentInBits == 0 ? 0 : currentSize % elementAlignmentInBits;
		if (remainder == 0)
		{
			currentSize += elementSizeInBits;
		}
		else
		{
			currentSize = (currentSize / elementAlignmentInBits + 1) * elementAlignmentInBits + elementAlignmentInBits;
		}

		final int alignmentInBits = element.alignmentInBits();
		if (alignmentInBits > largestAlignmentInBits)
		{
			largestAlignmentInBits = alignmentInBits;
		}
	}

	@Override
	public int alignmentInBits()
	{
		return largestAlignmentInBits;
	}

	@Override
	public int sizeInBits()
	{
		if (currentSize % largestAlignmentInBits == 0)
		{
			return currentSize;
		}
		return (currentSize / largestAlignmentInBits + 1) * largestAlignmentInBits;
	}

	@Override
	public int currentOffsetInBits()
	{
		return currentSize;
	}
}
