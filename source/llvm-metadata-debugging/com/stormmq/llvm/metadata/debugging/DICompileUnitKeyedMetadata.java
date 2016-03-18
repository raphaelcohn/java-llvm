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

import com.stormmq.llvm.metadata.CollectionMetadata;
import com.stormmq.llvm.metadata.AbstractKeyedMetadata;
import com.stormmq.llvm.api.writing.metadataWriters.*;
import com.stormmq.llvm.metadata.writers.MetadataNodeIndexProvider;
import com.stormmq.llvm.metadata.writers.SpecializedLabelledFieldsMetadataWriter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.metadata.module.LlvmIdentNamedMetadata.Producer;

public final class DICompileUnitKeyedMetadata extends AbstractKeyedMetadata
{
	@NotNull private final LlvmDebugLanguage llvmDebugLanguage;
	@NotNull private final DIFileKeyedMetadata file;
	@NotNull private final CollectionMetadata<?> enums;
	@NotNull private final CollectionMetadata<?> retainedTypes;
	@NotNull private final CollectionMetadata<DISubprogramKeyedMetadata> subprograms;
	@NotNull private final CollectionMetadata<DIGlobalVariableKeyedMetadata> globals;
	@NotNull private final CollectionMetadata<DIImportedEntityKeyedMetadata> imports;
	@NotNull private final CollectionMetadata<DIMacroFileKeyedMetadata> macros;

	public DICompileUnitKeyedMetadata(@NonNls @NotNull final LlvmDebugLanguage llvmDebugLanguage, @NotNull final DIFileKeyedMetadata file, @NotNull final CollectionMetadata<?> enums, @NotNull final CollectionMetadata<?> retainedTypes, @NotNull final CollectionMetadata<DISubprogramKeyedMetadata> subprograms, @NotNull final CollectionMetadata<DIGlobalVariableKeyedMetadata> globals, @NotNull final CollectionMetadata<DIImportedEntityKeyedMetadata> imports, @NotNull final CollectionMetadata<DIMacroFileKeyedMetadata> macros)
	{
		super(file, enums, retainedTypes, subprograms, globals, imports, macros);
		this.llvmDebugLanguage = llvmDebugLanguage;
		this.file = file;
		this.enums = enums;
		this.retainedTypes = retainedTypes;
		this.subprograms = subprograms;
		this.globals = globals;
		this.imports = imports;
		this.macros = macros;
	}

	@Override
	protected <X extends Exception> void writeLabelledFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final SpecializedLabelledFieldsMetadataWriter<X> specializedLabelledFieldsMetadataWriter) throws X
	{
		specializedLabelledFieldsMetadataWriter.writeLabelledField("language", llvmDebugLanguage);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(AbstractKeyedMetadata.file, file, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("producer", Producer);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(isOptimized, false);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(flags, "-00"); // where '-g' and '-coverage' go
		specializedLabelledFieldsMetadataWriter.writeLabelledField("runtimeVersion", 0);
		//specializedLabelledFieldsMetadataWriter.writeLabelledField("splitDebugFilename", "abc.debug");
		specializedLabelledFieldsMetadataWriter.writeLabelledField("emissionKind", 1);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("enums", enums, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("retainedTypes", retainedTypes, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("subprograms", subprograms, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("globals", globals, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("imports", imports, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("macros", macros, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledFieldUnquotedString("dwoId", "0x0abcd");
	}
}
