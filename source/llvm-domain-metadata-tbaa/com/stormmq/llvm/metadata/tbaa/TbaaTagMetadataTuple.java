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

package com.stormmq.llvm.metadata.tbaa;

import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.metadata.*;
import com.stormmq.llvm.metadata.metadataTuples.AnonymousMetadataTuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.stormmq.functions.ListHelper.newArrayList;
import static com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.IntegerConstantTypedValue.i64;

public final class TbaaTagMetadataTuple extends AnonymousMetadataTuple
{
	@NotNull private static final Metadata IsConstant = new ConstantMetadata(i64(1L));

	// identifier is typically a type name, eg float, const float, etc
	public TbaaTagMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @SuppressWarnings("TypeMayBeWeakened") @NotNull final StringConstantMetadata identifier, @SuppressWarnings("TypeMayBeWeakened") @Nullable final TbaaTagMetadataTuple parent, final boolean isConstant)
	{
		super(referenceTracker, convert(identifier, parent, isConstant));
	}

	@NotNull
	private static List<? extends Metadata> convert(@NotNull final Metadata identifier, @Nullable final Metadata parent, final boolean isConstant)
	{
		return newArrayList(3, tuple ->
		{
			tuple.add(identifier);

			if (parent != null)
			{
				tuple.add(parent);

				if (isConstant)
				{
					tuple.add(IsConstant);
				}
			}
			else if (isConstant)
			{
				throw new IllegalArgumentException("isConstant can not be true if the parent is null");
			}
		});
	}
}
