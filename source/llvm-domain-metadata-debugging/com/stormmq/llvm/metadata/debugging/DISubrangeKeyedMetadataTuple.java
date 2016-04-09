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

package com.stormmq.llvm.metadata.debugging;

import com.stormmq.functions.ListHelper;
import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.metadata.metadataTuples.KeyedMetadataTuple;
import com.stormmq.llvm.metadata.KeyWithMetadataField;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.stormmq.llvm.metadata.debugging.Key.align;
import static com.stormmq.llvm.metadata.debugging.Key.size;

public final class DISubrangeKeyedMetadataTuple extends KeyedMetadataTuple implements TypeMetadata
{
	private static final int EmptyCount = -1;
	private static final int EmptyLowerBound = -1;

	@NotNull
	public static DISubrangeKeyedMetadataTuple EmptyArray(@NotNull final ReferenceTracker referenceTracker)
	{
		return new DISubrangeKeyedMetadataTuple(referenceTracker, EmptyCount, EmptyLowerBound);
	}

	@SuppressWarnings("WeakerAccess")
	public DISubrangeKeyedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, final int count, final int lowerBound)
	{
		super(referenceTracker, false, "DISubrange", with(count, lowerBound));
	}

	@NotNull
	private static List<KeyWithMetadataField> with(final int count, final int lowerBound)
	{
		if (lowerBound == EmptyLowerBound)
		{
			if (count != EmptyCount)
			{
				throw new IllegalArgumentException("count must be -1 if lowerBound is -1");
			}
		}
		else
		{
			if (count == EmptyCount)
			{
				throw new IllegalArgumentException("if lower bound is -1 then count must be -1");
			}
		}
		return ListHelper.newArrayList(2, (tuple) ->
		{
			tuple.add(size.with(count));
			if (lowerBound != EmptyLowerBound)
			{
				tuple.add(align.with(lowerBound));
			}
		});
	}
}
