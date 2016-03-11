package com.stormmq.java.parsing.fileParsers.caches;

import com.stormmq.java.parsing.fileParsers.caches.Cache;
import org.jetbrains.annotations.NotNull;

public interface CacheMaker
{
	@NotNull
	<V> Cache<V> makeCache();
}
