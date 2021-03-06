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

package com.stormmq.llvm.domain.function.attributes.functionAttributes;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.attributes.AttributeKind;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.string.Formatting;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractKeyValueFunctionAttribute implements FunctionAttribute
{
	@NonNls
	@NotNull
	protected static <V> String valueAsString(@NotNull final Collection<V> values, @NotNull final Function<V, String> conversion)
	{
		final StringBuilder stringBuilder = new StringBuilder(1024);
		values.stream().map(conversion).sorted().forEach(new StringConsumer(stringBuilder));
		return stringBuilder.toString();
	}

	@NotNull private final String key;
	@NotNull private final String value;

	protected AbstractKeyValueFunctionAttribute(@NonNls @NotNull final String key, @NonNls @NotNull final String value)
	{
		this.key = key;
		this.value = value;
	}

	@NotNull
	@Override
	public final String name()
	{
		return key;
	}

	@NotNull
	@Override
	public final AttributeKind attributeKind()
	{
		return AttributeKind.Compiler;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		byteWriter.writeUtf8EncodedStringWithCertainty(Formatting.format("\"%1$s\"=\"%2$s\"", key, value));
	}

	private static final class StringConsumer implements Consumer<String>
	{
		@SuppressWarnings("StringBufferField") private final StringBuilder stringBuilder;
		private boolean afterFirst;

		private StringConsumer(final StringBuilder stringBuilder)
		{
			this.stringBuilder = stringBuilder;
			afterFirst = false;
		}

		@Override
		public void accept(final String s)
		{
			if (afterFirst)
			{
				stringBuilder.append(' ');
			}
			else
			{
				afterFirst = true;
			}
			stringBuilder.append(s);
		}
	}
}
