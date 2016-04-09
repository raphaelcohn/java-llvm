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

package com.stormmq.byteReaders;

import org.jetbrains.annotations.*;

import java.io.EOFException;
import java.nio.ByteBuffer;

public final class ByteBufferByteReader implements ByteReader
{
	@NotNull private final ByteBuffer byteBuffer;
	private int lastReadFullyPosition;

	public ByteBufferByteReader(@NotNull final ByteBuffer byteBuffer)
	{
		this.byteBuffer = byteBuffer;
		lastReadFullyPosition = 0;
	}

	@Override
	public long bytesReadSoFar()
	{
		return byteBuffer.position();
	}

	@Override
	public int readByte(@NonNls @NotNull final String what) throws EOFException
	{
		if (byteBuffer.remaining() == 0)
		{
			throw new EOFException("Not enough bytes left in byte buffer for readByte");
		}
		return byteBuffer.get() & ByteMask;
	}

	@Override
	public void readFully(@NotNull @NonNls final String what, final int length) throws EOFException
	{
		if (byteBuffer.remaining() < length)
		{
			throw new EOFException("Not enough bytes left in byte buffer for readFully");
		}
		lastReadFullyPosition = byteBuffer.position();
	}

	@Override
	public int byteAt(final int offset)
	{
		return byteBuffer.get(lastReadFullyPosition + offset);
	}

	@NotNull
	@Override
	public ByteBuffer readBytesBuffer(@NotNull @NonNls final String what, final int length) throws EOFException
	{
		if (byteBuffer.remaining() < length)
		{
			throw new EOFException("Not enough bytes left in byte buffer for readBytesBuffer");
		}
		final ByteBuffer slice = byteBuffer.slice();
		slice.limit(length);
		byteBuffer.position(byteBuffer.position() + length);
		return slice;
	}

	@Override
	public void close()
	{
	}
}
