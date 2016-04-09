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

import java.io.*;
import java.nio.ByteBuffer;

import static java.nio.ByteBuffer.wrap;

public final class InputStreamByteReader implements ByteReader
{
	private long bytesReadSoFar;

	@NotNull private final InputStream inputStream;
	@NotNull private final byte[] buffer;

	public InputStreamByteReader(@NotNull final InputStream inputStream)
	{
		this.inputStream = inputStream;

		buffer = new byte[8];
		bytesReadSoFar = 0L;
	}

	@Override
	public void close() throws IOException
	{
		inputStream.close();
	}

	@Override
	public long bytesReadSoFar()
	{
		return bytesReadSoFar;
	}

	@Override
	public int readByte(@NonNls @NotNull final String what) throws EOFException
	{
		final int value;
		try
		{
			value = inputStream.read();
		}
		catch (final IOException e)
		{
			throw new UnexpectedIoException(what, e);
		}
		if (value == EndOfFile)
		{
			throw new EOFException(what);
		}
		bytesReadSoFar += OneByte;
		return value;
	}

	private void readBytes(@NotNull @NonNls final String what, @NotNull final byte[] buffer, final int length) throws EOFException
	{
		int readSoFar = 0;
		do
		{
			final int count;
			try
			{
				count = inputStream.read(buffer, readSoFar, length - readSoFar);
			}
			catch (final IOException e)
			{
				throw new UnexpectedIoException(what, e);
			}
			if (count == EndOfFile)
			{
				throw new EOFException(what);
			}
			readSoFar += count;
		}
		while (readSoFar < length);
		bytesReadSoFar += length;
	}

	@Override
	public void readFully(@NotNull @NonNls final String what, final int length) throws EOFException
	{
		readBytes(what, buffer, length);
	}

	@Override
	public int byteAt(final int offset)
	{
		return buffer[offset];
	}

	@NotNull
	@Override
	public ByteBuffer readBytesBuffer(@NotNull @NonNls final String what, final int length) throws EOFException
	{
		final byte[] buffer = new byte[length];
		readBytes(what, buffer, length);
		return wrap(buffer);
	}
}
