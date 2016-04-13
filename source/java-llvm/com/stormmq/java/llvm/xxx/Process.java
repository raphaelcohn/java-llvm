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
import com.stormmq.java.classfile.domain.information.FieldInformation;
import com.stormmq.java.classfile.domain.uniqueness.FieldUniqueness;
import com.stormmq.java.classfile.processing.Records;
import com.stormmq.java.classfile.processing.typeInformationUsers.TypeInformationTriplet;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.llvm.domain.asm.ModuleLevelInlineAsm;
import com.stormmq.llvm.domain.function.FunctionDeclaration;
import com.stormmq.llvm.domain.function.FunctionDefinition;
import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import com.stormmq.llvm.domain.metadata.creation.DebuggingTypeDefinitions;
import com.stormmq.llvm.domain.metadata.creation.MetadataCreator;
import com.stormmq.llvm.domain.metadata.debugging.*;
import com.stormmq.llvm.domain.metadata.metadataTuples.TypedMetadataTuple;
import com.stormmq.llvm.domain.target.Architecture;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.types.SizedType;
import com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.*;
import com.stormmq.llvm.domain.variables.Alias;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.stormmq.functions.CollectionHelper.add;
import static com.stormmq.functions.CollectionHelper.addOnce;
import static com.stormmq.java.llvm.xxx.ToLlvmConstantFieldConstantUser.toLlvmConstant;
import static com.stormmq.llvm.domain.AddressSpace.GlobalAddressSpace;
import static com.stormmq.llvm.domain.DllStorageClass.NoDllStorageClass;
import static com.stormmq.llvm.domain.Linkage.external;
import static com.stormmq.llvm.domain.Linkage.linkonce_odr;
import static com.stormmq.llvm.domain.ThreadLocalStorageModel.NoThreadLocalStorageModel;
import static com.stormmq.llvm.domain.Visibility._default;
import static com.stormmq.llvm.domain.instructions.PointerToIntegerInstruction.offsetOfFieldInStructureTypeConstantTypedValue;
import static com.stormmq.llvm.domain.instructions.PointerToIntegerInstruction.sizeOfStructureTypeConstantTypedValue;
import static java.util.Collections.*;

public final class Process
{
	private static final int BestGuessOfNumberOfParentFields = 64;
	@NotNull private static final Map<Architecture, List<ModuleLevelInlineAsm>> NoModuleLevelInlineAssembly = emptyMap();
	@NotNull private static final Set<Alias> NoAliases = emptySet();

	@NotNull private final Records records;
	@NotNull private final TypeInformationTriplet self;
	@NotNull private final DataLayoutSpecification dataLayoutSpecification;
	@NotNull private final MetadataCreator<PackageName> metadataCreator;
	@NotNull private final ReferencedClasses referencedClasses;
	@NotNull private final UsefulRecords usefulRecords;

	public Process(@NotNull final Records records, @NotNull final TypeInformationTriplet self, @NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final MetadataCreator<PackageName> metadataCreator)
	{
		this.records = records;
		this.self = self;
		this.dataLayoutSpecification = dataLayoutSpecification;
		this.metadataCreator = metadataCreator;

		referencedClasses = new ReferencedClasses(self);
		usefulRecords = new UsefulRecords(records);
	}

	public void process(@NotNull final ModuleCreatorAndWriter moduleCreatorAndWriter)
	{
		final Set<GlobalVariable<?>> globalVariablesAndConstants = new HashSet<>(self.numberOfStaticFields() + 1);
		final List<DIGlobalVariableKeyedMetadataTuple> debugGlobals = new ArrayList<>(globalVariablesAndConstants.size());
		final Set<LocallyIdentifiedStructureType> structureTypes = new HashSet<>(referencedClasses.size() + 1);
		final Set<FunctionDefinition> functionsDefinitions = new HashSet<>(self.numberOfStaticMethods() + self.numberOfInstanceMethods());

		processStaticFields(globalVariablesAndConstants, debugGlobals);
		processInstanceFields(structureTypes, globalVariablesAndConstants);
		processStaticFunctions(functionsDefinitions);
		processInstanceFunctions(functionsDefinitions);

		final Set<FunctionDeclaration> functionDeclarations = emptySet();
		final TypedMetadataTuple<DISubprogramKeyedMetadataTuple> subprograms = metadataCreator.emptyTypedMetadataTuple();
		final TypedMetadataTuple<DIGlobalVariableKeyedMetadataTuple> globals = metadataCreator.typedMetadataTuple(debugGlobals);

		moduleCreatorAndWriter.createAndWriteModule(self, metadataCreator, NoModuleLevelInlineAssembly, structureTypes, globalVariablesAndConstants, functionDeclarations, functionsDefinitions, subprograms, globals, NoAliases);
	}

	private void processStaticFields(@NotNull final Set<GlobalVariable<?>> globals, @NotNull final List<DIGlobalVariableKeyedMetadataTuple> debugGlobals)
	{
		self.forEachStaticField((fieldInformation) ->
		{
			final FieldUniqueness fieldUniqueness = fieldInformation.fieldUniqueness;
			final GlobalVariable<?> globalVariable = add(globals, staticFieldToGlobalVariable(fieldUniqueness, fieldInformation));

			final InternalTypeName internalTypeName = fieldUniqueness.fieldDescriptor.internalTypeName;
			final TypeMetadata resolved = typeConverter.convertInternalTypeNameToLlvmTypeMetadata(internalTypeName);

			final TypeMetadata type = globalVariable.isConstant() ? metadataCreator.constantDerivedType(resolved) : resolved;
			debugGlobals.add(metadataCreator.newGlobalVariable(fieldUniqueness.fieldName.name(), type, fieldInformation.isPrivate(), globalVariable));
		});
	}

	@NotNull
	private <T extends SizedType> GlobalVariable<T> staticFieldToGlobalVariable(@NotNull final FieldUniqueness fieldUniqueness, @NotNull final FieldInformation fieldInformation)
	{
		final GlobalIdentifier staticFieldName = referencedClasses.staticFieldGlobalIdentifier(fieldUniqueness);
		@SuppressWarnings("unchecked") final T llvmType = (T) typeConverter.convertFieldToLlvmType(fieldUniqueness);
		@Nullable final ConstantTypedValue<T> initializerConstantTypedValue = toLlvmConstant(fieldInformation, llvmType);
		final int alignment = llvmType.abiAlignmentInBytesToNextNearestPowerOfTwo(dataLayoutSpecification);
		return new GlobalVariable<>(staticFieldName, external, _default, NoDllStorageClass, NoThreadLocalStorageModel, false, GlobalAddressSpace, false, llvmType, initializerConstantTypedValue, null, null, alignment);
	}

	private void processInstanceFields(@NotNull final Set<LocallyIdentifiedStructureType> structureTypes, @NotNull final Set<GlobalVariable<?>> globals)
	{
		final SizedLocallyIdentifiedStructureType thisClassIdentifiedStructureType = referencedClasses.newIdentifiedStructureType(isPacked, fieldTypes);
		final String name = self.thisClassTypeName().name();

		final int length = fieldTypes.length;
		for (int index = 0; index < length; index++)
		{
			offsetOf(globals, thisClassIdentifiedStructureType, name, fieldTypes, index);
		}
		sizeOf(globals, thisClassIdentifiedStructureType, name);

		referencedClasses.referencedClassesOtherThanThis(referencedClassOtherThanThis -> addOnce(structureTypes, new OpaqueLocallyIdentifiedStructureType(referencedClassOtherThanThis)));
		addOnce(structureTypes, thisClassIdentifiedStructureType);
	}

	private void offsetOf(@NotNull final Set<GlobalVariable<?>> globals, @NotNull final SizedLocallyIdentifiedStructureType thisClassIdentifiedStructureType, @NotNull final String name, @NotNull final SizedType[] fieldTypes, final int index)
	{
		final SizedType fieldType = fieldTypes[index];
		final GlobalIdentifier globalIdentifier = new GlobalIdentifier("offsetof." + index + '.' + name);
		final ConstantTypedValue<IntegerValueType> offsetExpression = offsetOfFieldInStructureTypeConstantTypedValue(thisClassIdentifiedStructureType, fieldType, index);
		final IntegerValueType to = offsetExpression.to();
		globals.add(new GlobalVariable<>(globalIdentifier, linkonce_odr, _default, NoDllStorageClass, NoThreadLocalStorageModel, true, GlobalAddressSpace, false, to, offsetExpression, null, null, to.abiAlignmentInBytesToNextNearestPowerOfTwo(dataLayoutSpecification)));
	}

	private void sizeOf(@NotNull final Set<GlobalVariable<?>> globals, @NotNull final SizedLocallyIdentifiedStructureType thisClassIdentifiedStructureType, @NotNull final String name)
	{
		final ConstantTypedValue<IntegerValueType> sizeOfExpression = sizeOfStructureTypeConstantTypedValue(thisClassIdentifiedStructureType);
		final GlobalIdentifier globalIdentifier = new GlobalIdentifier("sizeof." + name);
		final IntegerValueType to = sizeOfExpression.to();
		globals.add(new GlobalVariable<>(globalIdentifier, linkonce_odr, _default, NoDllStorageClass, NoThreadLocalStorageModel, true, GlobalAddressSpace, false, to, sizeOfExpression, null, null, to.abiAlignmentInBytesToNextNearestPowerOfTwo(dataLayoutSpecification)));
	}

	private void processStaticFunctions(@NotNull final Set<FunctionDefinition> functionsDefinitions)
	{
	}

	private void processInstanceFunctions(@NotNull final Set<FunctionDefinition> functionsDefinitions)
	{
	}
}
