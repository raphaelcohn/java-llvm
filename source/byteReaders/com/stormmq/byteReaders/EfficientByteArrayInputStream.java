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

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

import static com.stormmq.byteReaders.ByteReader.ByteMask;
import static com.stormmq.byteReaders.ByteReader.EndOfFile;
import static java.lang.Math.min;
import static java.lang.System.arraycopy;

// Does less argument checking, too - watch out
public final class EfficientByteArrayInputStream extends InputStream
{
	@NotNull private final byte[] buffer;
	private final int count;
	private int position;
	private int mark;
	
	@SuppressWarnings("MethodCanBeVariableArityMethod")
	public EfficientByteArrayInputStream(@NotNull final byte[] buffer)
	{
		this(buffer, 0, buffer.length);
	}
	
	private EfficientByteArrayInputStream(@NotNull final byte[] buffer, final int offset, final int length)
	{
		this.buffer = buffer;
		count = min(offset + length, buffer.length);
		position = offset;
		mark = offset;
	}
	
	@Override
	public int read()
	{
		final int result = (position < count) ? (buffer[position] & ByteMask) : -1;
		position++;
		return result;
	}

	@SuppressWarnings("RefusedBequest")
	@Override
	public int read(@NotNull final byte[] buffer, final int offset, final int length)
	{
		if (position >= count)
		{
			return EndOfFile;
		}

		final int available = available();
		final int bytesRead = length > available ? available : length;

		if (bytesRead <= 0)
		{
			return 0;
		}

		arraycopy(this.buffer, position, buffer, offset, bytesRead);
		position += bytesRead;
		return bytesRead;
	}

	@SuppressWarnings("RefusedBequest")
	@Override
	public long skip(final long numberOfBytesToSkip)
	{
		@SuppressWarnings("NumericCastThatLosesPrecision") final int numberOfBytesToSkipInt = (int) numberOfBytesToSkip;

		final int numberOfBytesAvailable = available();
		final int available = numberOfBytesToSkipInt < numberOfBytesAvailable ? numberOfBytesToSkipInt : numberOfBytesAvailable;

		position += available;
		return available;
	}

	@SuppressWarnings("RefusedBequest")
	@Override
	public int available()
	{
		return count - position;
	}

	@SuppressWarnings("RefusedBequest")
	@Override
	public void close()
	{
	}

	@SuppressWarnings({"NonSynchronizedMethodOverridesSynchronizedMethod", "RefusedBequest"})
	@Override
	public void mark(final int readAheadLimit)
	{
		mark = position;
	}

	@SuppressWarnings({"NonSynchronizedMethodOverridesSynchronizedMethod", "RefusedBequest"})
	@Override
	public void reset()
	{
		position = mark;
	}

	@SuppressWarnings("RefusedBequest")
	@Override
	public boolean markSupported()
	{
		return true;
	}
}
