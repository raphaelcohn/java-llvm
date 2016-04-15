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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static com.stormmq.string.Formatting.format;

public class Triplet<A, B, C> extends Pair<A, B>
{
	@SuppressWarnings({"StandardVariableNames", "WeakerAccess"}) @NotNull public final C c;

	@SuppressWarnings("StandardVariableNames")
	public Triplet(@NotNull final A a, @NotNull final B b, @NotNull final C c)
	{
		super(a, b);
		this.c = c;
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
		if (!super.equals(o))
		{
			return false;
		}

		final Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;

		return c.equals(triplet.c);
	}

	@Override
	public int hashCode()
	{
		int result = super.hashCode();
		result = 31 * result + c.hashCode();
		return result;
	}

	@Override
	public int size()
	{
		return super.size() + 1;
	}

	@SuppressWarnings("CollectionDeclaredAsConcreteClass")
	@NotNull
	@Override
	public ArrayList<? super Object> toArrayList()
	{
		final ArrayList<? super Object> list = super.toArrayList();
		list.add(c);
		return list;
	}

	@SuppressWarnings("RefusedBequest")
	@Override
	@NotNull
	protected Object getInternal(final int index)
	{
		switch(index)
		{
			case 0:
				return a;

			case 1:
				return b;

			case 2:
				return c;

			default:
				throw new IllegalArgumentException(format("index '%1$s' can not be greater than or equal to 3", index));
		}
	}
}
