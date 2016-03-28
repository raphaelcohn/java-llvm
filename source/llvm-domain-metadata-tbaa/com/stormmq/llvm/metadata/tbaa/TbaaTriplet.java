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

import com.stormmq.llvm.domain.constants.simpleConstants.IntegerConstant;
import com.stormmq.llvm.metadata.ConstantMetadata;
import com.stormmq.llvm.metadata.Metadata;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.i64;

public final class TbaaTriplet
{
	private final long offset;
	private final long length;
	@NotNull private final TbaaTagMetadataTuple tbaaTagMetadataTuple;

	public TbaaTriplet(final long offset, final long length, @NotNull final TbaaTagMetadataTuple tbaaTagMetadataTuple)
	{
		this.offset = offset;
		this.length = length;
		this.tbaaTagMetadataTuple = tbaaTagMetadataTuple;
	}

	public void addTo(@NotNull final Collection<Metadata> metadata)
	{
		metadata.add(new ConstantMetadata(new IntegerConstant(i64, offset)));
		metadata.add(new ConstantMetadata(new IntegerConstant(i64, length)));
		metadata.add(tbaaTagMetadataTuple);
	}
}
