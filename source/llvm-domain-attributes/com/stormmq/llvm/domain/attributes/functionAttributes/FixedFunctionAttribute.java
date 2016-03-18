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

import com.stormmq.llvm.domain.attributes.AttributeKind;
import com.stormmq.llvm.domain.attributes.writers.AttributeWriter;
import org.jetbrains.annotations.NotNull;

public enum FixedFunctionAttribute implements FunctionAttribute
{
	alwaysinline,
	builtin,
	cold,
	convergent,
	inaccessiblememonly,
	inaccessiblemem_or_argmemonly,
	inlinehint,
	jumptable,
	minsize,
	naked,
	nobuiltin,
	noduplicate,
	noimplicitfloat,
	noinline,
	nonlazybind,
	noredzone,
	noreturn,
	norecurse,
	nounwind,
	optnone,
	optsize,
	readnone,
	readonly,
	argmemonly,
	returns_twice,
	safestack,
	sanitize_address,
	sanitize_memory,
	sanitize_thread,
	ssp,
	sspreq,
	sspstrong,
	thunk,
	uwtable,
	;

	@NotNull
	@Override
	public AttributeKind attributeKind()
	{
		return AttributeKind.Defined;
	}

	@Override
	public <X extends Exception> void write(@NotNull final AttributeWriter<X> attributeWriter) throws X
	{
		attributeWriter.write(name());
	}
}
