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

import com.stormmq.llvm.metadata.Metadata;
import com.stormmq.llvm.metadata.AbstractKeyedMetadata;
import com.stormmq.llvm.api.writing.metadataWriters.*;
import com.stormmq.llvm.metadata.writers.MetadataNodeIndexProvider;
import com.stormmq.llvm.metadata.writers.SpecializedLabelledFieldsMetadataWriter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class DILocalVariableKeyedMetadata extends AbstractKeyedMetadata
{
	@NotNull private final String variableName;
	private final int oneBasedIndex;
	@NotNull private final ScopeMetadata scope;
	@NotNull private final DIFileKeyedMetadata file;
	private final int lineNumber;
	@NotNull private final Metadata type;
	@NotNull private final Set<DIFlag> flags;

	// oneBasedIndex is zero, then this is NOT a method variable
	public DILocalVariableKeyedMetadata(@NotNull @NonNls final String variableName, final int oneBasedIndex, @NotNull final ScopeMetadata scope, @NotNull final DIFileKeyedMetadata file, final int lineNumber, @NotNull final Metadata type, @NotNull final Set<DIFlag> flags)
	{
		super(scope, file, type);
		// (name: "this", arg: 1, scope: !3, file: !2, line: 7, type: !3, flags:DIFlagArtificial)
		this.variableName = variableName;
		this.oneBasedIndex = oneBasedIndex;
		this.scope = scope;
		this.file = file;
		this.lineNumber = lineNumber;
		this.type = type;
		this.flags = flags;
	}

	@Override
	protected <X extends Exception> void writeLabelledFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final SpecializedLabelledFieldsMetadataWriter<X> specializedLabelledFieldsMetadataWriter) throws X
	{
		specializedLabelledFieldsMetadataWriter.writeLabelledField(name, variableName);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("arg", oneBasedIndex);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(AbstractKeyedMetadata.scope, scope, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(AbstractKeyedMetadata.file, file, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(AbstractKeyedMetadata.lineNumber, lineNumber);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(AbstractKeyedMetadata.type, type, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(AbstractKeyedMetadata.flags, flags);
	}
}
