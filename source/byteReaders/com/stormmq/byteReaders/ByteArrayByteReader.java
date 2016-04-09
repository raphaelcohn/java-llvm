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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.nio.ByteBuffer;

import static java.lang.Math.min;
import static java.nio.ByteBuffer.wrap;

public final class ByteArrayByteReader implements ByteReader
{
	@NotNull private final byte[] byteArray;
	private final int offset;
	private final int count;
	private int position;
	private int lastReadFullyPosition;

	@SuppressWarnings("MethodCanBeVariableArityMethod")
	public ByteArrayByteReader(@NotNull final byte[] byteArray)
	{
		this(byteArray, 0, byteArray.length);
	}

	private ByteArrayByteReader(@NotNull final byte[] byteArray, final int offset, final int length)
	{
		this.byteArray = byteArray;
		this.offset = offset;
		count = min(offset + length, byteArray.length);
		position = offset;
		lastReadFullyPosition = 0;
	}

	@Override
	public void close()
	{
	}

	@Override
	public long bytesReadSoFar()
	{
		return position - offset;
	}

	@Override
	public int readByte(@NonNls @NotNull final String what) throws EOFException
	{
		if (available() == 0)
		{
			throw new EOFException("Not enough bytes available for readByte");
		}
		final int result = byteArray[position] & ByteMask;
		position++;
		return result;
	}

	@Override
	public void readFully(@NotNull @NonNls final String what, final int length) throws EOFException
	{
		if (available() < length)
		{
			throw new EOFException("Not enough bytes available for readFully");
		}
		lastReadFullyPosition = position;
		position += length;
	}

	@Override
	public int byteAt(final int offset)
	{
		return byteArray[lastReadFullyPosition + offset];
	}

	@NotNull
	@Override
	public ByteBuffer readBytesBuffer(@NotNull @NonNls final String what, final int length) throws EOFException
	{
		if (available() < length)
		{
			throw new EOFException("Not enough bytes available for readBytesBuffer");
		}
		final ByteBuffer slice = wrap(byteArray, position, length).slice();
		position += length;
		return slice;
	}

	private int available()
	{
		return count - position;
	}
}
