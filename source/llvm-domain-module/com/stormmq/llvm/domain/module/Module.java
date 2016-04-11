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
import com.stormmq.llvm.domain.Writable;
import com.stormmq.llvm.domain.asm.ModuleLevelInlineAsm;
import com.stormmq.llvm.domain.comdat.ComdatDefinition;
import com.stormmq.llvm.domain.comdat.MayHaveComdatDefinition;
import com.stormmq.llvm.domain.function.FunctionDeclaration;
import com.stormmq.llvm.domain.function.FunctionDefinition;
import com.stormmq.llvm.domain.identifiers.*;
import com.stormmq.llvm.domain.target.dataLayout.DataLayoutSpecification;
import com.stormmq.llvm.domain.target.triple.Architecture;
import com.stormmq.llvm.domain.target.triple.TargetTriple;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.LocallyIdentifiedStructureType;
import com.stormmq.llvm.domain.variables.Alias;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import com.stormmq.llvm.domain.metadata.debugging.LlvmDbgCuNamedMetadataTuple;
import com.stormmq.llvm.domain.metadata.module.LlvmIdentNamedMetadataTuple;
import com.stormmq.llvm.domain.metadata.module.LlvmModuleFlagsNamedMetadataTuple;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.stormmq.functions.CollectionHelper.addOnce;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

public final class Module implements Writable
{
	@NotNull private static final List<ModuleLevelInlineAsm> NoModuleLevelInlineAsm = emptyList();

	@NotNull private final DataLayoutSpecification dataLayoutSpecification;
	@NotNull private final TargetTriple targetTriple;
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

	public Module(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final TargetTriple targetTriple, @NotNull final Map<Architecture, List<ModuleLevelInlineAsm>> moduleLevelInlineAssembly, @NotNull final LlvmIdentNamedMetadataTuple llvmIdentNamedMetadataTuple, @NotNull final LlvmModuleFlagsNamedMetadataTuple llvmModuleFlagsNamedMetadataTuple, @NotNull final LlvmDbgCuNamedMetadataTuple compileUnits, @NotNull final Set<LocallyIdentifiedStructureType> locallyIdentifiedStructureTypes, @NotNull final Set<GlobalVariable<?>> globalVariablesAndConstants, @NotNull final Set<FunctionDeclaration> functionDeclarations, @NotNull final Set<FunctionDefinition> functionDefinitions, @NotNull final Set<Alias> aliases)
	{
		guardDuplicateIdentifiers(globalVariablesAndConstants, functionDeclarations, functionDefinitions, aliases);

		this.dataLayoutSpecification = dataLayoutSpecification;
		this.targetTriple = targetTriple;
		this.moduleLevelInlineAssembly = targetTriple.chooseForArchitecture(moduleLevelInlineAssembly, NoModuleLevelInlineAsm);
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
		final Set<ComdatDefinition> comdatDefinitions = new HashSet<>(globalVariablesAndConstants.size() + functionDefinitions.size());

		for (final MayHaveComdatDefinition mayHaveComdatDefinition : globalVariablesAndConstants)
		{
			mayHaveComdatDefinition.adjustComdatDefinition(comdatDefinitions, targetTriple);
		}

		for (final MayHaveComdatDefinition mayHaveComdatDefinition : functionDefinitions)
		{
			mayHaveComdatDefinition.adjustComdatDefinition(comdatDefinitions, targetTriple);
		}

		if (comdatDefinitions.isEmpty())
		{
			return emptySet();
		}

		// Done to reduce potentially explosive memory usage if lots of modules are created
		return new HashSet<>(comdatDefinitions);
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		dataLayoutSpecification.write(byteWriter);
		targetTriple.write(byteWriter);
		byteWriter.writeLineFeed();

		write(byteWriter, moduleLevelInlineAssembly);

		llvmIdentNamedMetadataTuple.write(byteWriter);
		llvmModuleFlagsNamedMetadataTuple.write(byteWriter);
		compileUnits.write(byteWriter);
		byteWriter.writeLineFeed();

		write(byteWriter, comdatDefinitions);

		writeX(byteWriter, locallyIdentifiedStructureTypes);

		write(byteWriter, globalVariablesAndConstants);

		write(byteWriter, functionDeclarations);

		write(byteWriter, functionDefinitions);

		write(byteWriter, aliases);
	}

	private static <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter, @NotNull final Collection<? extends Writable> writables) throws X
	{
		for (final Writable writable : writables)
		{
			writable.write(byteWriter);
			byteWriter.writeLineFeed();
		}
		if (!writables.isEmpty())
		{
			byteWriter.writeLineFeed();
		}
	}

	private static <X extends Exception> void writeX(@NotNull final ByteWriter<X> byteWriter, @NotNull final Set<? extends LocallyIdentifiedStructureType> locallyIdentifiedStructureTypes) throws X
	{
		// For known structures, we can also do:-
		// @Struct_size = constant i32 ptrtoint (%Struct* getelementptr (%Struct* null, i32 1)) to i32
		for (final LocallyIdentifiedStructureType locallyIdentifiedStructureType : locallyIdentifiedStructureTypes)
		{
			final Identifier identifier = locallyIdentifiedStructureType.localIdentifier();
			identifier.write(byteWriter);
			byteWriter.writeBytes(SpaceEqualsSpace);
			locallyIdentifiedStructureType.write(byteWriter);
			byteWriter.writeLineFeed();
		}
		if (!locallyIdentifiedStructureTypes.isEmpty())
		{
			byteWriter.writeLineFeed();
		}
	}

}
