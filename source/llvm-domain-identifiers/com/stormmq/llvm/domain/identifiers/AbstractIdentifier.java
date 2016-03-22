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

package com.stormmq.llvm.domain.identifiers;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.Writable;
import com.stormmq.string.*;
import org.jetbrains.annotations.*;

import static com.stormmq.string.StringUtilities.encodeUtf8Bytes;
import static com.stormmq.string.StringUtilities.iterateOverStringCodePoints;
import static java.lang.String.format;

public abstract class AbstractIdentifier implements Writable
{
	private final byte prefix;
	@NotNull private final String identifier;
	@SuppressWarnings("FieldNotUsedInToString") private final boolean identifierNeedsEscaping;

	protected AbstractIdentifier(final byte prefix, @NonNls @NotNull final String identifier)
	{
		this.prefix = prefix;
		this.identifier = identifier;
		identifierNeedsEscaping = identifier.isEmpty() || doesIdentifierNeedsEscaping(identifier);
	}

	@NonNls
	private static boolean doesIdentifierNeedsEscaping(@NotNull @NonNls final String identifier)
	{
		final ValidatingCodePointUser codePointUser = new ValidatingCodePointUser();
		try
		{
			iterateOverStringCodePoints(identifier, codePointUser);
		}
		catch (final InvalidUtf16StringException e)
		{
			throw new IllegalArgumentException("LLVM identifier is not a valid UTF-16 string", e);
		}
		return codePointUser.needsEscaping;
	}

	@Override
	public final <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeByte(prefix);
		if (identifierNeedsEscaping)
		{
			byteWriter.writeByte('"');
			try
			{
				encodeUtf8Bytes(identifier, new WritingUtf8ByteUser<X>(byteWriter));
			}
			catch (final InvalidUtf16StringException e)
			{
				throw new IllegalStateException("Should never happen", e);
			}
			byteWriter.writeByte('"');
		}
		else
		{
			byteWriter.writeUtf8EncodedStringWithCertainty(identifier);
		}
	}

	@Override
	@NotNull
	public final String toString()
	{
		return ((char) prefix) + identifier;
	}

	@Override
	public final boolean equals(@Nullable final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		final AbstractIdentifier that = (AbstractIdentifier) o;

		return prefix == that.prefix && identifier.equals(that.identifier);
	}

	@Override
	public final int hashCode()
	{
		int result = (int) prefix;
		result = 31 * result + identifier.hashCode();
		return result;
	}

	@NotNull
	@NonNls
	public String name()
	{
		return identifier;
	}

	private static final class ValidatingCodePointUser implements CodePointUser<IllegalArgumentException>
	{
		private boolean needsEscaping;

		private ValidatingCodePointUser()
		{
			needsEscaping = false;
		}

		@Override
		public void useCodePoint(final int index, final int codePoint)
		{
			if (codePoint == 0)
			{
				throw new IllegalArgumentException("A LLVM identifier can not contain ASCII NUL");
			}

			if (needsEscaping)
			{
				return;
			}

			// [%@][-a-zA-Z$._][-a-zA-Z$._0-9]*
			if (index == 0)
			{
				switch (codePoint)
				{
					case '-':
					case 'a':
					case 'b':
					case 'c':
					case 'd':
					case 'e':
					case 'f':
					case 'g':
					case 'h':
					case 'i':
					case 'j':
					case 'k':
					case 'l':
					case 'm':
					case 'n':
					case 'o':
					case 'p':
					case 'q':
					case 'r':
					case 's':
					case 't':
					case 'u':
					case 'v':
					case 'w':
					case 'x':
					case 'y':
					case 'z':
					case 'A':
					case 'B':
					case 'C':
					case 'D':
					case 'E':
					case 'F':
					case 'G':
					case 'H':
					case 'I':
					case 'J':
					case 'K':
					case 'L':
					case 'M':
					case 'N':
					case 'O':
					case 'P':
					case 'Q':
					case 'R':
					case 'S':
					case 'T':
					case 'U':
					case 'V':
					case 'W':
					case 'X':
					case 'Y':
					case 'Z':
					case '$':
					case '.':
					case '_':
						return;

					default:
						needsEscaping = true;
						return;
				}
			}

			switch (codePoint)
			{
				case '-':
				case 'a':
				case 'b':
				case 'c':
				case 'd':
				case 'e':
				case 'f':
				case 'g':
				case 'h':
				case 'i':
				case 'j':
				case 'k':
				case 'l':
				case 'm':
				case 'n':
				case 'o':
				case 'p':
				case 'q':
				case 'r':
				case 's':
				case 't':
				case 'u':
				case 'v':
				case 'w':
				case 'x':
				case 'y':
				case 'z':
				case 'A':
				case 'B':
				case 'C':
				case 'D':
				case 'E':
				case 'F':
				case 'G':
				case 'H':
				case 'I':
				case 'J':
				case 'K':
				case 'L':
				case 'M':
				case 'N':
				case 'O':
				case 'P':
				case 'Q':
				case 'R':
				case 'S':
				case 'T':
				case 'U':
				case 'V':
				case 'W':
				case 'X':
				case 'Y':
				case 'Z':
				case '$':
				case '.':
				case '_':
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					return;

				default:
					needsEscaping = true;
			}

		}
	}

	private static final class WritingUtf8ByteUser<X extends Exception> implements Utf8ByteUser<X>
	{
		@SuppressWarnings("HardcodedFileSeparator") private static final char Slash = '\\';

		@NotNull private final ByteWriter<X> byteWriter;
		private int index;

		private WritingUtf8ByteUser(@NotNull final ByteWriter<X> byteWriter)
		{
			this.byteWriter = byteWriter;
			index = 0;
		}

		@Override
		public void useUnsignedByte(final int utf8Byte) throws X
		{
			// [%@][-a-zA-Z$._][-a-zA-Z$._0-9]*
			if (index == 0)
			{
				switch (utf8Byte)
				{
					case '-':
					case 'a':
					case 'b':
					case 'c':
					case 'd':
					case 'e':
					case 'f':
					case 'g':
					case 'h':
					case 'i':
					case 'j':
					case 'k':
					case 'l':
					case 'm':
					case 'n':
					case 'o':
					case 'p':
					case 'q':
					case 'r':
					case 's':
					case 't':
					case 'u':
					case 'v':
					case 'w':
					case 'x':
					case 'y':
					case 'z':
					case 'A':
					case 'B':
					case 'C':
					case 'D':
					case 'E':
					case 'F':
					case 'G':
					case 'H':
					case 'I':
					case 'J':
					case 'K':
					case 'L':
					case 'M':
					case 'N':
					case 'O':
					case 'P':
					case 'Q':
					case 'R':
					case 'S':
					case 'T':
					case 'U':
					case 'V':
					case 'W':
					case 'X':
					case 'Y':
					case 'Z':
					case '$':
					case '.':
					case '_':
						writeByte(utf8Byte);
						break;

					default:
						writeEscapedByte(utf8Byte);
				}
			}
			else
			{
				switch (utf8Byte)
				{
					case '-':
					case 'a':
					case 'b':
					case 'c':
					case 'd':
					case 'e':
					case 'f':
					case 'g':
					case 'h':
					case 'i':
					case 'j':
					case 'k':
					case 'l':
					case 'm':
					case 'n':
					case 'o':
					case 'p':
					case 'q':
					case 'r':
					case 's':
					case 't':
					case 'u':
					case 'v':
					case 'w':
					case 'x':
					case 'y':
					case 'z':
					case 'A':
					case 'B':
					case 'C':
					case 'D':
					case 'E':
					case 'F':
					case 'G':
					case 'H':
					case 'I':
					case 'J':
					case 'K':
					case 'L':
					case 'M':
					case 'N':
					case 'O':
					case 'P':
					case 'Q':
					case 'R':
					case 'S':
					case 'T':
					case 'U':
					case 'V':
					case 'W':
					case 'X':
					case 'Y':
					case 'Z':
					case '$':
					case '.':
					case '_':
					case '0':
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
						writeByte(utf8Byte);
						break;

					default:
						writeEscapedByte(utf8Byte);
				}
			}

			index++;
		}

		public void writeByte(final int utf8Byte) throws X
		{
			byteWriter.writeByte(utf8Byte);
		}

		private void writeEscapedByte(final int utf8Byte) throws X
		{
			byteWriter.writeByte(Slash);
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toHexString(utf8Byte));
		}
	}
}
