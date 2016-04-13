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
import com.stormmq.llvm.domain.typedValues.TypedValue;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.IntegerConstantTypedValue;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.NullPointerConstantTypedValue;
import com.stormmq.llvm.domain.types.CanBePointedToType;
import com.stormmq.llvm.domain.types.firstClassTypes.PointerValueType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.AggregateType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.LocallyIdentifiedStructureType;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.LlvmWritable.CommaSpace;
import static com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.IntegerConstantTypedValue.Int64One;
import static com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.IntegerConstantTypedValue.Int64Zero;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.i32;
import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;

/*
	; sizeof
	%.arrayindex.length.com.stormmq.MyClassExample = getelementptr %class.com.stormmq.MyClassExample, %class.com.stormmq.MyClassExample * null, i64 1
	%.sizeof.com.stormmq.MyClassExample = ptrtoint %class.com.stormmq.MyClassExample * %.arrayindex.length.com.stormmq.MyClassExample to i64

	; offsetof of field 3 (zero-based) of com.stormmq.MyClassExample [ie double]
	; Thus %.arrayindex.3.com.stormmq.MyClassExample is a _pointer to a double_ [ie take field type, make it a pointer]
	%.arrayindex.3.com.stormmq.MyClassExample = getelementptr %class.com.stormmq.MyClassExample, %class.com.stormmq.MyClassExample * null, i64 0, i32 3
	%.offsetof.3.com.stormmq.MyClassExample = ptrtoint double* %.arrayindex.3.com.stormmq.MyClassExample to i32
*/
public final class GetElementPointerInstruction<A extends AggregateType, T extends CanBePointedToType> extends AbstractInstruction<PointerValueType<T>>
{
	@NotNull
	public static <A extends AggregateType> GetElementPointerInstruction<A, A> arrayIndexLength(@NotNull final A aggregateType)
	{
		final PointerValueType<A> pointerToAggregateType = aggregateType.pointerTo();
		final ConstantTypedValue<PointerValueType<A>> expression = new NullPointerConstantTypedValue<>(pointerToAggregateType);
		return new GetElementPointerInstruction<>(false, pointerToAggregateType, aggregateType, expression, Int64One);
	}

	@NotNull
	public static <A extends AggregateType, T extends CanBePointedToType> GetElementPointerInstruction<A, T> offsetOf(@NotNull final A aggregateType, @NotNull final T to, final int zeroBasedFieldIndex)
	{
		final PointerValueType<A> pointerToAggregateType = aggregateType.pointerTo();
		final ConstantTypedValue<PointerValueType<A>> expression = new NullPointerConstantTypedValue<>(pointerToAggregateType);
		final PointerValueType<T> pointerToElementType = to.pointerTo();
		return new GetElementPointerInstruction<>(false, pointerToElementType, aggregateType, expression, Int64Zero, new IntegerConstantTypedValue(i32, zeroBasedFieldIndex));
	}

	@NotNull private static final byte[] getelementptrSpace = encodeToUtf8ByteArrayWithCertaintyValueIsValid("getelementptr ");
	@NotNull private static final byte[] getelementptrSpaceinboundSpace = encodeToUtf8ByteArrayWithCertaintyValueIsValid("getelementptr inbounds ");

	private final boolean isInbounds;
	@NotNull private final PointerValueType<T> to;
	@NotNull private final A type;
	@NotNull private final TypedValue<PointerValueType<A>> pointer;
	@NotNull private final IntegerConstantTypedValue[] indices;

	private GetElementPointerInstruction(final boolean isInbounds, @NotNull final PointerValueType<T> to, @NotNull final A type, @NotNull final TypedValue<PointerValueType<A>> pointer, @NotNull final IntegerConstantTypedValue... indices)
	{
		this.isInbounds = isInbounds;
		this.to = to;
		this.type = type;
		this.pointer = pointer;
		this.indices = indices;
	}

	@NotNull
	@Override
	public PointerValueType<T> to()
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
	protected <X extends Exception> void writeBody(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		if (type instanceof LocallyIdentifiedStructureType)
		{
			final LocalIdentifier identifier = ((LocallyIdentifiedStructureType) type).identifier();
			identifier.write(byteWriter, dataLayoutSpecification);
		}
		else
		{
			type.write(byteWriter, dataLayoutSpecification);
		}

		byteWriter.writeBytes(CommaSpace);
		pointer.write(byteWriter, dataLayoutSpecification);

		for (final IntegerConstantTypedValue integerConstantExpression : indices)
		{
			byteWriter.writeBytes(CommaSpace);
			integerConstantExpression.write(byteWriter, dataLayoutSpecification);
		}
	}

}

