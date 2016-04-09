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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.*;

import static com.stormmq.string.Formatting.format;

public final class MapHelper
{
	private MapHelper()
	{
	}
	
	@SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
	public static <K, V> boolean useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, final boolean ifAbsent, @NotNull final ToBooleanFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent;
		}
		return ifPresent.applyAsBoolean(value);
	}

	@SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
	public static <K, V> boolean useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final BooleanSupplier ifAbsent, @NotNull final ToBooleanFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent.getAsBoolean();
		}
		return ifPresent.applyAsBoolean(value);
	}
	
	public static <K, V> byte useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, final byte ifAbsent, @NotNull final ToByteFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent;
		}
		return ifPresent.applyAsByte(value);
	}
	
	public static <K, V> byte useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final ByteSupplier ifAbsent, @NotNull final ToByteFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent.getAsByte();
		}
		return ifPresent.applyAsByte(value);
	}
	
	public static <K, V> short useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, final short ifAbsent, @NotNull final ToShortFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent;
		}
		return ifPresent.applyAsShort(value);
	}
	
	public static <K, V> short useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final ShortSupplier ifAbsent, @NotNull final ToShortFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent.getAsShort();
		}
		return ifPresent.applyAsShort(value);
	}
	
	public static <K, V> char useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, final char ifAbsent, @NotNull final ToCharFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent;
		}
		return ifPresent.applyAsChar(value);
	}
	
	public static <K, V> char useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final CharSupplier ifAbsent, @NotNull final ToCharFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent.getAsChar();
		}
		return ifPresent.applyAsChar(value);
	}
	
	public static <K, V> int useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, final int ifAbsent, @NotNull final ToIntFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent;
		}
		return ifPresent.applyAsInt(value);
	}
	
	public static <K, V> int useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final IntSupplier ifAbsent, @NotNull final ToIntFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent.getAsInt();
		}
		return ifPresent.applyAsInt(value);
	}

	public static <K, V> long useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, final long ifAbsent, @NotNull final ToLongFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent;
		}
		return ifPresent.applyAsLong(value);
	}

	public static <K, V> long useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final LongSupplier ifAbsent, @NotNull final ToLongFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent.getAsLong();
		}
		return ifPresent.applyAsLong(value);
	}
	
	public static <K, V> float useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, final float ifAbsent, @NotNull final ToFloatFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent;
		}
		return ifPresent.applyAsFloat(value);
	}
	
	public static <K, V> float useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final FloatSupplier ifAbsent, @NotNull final ToFloatFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent.getAsFloat();
		}
		return ifPresent.applyAsFloat(value);
	}
	
	public static <K, V> double useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, final double ifAbsent, @NotNull final ToDoubleFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent;
		}
		return ifPresent.applyAsDouble(value);
	}
	
	public static <K, V> double useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final DoubleSupplier ifAbsent, @NotNull final ToDoubleFunction<V> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent.getAsDouble();
		}
		return ifPresent.applyAsDouble(value);
	}
	
	@NotNull
	public static <K, V, R> R useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final R ifAbsent, @NotNull final Function<V, R> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent;
		}
		return ifPresent.apply(value);
	}

	@NotNull
	public static <K, V, R> R useMapValue(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final Supplier<R> ifAbsent, @NotNull final Function<V, R> ifPresent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent.get();
		}
		return ifPresent.apply(value);
	}

	@NotNull
	public static <K, V, R, X extends Exception> R useMapValueExceptionallyWithDefault(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final R ifAbsent, @NotNull final ExceptionFunction<V, R, X> ifPresent) throws X
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent;
		}
		return ifPresent.apply(value);
	}

	@NotNull
	public static <K, V, R, X extends Exception> R useMapValueExceptionally(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final ExceptionSupplier<R, X> ifAbsent, @NotNull final ExceptionFunction<V, R, X> ifPresent) throws X
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent.get();
		}
		return ifPresent.apply(value);
	}

	@NotNull
	public static <K, V> V useMapValueOrGetDefault(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final Supplier<V> ifAbsent)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			return ifAbsent.get();
		}
		return value;
	}

	@NotNull
	public static <K, V> V getGuarded(@NotNull final Map<K, V> map, @NotNull final K key)
	{
		@Nullable final V value = map.get(key);
		if (value == null)
		{
			throw new IllegalStateException(format("key '%1$s' should be present in map but is not", key));
		}
		return value;
	}

	@Nullable
	public static <K, V, X extends Exception> V computeExceptionally(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final ExceptionFunction<? super V, ? extends V, X> remappingFunction) throws X
	{
		@Nullable final V oldValue = map.get(key);
		@Nullable final V newValue = remappingFunction.apply(oldValue);
		if (newValue == null)
		{
			if (oldValue != null || map.containsKey(key))
			{
				map.remove(key);
				return null;
			}

			return null;
		}

		map.put(key, newValue);
		return newValue;
	}

	@Nullable
	public static <K, V, X extends Exception> V computeIfAbsentExceptionally(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final ExceptionFunction<? super K, ? extends V, X> remappingFunction) throws X
	{
		@Nullable final V oldValue = map.get(key);
		if (oldValue == null)
		{
			@Nullable final V newValue = remappingFunction.apply(key);
			if (newValue == null)
			{
				return null;
			}
			map.put(key, newValue);
			return newValue;
		}
		return oldValue;
	}

	@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
	@NotNull
	public static <K, V> V putOnce(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final V ifAbsent)
	{
		return map.compute(key, (key1, value) ->
		{
			if (value == null)
			{
				return ifAbsent;
			}
			throw new PutOnceViolationException(key1, value);
		});
	}

	@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
	@NotNull
	public static <K, V> V putOnce(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final Supplier<V> ifAbsent)
	{
		return map.compute(key, (key1, value) ->
		{
			if (value == null)
			{
				return ifAbsent.get();
			}
			throw new PutOnceViolationException(key1, value);
		});
	}

	@SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
	@NotNull
	public static <K, V> V putOnce(@NotNull final Map<K, V> map, @NotNull final K key, @NotNull final Function<K, V> ifAbsent)
	{
		return map.compute(key, (key1, value) ->
		{
			if (value == null)
			{
				return ifAbsent.apply(key1);
			}
			throw new PutOnceViolationException(key1, value);
		});
	}

}
