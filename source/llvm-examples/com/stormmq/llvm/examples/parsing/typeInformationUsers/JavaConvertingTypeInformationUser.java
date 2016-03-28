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

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.byteWriters.OutputStreamByteWriter;
import com.stormmq.java.classfile.domain.information.TypeInformation;
import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.domain.function.FunctionDeclaration;
import com.stormmq.llvm.domain.function.FunctionDefinition;
import com.stormmq.llvm.domain.identifiers.LocalIdentifier;
import com.stormmq.llvm.domain.module.*;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.StructureType;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import com.stormmq.llvm.metadata.debugging.*;
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

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.Collections.*;

public final class JavaConvertingTypeInformationUser implements TypeInformationUser
{
	@NonNls private static final String dotLL = ".ll";

	@NotNull private final TargetModuleCreator targetModuleCreator;
	@NotNull private final Path outputRootFolderPath;

	public JavaConvertingTypeInformationUser(@NotNull final TargetModuleCreator targetModuleCreator, @NotNull final Path outputRootFolderPath)
	{
		this.targetModuleCreator = targetModuleCreator;
		this.outputRootFolderPath = outputRootFolderPath;
	}

	@Override
	public void use(@NotNull final TypeInformation typeInformation, @NotNull final String relativeFilePath, @NotNull final Path relativeRootFolderPath)
	{
		final MetadataCreator metadataCreator = new MetadataCreator();
		final DIFileKeyedMetadataTuple file = metadataCreator.file(relativeFilePath, relativeRootFolderPath.toString());

		// Will eventually carry some data
		final TypedMetadataTuple<DISubprogramKeyedMetadataTuple> subprograms = metadataCreator.emptyTypedMetadataTuple();
		final TypedMetadataTuple<DIGlobalVariableKeyedMetadataTuple> globals = metadataCreator.emptyTypedMetadataTuple();

		final Map<LocalIdentifier, StructureType> structureTypes = emptyMap();
		final List<GlobalVariable<?>> globalVariablesAndConstants = emptyList();
		final List<FunctionDeclaration> functionDeclarations = emptyList();
		final List<FunctionDefinition> functionsDefinitions = emptyList();

		final LlvmIdentNamedMetadataTuple identity = metadataCreator.identity();
		final LlvmModuleFlagsNamedMetadataTuple moduleFlags = metadataCreator.moduleFlags();
		final LlvmDbgCuNamedMetadataTuple compileUnits = metadataCreator.newJavaCompileUnits(file, subprograms, globals);
		final Module module = targetModuleCreator.newJavaModule(identity, moduleFlags, compileUnits, structureTypes, globalVariablesAndConstants, functionDeclarations, functionsDefinitions);

		writeModule(relativeFilePath, relativeRootFolderPath, module);
	}

	private void writeModule(@NotNull final String relativeFilePath, @NotNull final Path relativeRootFolderPath, final Module module)
	{
		final Path llvmFilePath = llvmFilePath(relativeFilePath, relativeRootFolderPath);
		try(final OutputStream outputStream = newOutputStream(llvmFilePath, WRITE, CREATE, TRUNCATE_EXISTING))
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
