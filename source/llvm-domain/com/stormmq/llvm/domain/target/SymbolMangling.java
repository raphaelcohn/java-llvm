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

package com.stormmq.llvm.domain.target;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.ObjectFileFormat;
import com.stormmq.llvm.domain.LlvmWritable;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.ObjectFileFormat.PECOFF;
import static com.stormmq.llvm.domain.target.Alignment.writeField;

public enum SymbolMangling implements LlvmWritable
{
	ELF(ObjectFileFormat.ELF, 'e'),
	MIPS(ObjectFileFormat.ELF, 'm'),
	MachO(ObjectFileFormat.MachO, 'o'),
	WindowsCOFF(PECOFF, 'w'),
	WindowsCOFFPrefix(PECOFF, 'x'),
	;

	@NotNull public final ObjectFileFormat objectFileFormat;
	@NotNull private final byte[] dataLayoutEncoding;

	@SuppressWarnings("NumericCastThatLosesPrecision")
	SymbolMangling(@NotNull final ObjectFileFormat objectFileFormat, final char dataLayoutEncoding)
	{
		this.objectFileFormat = objectFileFormat;
		this.dataLayoutEncoding = new byte[]{(byte) dataLayoutEncoding};
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		writeField(byteWriter, "m:", dataLayoutEncoding);
	}
}