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
import com.stormmq.llvm.metadata.writers.MetadataNodeIndexProvider;
import com.stormmq.llvm.metadata.writers.KeyedFieldsMetadataWriter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class DICompositeTypeKeyedMetadata extends AbstractKeyedMetadata implements TypeMetadata
{
	@NotNull private final DW_TAG tag;
	@NotNull private final String name;
	@NotNull private final DIFileKeyedMetadata file;
	private final int lineNumber;
	private final int sizeInBits;
	private final int alignmentInBits;
	@NotNull private final String identifier;
	@NotNull private final CollectionMetadata<?> elements;

	public DICompositeTypeKeyedMetadata(@NotNull final DW_TAG tag, @NonNls @NotNull final String name, @NotNull final DIFileKeyedMetadata file, final int lineNumber, final int sizeInBits, final int alignmentInBits, @NotNull @NonNls final String identifier, @NotNull final CollectionMetadata<?> elements)
	{
		super(file, elements);
		if (!tag.validForCompositeType)
		{
			throw new IllegalArgumentException(format(ENGLISH, "Tag '%1$s' is not valid for a composite type", tag));
		}
		this.tag = tag;
		this.name = name;
		this.file = file;
		this.lineNumber = lineNumber;
		this.sizeInBits = sizeInBits;
		this.alignmentInBits = alignmentInBits;
		this.identifier = identifier;
		this.elements = elements;
	}

	@Override
	protected <X extends Exception> void writeLabelledFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final KeyedFieldsMetadataWriter<X> keyedFieldsMetadataWriter) throws X
	{
		keyedFieldsMetadataWriter.write("tag", tag);
		keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.name, name);
		keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.file, file, metadataNodeIndexProvider);
		keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.lineNumber, lineNumber);
		keyedFieldsMetadataWriter.write(size, sizeInBits);
		keyedFieldsMetadataWriter.write(align, alignmentInBits);
		keyedFieldsMetadataWriter.write("identifier", identifier);
		keyedFieldsMetadataWriter.write("elements", elements, metadataNodeIndexProvider);
	}
}
