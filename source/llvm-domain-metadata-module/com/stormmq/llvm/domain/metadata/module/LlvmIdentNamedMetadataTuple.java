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
import com.stormmq.llvm.domain.metadata.metadataTuples.AnonymousMetadataTuple;
import com.stormmq.llvm.domain.metadata.metadataTuples.NamedMetadataTuple;
import com.stormmq.llvm.domain.metadata.StringConstantMetadata;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.stormmq.llvm.domain.metadata.ProducerConstant.Producer;
import static java.util.Collections.singletonList;

public final class LlvmIdentNamedMetadataTuple extends NamedMetadataTuple
{
	@NotNull private static final List<StringConstantMetadata> ProducerOnly = singletonList(new StringConstantMetadata(Producer));

	public LlvmIdentNamedMetadataTuple(@NotNull final ReferenceTracker referenceTracker)
	{
		super(referenceTracker, "llvm.ident", singletonList(new AnonymousMetadataTuple(referenceTracker, singletonList(new StringConstantMetadata(Producer)))));
	}
}