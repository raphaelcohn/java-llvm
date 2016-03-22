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

package com.stormmq.llvm.metadata;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.Writable;
import com.stormmq.llvm.metadata.metadataTuples.KeyedMetadataTuple;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.metadata.metadataTuples.NamedMetadataTuple.SpaceEqualsSpace;

public final class KeyWithMetadataField
{
	@NotNull
	public static <E extends Enum<E>> KeyWithMetadataField keyValue(@NotNull @NonNls final String key, @NotNull final E value)
	{
		return keyValue(key, new EnumConstantMetadata<>(value));
	}

	@NotNull
	public static KeyWithMetadataField keyValue(@NotNull @NonNls final String key, @NotNull final Metadata underlying)
	{
		return new KeyWithMetadataField(key, underlying);
	}

	@NotNull private final String key;
	@NotNull private final Metadata underlying;

	public KeyWithMetadataField(@NotNull @NonNls final String key, @NotNull final Metadata underlying)
	{
		this.key = key;
		this.underlying = underlying;
	}

	private boolean isConstant()
	{
		return underlying.isConstant();
	}

	private boolean hasBeenWritten()
	{
		return underlying.hasBeenWritten();
	}

	private int referenceIndex()
	{
		return underlying.referenceIndex();
	}

	public <X extends Exception> void writeIfNotAConstant(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		if (isConstant())
		{
			return;
		}

		if (hasBeenWritten())
		{
			return;
		}

		byteWriter.writeByte('!');
		byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(referenceIndex()));
		byteWriter.writeBytes(SpaceEqualsSpace);

		underlying.write(byteWriter);

		Writable.writeLineFeed(byteWriter);
		Writable.writeLineFeed(byteWriter);
	}

	public <X extends Exception> void writeKeyValue(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeUtf8EncodedStringWithCertainty(key);
		byteWriter.writeBytes(KeyedMetadataTuple.ColonSpace);

		if (isConstant())
		{
			underlying.write(byteWriter);
		}
		else
		{
			byteWriter.writeByte('!');
			byteWriter.writeUtf8EncodedStringWithCertainty(Integer.toString(underlying.referenceIndex()));
		}
	}
}
