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

package com.stormmq.llvm.metadata.tbaa;

import com.stormmq.llvm.metadata.AbstractAnonymousMetadata;
import com.stormmq.llvm.metadata.AbstractNamedMetadata;
import com.stormmq.llvm.api.writing.metadataWriters.*;
import com.stormmq.llvm.metadata.writers.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class TbaaNamedMetadata extends AbstractNamedMetadata
{
	public TbaaNamedMetadata(@NotNull final List<TbaaStructEntry> structEntries)
	{
		super("tbaa.struct", new TbaaStructEntriesMetadata(structEntries));
	}

	private static final class TbaaStructEntriesMetadata extends AbstractAnonymousMetadata
	{
		private final List<TbaaStructEntry> structEntries;

		private TbaaStructEntriesMetadata(final List<TbaaStructEntry> structEntries)
		{
			this.structEntries = structEntries;
		}

		@Override
		protected <X extends Exception> void writeAnonymousFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final AnonymousFieldsMetadataWriter<X> anonymousFieldsMetadataWriter) throws X
		{
			for (final TbaaStructEntry structEntry : structEntries)
			{
				structEntry.writeAnonymousFields(metadataNodeIndexProvider, anonymousFieldsMetadataWriter);
			}
		}

		@Override
		protected <X extends Exception> void assignForwardReferencesForDependentNodes(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final MetadataWriter<X> metadataWriter) throws X, X
		{
			for (final TbaaStructEntry structEntry : structEntries)
			{
				structEntry.assignForwardReferencesForDependentNodes(metadataNodeIndexProvider);
			}
		}

		@Override
		protected <X extends Exception> void writeForDependentNodes(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final MetadataWriter<X> metadataWriter) throws X
		{
			for (final TbaaStructEntry structEntry : structEntries)
			{
				structEntry.write(metadataNodeIndexProvider, metadataWriter);
			}
		}
	}
}
