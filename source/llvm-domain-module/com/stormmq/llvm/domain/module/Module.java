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

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.byteWriters.Writable;
import com.stormmq.llvm.domain.LlvmWritable;
import com.stormmq.llvm.domain.ObjectFileFormat;
import com.stormmq.llvm.domain.asm.ModuleLevelInlineAsm;
import com.stormmq.llvm.domain.comdat.ComdatDefinition;
import com.stormmq.llvm.domain.comdat.MayHaveComdatDefinition;
import com.stormmq.llvm.domain.function.FunctionDeclaration;
import com.stormmq.llvm.domain.function.FunctionDefinition;
import com.stormmq.llvm.domain.identifiers.*;
import com.stormmq.llvm.domain.target.*;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.LocallyIdentifiedStructureType;
import com.stormmq.llvm.domain.variables.Alias;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import com.stormmq.llvm.domain.metadata.debugging.LlvmDbgCuNamedMetadataTuple;
import com.stormmq.llvm.domain.metadata.module.LlvmIdentNamedMetadataTuple;
import com.stormmq.llvm.domain.metadata.module.LlvmModuleFlagsNamedMetadataTuple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

import static com.stormmq.functions.CollectionHelper.addOnce;
import static com.stormmq.llvm.domain.LlvmWritable.SpaceEqualsSpace;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

public final class Module implements Writable
{
	@NotNull private static final List<ModuleLevelInlineAsm> NoModuleLevelInlineAsm = emptyList();
	@NotNull private static final Set<ComdatDefinition> EmptyComdatDefinitions = emptySet();

	@NotNull private final WritableDataLayoutSpecificationAndTargetTriple writableDataLayoutSpecificationAndTargetTriple;
	@NotNull private final List<ModuleLevelInlineAsm> moduleLevelInlineAssembly;
	@NotNull private final LlvmIdentNamedMetadataTuple llvmIdentNamedMetadataTuple;
	@NotNull private final LlvmModuleFlagsNamedMetadataTuple llvmModuleFlagsNamedMetadataTuple;
	@NotNull private final LlvmDbgCuNamedMetadataTuple compileUnits;
	@NotNull private final Set<ComdatDefinition> comdatDefinitions;
	@NotNull private final Set<LocallyIdentifiedStructureType> locallyIdentifiedStructureTypes;
	@NotNull private final Set<GlobalVariable<?>> globalVariablesAndConstants;
	@NotNull private final Set<FunctionDeclaration> functionDeclarations;
	@NotNull private final Set<FunctionDefinition> functionDefinitions;
	@NotNull private final Set<Alias> aliases;

	public Module(@NotNull final WritableDataLayoutSpecificationAndTargetTriple writableDataLayoutSpecificationAndTargetTriple, @NotNull final Map<Architecture, List<ModuleLevelInlineAsm>> moduleLevelInlineAssembly, @NotNull final LlvmIdentNamedMetadataTuple llvmIdentNamedMetadataTuple, @NotNull final LlvmModuleFlagsNamedMetadataTuple llvmModuleFlagsNamedMetadataTuple, @NotNull final LlvmDbgCuNamedMetadataTuple compileUnits, @NotNull final Set<LocallyIdentifiedStructureType> locallyIdentifiedStructureTypes, @NotNull final Set<GlobalVariable<?>> globalVariablesAndConstants, @NotNull final Set<FunctionDeclaration> functionDeclarations, @NotNull final Set<FunctionDefinition> functionDefinitions, @NotNull final Set<Alias> aliases)
	{
		guardDuplicateIdentifiers(globalVariablesAndConstants, functionDeclarations, functionDefinitions, aliases);

		this.writableDataLayoutSpecificationAndTargetTriple = writableDataLayoutSpecificationAndTargetTriple;
		this.moduleLevelInlineAssembly = writableDataLayoutSpecificationAndTargetTriple.chooseForArchitecture(moduleLevelInlineAssembly, NoModuleLevelInlineAsm);
		this.llvmIdentNamedMetadataTuple = llvmIdentNamedMetadataTuple;
		this.llvmModuleFlagsNamedMetadataTuple = llvmModuleFlagsNamedMetadataTuple;
		this.compileUnits = compileUnits;
		comdatDefinitions = findComdatDefinitions(globalVariablesAndConstants, functionDefinitions);
		this.locallyIdentifiedStructureTypes = locallyIdentifiedStructureTypes;
		this.globalVariablesAndConstants = globalVariablesAndConstants;
		this.functionDeclarations = functionDeclarations;
		this.functionDefinitions = functionDefinitions;
		this.aliases = aliases;
	}

	private static void guardDuplicateIdentifiers(@NotNull final Collection<GlobalVariable<?>> globalVariablesAndConstants, @NotNull final Collection<FunctionDeclaration> functionDeclarations, @NotNull final Collection<FunctionDefinition> functionDefinitions, @NotNull final Collection<Alias> aliases)
	{
		final Collection<GlobalIdentifier> globalIdentifiers = new HashSet<>(globalVariablesAndConstants.size() + functionDeclarations.size() + functionDefinitions.size() + aliases.size());
		guardDuplicateGlobalIdentifier(globalIdentifiers, globalVariablesAndConstants);
		guardDuplicateGlobalIdentifier(globalIdentifiers, functionDeclarations);
		guardDuplicateGlobalIdentifier(globalIdentifiers, functionDefinitions);
		guardDuplicateGlobalIdentifier(globalIdentifiers, aliases);
	}

	private static void guardDuplicateGlobalIdentifier(@NotNull final Collection<GlobalIdentifier> globalIdentifiers, @NotNull final Iterable<? extends GloballyIdentified> globalVariablesAndConstants)
	{
		for (final GloballyIdentified globallyIdentifier : globalVariablesAndConstants)
		{
			final GlobalIdentifier globalIdentifier = globallyIdentifier.globalIdentifier();
			addOnce(globalIdentifiers, globalIdentifier);
		}
	}

	@NotNull
	private Set<ComdatDefinition> findComdatDefinitions(@NotNull final Set<GlobalVariable<?>> globalVariablesAndConstants, @NotNull final Set<FunctionDefinition> functionDefinitions)
	{
		final int initialCapacity = globalVariablesAndConstants.size() + functionDefinitions.size();
		if (initialCapacity == 0)
		{
			return EmptyComdatDefinitions;
		}

		final Set<ComdatDefinition> comdatDefinitions = new HashSet<>(initialCapacity);

		final ObjectFileFormat objectFileFormat = writableDataLayoutSpecificationAndTargetTriple.objectFileFormat();
		final Consumer<ComdatDefinition> consumer = comdatDefinition ->
		{
			if (comdatDefinition != null)
			{
				@Nullable final ComdatDefinition adjusted = comdatDefinition.adjustComdatDefinition(objectFileFormat);
				if (adjusted != null)
				{
					comdatDefinitions.add(adjusted);
				}
			}
		};
		globalVariablesAndConstants.forEach(mayHaveComdatDefinition -> mayHaveComdatDefinition.useComdatDefinition(consumer));
		functionDefinitions.forEach(mayHaveComdatDefinition -> mayHaveComdatDefinition.useComdatDefinition(consumer));

		if (comdatDefinitions.isEmpty())
		{
			return EmptyComdatDefinitions;
		}

		// Done to reduce potentially explosive memory usage if lots of modules are created
		return new HashSet<>(comdatDefinitions);
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		writableDataLayoutSpecificationAndTargetTriple.write(byteWriter);
		byteWriter.writeLineFeed();

		write(byteWriter, moduleLevelInlineAssembly);

		llvmIdentNamedMetadataTuple.write(byteWriter, writableDataLayoutSpecificationAndTargetTriple);
		llvmModuleFlagsNamedMetadataTuple.write(byteWriter, writableDataLayoutSpecificationAndTargetTriple);
		compileUnits.write(byteWriter, writableDataLayoutSpecificationAndTargetTriple);
		byteWriter.writeLineFeed();

		write(byteWriter, comdatDefinitions);

		writeX(byteWriter, locallyIdentifiedStructureTypes);

		write(byteWriter, globalVariablesAndConstants);

		write(byteWriter, functionDeclarations);

		write(byteWriter, functionDefinitions);

		write(byteWriter, aliases);
	}

	private <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter, @NotNull final Collection<? extends LlvmWritable> writables) throws X
	{
		for (final LlvmWritable llvmWritable : writables)
		{
			llvmWritable.write(byteWriter, writableDataLayoutSpecificationAndTargetTriple);
			byteWriter.writeLineFeed();
		}
		if (!writables.isEmpty())
		{
			byteWriter.writeLineFeed();
		}
	}

	private <X extends Exception> void writeX(@NotNull final ByteWriter<X> byteWriter, @NotNull final Set<? extends LocallyIdentifiedStructureType> locallyIdentifiedStructureTypes) throws X
	{
		// For known structures, we can also do:-
		// @Struct_size = constant i32 ptrtoint (%Struct* getelementptr (%Struct* null, i32 1)) to i32
		for (final LocallyIdentifiedStructureType locallyIdentifiedStructureType : locallyIdentifiedStructureTypes)
		{
			final Identifier identifier = locallyIdentifiedStructureType.identifier();
			identifier.write(byteWriter, writableDataLayoutSpecificationAndTargetTriple);
			byteWriter.writeBytes(SpaceEqualsSpace);
			locallyIdentifiedStructureType.write(byteWriter, writableDataLayoutSpecificationAndTargetTriple);
			byteWriter.writeLineFeed();
		}
		if (!locallyIdentifiedStructureTypes.isEmpty())
		{
			byteWriter.writeLineFeed();
		}
	}

}
