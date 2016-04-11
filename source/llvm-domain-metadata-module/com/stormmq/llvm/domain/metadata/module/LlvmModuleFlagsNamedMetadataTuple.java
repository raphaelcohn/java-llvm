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

package com.stormmq.llvm.domain.metadata.module;

import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.domain.metadata.metadataTuples.NamedMetadataTuple;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.metadata.module.EnumWidth.AtLeastAsLargeAsInt;
import static com.stormmq.llvm.domain.metadata.module.LlvmBehaviourMetadataTuple.*;
import static com.stormmq.llvm.domain.metadata.module.WcharWidth.Wide;
import static java.util.Arrays.asList;

public final class LlvmModuleFlagsNamedMetadataTuple extends NamedMetadataTuple
{
	@NotNull
	public static LlvmModuleFlagsNamedMetadataTuple typicalLlvmModuleFlags(@NotNull final ReferenceTracker referenceTracker)
	{
		return new LlvmModuleFlagsNamedMetadataTuple(referenceTracker, DwarfVersion2(referenceTracker), DebugInfoVersion(referenceTracker), PicLevel2(referenceTracker), armCTypeWidthEnum(referenceTracker, Wide), armCTypeWidthEnum(referenceTracker, AtLeastAsLargeAsInt));
	}

	private LlvmModuleFlagsNamedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @NotNull final LlvmBehaviourMetadataTuple... llvmBehaviourMetadataTuples)
	{
		super(referenceTracker, "llvm.module.flags", asList(llvmBehaviourMetadataTuples));
	}

}
