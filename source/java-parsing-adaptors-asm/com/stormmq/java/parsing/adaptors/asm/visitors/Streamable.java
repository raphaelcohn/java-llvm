package com.stormmq.java.parsing.adaptors.asm.visitors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

import static java.util.Collections.addAll;

public final class Streamable<V>
{
	@NotNull private final List<V> visitorAdaptors;
	private final int listSize;

	public Streamable(@NotNull final List<V> visitorAdaptors)
	{
		this.visitorAdaptors = visitorAdaptors;
		listSize = visitorAdaptors.size();
	}

	@NotNull
	private Stream<V> stream()
	{
		return visitorAdaptors.stream();
	}

	public void forEach(@NotNull final Consumer<V> visitorAdaptorConsumer)
	{
		stream().forEach(visitorAdaptorConsumer);
	}

	@NotNull
	public <V2> List<V2> map(@NotNull final Function<V, V2> action)
	{
		final List<V2> list = new ArrayList<>(listSize);
		stream().map(action).filter(annotationVisitor -> annotationVisitor != null).forEach(list::add);
		return list;
	}

	@NotNull
	public <S, V2, C> C combination(@Nullable final S superVisitor, @NotNull final Function<V, V2> action, @NotNull final BiFunction<S, List<V2>, C> constructor)
	{
		return constructor.apply(superVisitor, map(action));
	}

	@NotNull
	public <S, V2, C> C combinationOfCombination(@Nullable final S superVisitor, @NotNull final Function<V, V2[]> action, @NotNull final BiFunction<S, List<V2>, C> constructor)
	{
		final List<V2> list = new ArrayList<>(listSize);
		stream().map(action).forEach(v2s -> addAll(list, v2s));
		return constructor.apply(superVisitor, list);
	}
}
