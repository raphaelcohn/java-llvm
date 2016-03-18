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

import com.stormmq.llvm.metadata.AbstractKeyedMetadata;
import com.stormmq.llvm.metadata.writers.MetadataNodeIndexProvider;
import com.stormmq.llvm.metadata.writers.KeyedFieldsMetadataWriter;
import org.jetbrains.annotations.NotNull;

public final class DILexicalBlockFileKeyedMetadata extends AbstractKeyedMetadata implements ScopeMetadata
{
	private final int FieldNotInUse = -1;

	@NotNull private final ScopeMetadata scope;
	@NotNull private final DIFileKeyedMetadata file;
	private final int lineNumber;
	private final int column;
	private final int discriminator;

	public DILexicalBlockFileKeyedMetadata(@NotNull final ScopeMetadata scope, @NotNull final DIFileKeyedMetadata file, final int discriminator)
	{
		super(scope, file);
		if (discriminator < 0)
		{
			throw new IllegalArgumentException("discriminator can not be negative");
		}
		this.scope = scope;
		this.file = file;
		lineNumber = FieldNotInUse;
		column = FieldNotInUse;
		this.discriminator = discriminator;
	}

	public DILexicalBlockFileKeyedMetadata(@NotNull final ScopeMetadata scope, @NotNull final DIFileKeyedMetadata file, final int lineNumber, final int column)
	{
		super(scope, file);
		if (lineNumber < 0 || column < 0)
		{
			throw new IllegalArgumentException("lineNumber and column can not be negative");
		}
		this.scope = scope;
		this.file = file;
		this.lineNumber = lineNumber;
		this.column = column;
		discriminator = FieldNotInUse;
	}

	@Override
	protected <X extends Exception> void writeLabelledFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final KeyedFieldsMetadataWriter<X> keyedFieldsMetadataWriter) throws X
	{
		keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.scope, scope, metadataNodeIndexProvider);
		keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.file, file, metadataNodeIndexProvider);
		if (discriminator != FieldNotInUse)
		{
			keyedFieldsMetadataWriter.write("discriminator", discriminator);
		}
		else
		{
			keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.line, lineNumber);
			keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.column, column);
		}

	}
}
