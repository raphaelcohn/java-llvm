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

package com.stormmq.tuples;

import org.jetbrains.annotations.*;

import java.util.*;
import java.util.Map.Entry;

import static com.stormmq.string.Formatting.format;

public class Pair<A, B> extends AbstractList<Object> implements Entry<A, B>
{
	@NotNull public final A a;
	@SuppressWarnings({"StandardVariableNames", "WeakerAccess"}) @NotNull public final B b;

	@SuppressWarnings("StandardVariableNames")
	public Pair(@NotNull final A a, @NotNull final B b)
	{
		this.a = a;
		this.b = b;
	}

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

		final Pair<?, ?> pair = (Pair<?, ?>) o;

		return a.equals(pair.a) && b.equals(pair.b);
	}

	@Override
	public int hashCode()
	{
		int result = a.hashCode();
		result = 31 * result + b.hashCode();
		return result;
	}

	@SuppressWarnings("CollectionDeclaredAsConcreteClass")
	@NotNull
	public ArrayList<? super Object> toArrayList()
	{
		final ArrayList<? super Object> list = new ArrayList<>(size());
		list.add(a);
		list.add(b);
		return list;
	}

	@Override
	public int size()
	{
		return 2;
	}

	@Override
	public final Object get(final int index)
	{
		if (index < 0)
		{
			throw new IllegalArgumentException(format("index '%1$s' can not be negative", index));
		}

		return getInternal(index);
	}

	@NotNull
	protected Object getInternal(final int index)
	{
		switch(index)
		{
			case 0:
				return a;

			case 1:
				return b;

			default:
				throw new IllegalArgumentException(format("index '%1$s' can not be greater than or equal to 2", index));
		}
	}

	@Override
	@NotNull
	public final A getKey()
	{
		return a;
	}

	@Override
	@NotNull
	public final B getValue()
	{
		return b;
	}

	@Override
	@NotNull
	public final B setValue(@NotNull final B value)
	{
		throw new UnsupportedOperationException("Can not mutate a pair");
	}
}
