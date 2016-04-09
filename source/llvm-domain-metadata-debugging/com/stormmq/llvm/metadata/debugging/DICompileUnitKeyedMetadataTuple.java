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

package com.stormmq.llvm.metadata.debugging;

import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.metadata.KeyWithMetadataField;
import com.stormmq.llvm.metadata.Metadata;
import com.stormmq.llvm.metadata.metadataTuples.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import static com.stormmq.llvm.metadata.ProducerConstant.Producer;
import static com.stormmq.llvm.metadata.debugging.Key.*;
import static java.util.Collections.emptyList;

public final class DICompileUnitKeyedMetadataTuple extends KeyedMetadataTuple implements ScopeMetadata
{
	public DICompileUnitKeyedMetadataTuple(@NotNull final ReferenceTracker referenceTracker)
	{
		super(referenceTracker, true, "DICompileUnit");
	}

	@NotNull
	public DICompileUnitKeyedMetadataTuple populate(@NotNull final LlvmDebugLanguage llvmDebugLanguage, @NotNull final DIFileKeyedMetadataTuple file, @NotNull final Metadata enums, @NotNull final Metadata retainedTypes, @NotNull final TypedMetadataTuple<DISubprogramKeyedMetadataTuple> subprograms, @NotNull final TypedMetadataTuple<DIGlobalVariableKeyedMetadataTuple> globals, @NotNull final TypedMetadataTuple<DIImportedEntityKeyedMetadataTuple> imports, @NotNull final TypedMetadataTuple<DIMacroFileKeyedMetadataTuple> macros)
	{
		populate(language.with(llvmDebugLanguage), Key.file.with(file), producer.with(Producer), isOptimized.with(false), flags.with("-O0"), runtimeVersion.with(0), emissionKind.with(1), Key.enums.with(enums), Key.retainedTypes.with(retainedTypes), Key.subprograms.with(subprograms), Key.globals.with(globals), Key.imports.with(imports), Key.macros.with(macros)); // dwoId.with(dwoId)
		return this;
	}
}
