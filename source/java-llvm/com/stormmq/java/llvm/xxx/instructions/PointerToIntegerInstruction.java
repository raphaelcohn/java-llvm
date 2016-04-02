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

package com.stormmq.java.llvm.xxx.instructions;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.typedValues.AddressableIdentifierTypedValue;
import com.stormmq.llvm.domain.typedValues.TypedValue;
import com.stormmq.llvm.domain.types.AddressableIdentifierType;
import com.stormmq.llvm.domain.types.CanBePointedToType;
import com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType;
import com.stormmq.llvm.domain.types.firstClassTypes.PointerValueType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.LocallyIdentifiedStructureType;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

/*
%.sizeof.com.stormmq.MyClassExample = ptrtoint %class.com.stormmq.MyClassExample * %.arrayindex.length.com.stormmq.MyClassExample to i64
*/
public final class PointerToIntegerInstruction<T extends CanBePointedToType> implements Instruction<IntegerValueType>
{
	@NotNull private static final byte[] ptrtointSpace = encodeUtf8BytesWithCertaintyValueIsValid("ptrtoint ");

	@NotNull
	public static PointerToIntegerInstruction<AddressableIdentifierType> pointerToIntegerExpressionForStructureType(@NotNull final LocallyIdentifiedStructureType locallyIdentifiedStructureType, @NotNull final IntegerValueType size)
	{
		final PointerValueType<AddressableIdentifierType> addressableIdentifierTypePointerValueType = locallyIdentifiedStructureType.pointerTo(0);
		return new PointerToIntegerInstruction<>(new AddressableIdentifierTypedValue<>(addressableIdentifierTypePointerValueType, locallyIdentifiedStructureType.localIdentifier()), size);
	}

	@NotNull private final TypedValue<PointerValueType<T>> pointerValue;
	@NotNull private final IntegerValueType to;

	private PointerToIntegerInstruction(@NotNull final TypedValue<PointerValueType<T>> pointerValue, @NotNull final IntegerValueType to)
	{
		this.pointerValue = pointerValue;
		this.to = to;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(ptrtointSpace);
		pointerValue.write(byteWriter);
		byteWriter.writeBytes(SpaceToSpace);
		to.write(byteWriter);
	}
}
