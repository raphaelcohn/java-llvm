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

import static com.stormmq.java.parsing.utilities.ReservedIdentifiers._false;
import static com.stormmq.java.parsing.utilities.ReservedIdentifiers._true;

public final class BooleanKeyValueFunctionAttribute extends AbstractKeyValueFunctionAttribute
{
	@NotNull public static final FunctionAttribute DisableTailCalls_Off = falseBooleanKey("disable-tail-calls");
	@SuppressWarnings("SpellCheckingInspection") @NotNull public static final FunctionAttribute NoFramePointerElimination_Off = falseBooleanKey("no-frame-pointer-elim");
	@SuppressWarnings("SpellCheckingInspection") @NotNull public static final FunctionAttribute LessPreciseFloatingPointMath_Off = falseBooleanKey("less-precise-fpmad");
	@SuppressWarnings("SpellCheckingInspection") @NotNull public static final FunctionAttribute NoInfinitiesFloatingPointMath_Off = falseBooleanKey("no-infs-fp-math");
	@NotNull public static final FunctionAttribute NoNotANumbersFloatingPointMath_Off = falseBooleanKey("no-nans-fp-math");
	@NotNull public static final FunctionAttribute UnsafeFloatingPointMath_Off = falseBooleanKey("unsafe-fp-math");
	@NotNull public static final FunctionAttribute UseSoftFloat_Off = falseBooleanKey("use-soft-float");

	@NotNull
	private static FunctionAttribute falseBooleanKey(@NonNls @NotNull final String key)
	{
		return new BooleanKeyValueFunctionAttribute(key, false);
	}

	private BooleanKeyValueFunctionAttribute(@NonNls @NotNull final String key, final boolean value)
	{
		super(key, value ? _true : _false);
	}
}
