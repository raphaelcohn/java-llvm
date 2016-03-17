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

package com.stormmq.llvm.api.metadataWriters;

import org.jetbrains.annotations.*;

import java.util.*;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class MetadataNodeIndexProvider
{
	private static final int NoMetadataNodeIndex = -1;

	private int metadataNodeIndex = -1;
	@NotNull private final Map<Object, Integer> assignedForwardReferences;

	public MetadataNodeIndexProvider()
	{
		metadataNodeIndex = NoMetadataNodeIndex;
		assignedForwardReferences = new IdentityHashMap<>(1024);
	}

	public int assignForwardReference(@NotNull final Object reference)
	{
		final int next = metadataNodeIndex + 1;
		@Nullable final Integer assigned = assignedForwardReferences.putIfAbsent(reference, next);
		if (assigned != null)
		{
			return assigned;
		}
		metadataNodeIndex = next;
		return next;
	}

	public int assignedMetadataNodeIndex(@NotNull final Object reference)
	{
		@Nullable final Integer assigned = assignedForwardReferences.get(reference);
		if (assigned == null)
		{
			throw new IllegalArgumentException(format(ENGLISH, "The reference '%1$s' has not bee assigned a forward reference", reference));
		}
		return assigned;
	}
}
