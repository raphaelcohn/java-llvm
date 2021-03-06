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
import com.stormmq.llvm.domain.identifiers.LocalIdentifier;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.llvm.domain.typedValues.AddressableIdentifierTypedValue;
import com.stormmq.llvm.domain.typedValues.TypedValue;
import com.stormmq.llvm.domain.types.*;
import com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType;
import com.stormmq.llvm.domain.types.firstClassTypes.PointerValueType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.AggregateType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.LocallyIdentifiedStructureType;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.instructions.GetElementPointerInstruction.arrayIndexLength;
import static com.stormmq.llvm.domain.instructions.GetElementPointerInstruction.offsetOf;
import static com.stormmq.llvm.domain.LlvmWritable.SpaceToSpace;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.i64;
import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;

/*
%.sizeof.com.stormmq.MyClassExample = ptrtoint %class.com.stormmq.MyClassExample * %.arrayindex.length.com.stormmq.MyClassExample to i64
*/
public final class PointerToIntegerInstruction<T extends CanBePointedToType> extends AbstractInstruction<IntegerValueType>
{
	@NotNull private static final byte[] ptrtointSpace = encodeToUtf8ByteArrayWithCertaintyValueIsValid("ptrtoint ");

	// @.sizeof.com.stormmq.MyClassExample = constant i64 ptrtoint (%class.com.stormmq.MyClassExample * getelementptr inbounds (%class.com.stormmq.MyClassExample, %class.com.stormmq.MyClassExample * null, i64 1) to i32)
	@NotNull
	public static <A extends AggregateType, T extends CanBePointedToType> ConstantExpression<IntegerValueType> sizeOfStructureTypeConstantTypedValue(@NotNull final A aggregateType)
	{
		return new ConstantExpression<>(sizeOfStructureType(aggregateType));
	}

	// @.offsetof.3.com.stormmq.MyClassExample = constant i32 ptrtoint (double * getelementptr (%class.com.stormmq.MyClassExample, %class.com.stormmq.MyClassExample * null, i64 0, i32 3) to i32)
	@NotNull
	public static <A extends AggregateType, T extends CanBePointedToType> ConstantExpression<IntegerValueType> offsetOfFieldInStructureTypeConstantTypedValue(@NotNull final A aggregateType, @NotNull final T to, final int zeroBasedFieldIndex)
	{
		return new ConstantExpression<>(offsetOfFieldInStructureType(aggregateType, to, zeroBasedFieldIndex));
	}

	@NotNull
	public static PointerToIntegerInstruction<AddressableIdentifierType<LocalIdentifier>> pointerToIntegerExpressionForStructureType(@NotNull final LocallyIdentifiedStructureType locallyIdentifiedStructureType, @NotNull final IntegerValueType size)
	{
		final PointerValueType<AddressableIdentifierType<LocalIdentifier>> addressableIdentifierTypePointerValueType = locallyIdentifiedStructureType.pointerTo();
		return new PointerToIntegerInstruction<>(new AddressableIdentifierTypedValue<>(addressableIdentifierTypePointerValueType, locallyIdentifiedStructureType.identifier()), size);
	}

	@NotNull
	private static <A extends AggregateType> PointerToIntegerInstruction<A> sizeOfStructureType(@NotNull final A aggregateType)
	{
		final Instruction<PointerValueType<A>> constantInstruction = arrayIndexLength(aggregateType);
		final TypedValue<PointerValueType<A>> pointerValue = new ConstantExpression<>(constantInstruction);
		return new PointerToIntegerInstruction<>(pointerValue, i64);
	}

	@NotNull
	private static <A extends AggregateType, T extends CanBePointedToType> PointerToIntegerInstruction<T> offsetOfFieldInStructureType(@NotNull final A aggregateType, @NotNull final T to, final int zeroBasedFieldIndex)
	{
		final Instruction<PointerValueType<T>> constantInstruction = offsetOf(aggregateType, to, zeroBasedFieldIndex);
		final TypedValue<PointerValueType<T>> pointerValue = new ConstantExpression<>(constantInstruction);
		return new PointerToIntegerInstruction<>(pointerValue, i64);
	}

	@NotNull private final TypedValue<PointerValueType<T>> pointerValue;
	@NotNull private final IntegerValueType to;

	private PointerToIntegerInstruction(@NotNull final TypedValue<PointerValueType<T>> pointerValue, @NotNull final IntegerValueType to)
	{
		this.pointerValue = pointerValue;
		this.to = to;
	}

	@NotNull
	@Override
	public IntegerValueType to()
	{
		return to;
	}

	@NotNull
	@Override
	protected byte[] nameWithSpaceAsLlAssemblyValue()
	{
		return ptrtointSpace;
	}

	@Override
	protected <X extends Exception> void writeBody(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		pointerValue.write(byteWriter, dataLayoutSpecification);
		byteWriter.writeBytes(SpaceToSpace);
		to.write(byteWriter, dataLayoutSpecification);
	}

}
