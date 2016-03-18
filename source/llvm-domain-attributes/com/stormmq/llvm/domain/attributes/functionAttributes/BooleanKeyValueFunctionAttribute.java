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

package com.stormmq.llvm.domain.attributes.functionAttributes;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public final class BooleanKeyValueFunctionAttribute extends AbstractKeyValueFunctionAttribute
{
	@NotNull public static final FunctionAttribute DisableTailCalls_Off = new BooleanKeyValueFunctionAttribute("disable-tail-calls", false);
	@NotNull public static final FunctionAttribute NoFramePointerElimination_Off = new BooleanKeyValueFunctionAttribute("no-frame-pointer-elim", false);
	@NotNull public static final FunctionAttribute LessPreciseFloatingPointMath_Off = new BooleanKeyValueFunctionAttribute("less-precise-fpmad", false);
	@NotNull public static final FunctionAttribute NoInfinitiesFloatingPointMath_Off = new BooleanKeyValueFunctionAttribute("no-infs-fp-math", false);
	@NotNull public static final FunctionAttribute NoNotANumbersFloatingPointMath_Off = new BooleanKeyValueFunctionAttribute("no-nans-fp-math", false);
	@NotNull public static final FunctionAttribute UnsafeFloatingPointMath_Off = new BooleanKeyValueFunctionAttribute("unsafe-fp-math", false);
	@NotNull public static final FunctionAttribute UseSoftFloat_Off = new BooleanKeyValueFunctionAttribute("use-soft-float", false);

	public BooleanKeyValueFunctionAttribute(@NonNls @NotNull final String key, final boolean value)
	{
		super(key, value ? "true" : "false");
	}
}
