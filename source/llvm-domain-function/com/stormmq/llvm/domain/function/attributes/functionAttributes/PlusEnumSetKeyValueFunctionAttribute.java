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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.stormmq.llvm.domain.function.attributes.functionAttributes.TargetFeature.*;
import static java.util.Arrays.asList;

public final class PlusEnumSetKeyValueFunctionAttribute<E extends Enum<E>> extends AbstractKeyValueFunctionAttribute
{
	@NotNull
	private static FunctionAttribute TargetFeatures(@NotNull final TargetFeature... targetFeatures)
	{
		return new PlusEnumSetKeyValueFunctionAttribute<>("target-features", targetFeatures);
	}

	@NotNull public static final FunctionAttribute TargetFeatures_core2 = TargetFeatures(cx16, sse, sse2, sse3, ssse3);

	@SuppressWarnings("OverloadedVarargsMethod")
	@SafeVarargs
	private PlusEnumSetKeyValueFunctionAttribute(@NonNls @NotNull final String key, @NotNull final E... values)
	{
		this(key, new HashSet<>(asList(values)));
	}

	private PlusEnumSetKeyValueFunctionAttribute(@NonNls @NotNull final String key, @SuppressWarnings("TypeMayBeWeakened") @NotNull final Set<E> values)
	{
		super(key, valueAsString(values, anEnum -> '+' + anEnum.name()));
	}

}
