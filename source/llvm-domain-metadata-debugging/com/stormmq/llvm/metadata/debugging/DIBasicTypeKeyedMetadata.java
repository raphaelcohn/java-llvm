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
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public final class DIBasicTypeKeyedMetadata extends AbstractKeyedMetadata implements TypeMetadata
{
	@NotNull private final DW_TAG tag;
	@NotNull private final String typeName;
	private final int sizeInBits;
	private final int alignmentInBits;
	@NotNull private final DW_ATE encoding;

	public DIBasicTypeKeyedMetadata(@NotNull final DW_TAG tag, @NotNull @NonNls final String typeName, final int sizeInBits, final int alignmentInBits, @NotNull final DW_ATE encoding)
	{
		this.tag = tag;
		// (name: "this", arg: 1, scope: !3, file: !2, line: 7, type: !3, flags:DIFlagArtificial)
		this.typeName = typeName;
		this.sizeInBits = sizeInBits;
		this.alignmentInBits = alignmentInBits;
		this.encoding = encoding;
	}

	@Override
	protected <X extends Exception> void writeLabelledFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final KeyedFieldsMetadataWriter<X> keyedFieldsMetadataWriter) throws X
	{
		keyedFieldsMetadataWriter.write("tag", tag);
		keyedFieldsMetadataWriter.write(name, typeName);
		keyedFieldsMetadataWriter.write(size, sizeInBits);
		keyedFieldsMetadataWriter.write(align, alignmentInBits);
		keyedFieldsMetadataWriter.write("encoding", encoding);
	}
}
