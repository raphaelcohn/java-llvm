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
import com.stormmq.llvm.domain.types.LlvmType;
import org.jetbrains.annotations.*;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class Alias extends AbstractVariable
{
	@NotNull private static final byte[] SpaceAlias = encodeUtf8BytesWithCertaintyValueIsValid(" alias");
	@NotNull private final LlvmType[] aliaseeTypes;
	@NotNull private final String originalGlobalVariableOrFunctionName;

	// TODO: AliaseeTy, Aliasee
	public Alias(@NotNull @NonNls final String name, @NotNull final Linkage linkage, @NotNull final Visibility visibility, @Nullable final DllStorageClass dllStorageClass, @Nullable final ThreadLocalStorageModel threadLocalStorageModel, final boolean hasUnnamedAddress, @NotNull final LlvmType[] aliaseeTypes, @NotNull @NonNls final String originalGlobalVariableOrFunctionName)
	{
		super(name, linkage, visibility, dllStorageClass, threadLocalStorageModel, hasUnnamedAddress);
		this.aliaseeTypes = aliaseeTypes;
		this.originalGlobalVariableOrFunctionName = originalGlobalVariableOrFunctionName;

		if (!linkage.isPermittedForAlias)
		{
			throw new IllegalArgumentException(format(ENGLISH, "The linkage '%1$s' is not permitted for an alias", linkage));
		}

		if (aliaseeTypes.length == 0)
		{
			throw new IllegalArgumentException("There must be at least one aliasee");
		}

		if (name.equals(originalGlobalVariableOrFunctionName))
		{
			throw new IllegalArgumentException(format(ENGLISH, "The alias name '%1$s' can not match the name of the thing being aliased '%2$s'", name, originalGlobalVariableOrFunctionName));
		}
	}

	// @<Name> = [Linkage] [Visibility] [DLLStorageClass] [ThreadLocal] [unnamed_addr] alias <AliaseeTy>, <AliaseeTy>* @<Aliasee>
	@Override
	protected <X extends Exception> void writeVariable(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(SpaceAlias);

		final int length = aliaseeTypes.length;
		for(int index = 0; index < length; index++)
		{
			if (index == 0)
			{
				Writable.writeSpace(byteWriter);
			}
			else
			{
				writeCommaSpace(byteWriter);
			}
			aliaseeTypes[index].write(byteWriter);
		}

		Writable.writeSpace(byteWriter);
		byteWriter.writeUtf8EncodedStringWithCertainty(originalGlobalVariableOrFunctionName);
	}
}
