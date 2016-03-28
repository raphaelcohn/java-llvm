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

import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.metadata.Metadata;
import com.stormmq.llvm.metadata.metadataTuples.KeyedMetadataTuple;
import org.jetbrains.annotations.NotNull;

public final class DILexicalBlockKeyedMetadataTuple extends KeyedMetadataTuple implements ScopeMetadata
{
	public DILexicalBlockKeyedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @NotNull final Metadata scope, @NotNull final Metadata file, final int lineNumber, final int column)
	{
		super(referenceTracker, false, "DILexicalBlock", Key.scope.with(scope), Key.file.with(file), Key.line.with(lineNumber), Key.column.with(column));

		if (lineNumber < 0)
		{
			throw new IllegalArgumentException("lexical block lineNumber can not be negative");
		}

		if (column < 0)
		{
			throw new IllegalArgumentException("lexical block column can not be negative");
		}
	}
}
