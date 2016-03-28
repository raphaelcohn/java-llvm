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
import org.jetbrains.annotations.NotNull;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public enum FixedFunctionAttribute implements FunctionAttribute
{
	@SuppressWarnings({"unused", "SpellCheckingInspection"})alwaysinline,
	@SuppressWarnings("unused")builtin,
	@SuppressWarnings("unused")cold,
	@SuppressWarnings("unused")convergent,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})inaccessiblememonly,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})inaccessiblemem_or_argmemonly,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})inlinehint,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})jumptable,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})minsize,
	@SuppressWarnings("unused")naked,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})nobuiltin,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})noduplicate,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})noimplicitfloat,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})noinline,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})nonlazybind,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})noredzone,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})noreturn,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})norecurse,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})nounwind,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})optnone,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})optsize,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})readnone,
	@SuppressWarnings("unused")readonly,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})argmemonly,
	@SuppressWarnings("unused")returns_twice,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})safestack,
	@SuppressWarnings("unused")sanitize_address,
	@SuppressWarnings("unused")sanitize_memory,
	@SuppressWarnings("unused")sanitize_thread,
	@SuppressWarnings("unused")ssp,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})sspreq,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})sspstrong,
	@SuppressWarnings("unused")thunk,
	@SuppressWarnings({"unused", "SpellCheckingInspection"})uwtable,
	;

	@NotNull private final byte[] llvmAssemblyEncoding;

	FixedFunctionAttribute()
	{
		llvmAssemblyEncoding = encodeUtf8BytesWithCertaintyValueIsValid(name());
	}

	@NotNull
	@Override
	public AttributeKind attributeKind()
	{
		return AttributeKind.Defined;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(llvmAssemblyEncoding);
	}
}
