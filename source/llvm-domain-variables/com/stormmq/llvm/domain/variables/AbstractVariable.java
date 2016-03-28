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

package com.stormmq.llvm.domain.variables;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.*;
import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import org.jetbrains.annotations.*;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public abstract class AbstractVariable implements Writable
{
	@NotNull private static final byte[] CommaSpace = {',', ' '};
	@NotNull private static final byte[] SpaceEqualsSpace = encodeUtf8BytesWithCertaintyValueIsValid(" = ");
	@NotNull private static final byte[] SpaceUnnamedAddress = encodeUtf8BytesWithCertaintyValueIsValid(_unnamed_addr);

	@NotNull private final GlobalIdentifier identifier;
	@NotNull private final Linkage linkage;
	@NotNull private final Visibility visibility;
	@Nullable private final DllStorageClass dllStorageClass;
	@Nullable private final ThreadLocalStorageModel threadLocalStorageModel;
	private final boolean hasUnnamedAddress;

	protected AbstractVariable(@NotNull final GlobalIdentifier identifier, @NotNull final Linkage linkage, @NotNull final Visibility visibility, @Nullable final DllStorageClass dllStorageClass, @Nullable final ThreadLocalStorageModel threadLocalStorageModel, final boolean hasUnnamedAddress)
	{
		this.identifier = identifier;
		this.linkage = linkage;
		this.visibility = visibility;
		this.dllStorageClass = dllStorageClass;
		this.threadLocalStorageModel = threadLocalStorageModel;
		this.hasUnnamedAddress = hasUnnamedAddress;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		identifier.write(byteWriter);

		byteWriter.writeBytes(SpaceEqualsSpace);

		byteWriter.writeBytes(linkage.llAssemblyValue);

		Writable.writeSpace(byteWriter);
		byteWriter.writeBytes(visibility.llAssemblyValue);

		if (dllStorageClass != null)
		{
			Writable.writeSpace(byteWriter);
			byteWriter.writeBytes(dllStorageClass.llAssemblyValue);
		}

		if (threadLocalStorageModel != null)
		{
			Writable.writeSpace(byteWriter);
			byteWriter.writeBytes(threadLocalStorageModel.llvmAssemblyEncoding);
		}

		if (hasUnnamedAddress)
		{
			byteWriter.writeBytes(SpaceUnnamedAddress);
		}

		writeVariable(byteWriter);

		Writable.writeLineFeed(byteWriter);
	}

	protected abstract <X extends Exception> void writeVariable(@NotNull final ByteWriter<X> byteWriter) throws X;

	protected static <X extends Exception> void writeCommaSpace(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(CommaSpace);
	}
}
