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

package com.stormmq.llvm.domain;

import org.jetbrains.annotations.NotNull;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public enum Linkage
{
	@SuppressWarnings("unused")_private,
	@SuppressWarnings("unused")internal(true),
	@SuppressWarnings("unused")available_externally,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})linkonce,
	@SuppressWarnings("unused")weak(true),
	@SuppressWarnings("unused")common,
	@SuppressWarnings("unused")appending,
	@SuppressWarnings("unused")extern_weak,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})linkonce_odr,
	@SuppressWarnings("unused")weak_odr,
	@SuppressWarnings("unused")external(true),
	;

	public final boolean isPermittedForAlias;
	@NotNull public final byte[] llAssemblyValue;

	Linkage(final boolean isPermittedForAlias)
	{
		this.isPermittedForAlias = isPermittedForAlias;
		final String name = name();
		final String actualName = name.charAt(0) == '_' ? name.substring(1) : name;
		llAssemblyValue = encodeUtf8BytesWithCertaintyValueIsValid(actualName);
	}

	Linkage()
	{
		this(false);
	}
}
