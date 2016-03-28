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
import com.stormmq.llvm.metadata.Metadata;
import com.stormmq.llvm.metadata.debugging.*;
import com.stormmq.llvm.metadata.metadataTuples.AnonymousMetadataTuple;
import com.stormmq.llvm.metadata.metadataTuples.TypedMetadataTuple;
import com.stormmq.llvm.metadata.module.LlvmIdentNamedMetadataTuple;
import com.stormmq.llvm.metadata.module.LlvmModuleFlagsNamedMetadataTuple;
import org.jetbrains.annotations.*;

import java.util.List;

import static com.stormmq.llvm.metadata.debugging.LlvmDebugLanguage.DW_LANG_Java;
import static com.stormmq.llvm.metadata.metadataTuples.AnonymousMetadataTuple.emptyAnonymousMetadataTuple;
import static com.stormmq.llvm.metadata.module.LlvmModuleFlagsNamedMetadataTuple.typicalLlvmModuleFlags;
import static java.util.Collections.singletonList;

public final class MetadataCreator
{
	@NotNull private final ReferenceTracker referenceTracker;

	public MetadataCreator()
	{
		referenceTracker = new ReferenceTracker();
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
	public DIFileKeyedMetadataTuple file(@NotNull @NonNls final String relativeFilePath, @NotNull @NonNls final String directory)
	{
		return new DIFileKeyedMetadataTuple(referenceTracker, relativeFilePath, directory);
	}

	@NotNull
	public LlvmDbgCuNamedMetadataTuple newJavaCompileUnits(@NotNull final DIFileKeyedMetadataTuple file, @NotNull final TypedMetadataTuple<DISubprogramKeyedMetadataTuple> subprograms, final TypedMetadataTuple<DIGlobalVariableKeyedMetadataTuple> globals)
	{
		final DICompileUnitKeyedMetadataTuple diCompileUnitKeyedMetadataTuple = newJavaCompileUnit(file, subprograms, globals);
		final List<DICompileUnitKeyedMetadataTuple> compileUnits = singletonList(diCompileUnitKeyedMetadataTuple);
		return new LlvmDbgCuNamedMetadataTuple(referenceTracker, compileUnits);
	}

	@NotNull
	private DICompileUnitKeyedMetadataTuple newJavaCompileUnit(@NotNull final DIFileKeyedMetadataTuple file, @NotNull final TypedMetadataTuple<DISubprogramKeyedMetadataTuple> subprograms, final TypedMetadataTuple<DIGlobalVariableKeyedMetadataTuple> globals)
	{
		final AnonymousMetadataTuple enums = emptyAnonymousMetadataTuple(referenceTracker);
		final AnonymousMetadataTuple retainedTypes = emptyAnonymousMetadataTuple(referenceTracker);
		final TypedMetadataTuple<DIImportedEntityKeyedMetadataTuple> imports = TypedMetadataTuple.emptyTypedMetadataTuple(referenceTracker);
		final TypedMetadataTuple<DIMacroFileKeyedMetadataTuple> macros = TypedMetadataTuple.emptyTypedMetadataTuple(referenceTracker);
		return new DICompileUnitKeyedMetadataTuple(referenceTracker, DW_LANG_Java, file, enums, retainedTypes, subprograms, globals, imports, macros);
	}
}
