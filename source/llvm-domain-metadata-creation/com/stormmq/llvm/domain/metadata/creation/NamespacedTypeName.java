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

package com.stormmq.llvm.domain.metadata.creation;

import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.domain.metadata.debugging.DIFileKeyedMetadataTuple;
import com.stormmq.llvm.domain.metadata.debugging.DINamespaceKeyedMetadataTuple;
import org.jetbrains.annotations.*;

public final class NamespacedTypeName<N>
{
	@NotNull private final N namespace;
	@NotNull public final String simpleName;

	public NamespacedTypeName(@NotNull final N namespace, @NotNull @NonNls final String simpleName)
	{
		this.namespace = namespace;
		this.simpleName = simpleName;
	}

	@Override
	public boolean equals(@Nullable final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		final NamespacedTypeName<?> that = (NamespacedTypeName<?>) o;

		return namespace.equals(that.namespace) && simpleName.equals(that.simpleName);
	}

	@Override
	public int hashCode()
	{
		int result = namespace.hashCode();
		result = 31 * result + simpleName.hashCode();
		return result;
	}

	@NotNull
	@NonNls
	public String mangleIdentifier(@NotNull final NamespaceSplitter<N> namespaceSplitter)
	{
		return namespaceSplitter.mangleIdentifier(namespace, simpleName);
	}

	@NotNull
	public DINamespaceKeyedMetadataTuple createDebuggingNamespace(@NotNull final ReferenceTracker referenceTracker, @NotNull final DIFileKeyedMetadataTuple file, @NotNull final NamespaceSplitter<N> namespaceSplitter)
	{
		return namespaceSplitter.namespace(referenceTracker, file, namespace);
	}
}
