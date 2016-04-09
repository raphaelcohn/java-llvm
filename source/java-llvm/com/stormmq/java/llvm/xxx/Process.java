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

import com.stormmq.functions.ListHelper;
import com.stormmq.java.classfile.domain.InternalTypeName;
import com.stormmq.java.classfile.domain.information.FieldInformation;
import com.stormmq.java.classfile.domain.uniqueness.FieldUniqueness;
import com.stormmq.java.classfile.processing.Records;
import com.stormmq.java.classfile.processing.typeInformationUsers.TypeInformationTriplet;
import com.stormmq.java.llvm.api.Packed;
import com.stormmq.java.llvm.xxx.debugging.MetadataCreator;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.llvm.domain.*;
import com.stormmq.llvm.domain.instructions.PointerToIntegerInstruction;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import com.stormmq.llvm.domain.types.*;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.*;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import com.stormmq.llvm.metadata.debugging.DIGlobalVariableKeyedMetadataTuple;
import com.stormmq.llvm.metadata.debugging.TypeMetadata;
import org.jetbrains.annotations.*;

import java.util.*;

import static com.stormmq.functions.CollectionHelper.*;
import static com.stormmq.java.llvm.xxx.ToLlvmConstantFieldConstantUser.toLlvmConstant;
import static com.stormmq.llvm.domain.AddressSpace.DefaultAddressSpace;
import static com.stormmq.llvm.domain.DllStorageClass.NoDllStorageClass;
import static com.stormmq.llvm.domain.Linkage.external;
import static com.stormmq.llvm.domain.ThreadLocalStorageModel.NoThreadLocalStorageModel;
import static com.stormmq.llvm.domain.Visibility._default;
import static com.stormmq.llvm.domain.Writable.AutomaticAlignment;
import static java.util.Collections.reverse;

public final class Process
{
	private static final int BestGuessOfNumberOfParentFields = 64;
	@NotNull private static final Type[] EmptyTypes = new Type[0];

	@NotNull private final Records records;
	@NotNull private final TypeInformationTriplet self;
	@NotNull private final MetadataCreator<PackageName> metadataCreator;
	@NotNull private final ReferencedClasses referencedClasses;

	public Process(@NotNull final Records records, @NotNull final TypeInformationTriplet self, @NotNull final MetadataCreator<PackageName> metadataCreator)
	{
		this.records = records;
		this.self = self;
		this.metadataCreator = metadataCreator;

		referencedClasses = new ReferencedClasses(self);
	}

	public void process()
	{
		final Set<GlobalVariable<?>> globals = new HashSet<>(self.numberOfStaticFields() + 1);
		final List<DIGlobalVariableKeyedMetadataTuple> debugGlobals = new ArrayList<>(globals.size());
		final Set<StructureType> structureTypes = new HashSet<>(referencedClasses.size() + 1);

		processStaticFields(globals, debugGlobals);
		processInstanceFields(structureTypes);
	}

	private void processStaticFields(@NotNull final Set<GlobalVariable<? super CanBePointedToType>> globals, @NotNull final List<DIGlobalVariableKeyedMetadataTuple> debugGlobals)
	{
		self.forEachStaticField((fieldInformation) ->
		{
			final FieldUniqueness fieldUniqueness = fieldInformation.fieldUniqueness;
			final GlobalVariable<?> globalVariable = add(globals, staticFieldToGlobalVariable(fieldUniqueness, fieldInformation));

			final InternalTypeName internalTypeName = fieldUniqueness.fieldDescriptor.internalTypeName;
			final TypeMetadata resolved = referencedClasses.convertInternalTypeNameToLlvmTypeMetadata(internalTypeName, debuggingTypeDefinitions);

			final TypeMetadata type = globalVariable.isConstant() ? metadataCreator.constantDerivedType(resolved) : resolved;
			debugGlobals.add(metadataCreator.newGlobalVariable(fieldUniqueness.fieldName.name(), type, fieldInformation.isPrivate(), globalVariable));
		});
	}

	@NotNull
	private <T extends CanBePointedToType> GlobalVariable<T> staticFieldToGlobalVariable(@NotNull final FieldUniqueness fieldUniqueness, @NotNull final FieldInformation fieldInformation)
	{
		final GlobalIdentifier staticFieldName = referencedClasses.staticFieldGlobalIdentifier(fieldUniqueness);
		final T llvmType = referencedClasses.convertFieldToLlvmType(fieldUniqueness);
		@Nullable final ConstantTypedValue<T> initializerConstantTypedValue = toLlvmConstant(fieldInformation, llvmType);
		return new GlobalVariable<>(staticFieldName, external, _default, NoDllStorageClass, NoThreadLocalStorageModel, false, DefaultAddressSpace, false, llvmType, initializerConstantTypedValue, null, null, AutomaticAlignment);
	}

	private void processInstanceFields(@NotNull final Set<StructureType> structureTypes)
	{
		final boolean isPacked = isPacked();
		final Type[] fieldTypes = fieldTypesIncludingSuperclasses();
		final IdentifiedLocallyIdentifiedStructureType thisClassIdentifiedStructureType = referencedClasses.newIdentifiedStructureType(isPacked, fieldTypes);

		final int length = fieldTypes.length;
		for (int index = 0; index < length; index++)
		{
			final Type fieldType = fieldTypes[index];
			PointerToIntegerInstruction.offsetOfFieldInStructureTypeConstantTypedValue()
		}

		referencedClasses.referencedClassesOtherThanThis(referencedClassOtherThanThis -> addOnce(structureTypes, new OpaqueLocallyIdentifiedStructureType(referencedClassOtherThanThis)));
		addOnce(structureTypes, thisClassIdentifiedStructureType);
	}

	private boolean isPacked()
	{
		return records.hasAnnotation(self, Packed.class);
	}

	@NotNull
	private Type[] fieldTypesIncludingSuperclasses()
	{
		final List<Type> fieldTypesInWrongOrderX = ListHelper.newArrayList(self.numberOfInstanceFields() + BestGuessOfNumberOfParentFields, fieldTypesInWrongOrder ->
		{
			records.loopOverSelfAndSuperclasses(self, value ->
			{
				final List<FieldInformation> fields =
				value.forEachInstanceField((fieldInformation) ->
				{
					final FieldUniqueness fieldUniqueness = fieldInformation.fieldUniqueness;
					final Type type = referencedClasses.convertFieldToLlvmType(fieldUniqueness);
					fieldTypesInWrongOrder.add(type);
				});
				return false;
			});
			reverse(fieldTypesInWrongOrder);
		});
		return ListHelper.listToArray(fieldTypesInWrongOrderX, Type[]::new, EmptyTypes);
	}
}
