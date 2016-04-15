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

package com.stormmq.byteWriters;

import com.stormmq.string.InvalidUtf16StringException;
import com.stormmq.string.Utf8ByteUser;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public interface ByteWriter<X extends Exception> extends AutoCloseable
{
	@Override
	void close() throws X;

	void writeByte(final int value) throws X;

	void writeBytes(@NotNull final byte... bytes) throws X;

	default void writeUtf8EncodedString(@NotNull @NonNls final String value) throws InvalidUtf16StringException, X
	{
		((Utf8ByteUser<X>) this::writeByte).encodeUtf8Bytes(value);
	}

	default void writeUtf8EncodedStringWithCertainty(@NotNull @NonNls final String value) throws X
	{
		try
		{
			writeUtf8EncodedString(value);
		}
		catch (final InvalidUtf16StringException e)
		{
			throw new IllegalArgumentException("value should have been a valid UTF-16 string", e);
		}
	}

	default void writeSpace() throws X
	{
		writeByte(' ');
	}

	@SuppressWarnings("HardcodedLineSeparator")
	default void writeLineFeed() throws X
	{
		writeByte('\n');
	}

	default void writeAsciiNull() throws X
	{
		writeByte(0);
	}

	@SuppressWarnings("HardcodedFileSeparator")
	default void writeSlash() throws X
	{
		writeByte('\\');
	}

	default void writeDoubleQuote() throws X
	{
		writeByte('"');
	}

	default void writeOpenBracket() throws X
	{
		writeByte('(');
	}

	default void writeCloseBracket() throws X
	{
		writeByte(')');
	}

	default void writeOpenAngleBracket() throws X
	{
		writeByte('<');
	}

	default void writeCloseAngleBracket() throws X
	{
		writeByte('>');
	}

	default void writeOpenSquareBracket() throws X
	{
		writeByte('[');
	}

	default void writeCloseSquareBracket() throws X
	{
		writeByte(']');
	}

	default void writeHash() throws X
	{
		writeByte('#');
	}

	default void writeAsterisk() throws X
	{
		writeByte('*');
	}

	default void writeHyphen() throws X
	{
		writeByte('-');
	}

	default void writeExclamationMark() throws X
	{
		writeByte('!');
	}
}
