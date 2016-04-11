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

import com.stormmq.llvm.domain.asm.ModuleLevelInlineAsm;
import com.stormmq.llvm.domain.function.FunctionDeclaration;
import com.stormmq.llvm.domain.function.FunctionDefinition;
import com.stormmq.llvm.domain.target.CxxNameMangling;
import com.stormmq.llvm.domain.target.dataLayout.DataLayoutSpecification;
import com.stormmq.llvm.domain.target.triple.Architecture;
import com.stormmq.llvm.domain.target.triple.TargetTriple;
import com.stormmq.llvm.domain.types.CanBePointedToType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.LocallyIdentifiedStructureType;
import com.stormmq.llvm.domain.variables.Alias;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import com.stormmq.llvm.domain.metadata.debugging.LlvmDbgCuNamedMetadataTuple;
import com.stormmq.llvm.domain.metadata.module.LlvmIdentNamedMetadataTuple;
import com.stormmq.llvm.domain.metadata.module.LlvmModuleFlagsNamedMetadataTuple;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.stormmq.llvm.domain.target.CxxNameMangling.Intel;
import static com.stormmq.llvm.domain.target.dataLayout.DataLayoutSpecification.DarwinOnX86_64;
import static com.stormmq.llvm.domain.target.triple.TargetTriple.MacOsXMavericksOnX86_64;

public final class TargetModuleCreator
{
	@NotNull public static final TargetModuleCreator MacOsXMavericksX86_64 = new TargetModuleCreator(DarwinOnX86_64, MacOsXMavericksOnX86_64, Intel);

	@NotNull private final DataLayoutSpecification dataLayoutSpecification;
	@NotNull private final TargetTriple targetTriple;
	@NotNull private final CxxNameMangling cxxNameMangling;

	@SuppressWarnings("WeakerAccess")
	public TargetModuleCreator(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final TargetTriple targetTriple, @NotNull final CxxNameMangling cxxNameMangling)
	{
		this.dataLayoutSpecification = dataLayoutSpecification;
		this.targetTriple = targetTriple;
		this.cxxNameMangling = cxxNameMangling;
	}

	@NotNull
	public Module newModule(@NotNull final Map<Architecture, List<ModuleLevelInlineAsm>> moduleLevelInlineAssembly, @NotNull final LlvmIdentNamedMetadataTuple identity, @NotNull final LlvmModuleFlagsNamedMetadataTuple moduleFlags, @NotNull final LlvmDbgCuNamedMetadataTuple compileUnits, @NotNull final Set<LocallyIdentifiedStructureType> locallyIdentifiedStructureTypes, @NotNull final Set<GlobalVariable<?>> globalVariablesAndConstants, @NotNull final Set<FunctionDeclaration> functionDeclarations, @NotNull final Set<FunctionDefinition> functionsDefinitions, @NotNull final Set<Alias> aliases)
	{
		return new Module(dataLayoutSpecification, targetTriple, moduleLevelInlineAssembly, identity, moduleFlags, compileUnits, locallyIdentifiedStructureTypes, globalVariablesAndConstants, functionDeclarations, functionsDefinitions, aliases);
	}

	@NotNull
	public CxxNameMangling cxxNameMangling()
	{
		return cxxNameMangling;
	}

	@NotNull
	public DataLayoutSpecification dataLayoutSpecification()
	{
		return dataLayoutSpecification;
	}
}
