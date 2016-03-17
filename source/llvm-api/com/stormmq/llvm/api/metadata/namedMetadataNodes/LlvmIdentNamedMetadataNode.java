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

package com.stormmq.llvm.api.metadata.namedMetadataNodes;

import com.stormmq.llvm.api.metadata.anonymousMetadataNodes.AbstractChildlessAnonymousMetadataNode;
import com.stormmq.llvm.api.metadataWriters.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public final class LlvmIdentNamedMetadataNode extends AbstractNamedMetadataNode
{
	@SuppressWarnings("HardcodedFileSeparator") @NotNull @NonNls public static final String Producer = "java-llvm (https://github.com/raphaelcohn/java-llvm)";
	@NotNull public static final NamedMetadataNode LLvmIdent = new LlvmIdentNamedMetadataNode(Producer);

	private LlvmIdentNamedMetadataNode(@NotNull final String producer)
	{
		super("llvm.ident", new AbstractChildlessAnonymousMetadataNode()
		{
			@Override
			protected <X extends Exception> void writeAnonymousFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final AnonymousFieldsMetadataWriter<X> anonymousFieldsMetadataWriter) throws X
			{
				anonymousFieldsMetadataWriter.writeAnonymousField(producer);
			}
		});
	}
}
