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
import com.stormmq.llvm.api.writing.metadataWriters.*;
import com.stormmq.llvm.metadata.writers.*;
import org.jetbrains.annotations.*;

public final class TbaaTagMetadata extends AbstractAnonymousMetadata
{
	@NotNull private final String identifier;
	@Nullable private final TbaaTagMetadata parent;
	private final boolean isConstant;

	// identifier is typically a type name, eg float, const float, etc
	public TbaaTagMetadata(@NotNull @NonNls final String identifier, @Nullable final TbaaTagMetadata parent, final boolean isConstant)
	{
		this.identifier = identifier;
		this.parent = parent;
		this.isConstant = isConstant;
	}

	@Override
	protected <X extends Exception> void writeAnonymousFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final AnonymousFieldsMetadataWriter<X> anonymousFieldsMetadataWriter) throws X
	{
		anonymousFieldsMetadataWriter.writeAnonymousField(identifier);
		if (parent != null)
		{
			anonymousFieldsMetadataWriter.writeAnonymousFieldMetadataNode(metadataNodeIndexProvider.assignedReference(parent));
		}
		if (isConstant)
		{
			anonymousFieldsMetadataWriter.writeAnonymousField_i64(1);
		}
	}

	@Override
	protected <X extends Exception> void assignForwardReferencesForDependentNodes(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final MetadataWriter<X> metadataWriter) throws X, X
	{
		if (parent != null)
		{
			parent.assignForwardReferences(metadataNodeIndexProvider, metadataWriter);
		}
	}

	@Override
	protected <X extends Exception> void writeForDependentNodes(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final MetadataWriter<X> metadataWriter) throws X
	{
		if (parent != null)
		{
			parent.write(metadataNodeIndexProvider, metadataWriter);
		}
	}

}
