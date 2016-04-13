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
import com.stormmq.llvm.domain.target.*;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.LocallyIdentifiedStructureType;
import com.stormmq.llvm.domain.variables.Alias;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import com.stormmq.llvm.domain.metadata.debugging.LlvmDbgCuNamedMetadataTuple;
import com.stormmq.llvm.domain.metadata.module.LlvmIdentNamedMetadataTuple;
import com.stormmq.llvm.domain.metadata.module.LlvmModuleFlagsNamedMetadataTuple;
import org.jetbrains.annotations.*;

import java.util.*;

import static com.stormmq.llvm.domain.target.WritableDataLayoutSpecificationAndTargetTriple.*;

public enum TargetModuleCreator
{
	MacOsXMavericksX86_64(MacOsXMavericksOnX86_64),
	MacOsXYoesmiteX86_64(MacOsXMavericksOnX86_64),
	MacOsXElCapitanX86_64(MacOsXMavericksOnX86_64),
	LinuxX864_64(LinuxOnX86_64),
	LinuxMips64LittleEndian(LinuxOnMips64LittleEndian),
	LinuxMips64BigEndian(LinuxOnMips64BigEndian),
	LinuxAArch64LittleEndian(LinuxOnAArch64LittleEndian),
	LinuxAArch64BigEndian(LinuxOnAArch64BigEndian),
	LinuxPowerPC64LittleEndian(LinuxOnPowerPC64LittleEndian),
	LinuxPowerPC64BigEndian(LinuxOnPowerPC64BigEndian),
	LinuxI686(LinuxOnI686),
	LinuxArmV7LittleEndianEabi(LinuxOnArmV7Eabi),
	;
	
	@NotNull private final WritableDataLayoutSpecificationAndTargetTriple dataLayoutSpecification;
	@NotNull private final CxxNameMangling cxxNameMangling;

	@SuppressWarnings("WeakerAccess")
	TargetModuleCreator(@NotNull final WritableDataLayoutSpecificationAndTargetTriple dataLayoutSpecification)
	{
		this.dataLayoutSpecification = dataLayoutSpecification;
		cxxNameMangling = dataLayoutSpecification.cxxNameMangling();
	}

	@NotNull
	public Module newModule(@NotNull final Map<Architecture, List<ModuleLevelInlineAsm>> moduleLevelInlineAssembly, @NotNull final LlvmIdentNamedMetadataTuple identity, @NotNull final LlvmModuleFlagsNamedMetadataTuple moduleFlags, @NotNull final LlvmDbgCuNamedMetadataTuple compileUnits, @NotNull final Set<LocallyIdentifiedStructureType> locallyIdentifiedStructureTypes, @NotNull final Set<GlobalVariable<?>> globalVariablesAndConstants, @NotNull final Set<FunctionDeclaration> functionDeclarations, @NotNull final Set<FunctionDefinition> functionsDefinitions, @NotNull final Set<Alias> aliases)
	{
		return new Module(dataLayoutSpecification, moduleLevelInlineAssembly, identity, moduleFlags, compileUnits, locallyIdentifiedStructureTypes, globalVariablesAndConstants, functionDeclarations, functionsDefinitions, aliases);
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
