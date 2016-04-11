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

package com.stormmq.llvm.domain.metadata.debugging;

import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.domain.metadata.Metadata;
import com.stormmq.llvm.domain.metadata.metadataTuples.KeyedMetadataTuple;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.metadata.debugging.NullTypeMetadata.NullTypeMetadataInstance;

public final class DISubroutineTypeKeyedMetadataTuple extends KeyedMetadataTuple implements TypeMetadata
{
	@NotNull
	public static DISubroutineTypeKeyedMetadataTuple functionWithReturnType(@NotNull final ReferenceTracker referenceTracker, @NotNull final TypeMetadata returnType, @NotNull final TypeMetadata... formalParameterTypes)
	{
		return new DISubroutineTypeKeyedMetadataTuple(referenceTracker, returnType, formalParameterTypes);
	}

	@NotNull
	public static DISubroutineTypeKeyedMetadataTuple functionWithoutReturnType(@NotNull final ReferenceTracker referenceTracker, @NotNull final TypeMetadata... formalParameterTypes)
	{
		return new DISubroutineTypeKeyedMetadataTuple(referenceTracker, NullTypeMetadataInstance, formalParameterTypes);
	}

	private DISubroutineTypeKeyedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @NotNull final Metadata returnType, @NotNull final TypeMetadata[] formalParameterTypes)
	{
		super(referenceTracker, false, "DISubroutineType", Key.types.with(referenceTracker, returnType, formalParameterTypes));
	}

	@Override
	public int sizeInBits()
	{
		throw new UnsupportedOperationException("A function does not have a size in this context");
	}

	@Override
	public int alignmentInBits()
	{
		throw new UnsupportedOperationException("A function does not have an alignment in this context");
	}
}
