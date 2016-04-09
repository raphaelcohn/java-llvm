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

import com.stormmq.java.classfile.processing.Records;
import com.stormmq.java.classfile.processing.TypeInformationTripletUser;
import com.stormmq.java.classfile.processing.typeInformationUsers.TypeInformationTriplet;
import com.stormmq.java.llvm.xxx.debugging.MetadataCreator;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.llvm.domain.asm.ModuleLevelInlineAsm;
import com.stormmq.llvm.domain.function.FunctionDeclaration;
import com.stormmq.llvm.domain.function.FunctionDefinition;
import com.stormmq.llvm.domain.target.triple.Architecture;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.LocallyIdentifiedStructureType;
import com.stormmq.llvm.domain.variables.Alias;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import com.stormmq.llvm.metadata.debugging.DIGlobalVariableKeyedMetadataTuple;
import com.stormmq.llvm.metadata.debugging.DISubprogramKeyedMetadataTuple;
import com.stormmq.llvm.metadata.metadataTuples.TypedMetadataTuple;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;

public final class JavaConvertingTypeInformationTripletUser implements TypeInformationTripletUser
{
	@NotNull private static final Map<Architecture, List<ModuleLevelInlineAsm>> NoModuleLevelInlineAssembly = emptyMap();
	@NotNull private static final Set<Alias> NoAliases = emptySet();

	@NotNull private final ModuleCreatorAndWriter moduleCreatorAndWriter;

	public JavaConvertingTypeInformationTripletUser(@NotNull final ModuleCreatorAndWriter moduleCreatorAndWriter)
	{
		this.moduleCreatorAndWriter = moduleCreatorAndWriter;
	}

	@Override
	public void use(@NotNull final Records records, @NotNull final TypeInformationTriplet typeInformationTriplet)
	{
		final Process process = new Process(records, typeInformationTriplet);


		process.process();

		final Set<GlobalVariable<?>> globalVariablesAndConstants = process.globalVariables();
		final Set<LocallyIdentifiedStructureType> locallyIdentifiedStructureTypes = process.structureTypes();

		final Set<FunctionDeclaration> functionDeclarations = emptySet();
		final Set<FunctionDefinition> functionsDefinitions = emptySet();

		final MetadataCreator<PackageName> metadataCreator = new MetadataCreator<>(typeInformationTriplet.relativeFilePath, typeInformationTriplet.relativeRootFolderPath);
		final TypedMetadataTuple<DISubprogramKeyedMetadataTuple> subprograms = metadataCreator.emptyTypedMetadataTuple();
		final TypedMetadataTuple<DIGlobalVariableKeyedMetadataTuple> globals = metadataCreator.emptyTypedMetadataTuple();

		moduleCreatorAndWriter.createAndWriteModule(typeInformationTriplet, metadataCreator, NoModuleLevelInlineAssembly, locallyIdentifiedStructureTypes, globalVariablesAndConstants, functionDeclarations, functionsDefinitions, subprograms, globals, NoAliases);
	}

}