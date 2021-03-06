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

package com.stormmq.llvm.domain;

import com.stormmq.string.AbstractToString;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.identityHashCode;

public final class ReferenceTracker extends AbstractToString
{
	@NotNull private final Map<Object, Integer> references;
	private int nextReferenceIndex;

	public ReferenceTracker()
	{
		references = new HashMap<>(64);
		nextReferenceIndex = 0;
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(references.size(), nextReferenceIndex);
	}

	public boolean hasBeenWritten(@NotNull final Object reference)
	{
		return references.containsKey(reference);
	}

	public int referenceIndex(@NotNull final Object reference)
	{
		return references.computeIfAbsent(reference, key ->
		{
			final int referenceIndex = nextReferenceIndex;
			nextReferenceIndex++;
			return referenceIndex;
		});
	}
}
