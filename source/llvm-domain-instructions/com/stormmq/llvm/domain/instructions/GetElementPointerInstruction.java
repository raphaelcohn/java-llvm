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

package com.stormmq.llvm.domain.instructions;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.AddressSpace;
import com.stormmq.llvm.domain.identifiers.LocalIdentifier;
import com.stormmq.llvm.domain.typedValues.TypedValue;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.IntegerConstantTypedValue;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.NullPointerConstantTypedValue;
import com.stormmq.llvm.domain.types.CanBePointedToType;
import com.stormmq.llvm.domain.types.firstClassTypes.PointerValueType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.LocallyIdentifiedStructureType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.StructureType;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.AddressSpace.DefaultAddressSpace;
import static com.stormmq.llvm.domain.Writable.CommaSpace;
import static com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.IntegerConstantTypedValue.Int64One;
import static com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.IntegerConstantTypedValue.Int64Zero;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.i32;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

/*

	; sizeof
	%.arrayindex.length.com.stormmq.MyClassExample = getelementptr %class.com.stormmq.MyClassExample, %class.com.stormmq.MyClassExample * null, i64 1
	%.sizeof.com.stormmq.MyClassExample = ptrtoint %class.com.stormmq.MyClassExample * %.arrayindex.length.com.stormmq.MyClassExample to i64

	; offsetof of field 3 (zero-based) of com.stormmq.MyClassExample [ie double]
	; Thus %.arrayindex.3.com.stormmq.MyClassExample is a _pointer to a double_ [ie take field type, make it a pointer]
	%.arrayindex.3.com.stormmq.MyClassExample = getelementptr %class.com.stormmq.MyClassExample, %class.com.stormmq.MyClassExample * null, i64 0, i32 3
	%.offsetof.3.com.stormmq.MyClassExample = ptrtoint double* %.arrayindex.3.com.stormmq.MyClassExample to i32
*/
// Aka Instruction
public final class GetElementPointerInstruction<T extends CanBePointedToType, U extends CanBePointedToType> extends AbstractInstruction<PointerValueType<U>>
{
	@NotNull
	public static <S extends StructureType> GetElementPointerInstruction<S, S> arrayIndexLength(@NotNull final S structureType)
	{
		final PointerValueType<S> pointerToStructure = structureType.pointerTo(DefaultAddressSpace);
		final ConstantTypedValue<PointerValueType<S>> expression = new NullPointerConstantTypedValue<>(pointerToStructure);
		return new GetElementPointerInstruction<>(false, pointerToStructure, structureType, expression, Int64One);
	}

	@NotNull
	public static <S extends StructureType, T extends CanBePointedToType> GetElementPointerInstruction<S, T> offsetOf(@NotNull final S structureType, @NotNull final T to, final int zeroBasedFieldIndex)
	{
		final ConstantTypedValue<PointerValueType<S>> expression = new NullPointerConstantTypedValue<>(structureType.pointerTo(DefaultAddressSpace));
		final PointerValueType<T> pointerToStructureFieldType = to.pointerTo(DefaultAddressSpace);
		return new GetElementPointerInstruction<>(false, pointerToStructureFieldType, structureType, expression, Int64Zero, new IntegerConstantTypedValue(i32, zeroBasedFieldIndex));
	}

	@NotNull private static final byte[] getelementptrSpace = encodeUtf8BytesWithCertaintyValueIsValid("getelementptr ");
	@NotNull private static final byte[] getelementptrSpaceinboundSpace = encodeUtf8BytesWithCertaintyValueIsValid("getelementptr inbounds ");

	private final boolean isInbounds;
	@NotNull private final PointerValueType<U> to;
	@NotNull private final T type;
	@NotNull private final TypedValue<PointerValueType<T>> pointer;
	@NotNull private final IntegerConstantTypedValue[] indices;

	private GetElementPointerInstruction(final boolean isInbounds, @NotNull final PointerValueType<U> to, @NotNull final T type, @NotNull final TypedValue<PointerValueType<T>> pointer, @NotNull final IntegerConstantTypedValue... indices)
	{
		this.isInbounds = isInbounds;
		this.to = to;
		this.type = type;
		this.pointer = pointer;
		this.indices = indices;
	}

	@NotNull
	@Override
	public PointerValueType<U> to()
	{
		return to;
	}

	@NotNull
	@Override
	protected byte[] nameWithSpaceAsLlAssemblyValue()
	{
		return isInbounds ? getelementptrSpace : getelementptrSpaceinboundSpace;
	}

	@Override
	protected <X extends Exception> void writeBody(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		if (type instanceof LocallyIdentifiedStructureType)
		{
			final LocalIdentifier identifier = ((LocallyIdentifiedStructureType) type).localIdentifier();
			identifier.write(byteWriter);
		}
		else
		{
			type.write(byteWriter);
		}

		byteWriter.writeBytes(CommaSpace);
		pointer.write(byteWriter);

		for (final IntegerConstantTypedValue integerConstantExpression : indices)
		{
			byteWriter.writeBytes(CommaSpace);
			integerConstantExpression.write(byteWriter);
		}
	}

}
