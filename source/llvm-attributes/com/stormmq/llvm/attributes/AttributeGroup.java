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

package com.stormmq.llvm.attributes;

import com.stormmq.llvm.attributes.functionAttributes.FunctionAttribute;
import com.stormmq.llvm.attributes.parameterAttributes.ParameterAttribute;
import com.stormmq.llvm.attributes.writers.AttributeGroupWriter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static java.util.Arrays.asList;

public final class AttributeGroup<A extends Attribute>
{
	@NotNull
	public static AttributeGroup<FunctionAttribute> functionAttributes(@NotNull final FunctionAttribute... functionAttributes)
	{
		return attributeGroup(functionAttributes);
	}

	@NotNull
	public static AttributeGroup<ParameterAttribute> parameterAttributes(@NotNull final ParameterAttribute... parameterAttributes)
	{
		return attributeGroup(parameterAttributes);
	}

	@SafeVarargs
	@NotNull
	private static <A extends Attribute> AttributeGroup<A> attributeGroup(@NotNull final A... attributes)
	{
		return new AttributeGroup<A>(asList(attributes));
	}

	@NotNull private final SortedSet<A> attributes;

	public AttributeGroup(@NotNull final Collection<A> attributes)
	{
		this.attributes = new TreeSet<>((Comparator<A>) (left, right) ->
		{
			final AttributeKind attributeKindLeft = left.attributeKind();
			final AttributeKind attributeKindRight = right.attributeKind();

			final int attributeKindComparison = attributeKindLeft.compareTo(attributeKindRight);
			if (attributeKindComparison != 0)
			{
				return attributeKindComparison < 0 ? -1 : 1;
			}

			final String leftName = left.name();
			final String rightName = right.name();
			return leftName.compareTo(rightName);
		});
		this.attributes.addAll(attributes);
	}

	public <X extends Exception> void write(@NotNull final AttributeGroupWriter<X> attributeGroupWriter) throws X
	{
		attributeGroupWriter.write(attributes);
	}
}
