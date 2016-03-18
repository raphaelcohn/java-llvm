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

package com.stormmq.llvm.metadata;

import com.stormmq.java.parsing.utilities.StringConstants;
import com.stormmq.llvm.api.writing.metadataWriters.*;
import com.stormmq.llvm.metadata.writers.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractKeyedMetadata extends AbstractMetadata
{
	@NonNls @NotNull public static final String align = "align";
	@NonNls @NotNull public static final String column = "column";
	@NonNls @NotNull public static final String declaration = "declaration";
	@NonNls @NotNull public static final String flags = "flags";
	@NonNls @NotNull public static final String file = "file";
	@NonNls @NotNull public static final String isOptimized = "isOptimized";
	@NonNls @NotNull public static final String line = "line";
	@NonNls @NotNull public static final String lineNumber = "lineNumber";
	@NonNls @NotNull public static final String linkageName = "linkageName";
	@NonNls @NotNull public static final String macinfo = "macinfo";
	@NonNls @NotNull public static final String name = "name";
	@NonNls @NotNull public static final String size = "size";
	@NonNls @NotNull public static final String scope = "scope";
	@NonNls @NotNull public static final String type = "type";
	@NonNls @NotNull public static final String value = StringConstants.value;
	private static final int length = "KeyedMetadata".length();

	@NotNull @NonNls private final String nodeName;
	@NotNull private final Metadata[] dependentNodes;

	protected AbstractKeyedMetadata(@NotNull final Metadata... dependentNodes)
	{
		final String simpleName = getClass().getSimpleName();
		this.nodeName = simpleName.substring(0, simpleName.length() - length);
		this.dependentNodes = dependentNodes;
	}

	@Override
	protected final <X extends Exception> void assignForwardReferencesX(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final MetadataWriter<X> metadataWriter) throws X
	{
		metadataNodeIndexProvider.assignReference(this);
	}

	@Override
	protected final <X extends Exception> void assignForwardReferencesForDependentNodes(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final MetadataWriter<X> metadataWriter) throws X
	{
		for (final Metadata metadata : dependentNodes)
		{
			metadata.assignForwardReferences(metadataNodeIndexProvider, metadataWriter);
		}
	}

	@Override
	protected final <X extends Exception> void writeX(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final MetadataWriter<X> metadataWriter) throws X
	{
		final int metadataNodeIndex = metadataNodeIndexProvider.assignedReference(this);
		final SpecializedLabelledFieldsMetadataWriter<X> specializedLabelledFieldsMetadataWriter = metadataWriter.writeSpecializedNodeStart(metadataNodeIndex, nodeName);
		writeLabelledFields(metadataNodeIndexProvider, specializedLabelledFieldsMetadataWriter);
		metadataWriter.writeSpecializedNodeEnd();
	}

	@Override
	protected final <X extends Exception> void writeForDependentNodes(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final MetadataWriter<X> metadataWriter) throws X
	{
		for (final Metadata metadata : dependentNodes)
		{
			metadata.write(metadataNodeIndexProvider, metadataWriter);
		}
	}

	protected abstract <X extends Exception> void writeLabelledFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final SpecializedLabelledFieldsMetadataWriter<X> specializedLabelledFieldsMetadataWriter) throws X;
}
