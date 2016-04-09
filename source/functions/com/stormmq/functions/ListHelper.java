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

package com.stormmq.functions;

import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public final class ListHelper
{
	@NotNull
	public static <V> List<V> newArrayList(final int size, @NotNull final Consumer<List<V>> addToList)
	{
		final List<V> list = new ArrayList<>(size);
		addToList.accept(list);
		return list;
	}

	@NotNull
	public static <V, X extends Exception> List<V> newArrayListExceptionally(final int size, @NotNull final ExceptionConsumer<List<V>, X> addToList) throws X
	{
		final List<V> list = new ArrayList<>(size);
		addToList.accept(list);
		return list;
	}

	@SuppressWarnings("MethodCanBeVariableArityMethod")
	@NotNull
	public static <V> V[] listToArray(@NotNull final List<V> list, @NotNull final IntFunction<V[]> arrayConstructor, @NotNull final V[] empty)
	{
		final int size = list.size();
		if (size == 0)
		{
			return empty;
		}
		return list.toArray(arrayConstructor.apply(size));
	}

	@NotNull
	public static <V> List<V> trimToSizeOrReplaceWithEmptyOrSingleton(@NotNull final ArrayList<V> list)
	{
		final int size = list.size();
		if (size == 0)
		{
			return emptyList();
		}
		if (size == 1)
		{
			return singletonList(list.get(0));
		}
		list.trimToSize();
		return list;
	}

	private ListHelper()
	{
	}
}
