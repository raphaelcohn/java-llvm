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

package com.stormmq.llvm.domain.module;

import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.types.SizedType;
import com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.SizedLocallyIdentifiedStructureType;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import org.jetbrains.annotations.*;

import static com.stormmq.llvm.domain.AddressSpace.GlobalAddressSpace;
import static com.stormmq.llvm.domain.DllStorageClass.NoDllStorageClass;
import static com.stormmq.llvm.domain.Linkage.linkonce_odr;
import static com.stormmq.llvm.domain.ThreadLocalStorageModel.NoThreadLocalStorageModel;
import static com.stormmq.llvm.domain.Visibility._default;
import static com.stormmq.llvm.domain.instructions.PointerToIntegerInstruction.offsetOfFieldInStructureTypeConstantTypedValue;
import static com.stormmq.llvm.domain.instructions.PointerToIntegerInstruction.sizeOfStructureTypeConstantTypedValue;

public final class StructureHelper
{
	@NotNull
	public static GlobalVariable<IntegerValueType> offsetOf(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedLocallyIdentifiedStructureType thisClassIdentifiedStructureType, @NotNull final String name, @NotNull final SizedType[] fieldTypes, final int index)
	{
		final SizedType fieldType = fieldTypes[index];
		final GlobalIdentifier globalIdentifier = new GlobalIdentifier("offsetof." + index + '.' + name);
		final ConstantTypedValue<IntegerValueType> offsetExpression = offsetOfFieldInStructureTypeConstantTypedValue(thisClassIdentifiedStructureType, fieldType, index);
		final IntegerValueType to = offsetExpression.to();
		return new GlobalVariable<>(globalIdentifier, linkonce_odr, _default, NoDllStorageClass, NoThreadLocalStorageModel, true, GlobalAddressSpace, false, to, offsetExpression, null, null, to.abiAlignmentInBytesToNextNearestPowerOfTwo(dataLayoutSpecification));
	}

	@NotNull
	public static GlobalVariable<IntegerValueType> sizeOf(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedLocallyIdentifiedStructureType thisClassIdentifiedStructureType, @NotNull final String name)
	{
		final ConstantTypedValue<IntegerValueType> sizeOfExpression = sizeOfStructureTypeConstantTypedValue(thisClassIdentifiedStructureType);
		final GlobalIdentifier globalIdentifier = new GlobalIdentifier("sizeof." + name);
		final IntegerValueType to = sizeOfExpression.to();
		return new GlobalVariable<>(globalIdentifier, linkonce_odr, _default, NoDllStorageClass, NoThreadLocalStorageModel, true, GlobalAddressSpace, false, to, sizeOfExpression, null, null, to.abiAlignmentInBytesToNextNearestPowerOfTwo(dataLayoutSpecification));
	}

	private StructureHelper()
	{
	}
}
