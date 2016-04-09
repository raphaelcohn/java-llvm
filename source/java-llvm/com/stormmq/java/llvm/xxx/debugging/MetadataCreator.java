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

package com.stormmq.java.llvm.xxx.debugging;

import com.stormmq.java.llvm.xxx.NameMangling;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.domain.types.*;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import com.stormmq.llvm.metadata.Metadata;
import com.stormmq.llvm.metadata.debugging.*;
import com.stormmq.llvm.metadata.metadataTuples.AnonymousMetadataTuple;
import com.stormmq.llvm.metadata.metadataTuples.TypedMetadataTuple;
import com.stormmq.llvm.metadata.module.LlvmIdentNamedMetadataTuple;
import com.stormmq.llvm.metadata.module.LlvmModuleFlagsNamedMetadataTuple;
import org.jetbrains.annotations.*;

import java.nio.file.Path;
import java.util.*;
import java.util.function.*;

import static com.stormmq.java.llvm.xxx.NameMangling.Intel;
import static com.stormmq.java.parsing.utilities.names.PackageName.NamespaceSplitter;
import static com.stormmq.java.parsing.utilities.names.PackageName.NamespaceWithSimpleTypeNameJoiner;
import static com.stormmq.llvm.metadata.debugging.ScopeMetadata.UnknownLineNumber;
import static com.stormmq.llvm.metadata.debugging.LlvmDebugLanguage.DW_LANG_Java;
import static com.stormmq.llvm.metadata.metadataTuples.AnonymousMetadataTuple.emptyAnonymousMetadataTuple;
import static com.stormmq.llvm.metadata.module.LlvmModuleFlagsNamedMetadataTuple.typicalLlvmModuleFlags;
import static java.util.Collections.singletonList;

public final class MetadataCreator<N extends PackageName> implements NamespaceMetadataCreator<N>
{
	@NotNull private final ReferenceTracker referenceTracker;
	@NotNull private final DIFileKeyedMetadataTuple file;
	@NotNull private final DICompileUnitKeyedMetadataTuple diCompileUnit;
	@NotNull private final DebuggingTypeDefinitions<N> debuggingTypeDefinitions;
	@NotNull private final TrackingNamespaceCreator trackingNamespaceCreator;
	@NotNull private final BiConsumer<N, Consumer<String>> namespaceSplitter;
	@NotNull private final BiFunction<N, String, String> namespaceWithSimpleTypeNameJoiner;
	@NotNull private final NameMangling nameMangling;
	@NotNull private final LlvmDebugLanguage llvmDebugLanguage;

	public MetadataCreator(@NotNull final String relativeFilePath, @NotNull final Path relativeRootFolderPath)
	{
		this((BiConsumer<N, Consumer<String>>) NamespaceSplitter, (BiFunction<N, String, String>) NamespaceWithSimpleTypeNameJoiner, Intel, DW_LANG_Java, relativeFilePath, relativeRootFolderPath);
	}

	private MetadataCreator(@NotNull final BiConsumer<N, Consumer<String>> namespaceSplitter, @NotNull @NonNls final BiFunction<N, String, String> namespaceWithSimpleTypeNameJoiner, @NotNull final NameMangling nameMangling, @NotNull final LlvmDebugLanguage llvmDebugLanguage, @NotNull final String relativeFilePath, @NotNull final Path relativeRootFolderPath)
	{
		this.namespaceSplitter = namespaceSplitter;
		this.namespaceWithSimpleTypeNameJoiner = namespaceWithSimpleTypeNameJoiner;
		this.nameMangling = nameMangling;
		this.llvmDebugLanguage = llvmDebugLanguage;
		referenceTracker = new ReferenceTracker();
		file = file(relativeFilePath, relativeRootFolderPath.toString());
		diCompileUnit = new DICompileUnitKeyedMetadataTuple(referenceTracker);
		debuggingTypeDefinitions = new DebuggingTypeDefinitions<>(referenceTracker, file, this);
		trackingNamespaceCreator = new TrackingNamespaceCreator(referenceTracker);
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

	@Override
	@NotNull
	public DINamespaceKeyedMetadataTuple namespace(@NotNull final N namespace)
	{
		return trackingNamespaceCreator.namespace(namespace, namespaceSplitter, file);
	}

	@NotNull
	@Override
	public String identifier(@NotNull final N namespace, @NotNull @NonNls final String simpleTypeName)
	{
		return nameMangling.mangleIdentifier(namespaceSplitter,  namespace, simpleTypeName);
	}

	@NotNull
	public LlvmDbgCuNamedMetadataTuple newSimpleCompileUnits(@NotNull final TypedMetadataTuple<DISubprogramKeyedMetadataTuple> subprograms, final TypedMetadataTuple<DIGlobalVariableKeyedMetadataTuple> globals)
	{
		final DICompileUnitKeyedMetadataTuple diCompileUnitKeyedMetadataTuple = newSimpleCompileUnit(subprograms, globals);
		final List<DICompileUnitKeyedMetadataTuple> compileUnits = singletonList(diCompileUnitKeyedMetadataTuple);
		return new LlvmDbgCuNamedMetadataTuple(referenceTracker, compileUnits);
	}

	@NotNull
	private DICompileUnitKeyedMetadataTuple newSimpleCompileUnit(@NotNull final TypedMetadataTuple<DISubprogramKeyedMetadataTuple> subprograms, final TypedMetadataTuple<DIGlobalVariableKeyedMetadataTuple> globals)
	{
		final AnonymousMetadataTuple enums = emptyAnonymousMetadataTuple(referenceTracker);
		final AnonymousMetadataTuple retainedTypes = emptyAnonymousMetadataTuple(referenceTracker);
		final TypedMetadataTuple<DIImportedEntityKeyedMetadataTuple> imports = TypedMetadataTuple.emptyTypedMetadataTuple(referenceTracker);
		final TypedMetadataTuple<DIMacroFileKeyedMetadataTuple> macros = TypedMetadataTuple.emptyTypedMetadataTuple(referenceTracker);
		return diCompileUnit.populate(llvmDebugLanguage, file, enums, retainedTypes, subprograms, globals, imports, macros);
	}

	@NotNull
	public <T extends CanBePointedToType> DIGlobalVariableKeyedMetadataTuple newGlobalVariable(@NonNls @NotNull final String unmangledName, @NotNull final TypeMetadata type, final boolean isLocal, @NotNull final GlobalVariable<T> globalVariable)
	{
		// type ==> !60 = !DIDerivedType(tag: DW_TAG_const_type, baseType: !12)  => for a constant
		// !59 = !DIGlobalVariable(name: "XXXXXX", scope: !0, file: !1, line: 6, type: !60, isLocal: false, isDefinition: true, variable: i32* @XXXXXX)
		return new DIGlobalVariableKeyedMetadataTuple(referenceTracker, unmangledName, diCompileUnit, file, UnknownLineNumber, type, isLocal, true, globalVariable);
	}

	@NotNull
	public TypeMetadata constantDerivedType(@NotNull final TypeMetadata typeMetadata)
	{
		return debuggingTypeDefinitions.constantDerivedType(typeMetadata);
	}
}
