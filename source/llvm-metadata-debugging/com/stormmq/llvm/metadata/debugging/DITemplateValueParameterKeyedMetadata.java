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
import com.stormmq.llvm.metadata.TypeValue;
import com.stormmq.llvm.metadata.writers.MetadataNodeIndexProvider;
import com.stormmq.llvm.metadata.writers.KeyedFieldsMetadataWriter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public final class DITemplateValueParameterKeyedMetadata extends AbstractKeyedMetadata implements TemplateParameterMetadata
{
	@NotNull @NonNls private final String name;
	@NotNull private final TypeMetadata type;
	@NotNull private final TypeValue typeValue;

	public DITemplateValueParameterKeyedMetadata(@NotNull final String name, @NotNull final TypeMetadata type, @NotNull final TypeValue typeValue)
	{
		super(type);
		this.name = name;
		this.type = type;
		this.typeValue = typeValue;
	}

	@Override
	protected <X extends Exception> void writeLabelledFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final KeyedFieldsMetadataWriter<X> keyedFieldsMetadataWriter) throws X
	{
		keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.name, name);
		keyedFieldsMetadataWriter.write(AbstractKeyedMetadata.type, type, metadataNodeIndexProvider);
		keyedFieldsMetadataWriter.writeUnquotedString(AbstractKeyedMetadata.value, typeValue.encoded()); // eg value: i32 7
	}
}
