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

package com.stormmq.java.llvm.xxx.happy;

import com.stormmq.java.classfile.domain.uniqueness.FieldUniqueness;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.llvm.domain.identifiers.*;
import org.jetbrains.annotations.*;

public final class Identifiers
{
	@NonNls @NotNull private static final String FieldIdentifierPrefix = "field.";
	@NonNls @NotNull private static final String ClassIdentifierPrefix = "class.";

	private Identifiers()
	{
	}

	@NotNull
	public static LocalIdentifier createClassIdentifier(@NotNull final KnownReferenceTypeName knownReferenceTypeName)
	{
		return new LocalIdentifier(classIdentifier(knownReferenceTypeName));
	}

	@NotNull
	public static GlobalIdentifier createStaticFieldGlobalIdentifier(@NotNull final KnownReferenceTypeName knownReferenceTypeName, @NotNull final FieldUniqueness fieldUniqueness)
	{
		return new GlobalIdentifier(fieldIdentifier(knownReferenceTypeName, fieldUniqueness));
	}

	@NotNull
	private static String classIdentifier(@NotNull final KnownReferenceTypeName referencedTypeName)
	{
		return ClassIdentifierPrefix + referencedTypeName.name();
	}

	@NotNull
	private static String fieldIdentifier(@NotNull final KnownReferenceTypeName referencedTypeName, @NotNull final FieldUniqueness fieldUniqueness)
	{
		return FieldIdentifierPrefix + referencedTypeName.name() + '.' + fieldUniqueness.uniqueName();
	}
}
