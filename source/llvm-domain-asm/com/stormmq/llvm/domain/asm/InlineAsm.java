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

package com.stormmq.llvm.domain.asm;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.LlvmWritable;
import com.stormmq.llvm.domain.identifiers.LlvmString;
import com.stormmq.llvm.domain.identifiers.LocalIdentifier;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.asm.Dialect.Intel;
import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;

public final class InlineAsm implements LlvmWritable
{
	@NotNull private static final byte[] asmSpace = encodeToUtf8ByteArrayWithCertaintyValueIsValid("asm ");
	@SuppressWarnings("SpellCheckingInspection") @NotNull private static final byte[] sideeffectSpace = encodeToUtf8ByteArrayWithCertaintyValueIsValid("sideeffect ");
	@SuppressWarnings("SpellCheckingInspection") @NotNull private static final byte[] alignstackSpace = encodeToUtf8ByteArrayWithCertaintyValueIsValid("alignstack ");
	@SuppressWarnings("SpellCheckingInspection") @NotNull private static final byte[] inteldialectSpace = encodeToUtf8ByteArrayWithCertaintyValueIsValid("inteldialect ");

	@NotNull private final LlvmString templateWithEscapedDollarSigns;
	@NotNull private final LlvmString constraintLetters;
	private final boolean hasSideEffects;
	private final boolean shouldAlignStack;
	@NotNull private final Dialect dialect;
	@NotNull private final TypeAndIdentifier<LocalIdentifier>[] parameters;

	@SafeVarargs
	public InlineAsm(@NotNull @NonNls final LlvmString templateWithEscapedDollarSigns, @NotNull @NonNls final LlvmString constraintLetters, final boolean hasSideEffects, final boolean shouldAlignStack, @NotNull final Dialect dialect, @NotNull final TypeAndIdentifier<LocalIdentifier>... parameters)
	{
		this.templateWithEscapedDollarSigns = templateWithEscapedDollarSigns;
		this.constraintLetters = constraintLetters;
		this.hasSideEffects = hasSideEffects;
		this.shouldAlignStack = shouldAlignStack;
		this.dialect = dialect;
		this.parameters = parameters;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		byteWriter.writeBytes(asmSpace);

		if (hasSideEffects)
		{
			byteWriter.writeBytes(sideeffectSpace);
		}

		if (shouldAlignStack)
		{
			byteWriter.writeBytes(alignstackSpace);
		}

		if (dialect == Intel)
		{
			byteWriter.writeBytes(inteldialectSpace);
		}

		templateWithEscapedDollarSigns.write(byteWriter, dataLayoutSpecification);
		byteWriter.writeBytes(CommaSpace);

		constraintLetters.write(byteWriter, dataLayoutSpecification);

		byteWriter.writeOpenBracket();
		final int length = parameters.length;
		for(int index = 0; index < length; index++)
		{
			if (index != 0)
			{
				byteWriter.writeBytes(CommaSpace);
			}
			parameters[index].write(byteWriter, dataLayoutSpecification);
		}
		byteWriter.writeBytes(LlvmWritable.CloseBracketLineFeed);
	}
}
