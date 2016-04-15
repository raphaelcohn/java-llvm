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

package com.stormmq.java.llvm.xxx.happy;

import com.stormmq.java.classfile.processing.typeInformationUsers.TypeInformationTriplet;
import com.stormmq.llvm.domain.asm.ModuleLevelInlineAsm;
import com.stormmq.llvm.domain.function.FunctionDeclaration;
import com.stormmq.llvm.domain.function.FunctionDefinition;
import com.stormmq.llvm.domain.metadata.creation.MetadataCreator;
import com.stormmq.llvm.domain.metadata.debugging.*;
import com.stormmq.llvm.domain.metadata.metadataTuples.TypedMetadataTuple;
import com.stormmq.llvm.domain.metadata.module.LlvmIdentNamedMetadataTuple;
import com.stormmq.llvm.domain.metadata.module.LlvmModuleFlagsNamedMetadataTuple;
import com.stormmq.llvm.domain.module.*;
import com.stormmq.llvm.domain.target.Architecture;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.LocallyIdentifiedStructureType;
import com.stormmq.llvm.domain.variables.Alias;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import com.stormmq.string.AbstractToString;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.*;

public final class ModuleCreatorAndWriter extends AbstractToString
{
	@NotNull private final TargetModuleCreator targetModuleCreator;
	@NotNull private final ModuleWriter moduleWriter;

	public ModuleCreatorAndWriter(@NotNull final TargetModuleCreator targetModuleCreator, @NotNull final ModuleWriter moduleWriter)
	{
		this.targetModuleCreator = targetModuleCreator;
		this.moduleWriter = moduleWriter;
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(targetModuleCreator, moduleWriter);
	}

	public void createAndWriteModule(@NotNull final TypeInformationTriplet typeInformationTriplet, @NotNull final MetadataCreator<?> metadataCreator, @NotNull final Map<Architecture, List<ModuleLevelInlineAsm>> moduleLevelInlineAssembly, @NotNull final Set<LocallyIdentifiedStructureType> locallyIdentifiedStructureTypes, @NotNull final Set<GlobalVariable<?>> globalVariablesAndConstants, @NotNull final Set<FunctionDeclaration> functionDeclarations, @NotNull final Set<FunctionDefinition> functionsDefinitions, @NotNull final TypedMetadataTuple<DISubprogramKeyedMetadataTuple> subprograms, @NotNull final TypedMetadataTuple<DIGlobalVariableKeyedMetadataTuple> globals, @NotNull final Set<Alias> aliases)
	{
		final String relativeFilePath = typeInformationTriplet.relativeFilePath;
		final Path relativeRootFolderPath = typeInformationTriplet.relativeRootFolderPath;

		final LlvmIdentNamedMetadataTuple identity = metadataCreator.identity();
		final LlvmModuleFlagsNamedMetadataTuple moduleFlags = metadataCreator.moduleFlags();
		final LlvmDbgCuNamedMetadataTuple compileUnits = metadataCreator.newSimpleCompileUnits(subprograms, globals);
		final Module module = targetModuleCreator.newModule(moduleLevelInlineAssembly, identity, moduleFlags, compileUnits, locallyIdentifiedStructureTypes, globalVariablesAndConstants, functionDeclarations, functionsDefinitions, aliases);

		moduleWriter.writeModule(relativeFilePath, relativeRootFolderPath, module);
	}
}
