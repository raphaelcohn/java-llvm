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

package com.stormmq.java.llvm.xxx;

import com.stormmq.java.classfile.domain.InternalTypeName;
import com.stormmq.java.classfile.domain.descriptors.FieldDescriptor;
import com.stormmq.java.classfile.domain.uniqueness.FieldUniqueness;
import com.stormmq.java.parsing.utilities.names.typeNames.TypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.TypeNameVisitor;
import org.jetbrains.annotations.*;

public final class TypeConverter<T>
{
	@NotNull private final TypeNameVisitor<T> typeNameVisitor;

	public TypeConverter(@NotNull final TypeNameVisitor<T> typeNameVisitor)
	{
		this.typeNameVisitor = typeNameVisitor;
	}

	@NotNull
	public T convertField(@NotNull final FieldUniqueness fieldUniqueness)
	{
		final FieldDescriptor fieldDescriptor = fieldUniqueness.fieldDescriptor;
		final InternalTypeName internalTypeName = fieldDescriptor.internalTypeName;
		return convertInternalTypeName(internalTypeName);
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public T convertInternalTypeName(@NotNull final InternalTypeName internalTypeName)
	{
		if (internalTypeName.isArray())
		{
			// TODO: Handle arrays
			System.err.println("Arrays are not yet supported");
		}

		final TypeName typeName = internalTypeName.typeName();
		return typeName.visit(typeNameVisitor);
	}

}
