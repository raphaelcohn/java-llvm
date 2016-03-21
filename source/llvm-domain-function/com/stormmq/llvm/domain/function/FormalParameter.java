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

package com.stormmq.llvm.domain.function;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.Writable;
import com.stormmq.llvm.domain.attributes.AttributeGroup;
import com.stormmq.llvm.domain.function.attributes.parameterAttributes.ParameterAttribute;
import com.stormmq.llvm.domain.types.TypeExcludingVoid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FormalParameter implements Writable
{
	@NotNull private final TypeExcludingVoid typeExcludingVoid;
	@NotNull private final AttributeGroup<ParameterAttribute> parameterAttributes;
	@Nullable private final ParameterName name;

	public FormalParameter(@NotNull final TypeExcludingVoid typeExcludingVoid, @NotNull final AttributeGroup<ParameterAttribute> parameterAttributes, @Nullable final ParameterName name)
	{
		this.typeExcludingVoid = typeExcludingVoid;
		this.parameterAttributes = parameterAttributes;
		this.name = name;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		typeExcludingVoid.write(byteWriter);

		parameterAttributes.write(byteWriter);

		if (name != null)
		{
			name.write(byteWriter);
		}
	}
}