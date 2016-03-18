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

package com.stormmq.llvm.metadata.writers;

import com.stormmq.llvm.metadata.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class MetadataNodeIndexProvider
{
	private static final int NoMetadataNodeIndex = -1;

	private int metadataNodeIndex = -1;
	@NotNull private final Map<Metadata, Integer> assignedForwardReferences;
	@NotNull private final Map<Metadata, Boolean> alreadyWritten;

	public MetadataNodeIndexProvider()
	{
		metadataNodeIndex = NoMetadataNodeIndex;
		assignedForwardReferences = new IdentityHashMap<>(1024);
		alreadyWritten = new IdentityHashMap<>(1024);
	}

	public int assignReference(@NotNull final Metadata metadata)
	{
		final int next = metadataNodeIndex + 1;
		@Nullable final Integer assigned = assignedForwardReferences.putIfAbsent(metadata, next);
		if (assigned != null)
		{
			return assigned;
		}
		metadataNodeIndex = next;
		return next;
	}

	public int assignedReference(@NotNull final Metadata metadata)
	{
		@Nullable final Integer assigned = assignedForwardReferences.get(metadata);
		if (assigned == null)
		{
			throw new IllegalArgumentException(format(ENGLISH, "The metadata '%1$s' has not been assigned a reference", metadata));
		}
		return assigned;
	}

	public boolean isAlreadyWritten(@NotNull final Metadata metadata)
	{
		return alreadyWritten.putIfAbsent(metadata, true) == null;
	}
}
