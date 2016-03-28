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

import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.domain.asm.ModuleLevelInlineAsm;
import com.stormmq.llvm.domain.comdat.ComdatDefinition;
import com.stormmq.llvm.domain.function.FunctionDeclaration;
import com.stormmq.llvm.domain.function.FunctionDefinition;
import com.stormmq.llvm.domain.identifiers.LocalIdentifier;
import com.stormmq.llvm.domain.target.dataLayout.DataLayoutSpecification;
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
import org.jetbrains.annotations.*;

import java.util.List;
import java.util.Map;

import static com.stormmq.llvm.domain.target.dataLayout.DataLayoutSpecification.DarwinOnX86_64;
import static com.stormmq.llvm.domain.target.triple.TargetTriple.MacOsXMavericksOnX86_64;
import static com.stormmq.llvm.metadata.debugging.LlvmDebugLanguage.DW_LANG_Java;
import static com.stormmq.llvm.metadata.metadataTuples.AnonymousMetadataTuple.emptyAnonymousMetadataTuple;
import static com.stormmq.llvm.metadata.metadataTuples.TypedMetadataTuple.emptyTypedMetadataTuple;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;

public final class TargetModuleCreator
{
	@NotNull public static final TargetModuleCreator MacOsXMavericksX86_64 = new TargetModuleCreator(DarwinOnX86_64, MacOsXMavericksOnX86_64);

	@NotNull private final List<ModuleLevelInlineAsm> NoModuleLevelInlineAsm = emptyList();
	@NotNull private static final List<ComdatDefinition> NoComdatDefinitions = emptyList();
	@NotNull private static final Map<LocalIdentifier, OpaqueStructureType> NoOpaqueStructureTypes = emptyMap();
	@NotNull private static final List<Alias> NoAliases = emptyList();

	@NotNull private final DataLayoutSpecification dataLayoutSpecification;
	@NotNull private final TargetTriple targetTriple;

	@SuppressWarnings("WeakerAccess")
	public TargetModuleCreator(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final TargetTriple targetTriple)
	{
		this.dataLayoutSpecification = dataLayoutSpecification;
		this.targetTriple = targetTriple;
	}

	@NotNull
	public Module newJavaModule(@NotNull final LlvmIdentNamedMetadataTuple identity, @NotNull final LlvmModuleFlagsNamedMetadataTuple moduleFlags, @NotNull final LlvmDbgCuNamedMetadataTuple compileUnits, @NotNull final Map<LocalIdentifier, StructureType> structureTypes, @NotNull final List<GlobalVariable<?>> globalVariablesAndConstants, @NotNull final List<FunctionDeclaration> functionDeclarations, @NotNull final List<FunctionDefinition> functionsDefinitions)
	{
		return new Module(dataLayoutSpecification, targetTriple, NoModuleLevelInlineAsm, identity, moduleFlags, compileUnits, NoComdatDefinitions, structureTypes, NoOpaqueStructureTypes, globalVariablesAndConstants, functionDeclarations, functionsDefinitions, NoAliases);
	}
}
