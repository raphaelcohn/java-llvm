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

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.byteWriters.OutputStreamByteWriter;
import com.stormmq.java.classfile.processing.typeInformationUsers.TypeInformationTriplet;
import com.stormmq.llvm.domain.asm.ModuleLevelInlineAsm;
import com.stormmq.llvm.domain.function.*;
import com.stormmq.llvm.domain.identifiers.LocalIdentifier;
import com.stormmq.llvm.domain.module.*;
import com.stormmq.llvm.domain.target.triple.Architecture;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.StructureType;
import com.stormmq.llvm.domain.variables.Alias;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import com.stormmq.llvm.metadata.debugging.*;
import com.stormmq.llvm.metadata.metadataTuples.TypedMetadataTuple;
import com.stormmq.llvm.metadata.module.LlvmIdentNamedMetadataTuple;
import com.stormmq.llvm.metadata.module.LlvmModuleFlagsNamedMetadataTuple;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.*;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.StandardOpenOption.*;

public final class ModuleWriter
{
	@NotNull @NonNls private static final String dotLL = ".ll";

	@NotNull private final TargetModuleCreator targetModuleCreator;
	@NotNull private final Path outputRootFolderPath;

	public ModuleWriter(@NotNull final TargetModuleCreator targetModuleCreator, @NotNull final Path outputRootFolderPath)
	{
		this.targetModuleCreator = targetModuleCreator;
		this.outputRootFolderPath = outputRootFolderPath;
	}

	public void createAndWriteModule(@NotNull final TypeInformationTriplet typeInformationTriplet, @NotNull final MetadataCreator metadataCreator, @NotNull final Map<Architecture, List<ModuleLevelInlineAsm>> moduleLevelInlineAssembly, @NotNull final Map<LocalIdentifier, StructureType> structureTypes, @NotNull final Set<GlobalVariable<?>> globalVariablesAndConstants, @NotNull final Set<FunctionDeclaration> functionDeclarations, @NotNull final Set<FunctionDefinition> functionsDefinitions, @NotNull final TypedMetadataTuple<DISubprogramKeyedMetadataTuple> subprograms, @NotNull final TypedMetadataTuple<DIGlobalVariableKeyedMetadataTuple> globals, @NotNull final Set<Alias> aliases)
	{
		final String relativeFilePath = typeInformationTriplet.relativeFilePath;
		final Path relativeRootFolderPath = typeInformationTriplet.relativeRootFolderPath;

		final Module module = createModule(relativeFilePath, relativeRootFolderPath, metadataCreator, moduleLevelInlineAssembly, structureTypes, globalVariablesAndConstants, functionDeclarations, functionsDefinitions, subprograms, globals, aliases);
		writeModule(relativeFilePath, relativeRootFolderPath, module);
	}

	@NotNull
	private Module createModule(@NotNull final String relativeFilePath, @NotNull final Path relativeRootFolderPath, @NotNull
	final MetadataCreator metadataCreator, @NotNull final Map<Architecture, List<ModuleLevelInlineAsm>> moduleLevelInlineAssembly, @NotNull final Map<LocalIdentifier, StructureType> structureTypes, @NotNull final Set<GlobalVariable<?>> globalVariablesAndConstants, final Set<FunctionDeclaration> functionDeclarations, @NotNull final Set<FunctionDefinition> functionsDefinitions, @NotNull final TypedMetadataTuple<DISubprogramKeyedMetadataTuple> subprograms, @NotNull final TypedMetadataTuple<DIGlobalVariableKeyedMetadataTuple> globals, @NotNull final Set<Alias> aliases)
	{
		final DIFileKeyedMetadataTuple file = metadataCreator.file(relativeFilePath, relativeRootFolderPath.toString());
		final LlvmIdentNamedMetadataTuple identity = metadataCreator.identity();
		final LlvmModuleFlagsNamedMetadataTuple moduleFlags = metadataCreator.moduleFlags();
		final LlvmDbgCuNamedMetadataTuple compileUnits = metadataCreator.newJavaCompileUnits(file, subprograms, globals);

		return targetModuleCreator.newModule(moduleLevelInlineAssembly, identity, moduleFlags, compileUnits, structureTypes, globalVariablesAndConstants, functionDeclarations, functionsDefinitions, aliases);
	}

	private void writeModule(@NotNull final String relativeFilePath, @NotNull final Path relativeRootFolderPath, @NotNull final Module module)
	{
		final Path llvmFilePath = llvmFilePath(relativeFilePath, relativeRootFolderPath);
		try (final OutputStream outputStream = newOutputStream(llvmFilePath, WRITE, CREATE, TRUNCATE_EXISTING))
		{
			final ByteWriter<IOException> byteWriter = new OutputStreamByteWriter(outputStream);
			module.write(byteWriter);
		}
		catch (final IOException e)
		{
			throw new IllegalStateException("Could not create output file '" + llvmFilePath + '\'', e);
		}
	}

	@NotNull
	private Path llvmFilePath(@NotNull final String relativeFilePath, @NotNull final Path relativeRootFolderPath)
	{
		final int lastIndex = relativeFilePath.lastIndexOf('.');
		final String relativeFilePathWithoutFileExtension = lastIndex == -1 ? relativeFilePath : relativeFilePath.substring(0, lastIndex);
		final Path llvmFilePath = outputRootFolderPath.resolve(relativeRootFolderPath).resolve(relativeFilePathWithoutFileExtension + dotLL);
		final Path parentFolder = llvmFilePath.getParent();
		try
		{
			createDirectories(parentFolder);
		}
		catch (final IOException e)
		{
			throw new IllegalStateException("Could not create output file parent folder '" + parentFolder + '\'', e);
		}
		return llvmFilePath;
	}
}
