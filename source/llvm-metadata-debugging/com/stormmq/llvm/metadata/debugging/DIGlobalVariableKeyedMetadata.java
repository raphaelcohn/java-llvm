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

import com.stormmq.llvm.metadata.writers.MetadataNodeIndexProvider;
import com.stormmq.llvm.metadata.writers.KeyedFieldsMetadataWriter;
import com.stormmq.llvm.metadata.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public final class DIGlobalVariableKeyedMetadata extends AbstractKeyedMetadata implements TypeMetadata
{
	@NotNull @NonNls private final String name;
	@NotNull private final String linkageName;
	@NotNull private final ScopeMetadata scope;
	@NotNull private final DIFileKeyedMetadata file;
	private final int lineNumber;
	@NotNull private final TypeMetadata type;
	private final boolean isLocal;
	private final boolean isDefinition;
	@NotNull private final TypeValue variable;
	@NotNull private final Metadata declaration;

	public DIGlobalVariableKeyedMetadata(@NotNull @NonNls final String name, @NotNull @NonNls final String linkageName, @NotNull final ScopeMetadata scope, @NotNull final DIFileKeyedMetadata file, final int lineNumber, @NotNull final TypeMetadata type, final boolean isLocal, final boolean isDefinition, @NotNull final TypeValue variable, @NotNull final Metadata declaration)
	{
		super(scope, file, type, declaration);
		this.name = name;
		this.linkageName = linkageName;
		this.scope = scope;
		this.file = file;
		this.lineNumber = lineNumber;
		this.type = type;
		this.isLocal = isLocal;
		this.isDefinition = isDefinition;
		this.variable = variable;
		this.declaration = declaration;
	}

	@Override
	protected <X extends Exception> void writeLabelledFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final KeyedFieldsMetadataWriter<X> keyedFieldsMetadataWriter) throws X
	{
		keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.name, name);
		keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.linkageName, linkageName);
		keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.scope, scope, metadataNodeIndexProvider);
		keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.file, file, metadataNodeIndexProvider);
		keyedFieldsMetadataWriter.write(line, lineNumber);
		keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.type, type, metadataNodeIndexProvider);
		keyedFieldsMetadataWriter.write("isLocal", isLocal);
		keyedFieldsMetadataWriter.write("isDefinition", isDefinition);
		keyedFieldsMetadataWriter.write("variable", variable.encoded());
		keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.declaration, declaration, metadataNodeIndexProvider);
	}
}
