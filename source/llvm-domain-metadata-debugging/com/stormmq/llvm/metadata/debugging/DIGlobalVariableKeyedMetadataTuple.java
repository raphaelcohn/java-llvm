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
import com.stormmq.llvm.domain.constants.Constant;
import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import com.stormmq.llvm.metadata.Metadata;
import com.stormmq.llvm.metadata.metadataTuples.KeyedMetadataTuple;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public final class DIGlobalVariableKeyedMetadataTuple extends KeyedMetadataTuple implements TypeMetadata
{
	public DIGlobalVariableKeyedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @NotNull final GlobalIdentifier globalIdentifier, @NotNull @NonNls final String linkageName, @NotNull final ScopeMetadata scope, @NotNull final DIFileKeyedMetadataTuple file, final int lineNumber, @NotNull final TypeMetadata type, final boolean isLocal, final boolean isDefinition, @NotNull final Constant<?> variable, @NotNull final Metadata declaration)
	{
		super(referenceTracker, false, "DIGlobalVariable", Key.name.with(globalIdentifier), Key.linkageName.with(linkageName), Key.scope.with(scope), Key.file.with(file), Key.line.with(lineNumber), Key.type.with(type), Key.isLocal.with(isLocal), Key.isDefinition.with(isDefinition), Key.variable.with(variable), Key.declaration.with(declaration));
	}
}
