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

package com.stormmq.llvm.api.metadata.specializedMetadataNodes;

import com.stormmq.llvm.api.metadata.namedMetadataNodes.LlvmIdentNamedMetadataNode;
import com.stormmq.llvm.api.metadataWriters.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class DICompileUnitSpecializedMetadataNode extends AbstractSpecializedMetadataNode
{

	@NotNull private final LlvmDebugLanguage llvmDebugLanguage;
	@NotNull private final DIFileSpecializedMetadataNode file;

	public enum LlvmDebugLanguage
	{
		DW_LANG_C99,
		;
	}

	public DICompileUnitSpecializedMetadataNode(@NonNls @NotNull final LlvmDebugLanguage llvmDebugLanguage, @NotNull final DIFileSpecializedMetadataNode file)
	{
		this.llvmDebugLanguage = llvmDebugLanguage;
		this.file = file;
	}

	@NonNls
	@NotNull
	@Override
	protected String specializedName()
	{
		return "DICompileUnit";
	}

	@Override
	protected <X extends Exception> void writeLabelledFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final SpecializedLabelledFieldsMetadataWriter<X> specializedLabelledFieldsMetadataWriter) throws X
	{
		specializedLabelledFieldsMetadataWriter.writeLabelledField("language", llvmDebugLanguage);
		specializedLabelledFieldsMetadataWriter.writeLabelledFieldMetadataNode("file", 1);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("producer", LlvmIdentNamedMetadataNode.Producer);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("isOptimized", false);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("flags", "-00"); // where '-g' and '-coverage' go
		specializedLabelledFieldsMetadataWriter.writeLabelledField("runtimeVersion", 0);
		//specializedLabelledFieldsMetadataWriter.writeLabelledField("splitDebugFilename", "abc.debug");
		specializedLabelledFieldsMetadataWriter.writeLabelledField("emissionKind", 1);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("enums", !2);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("retainedTypes", !3);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("subprograms", !4);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("globals", !5);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("imports", !6);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("macros", !7);
		specializedLabelledFieldsMetadataWriter.writeLabelledFieldUnquotedString("dwoId", "0x0abcd");
	}

	@Override
	protected <X extends Exception> void writeForDependentNodes(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final NodeMetadataWriter<X> nodeMetadataWriter) throws X
	{
		file.write(nodeMetadataWriter);
		writeEnums(nodeMetadataWriter);
		writeRetainedTypes(nodeMetadataWriter);
		writeSubprograms(nodeMetadataWriter);
		writeGlobals(nodeMetadataWriter);
		writeImports(nodeMetadataWriter);
		writeMacros(nodeMetadataWriter);
	}

	private <X extends Exception> void writeMacros(@NotNull final NodeMetadataWriter<X> nodeMetadataWriter)
	{
		// references can be forward, they can't be back
		final List<String> macros;
		for (String macro : macros)
		{

		}
	}
}
