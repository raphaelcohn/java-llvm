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

package com.stormmq.llvm.metadata.writers;

import com.stormmq.java.parsing.utilities.string.InvalidUtf16StringException;
import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.metadata.Metadata;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.stormmq.java.parsing.utilities.string.StringUtilities.encodeUtf8Bytes;
import static java.lang.Integer.toHexString;
import static java.util.Locale.ENGLISH;

public final class CombinedMetadataWriter<X extends Exception> implements MetadataWriter<X>, SpecializedLabelledFieldsMetadataWriter<X>, AnonymousFieldsMetadataWriter<X>
{
	private static final byte ExclamationMark = '!';
	private static final byte Comma = ',';
	private static final byte Colon = ':';
	private static final byte DoubleQuote = '"';
	private static final byte Space = ',';
	private static final byte Equals = '=';
	@SuppressWarnings("HardcodedFileSeparator") private static final byte Slash = '\\';
	private static final byte Zero = '0';
	private static final byte[] SpaceEqualsSpaceExclamationMark = {Space, Equals, Space, ExclamationMark};
	private static final byte[] CommaSpace = {Comma, Space};
	private static final byte[] ColonSpace = {Colon, Space};
	private static final byte[] i32Space = {'i', '3', '2', Space};
	private static final byte[] i64Space = {'i', '6', '4', Space};
	private static final byte OpenBracket = '(';
	private static final byte CloseBracket = ')';
	private static final byte OpenBrace = '{';
	private static final byte CloseBrace = '}';
	@SuppressWarnings("HardcodedLineSeparator") private static final byte NewLine = '\n';
	private static final int NotWritingFields = -1;

	@NotNull private final ByteWriter<X> byteWriter;
	@NotNull private State state;
	private int fieldCount;

	private enum State
	{
		DoingNothing,
		WritingLabelledFields,
		WritingAnonymousFields,;

		private void guardDoingNothing()
		{
			if (this != DoingNothing)
			{
				throw new IllegalStateException(name());
			}
		}

		private void guardWritingLabelledFields()
		{
			if (this != WritingLabelledFields)
			{
				throw new IllegalStateException(name());
			}
		}

		private void guardWritingAnonymousFields()
		{
			if (this != WritingAnonymousFields)
			{
				throw new IllegalStateException(name());
			}
		}
	}

	public CombinedMetadataWriter(@NotNull final ByteWriter<X> byteWriter)
	{
		this.byteWriter = byteWriter;
		state = State.DoingNothing;
		fieldCount = NotWritingFields;
	}

	@Override
	public void close() throws X
	{
		byteWriter.close();
	}

	@Override
	public int writeNamedMetadataNodeForwardReference(final int metadataNodeIndex, @NotNull @NonNls final String metadataNodeName) throws X
	{
		writeByte(ExclamationMark);
		writeString(metadataNodeName);
		writeBytes(SpaceEqualsSpaceExclamationMark);
		writeByte(OpenBrace);
		writeInteger(metadataNodeIndex);
		writeByte(CloseBrace);

		return metadataNodeIndex;
	}

	@NotNull
	@Override
	public SpecializedLabelledFieldsMetadataWriter<X> writeSpecializedNodeStart(final int metadataNodeIndex, @NonNls @NotNull final String specializedNodeName) throws X
	{
		state.guardDoingNothing();
		state = State.WritingLabelledFields;
		fieldCount = 0;

		// !1 = !DIFile(...)\n
		writeMetadataNodeReference(metadataNodeIndex);
		writeBytes(SpaceEqualsSpaceExclamationMark);
		writeString(specializedNodeName);
		writeByte(OpenBracket);

		return this;
	}

	@Override
	public void writeSpecializedNodeEnd() throws X
	{
		state.guardWritingLabelledFields();
		state = State.DoingNothing;
		fieldCount = NotWritingFields;
		writeByte(CloseBracket);
		writeByte(NewLine);
	}

	@NotNull
	@Override
	public AnonymousFieldsMetadataWriter<X> writeAnonymousMetadataNodeStart(final int metadataNodeIndex) throws X
	{
		state.guardDoingNothing();
		state = State.WritingAnonymousFields;
		fieldCount = 0;

		writeMetadataNodeReference(metadataNodeIndex);
		writeBytes(SpaceEqualsSpaceExclamationMark);
		writeByte(OpenBrace);

		return this;
	}

	@Override
	public void writeAnonymousMetadataNodeEnd() throws X
	{
		state.guardWritingAnonymousFields();
		state = State.DoingNothing;
		fieldCount = NotWritingFields;
		writeByte(CloseBrace);
		writeByte(NewLine);
	}

	// eg <label: "value">
	@Override
	public void writeLabelledField(@NonNls @NotNull final String label, @NonNls @NotNull final String value) throws X
	{
		writeLabelledFieldExceptForValue(label);
		writeDoubleQuotedString(value);
	}

	@Override
	public <E extends Enum<E>> void writeLabelledField(@NonNls @NotNull final String label, @NonNls @NotNull final E value) throws X
	{
		writeLabelledFieldExceptForValue(label);
		writeString(value.name());
	}

	@Override
	public void writeLabelledField(@NonNls @NotNull final String label, final boolean value) throws X
	{
		writeLabelledFieldExceptForValue(label);
		writeString(value ? "true" : "false");
	}

	@Override
	public void writeLabelledField(@NonNls @NotNull final String label, final int value) throws X
	{
		writeLabelledFieldExceptForValue(label);
		writeInteger(value);
	}

	@Override
	public <E extends Enum<E>> void writeLabelledField(@NonNls @NotNull final String label, @NotNull final Set<E> values) throws X
	{
		final int size = values.size();
		if (size == 0)
		{
			return;
		}

		writeLabelledFieldExceptForValue(label);

		if (size != 1)
		{
			throw new UnsupportedOperationException("Do not know how to write multiple flags yet");
		}

		for (final E value : values)
		{
			writeString(value.name());
		}
	}

	private void writeLabelledFieldMetadataNode(@NonNls @NotNull final String label, final int metadataNodeIndex) throws X
	{
		writeLabelledFieldExceptForValue(label);
		writeMetadataNodeReference(metadataNodeIndex);
	}

	@Override
	public void writeLabelledField(@NonNls @NotNull final String label, @NotNull final Metadata reference, @NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider) throws X
	{
		final int metadataNodeIndex = metadataNodeIndexProvider.assignedReference(reference);
		writeLabelledFieldMetadataNode(label, metadataNodeIndex);
	}

	@Override
	public void writeLabelledFieldUnquotedString(@NonNls @NotNull final String label, @NonNls @NotNull final String value) throws X
	{
		writeLabelledFieldExceptForValue(label);
		writeString(value);
	}

	@Override
	public void writeAnonymousField(@NonNls @NotNull final String value) throws X
	{
		writeAnonymousFieldExceptForValue();
		writeByte(ExclamationMark);
		writeDoubleQuotedString(value);
	}

	@Override
	public void writeAnonymousField_i32(@NonNls final int value) throws X
	{
		writeAnonymousFieldExceptForValue();
		writeBytes(i32Space);
		writeInteger(value);
	}

	@Override
	public void writeAnonymousFieldMetadataNode(final int metadataNodeIndex) throws X
	{
		writeAnonymousFieldExceptForValue();
		writeMetadataNodeReference(metadataNodeIndex);
	}

	@Override
	public void writeAnonymousFieldUnquotedString(@NonNls @NotNull final String value) throws X
	{
		writeAnonymousFieldExceptForValue();
		writeString(value);
	}

	@Override
	public void writeAnonymousField_i64(final long value) throws X
	{
		writeAnonymousFieldExceptForValue();
		writeBytes(i64Space);
		writeLong(value);
	}

	private void writeMetadataNodeReference(final int metadataNodeIndex) throws X
	{
		writeByte(ExclamationMark);
		writeInteger(metadataNodeIndex);
	}

	private void writeInteger(final int value) throws X
	{
		writeString(Integer.toString(value));
	}

	private void writeLong(final long value) throws X
	{
		writeString(Long.toString(value));
	}

	private void writeLabelledFieldExceptForValue(@NotNull @NonNls final String label) throws X
	{
		state.guardWritingLabelledFields();
		writeCommaSpaceIfRequired();
		writeString(label);
		writeBytes(ColonSpace);
	}

	private void writeAnonymousFieldExceptForValue() throws X
	{
		state.guardWritingAnonymousFields();
		writeCommaSpaceIfRequired();
	}

	private void writeCommaSpaceIfRequired() throws X
	{
		if (fieldCount != 0)
		{
			writeBytes(CommaSpace);
		}
		fieldCount++;
	}

	@SuppressWarnings("MagicNumber")
	private void writeDoubleQuotedString(@NotNull @NonNls final String value) throws X
	{
		writeByte(DoubleQuote);
		try
		{
			encodeUtf8Bytes(value, utf8Byte ->
			{
				if (utf8Byte > 0x80 || utf8Byte < 32)
				{
					byteWriter.writeByte(Slash);
					if (utf8Byte < 0x10)
					{
						writeByte(Zero);
					}
					writeString(toHexString(utf8Byte).toUpperCase(ENGLISH));
				}
				else
				{
					byteWriter.writeByte(utf8Byte);
				}
			});
		}
		catch (final InvalidUtf16StringException e)
		{
			throw new IllegalArgumentException("double quoted string value should not be an invalid UTf-16 string", e);
		}
		writeByte(DoubleQuote);
	}

	private void writeByte(final byte value) throws X
	{
		byteWriter.writeByte(value);
	}

	private void writeBytes(@NotNull final byte... bytes) throws X
	{
		byteWriter.writeBytes(bytes);
	}

	private void writeString(@NotNull @NonNls final String value) throws X
	{
		try
		{
			byteWriter.writeUtf8EncodedString(value);
		}
		catch (final InvalidUtf16StringException e)
		{
			throw new IllegalArgumentException("value should not be an invalid UTf-16 string", e);
		}
	}
}
