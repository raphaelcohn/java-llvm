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

package com.stormmq.java.llvm.xxx.typeConverters.typeNameVisitors;

import com.stormmq.java.llvm.xxx.happy.ClassToStructureMap;
import com.stormmq.java.parsing.utilities.names.typeNames.TypeNameVisitor;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.llvm.domain.types.SizedType;
import org.jetbrains.annotations.*;

import static com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType._double;
import static com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType._float;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.*;

public final class ToSizedTypeTypeNameVisitor implements TypeNameVisitor<SizedType>
{
	@NotNull private final ClassToStructureMap classToStructureMap;

	public ToSizedTypeTypeNameVisitor(@NotNull final ClassToStructureMap classToStructureMap)
	{
		this.classToStructureMap = classToStructureMap;
	}

	@NotNull
	@Override
	public SizedType useVoid()
	{
		throw new IllegalStateException("void is not permissible in SizedType");
	}

	@NotNull
	@Override
	public SizedType useBoolean()
	{
		return i1;
	}

	@NotNull
	@Override
	public SizedType useByte()
	{
		return i8;
	}

	@NotNull
	@Override
	public SizedType useShort()
	{
		return i16;
	}

	@NotNull
	@Override
	public SizedType useChar()
	{
		return i16;
	}

	@NotNull
	@Override
	public SizedType useInt()
	{
		return i32;
	}

	@NotNull
	@Override
	public SizedType useLong()
	{
		return i64;
	}

	@NotNull
	@Override
	public SizedType useFloat()
	{
		return _float;
	}

	@NotNull
	@Override
	public SizedType useDouble()
	{
		return _double;
	}

	@NotNull
	@Override
	public SizedType useReference(@NotNull final KnownReferenceTypeName knownReferenceTypeName)
	{
		return classToStructureMap.pointerType(knownReferenceTypeName);
	}
}
