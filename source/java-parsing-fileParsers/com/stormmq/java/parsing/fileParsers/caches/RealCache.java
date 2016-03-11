package com.stormmq.java.parsing.fileParsers.caches;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class RealCache<V> implements Cache<V>
{
	public static final CacheMaker RealCacheMaker = new CacheMaker()
	{
		@NotNull
		@Override
		public <V2> Cache<V2> makeCache()
		{
			return new RealCache<>();
		}
	};

	@NotNull
	private final HashMap<String, V> cache;

	public RealCache()
	{
		cache = new HashMap<>(4096);
	}

	@Override
	public void cache(@NotNull final String filePathName, @NotNull final V parsedForm)
	{
		@Nullable final V extant = cache.put(filePathName, parsedForm);
		if (extant != null)
		{
			throw new IllegalStateException(format(ENGLISH, "The file '%1$s' should only be cached once", filePathName));
		}
	}

	@Override
	@Nullable
	public V retrieve(@NotNull final String filePathName)
	{
		return cache.get(filePathName);
	}
}
