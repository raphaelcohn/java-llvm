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
import com.stormmq.llvm.metadata.CollectionMetadata;
import com.stormmq.llvm.metadata.AbstractKeyedMetadata;
import com.stormmq.llvm.api.writing.metadataWriters.*;
import com.stormmq.llvm.metadata.writers.MetadataNodeIndexProvider;
import com.stormmq.llvm.metadata.writers.SpecializedLabelledFieldsMetadataWriter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class DISubprogramKeyedMetadata extends AbstractKeyedMetadata implements ScopeMetadata
{
	@NotNull private final String functionName;
	@NotNull private final String linkageName;
	@NotNull private final ScopeMetadata scope;
	@NotNull private final DIFileKeyedMetadata file;
	private final int lineNumber;
	@NotNull private final DISubroutineTypeKeyedMetadata type;
	private final boolean isLocal;
	private final boolean isDefinition;
	private final int scopeLine;
	@NotNull private final TypeMetadata containingType;
	@NotNull private final Virtuality virtuality;
	private final int virtualIndex;
	@NotNull private final Set<DIFlag> flags;
	private final boolean isOptimized;
	@NotNull private final CollectionMetadata<TemplateParameterMetadata> templateParameters;
	@NotNull private final Metadata declaration;
	@NotNull private final CollectionMetadata<DILocalVariableKeyedMetadata> variables;

	public DISubprogramKeyedMetadata(@NotNull @NonNls final String functionName, @NotNull @NonNls final String linkageName, @NotNull final ScopeMetadata scope, @NotNull final DIFileKeyedMetadata file, final int lineNumber, @NotNull final DISubroutineTypeKeyedMetadata type, final boolean isLocal, final boolean isDefinition, final int scopeLine, @NotNull final TypeMetadata containingType, @NotNull final Virtuality virtuality, final int virtualIndex, @NotNull final Set<DIFlag> flags, final boolean isOptimized, @NotNull final CollectionMetadata<TemplateParameterMetadata> templateParameters, @NotNull final Metadata declaration, @NotNull final CollectionMetadata<DILocalVariableKeyedMetadata> variables)
	{
		super(scope, file, type, containingType, templateParameters, declaration, variables);
		// (name: "foo", linkageName: "_Zfoov", scope: !1, file: !2, line: 7, type: !3, isLocal: true, isDefinition: false, scopeLine: 8, containingType: !4, virtuality: DW_VIRTUALITY_pure_virtual, virtualIndex: 10, flags: DIFlagPrototyped, isOptimized: true, templateParams: !5, declaration: !6, variables: !7)
		this.functionName = functionName;
		this.linkageName = linkageName;
		this.scope = scope;
		this.file = file;
		this.lineNumber = lineNumber;
		this.type = type;
		this.isLocal = isLocal;
		this.isDefinition = isDefinition;
		this.scopeLine = scopeLine;
		this.containingType = containingType;
		this.virtuality = virtuality;
		this.virtualIndex = virtualIndex;
		this.flags = flags;
		this.isOptimized = isOptimized;
		this.templateParameters = templateParameters;
		this.declaration = declaration;
		this.variables = variables;
	}

	@Override
	protected <X extends Exception> void writeLabelledFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final SpecializedLabelledFieldsMetadataWriter<X> specializedLabelledFieldsMetadataWriter) throws X
	{
		specializedLabelledFieldsMetadataWriter.writeLabelledField(name, functionName);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(AbstractKeyedMetadata.linkageName, linkageName);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(AbstractKeyedMetadata.scope, scope, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(AbstractKeyedMetadata.file, file, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(AbstractKeyedMetadata.lineNumber, lineNumber);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(AbstractKeyedMetadata.type, type, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("isLocal", isLocal);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("isDefinition", isDefinition);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("scopeLine", scopeLine);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("containingType", containingType, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("virtuality", virtuality);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("virtualIndex", virtualIndex);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(AbstractKeyedMetadata.flags, flags);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(AbstractKeyedMetadata.isOptimized, isOptimized);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("templateParams", templateParameters, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField(AbstractKeyedMetadata.declaration, declaration, metadataNodeIndexProvider);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("variables", variables, metadataNodeIndexProvider);
	}
}
