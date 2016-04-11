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

package com.stormmq.llvm.domain.metadata.debugging;

import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.domain.metadata.metadataTuples.KeyedMetadataTuple;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public final class DINamespaceKeyedMetadataTuple extends KeyedMetadataTuple implements NamespaceScopeMetadata
{
	@NonNls @NotNull private final String name;
	@NotNull private final NamespaceScopeMetadata parentScope;

	// scope can be null
	public DINamespaceKeyedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @NonNls @NotNull final String name, @NotNull final NamespaceScopeMetadata parentScope, @NotNull final DIFileKeyedMetadataTuple file, final int lineNumber)
	{
		super(referenceTracker, false, "DINamespace", Key.name.with(name), Key.scope.with(parentScope), Key.file.with(file), Key.line.with(lineNumber));
		this.name = name;
		this.parentScope = parentScope;
	}

	@Override
	@NonNls
	@NotNull
	public String fullyQualifiedNamespace(@NonNls @NotNull final String separator)
	{
		return parentScope.fullyQualifiedNamespace(separator) + separator + name;
	}
}
