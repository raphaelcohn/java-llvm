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

package com.stormmq.llvm.domain.attributes.parameterAttributes;

import com.stormmq.llvm.domain.attributes.AttributeKind;
import com.stormmq.llvm.domain.attributes.writers.AttributeWriter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class IntegerParameterAttribute implements ParameterAttribute
{
	@NotNull
	public static IntegerParameterAttribute align(final int alignmentInBytes)
	{
		return new IntegerParameterAttribute("align", alignmentInBytes);
	}

	@NotNull
	public static IntegerParameterAttribute dereferenceable(final int size)
	{
		return new IntegerParameterAttribute("dereferenceable", size);
	}

	@NotNull
	public static IntegerParameterAttribute dereferenceableOrNull(final int size)
	{
		return new IntegerParameterAttribute("dereferenceable_or_null", size);
	}

	@NotNull private final String name;
	private final int value;

	public IntegerParameterAttribute(@NonNls @NotNull final String name, final int value)
	{
		this.name = name;
		this.value = value;
	}

	@NotNull
	@Override
	public AttributeKind attributeKind()
	{
		return AttributeKind.Defined;
	}

	@NotNull
	@Override
	public String name()
	{
		return name;
	}

	@Override
	public <X extends Exception> void write(@NotNull final AttributeWriter<X> attributeWriter) throws X
	{
		attributeWriter.write(format(ENGLISH, "%1$s(%2$s)", name, value));
	}
}
