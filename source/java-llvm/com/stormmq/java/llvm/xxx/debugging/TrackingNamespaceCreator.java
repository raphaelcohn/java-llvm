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

package com.stormmq.java.llvm.xxx.debugging;

import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.metadata.debugging.*;
import org.jetbrains.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.stormmq.llvm.metadata.debugging.NullNamespaceScopeMetadata.NullNamespaceScopeMetadataInstance;
import static com.stormmq.llvm.metadata.debugging.ScopeMetadata.UnknownLineNumber;

public final class TrackingNamespaceCreator
{
	@NotNull private final ReferenceTracker referenceTracker;
	@NotNull private final Map<String, DINamespaceKeyedMetadataTuple> knownNamespaces;

	public TrackingNamespaceCreator(@NotNull final ReferenceTracker referenceTracker)
	{
		this.referenceTracker = referenceTracker;
		knownNamespaces = new HashMap<>(16);
	}

	@NotNull
	public <N> DINamespaceKeyedMetadataTuple namespace(@NotNull final N namespace, @NotNull final BiConsumer<N, Consumer<String>> namespaceSplitter, @NotNull final DIFileKeyedMetadataTuple file)
	{
		final NamespaceScopeMetadata[] parent = {NullNamespaceScopeMetadataInstance};

		final Consumer<String> stringConsumer = name ->
		{
			final NamespaceScopeMetadata parentScope = parent[0];
			parent[0] = knownNamespaces.computeIfAbsent(parentScope.fullyQualifiedNamespace(".") + '.' + name, key -> new DINamespaceKeyedMetadataTuple(referenceTracker, name, parentScope, file, UnknownLineNumber));
		};

		namespaceSplitter.accept(namespace, stringConsumer);

		return (DINamespaceKeyedMetadataTuple) parent[0];
	}
}
