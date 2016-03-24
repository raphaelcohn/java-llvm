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
import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import com.stormmq.llvm.metadata.Metadata;
import com.stormmq.llvm.metadata.metadataTuples.KeyedMetadataTuple;
import com.stormmq.llvm.metadata.metadataTuples.TypedMetadataTuple;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class DISubprogramKeyedMetadataTuple extends KeyedMetadataTuple implements ScopeMetadata
{
	public DISubprogramKeyedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @NotNull @NonNls final GlobalIdentifier functionName, @NotNull @NonNls final String linkageName, @NotNull final ScopeMetadata scope, @NotNull final DIFileKeyedMetadataTuple file, final int lineNumber, @NotNull final DISubroutineTypeKeyedMetadataTuple type, final boolean isLocal, final boolean isDefinition, final int scopeLine, @NotNull final TypeMetadata containingType, @NotNull final Virtuality virtuality, final int virtualIndex, @NotNull final Set<DIFlag> flags, final boolean isOptimized, @NotNull final TypedMetadataTuple<TemplateParameterMetadata> templateParameters, @NotNull final Metadata declaration, @NotNull final TypedMetadataTuple<DILocalVariableKeyedMetadataTuple> variables)
	{
		super(referenceTracker, true, "DISubprogram", Key.functionName.with(functionName), Key.linkageName.with(linkageName), Key.scope.with(scope), Key.file.with(file), Key.lineNumber.with(lineNumber), Key.type.with(type), Key.isLocal.with(isLocal), Key.isDefinition.with(isDefinition), Key.scopeLine.with(scopeLine), Key.containingType.with(containingType), Key.virtuality.with(virtuality), Key.virtualIndex.with(virtualIndex), Key.flags.with(flags), Key.isOptimized.with(isOptimized), Key.templateParams.with(templateParameters), Key.declaration.with(declaration), Key.variables.with(variables));
	}
}
