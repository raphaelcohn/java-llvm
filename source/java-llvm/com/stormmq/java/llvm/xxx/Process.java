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

import com.stormmq.java.classfile.domain.information.FieldInformation;
import com.stormmq.java.classfile.domain.uniqueness.FieldUniqueness;
import com.stormmq.java.classfile.processing.typeInformationUsers.TypeInformationTriplet;
import com.stormmq.java.llvm.xxx.happy.*;
import com.stormmq.java.llvm.xxx.typeConverters.*;
import com.stormmq.java.llvm.xxx.typeConverters.typeNameVisitors.*;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.llvm.domain.asm.ModuleLevelInlineAsm;
import com.stormmq.llvm.domain.function.FunctionDeclaration;
import com.stormmq.llvm.domain.function.FunctionDefinition;
import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import com.stormmq.llvm.domain.metadata.creation.*;
import com.stormmq.llvm.domain.metadata.debugging.*;
import com.stormmq.llvm.domain.metadata.metadataTuples.TypedMetadataTuple;
import com.stormmq.llvm.domain.target.Architecture;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.types.SizedType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.*;
import com.stormmq.llvm.domain.variables.Alias;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import com.stormmq.string.AbstractToString;
import com.stormmq.tuples.Triplet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.stormmq.functions.collections.CollectionHelper.add;
import static com.stormmq.java.llvm.xxx.happy.Identifiers.createStaticFieldGlobalIdentifier;
import static com.stormmq.java.llvm.xxx.happy.ToConstantTypedValueFieldConstantUser.toLlvmConstant;
import static com.stormmq.llvm.domain.AddressSpace.GlobalAddressSpace;
import static com.stormmq.llvm.domain.DllStorageClass.NoDllStorageClass;
import static com.stormmq.llvm.domain.Linkage.external;
import static com.stormmq.llvm.domain.ThreadLocalStorageModel.NoThreadLocalStorageModel;
import static com.stormmq.llvm.domain.Visibility._default;
import static java.util.Collections.*;

public final class Process extends AbstractToString
{
	@NotNull private static final Map<Architecture, List<ModuleLevelInlineAsm>> NoModuleLevelInlineAssembly = emptyMap();
	@NotNull private static final Set<Alias> NoAliases = emptySet();

	@NotNull private final TypeInformationTriplet self;
	@NotNull private final DataLayoutSpecification dataLayoutSpecification;
	@NotNull private final MetadataCreator<PackageName> metadataCreator;
	@NotNull private final ClassToStructureMap classToStructureMap;
	private final TypeConverter<TypeMetadata> typeMetadataTypeConverter;
	private final TypeConverter<SizedType> sizedTypeTypeConverter;

	public Process(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final ClassToStructureMap classToStructureMap, @NotNull final MetadataCreator<PackageName> metadataCreator, @NotNull final DebuggingTypeDefinitions<PackageName> debuggingTypeDefinitions, @NotNull final TypeInformationTriplet self)
	{
		this.self = self;
		this.dataLayoutSpecification = dataLayoutSpecification;
		this.metadataCreator = metadataCreator;

		this.classToStructureMap = classToStructureMap;
		typeMetadataTypeConverter = new ToTypeMetadataTypeConverter(new SimpleTypeConverter<>(new ToTypeMetadataTypeNameVisitor(this.classToStructureMap, debuggingTypeDefinitions)), debuggingTypeDefinitions);
		sizedTypeTypeConverter = new SimpleTypeConverter<>(new ToSizedTypeTypeNameVisitor(this.classToStructureMap));
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(self, dataLayoutSpecification, metadataCreator, classToStructureMap, typeMetadataTypeConverter, sizedTypeTypeConverter);
	}

	public void process(@NotNull final ModuleCreatorAndWriter moduleCreatorAndWriter)
	{
		final Set<GlobalVariable<?>> globalVariablesAndConstants = new HashSet<>(self.numberOfStaticFields() + 1);
		final List<DIGlobalVariableKeyedMetadataTuple> debugGlobals = new ArrayList<>(globalVariablesAndConstants.size());
		final Set<LocallyIdentifiedStructureType> structureTypes = new HashSet<>(1);
		final Set<FunctionDefinition> functionsDefinitions = new HashSet<>(self.numberOfStaticMethods() + self.numberOfInstanceMethods());

		processStaticFields(globalVariablesAndConstants, debugGlobals);
		processInstanceFields(structureTypes);
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
			final GlobalVariable<?> globalVariable = add(globals, staticFieldToGlobalVariable(fieldInformation));

			debugGlobals.add(staticFieldToDebugGlobalVariable(fieldInformation, globalVariable));
		});
	}

	@NotNull
	private GlobalVariable<SizedType> staticFieldToGlobalVariable(@NotNull final FieldInformation fieldInformation)
	{
		final GlobalIdentifier staticFieldName = createStaticFieldGlobalIdentifier(ourKnownReferenceTypeName(), fieldInformation);

		final SizedType sizedType = sizedTypeTypeConverter.convertField(fieldInformation);
		@Nullable final ConstantTypedValue<SizedType> initializerConstantTypedValue = toLlvmConstant(fieldInformation, sizedType);
		final int alignment = sizedType.abiAlignmentInBytesToNextNearestPowerOfTwo(dataLayoutSpecification);
		return new GlobalVariable<>(staticFieldName, external, _default, NoDllStorageClass, NoThreadLocalStorageModel, false, GlobalAddressSpace, false, sizedType, initializerConstantTypedValue, null, null, alignment);
	}

	@NotNull
	private DIGlobalVariableKeyedMetadataTuple staticFieldToDebugGlobalVariable(@NotNull final FieldInformation fieldInformation, @NotNull final GlobalVariable<?> globalVariable)
	{
		final FieldUniqueness fieldUniqueness = fieldInformation.fieldUniqueness;

		final TypeMetadata type = typeMetadataTypeConverter.convertField(fieldInformation);
		return metadataCreator.newGlobalVariable(fieldUniqueness.fieldName.name(), type, fieldInformation.isPrivate(), globalVariable);
	}

	private void processInstanceFields(@NotNull final Set<LocallyIdentifiedStructureType> structureTypes)
	{
		final KnownReferenceTypeName className = ourKnownReferenceTypeName();
		final Triplet<SizedLocallyIdentifiedStructureType, NamespacedTypeName<PackageName>, DebuggingFieldDetail<PackageName>[]> details = classToStructureMap.lookUpClassDetails(className);
		final SizedLocallyIdentifiedStructureType sizedLocallyIdentifiedStructureType = details.a;
		structureTypes.add(sizedLocallyIdentifiedStructureType);
	}

	private void processStaticFunctions(@NotNull final Set<FunctionDefinition> functionsDefinitions)
	{
	}

	private void processInstanceFunctions(@NotNull final Set<FunctionDefinition> functionsDefinitions)
	{
	}

	@NotNull
	private KnownReferenceTypeName ourKnownReferenceTypeName()
	{
		return self.thisClassTypeName();
	}
}
