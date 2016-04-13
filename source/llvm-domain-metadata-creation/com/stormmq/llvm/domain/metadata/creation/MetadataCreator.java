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

package com.stormmq.llvm.domain.metadata.creation;

import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.llvm.domain.types.SizedType;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import com.stormmq.llvm.domain.metadata.Metadata;
import com.stormmq.llvm.domain.metadata.debugging.*;
import com.stormmq.llvm.domain.metadata.metadataTuples.AnonymousMetadataTuple;
import com.stormmq.llvm.domain.metadata.metadataTuples.TypedMetadataTuple;
import com.stormmq.llvm.domain.metadata.module.LlvmIdentNamedMetadataTuple;
import com.stormmq.llvm.domain.metadata.module.LlvmModuleFlagsNamedMetadataTuple;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

import static com.stormmq.llvm.domain.metadata.debugging.ScopeMetadata.UnknownLineNumber;
import static com.stormmq.llvm.domain.metadata.metadataTuples.AnonymousMetadataTuple.emptyAnonymousMetadataTuple;
import static com.stormmq.llvm.domain.metadata.module.LlvmModuleFlagsNamedMetadataTuple.typicalLlvmModuleFlags;
import static java.util.Collections.singletonList;

public final class MetadataCreator<N>
{
	@NotNull private final ReferenceTracker referenceTracker;
	@NotNull private final DIFileKeyedMetadataTuple file;
	@NotNull private final DICompileUnitKeyedMetadataTuple diCompileUnit;
	@NotNull private final DebuggingTypeDefinitions<N> debuggingTypeDefinitions;
	@NotNull private final LlvmDebugLanguage llvmDebugLanguage;

	public MetadataCreator(@NotNull final LlvmDebugLanguage llvmDebugLanguage, @NotNull final String relativeFilePath, @NotNull final Path relativeRootFolderPath, @NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final NamespaceSplitter<N> namespaceSplitter, @NotNull final CTypeMappings cTypeMappings)
	{
		this.llvmDebugLanguage = llvmDebugLanguage;
		referenceTracker = new ReferenceTracker();
		file = file(relativeFilePath, relativeRootFolderPath.toString());
		diCompileUnit = new DICompileUnitKeyedMetadataTuple(referenceTracker);
		debuggingTypeDefinitions = new DebuggingTypeDefinitions<>(dataLayoutSpecification, referenceTracker, file, namespaceSplitter, cTypeMappings);
	}

	@NotNull
	public DebuggingTypeDefinitions<N> debuggingTypeDefinitions()
	{
		return debuggingTypeDefinitions;
	}

	@NotNull
	public LlvmIdentNamedMetadataTuple identity()
	{
		return new LlvmIdentNamedMetadataTuple(referenceTracker);
	}

	@NotNull
	public LlvmModuleFlagsNamedMetadataTuple moduleFlags()
	{
		return typicalLlvmModuleFlags(referenceTracker);
	}

	@NotNull
	public <M extends Metadata> TypedMetadataTuple<M> emptyTypedMetadataTuple()
	{
		return TypedMetadataTuple.emptyTypedMetadataTuple(referenceTracker);
	}

	@NotNull
	private DIFileKeyedMetadataTuple file(@NotNull @NonNls final String relativeFilePath, @NotNull @NonNls final String directory)
	{
		return new DIFileKeyedMetadataTuple(referenceTracker, relativeFilePath, directory);
	}

	@NotNull
	public <M extends Metadata> TypedMetadataTuple<M> typedMetadataTuple(@NotNull final List<M> tuple)
	{
		return new TypedMetadataTuple<>(referenceTracker, tuple);
	}

	@NotNull
	public LlvmDbgCuNamedMetadataTuple newSimpleCompileUnits(@NotNull final TypedMetadataTuple<DISubprogramKeyedMetadataTuple> subprograms, @NotNull final TypedMetadataTuple<DIGlobalVariableKeyedMetadataTuple> globals)
	{
		final DICompileUnitKeyedMetadataTuple diCompileUnitKeyedMetadataTuple = newSimpleCompileUnit(subprograms, globals);
		final List<DICompileUnitKeyedMetadataTuple> compileUnits = singletonList(diCompileUnitKeyedMetadataTuple);
		return new LlvmDbgCuNamedMetadataTuple(referenceTracker, compileUnits);
	}

	@NotNull
	private DICompileUnitKeyedMetadataTuple newSimpleCompileUnit(@NotNull final TypedMetadataTuple<DISubprogramKeyedMetadataTuple> subprograms, @NotNull final TypedMetadataTuple<DIGlobalVariableKeyedMetadataTuple> globals)
	{
		final AnonymousMetadataTuple enums = emptyAnonymousMetadataTuple(referenceTracker);
		final AnonymousMetadataTuple retainedTypes = emptyAnonymousMetadataTuple(referenceTracker);
		final TypedMetadataTuple<DIImportedEntityKeyedMetadataTuple> imports = emptyTypedMetadataTuple();
		final TypedMetadataTuple<DIMacroFileKeyedMetadataTuple> macros = emptyTypedMetadataTuple();
		return diCompileUnit.populate(llvmDebugLanguage, file, enums, retainedTypes, subprograms, globals, imports, macros);
	}

	@NotNull
	public <T extends SizedType> DIGlobalVariableKeyedMetadataTuple newGlobalVariable(@NonNls @NotNull final String unmangledName, @NotNull final TypeMetadata type, final boolean isLocal, @NotNull final GlobalVariable<T> globalVariable)
	{
		// type ==> !60 = !DIDerivedType(tag: DW_TAG_const_type, baseType: !12)  => for a constant
		// !59 = !DIGlobalVariable(name: "XXXXXX", scope: !0, file: !1, line: 6, type: !60, isLocal: false, isDefinition: true, variable: i32* @XXXXXX)
		return new DIGlobalVariableKeyedMetadataTuple(referenceTracker, unmangledName, diCompileUnit, file, UnknownLineNumber, type, isLocal, true, globalVariable);
	}
}
