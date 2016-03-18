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

import com.stormmq.llvm.api.writing.metadataWriters.*;
import com.stormmq.llvm.metadata.writers.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static java.util.Arrays.asList;

public final class CollectionMetadata<M extends Metadata> extends AbstractAnonymousMetadata
{
	@SafeVarargs
	@NotNull
	public static <M extends Metadata> CollectionMetadata<M> collectionAnonymousMetadataNode(@NotNull final M... metadataNodes)
	{
		return new CollectionMetadata<>(asList(metadataNodes));
	}

	@NotNull private final Collection<M> metadataNodes;

	public CollectionMetadata(@NotNull final Collection<M> metadataNodes)
	{
		this.metadataNodes = metadataNodes;
	}

	@Override
	protected <X extends Exception> void assignForwardReferencesForDependentNodes(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final MetadataWriter<X> metadataWriter) throws X
	{
		for (final Metadata metadata : metadataNodes)
		{
			metadata.assignForwardReferences(metadataNodeIndexProvider, metadataWriter);
		}
	}

	@Override
	protected <X extends Exception> void writeAnonymousFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final AnonymousFieldsMetadataWriter<X> anonymousFieldsMetadataWriter) throws X
	{
		for (final Metadata metadata : metadataNodes)
		{
			anonymousFieldsMetadataWriter.writeAnonymousFieldMetadataNode(metadataNodeIndexProvider.assignedReference(metadata));
		}
	}

	@Override
	protected <X extends Exception> void writeForDependentNodes(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final MetadataWriter<X> metadataWriter) throws X
	{
		for (final Metadata metadata : metadataNodes)
		{
			metadata.write(metadataNodeIndexProvider, metadataWriter);
		}
	}
}
