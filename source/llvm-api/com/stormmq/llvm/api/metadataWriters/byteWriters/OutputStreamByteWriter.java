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

package com.stormmq.llvm.api.metadataWriters.byteWriters;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

public final class OutputStreamByteWriter extends AbstractByteWriter<IOException>
{
	@NotNull private final OutputStream outputStream;

	public OutputStreamByteWriter(@NotNull final OutputStream outputStream)
	{
		this.outputStream = outputStream;
	}

	@Override
	public void close() throws IOException
	{
		outputStream.close();
	}

	@Override
	public void writeByte(final int value) throws IOException
	{
		outputStream.write(value);
	}

	@Override
	public void writeBytes(@NotNull final byte... bytes) throws IOException
	{
		outputStream.write(bytes, 0, bytes.length);
	}
}
