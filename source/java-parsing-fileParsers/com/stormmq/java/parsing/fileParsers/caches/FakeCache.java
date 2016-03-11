package com.stormmq.java.parsing.fileParsers.caches;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FakeCache<V> implements Cache<V>
{
	@NotNull private static final FakeCache<?> Instance = new FakeCache<>();

	@SuppressWarnings("unchecked")
	@NotNull
	public static <V> FakeCache<V> fakeCache()
	{
		return (FakeCache<V>) Instance;
	}

	public static final CacheMaker FakeCacheMaker = new CacheMaker()
	{
		@NotNull
		@Override
		public <V2> Cache<V2> makeCache()
		{
			return fakeCache();
		}
	};

	private FakeCache()
	{
	}

	@Override
	public void cache(@NotNull final String filePathName, @NotNull final V parsedForm)
	{
	}

	@Nullable
	@Override
	public V retrieve(@NotNull final String filePathName)
	{
		return null;
	}
}
