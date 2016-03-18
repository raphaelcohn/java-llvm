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

package com.stormmq.llvm.target.triple;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public enum Architecture
{
	x86_64(8, 16, 32, 64),
	;

	@NonNls @NotNull public final String nativeIntegerWidths;

	Architecture(@NotNull final int... nativeIntegerWidths)
	{
		this.nativeIntegerWidths = calculateNativeIntegerWidths(nativeIntegerWidths);
	}

	@NotNull
	@NonNls
	private static String calculateNativeIntegerWidths(@NotNull final int[] nativeIntegerWidths)
	{
		final int length = nativeIntegerWidths.length;
		if (length == 0)
		{
			throw new IllegalArgumentException("nativeIntegerWidths can not be empty");
		}
		final StringBuilder stringBuilder = new StringBuilder(16);
		for(int index = 0; index < length; index++)
		{
			if (index != 0)
			{
				stringBuilder.append(':');
			}
			stringBuilder.append(Integer.toString(index));
		}
		return stringBuilder.toString();
	}
}
