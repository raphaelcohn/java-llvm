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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;

public enum CallingConvention
{
	@SuppressWarnings("unused")ccc("ccc"),
	@SuppressWarnings({"SpellCheckingInspection", "unused"})fastcc("fastcc"),
	@SuppressWarnings({"SpellCheckingInspection", "unused"})coldcc("coldcc"),
	@SuppressWarnings("unused")cc10("cc 10"),
	@SuppressWarnings("unused")cc11("cc 11"),
	@SuppressWarnings({"SpellCheckingInspection", "unused"})webkit_jscc("webkit_jscc"),
	@SuppressWarnings({"SpellCheckingInspection", "unused"})anyregcc("anyregcc"),
	@SuppressWarnings({"SpellCheckingInspection", "unused"})preserve_mostcc("preserve_mostcc"),
	@SuppressWarnings({"SpellCheckingInspection", "unused"})preserve_allcc("preserve_allcc"),
	@SuppressWarnings({"SpellCheckingInspection", "unused"})cxx_fast_tlscc("cxx_fast_tlscc"),
	@SuppressWarnings("unused")cc64("cc 64"),
	;

	@NotNull public final byte[] llAssemblyValue;

	CallingConvention(@NonNls @NotNull final String name)
	{
		llAssemblyValue = encodeToUtf8ByteArrayWithCertaintyValueIsValid(name);
	}
}
