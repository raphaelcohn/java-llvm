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

import com.stormmq.llvm.metadata.AbstractKeyedMetadata;
import com.stormmq.llvm.api.writing.metadataWriters.*;
import com.stormmq.llvm.metadata.writers.MetadataNodeIndexProvider;
import com.stormmq.llvm.metadata.writers.SpecializedLabelledFieldsMetadataWriter;
import org.jetbrains.annotations.NotNull;

public final class DISubrangeKeyedMetadata extends AbstractKeyedMetadata implements TypeMetadata
{
	public static final int EmptyCount = -1;
	public static final int EmptyLowerBound = -1;

	@NotNull public static final DISubrangeKeyedMetadata EmptyArray = new DISubrangeKeyedMetadata(EmptyCount, EmptyLowerBound);

	private final int count;
	private final int lowerBound;

	public DISubrangeKeyedMetadata(final int count, final int lowerBound)
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
				throw new IllegalArgumentException("if lower vound is -1 then count must be -1");
			}
		}
		this.count = count;
		this.lowerBound = lowerBound;
	}

	@Override
	protected <X extends Exception> void writeLabelledFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final SpecializedLabelledFieldsMetadataWriter<X> specializedLabelledFieldsMetadataWriter) throws X
	{
		specializedLabelledFieldsMetadataWriter.writeLabelledField(size, count);
		if (lowerBound != EmptyLowerBound)
		{
			specializedLabelledFieldsMetadataWriter.writeLabelledField(align, lowerBound);
		}
	}
}
