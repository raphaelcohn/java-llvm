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

package com.stormmq.llvm.domain.comdat;

import com.stormmq.llvm.domain.ObjectFileFormat;
import org.jetbrains.annotations.*;

import java.util.EnumSet;

import static com.stormmq.llvm.domain.ObjectFileFormat.ELF;
import static com.stormmq.llvm.domain.ObjectFileFormat.PECOFF;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;
import static java.util.EnumSet.of;

public enum ComdatSelectionKind
{
	any(true),
	@SuppressWarnings({"SpellCheckingInspection", "unused"})exactmatch,
	@SuppressWarnings("unused")largest,
	@SuppressWarnings({"SpellCheckingInspection", "unused"})noduplicates,
	@SuppressWarnings({"SpellCheckingInspection", "unused"})samesize,
	;

	@NotNull public final byte[] llAssemblyValueWithLineFeed;
	@NotNull private final EnumSet<ObjectFileFormat> supportedObjectFileFormats;

	@SuppressWarnings("HardcodedLineSeparator")
	ComdatSelectionKind(final boolean isSupportedByElf)
	{
		llAssemblyValueWithLineFeed = encodeUtf8BytesWithCertaintyValueIsValid(name() + '\n');

		supportedObjectFileFormats = of(PECOFF);
		if (isSupportedByElf)
		{
			supportedObjectFileFormats.add(ELF);
		}
	}

	ComdatSelectionKind()
	{
		this(false);
	}

	public boolean isSupportedByObjectFileFormat(@NotNull final ObjectFileFormat objectFileFormat)
	{
		return supportedObjectFileFormats.contains(objectFileFormat);
	}
}
