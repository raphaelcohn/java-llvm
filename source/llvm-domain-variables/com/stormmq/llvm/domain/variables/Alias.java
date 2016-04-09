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
import com.stormmq.llvm.domain.types.Type;
import com.stormmq.string.Formatting;
import org.jetbrains.annotations.*;

import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class Alias extends AbstractVariable
{
	@NotNull private static final byte[] SpaceAlias = encodeUtf8BytesWithCertaintyValueIsValid(" alias");
	@NotNull private final Type[] aliasedTypes;
	@NotNull private final GlobalIdentifier originalGlobalVariableOrFunctionName;

	public Alias(@NotNull final GlobalIdentifier globalIdentifier, @NotNull final Linkage linkage, @NotNull final Visibility visibility, @NotNull final DllStorageClass dllStorageClass, @NotNull final ThreadLocalStorageModel threadLocalStorageModel, final boolean hasUnnamedAddress, @NotNull final Type[] aliasedTypes, @NotNull final GlobalIdentifier originalGlobalVariableOrFunctionName)
	{
		super(globalIdentifier, linkage, visibility, dllStorageClass, threadLocalStorageModel, hasUnnamedAddress);
		this.aliasedTypes = aliasedTypes;
		this.originalGlobalVariableOrFunctionName = originalGlobalVariableOrFunctionName;

		if (!linkage.isPermittedForAlias)
		{
			throw new IllegalArgumentException(Formatting.format("The linkage '%1$s' is not permitted for an alias", linkage));
		}

		if (aliasedTypes.length == 0)
		{
			throw new IllegalArgumentException("There must be at least one aliased type");
		}

		if (globalIdentifier.equals(originalGlobalVariableOrFunctionName))
		{
			throw new IllegalArgumentException(Formatting.format("The alias name '%1$s' can not match the name of the thing being aliased '%2$s'", globalIdentifier, originalGlobalVariableOrFunctionName));
		}
	}

	@SuppressWarnings("SpellCheckingInspection")
	// @<Name> = [Linkage] [Visibility] [DLLStorageClass] [ThreadLocal] [unnamed_addr] alias <AliaseeTy>, <AliaseeTy>* @<Aliasee>
	@Override
	protected <X extends Exception> void writeVariable(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(SpaceAlias);

		final int length = aliasedTypes.length;
		for(int index = 0; index < length; index++)
		{
			if (index == 0)
			{
				byteWriter.writeSpace();
			}
			else
			{
				writeCommaSpace(byteWriter);
			}
			aliasedTypes[index].write(byteWriter);
		}

		byteWriter.writeSpace();
		originalGlobalVariableOrFunctionName.write(byteWriter);
	}
}
