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

package com.stormmq.llvm.examples.parsing.typeInformationUsers;

import com.stormmq.byteWriters.OutputStreamByteWriter;
import com.stormmq.java.classfile.domain.information.TypeInformation;
import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.domain.comdat.ComdatDefinition;
import com.stormmq.llvm.domain.function.FunctionDeclaration;
import com.stormmq.llvm.domain.function.FunctionDefinition;
import com.stormmq.llvm.domain.identifiers.LocalIdentifier;
import com.stormmq.llvm.domain.module.Module;
import com.stormmq.llvm.domain.target.triple.TargetTriple;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.OpaqueStructureType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.StructureType;
import com.stormmq.llvm.domain.variables.Alias;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import com.stormmq.llvm.metadata.debugging.*;
import com.stormmq.llvm.metadata.metadataTuples.AnonymousMetadataTuple;
import com.stormmq.llvm.metadata.metadataTuples.TypedMetadataTuple;
import com.stormmq.llvm.metadata.module.LlvmIdentNamedMetadataTuple;
import com.stormmq.llvm.metadata.module.LlvmModuleFlagsNamedMetadataTuple;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Map;

import static com.stormmq.llvm.domain.target.dataLayout.DataLayoutSpecification.DarwinOnX86_64;
import static com.stormmq.llvm.metadata.debugging.LlvmDebugLanguage.DW_LANG_Java;
import static com.stormmq.llvm.metadata.metadataTuples.AnonymousMetadataTuple.EmptyAnonymousMetadataTuple;
import static com.stormmq.llvm.metadata.metadataTuples.TypedMetadataTuple.emptyTypedMetadataTuple;
import static com.stormmq.llvm.metadata.module.LlvmModuleFlagsNamedMetadataTuple.TypicalLlvmModuleFlags;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.Collections.*;

public final class NaiveTypeInformationUser implements TypeInformationUser
{
	@NotNull private static final List<ComdatDefinition> NoComdatDefinitions = emptyList();
	@NotNull private static final Map<LocalIdentifier, OpaqueStructureType> NoOpaqueStructureTypes = emptyMap();
	@NotNull private static final List<Alias> NoAliases = emptyList();
	@NonNls private static final String dotLL = ".ll";

	@NotNull private final Path outputRootFolderPath;

	public NaiveTypeInformationUser(@NotNull final Path outputRootFolderPath)
	{
		this.outputRootFolderPath = outputRootFolderPath;
	}

	@Override
	public void use(@NotNull final TypeInformation typeInformation, @NotNull final String sourceRootPath, @NotNull final String relativeFilePath)
	{
		final ReferenceTracker referenceTrackerStrings = new ReferenceTracker();
		final ReferenceTracker referenceTrackerMetadataAndListsOfMetadata = referenceTrackerStrings;
		final DIFileKeyedMetadataTuple file = new DIFileKeyedMetadataTuple(referenceTrackerMetadataAndListsOfMetadata, relativeFilePath, sourceRootPath);

		// Will eventually carry some data
		final TypedMetadataTuple<DISubprogramKeyedMetadataTuple> subprograms = emptyTypedMetadataTuple(referenceTrackerMetadataAndListsOfMetadata);
		final TypedMetadataTuple<DIGlobalVariableKeyedMetadataTuple> globals = emptyTypedMetadataTuple(referenceTrackerMetadataAndListsOfMetadata);

		final AnonymousMetadataTuple enums = EmptyAnonymousMetadataTuple(referenceTrackerMetadataAndListsOfMetadata);
		final AnonymousMetadataTuple retainedTypes = EmptyAnonymousMetadataTuple(referenceTrackerMetadataAndListsOfMetadata);
		final TypedMetadataTuple<DIImportedEntityKeyedMetadataTuple> imports = emptyTypedMetadataTuple(referenceTrackerMetadataAndListsOfMetadata);
		final TypedMetadataTuple<DIMacroFileKeyedMetadataTuple> macros = emptyTypedMetadataTuple(referenceTrackerMetadataAndListsOfMetadata);
		final DICompileUnitKeyedMetadataTuple diCompileUnitKeyedMetadataTuple = new DICompileUnitKeyedMetadataTuple(referenceTrackerMetadataAndListsOfMetadata, DW_LANG_Java, file, enums, retainedTypes, subprograms, globals, imports, macros);

		final LlvmIdentNamedMetadataTuple llvmIdent = new LlvmIdentNamedMetadataTuple(referenceTrackerStrings);
		final LlvmModuleFlagsNamedMetadataTuple llvmModuleFlags = TypicalLlvmModuleFlags(referenceTrackerStrings, referenceTrackerMetadataAndListsOfMetadata);
		final List<DICompileUnitKeyedMetadataTuple> compileUnits = singletonList(diCompileUnitKeyedMetadataTuple);
		final LlvmDbgCuNamedMetadataTuple llvmDbgCu = new LlvmDbgCuNamedMetadataTuple(referenceTrackerStrings, compileUnits);

		final Map<LocalIdentifier, StructureType> structureTypes = emptyMap();
		final List<GlobalVariable<?>> globalVariablesAndConstants = emptyList();
		final List<FunctionDeclaration> functionDeclarations = emptyList();
		final List<FunctionDefinition> functionsDefinitions = emptyList();

		final Module module = new Module(DarwinOnX86_64, TargetTriple.MacOsXMavericksOnX86_64, llvmIdent, llvmModuleFlags, llvmDbgCu, NoComdatDefinitions, structureTypes, NoOpaqueStructureTypes, globalVariablesAndConstants, functionDeclarations, functionsDefinitions, NoAliases);

		final Path llvmFilePath = llvmFilePath(relativeFilePath);
		try(final OutputStream outputStream = newOutputStream(llvmFilePath, WRITE, CREATE, TRUNCATE_EXISTING))
		{
			final OutputStreamByteWriter byteWriter = new OutputStreamByteWriter(outputStream);
			module.write(byteWriter);
		}
		catch (final IOException e)
		{
			throw new IllegalStateException("Could not create output file '" + llvmFilePath + '\'', e);
		}
	}

	@NotNull
	private Path llvmFilePath(@NotNull final String relativeFilePath)
	{
		final int lastIndex = relativeFilePath.lastIndexOf('.');
		final String relativeFilePathWithoutFileExtension = lastIndex == -1 ? relativeFilePath : relativeFilePath.substring(0, lastIndex);
		final Path llvmFilePath = outputRootFolderPath.resolve(relativeFilePathWithoutFileExtension + dotLL);
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
