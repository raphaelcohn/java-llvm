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

package com.stormmq.llvm.domain.metadata.debugging;

import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.domain.metadata.KeyWithMetadataField;
import com.stormmq.llvm.domain.metadata.debugging.dwarfTags.DerivedDwarfTag;
import com.stormmq.llvm.domain.metadata.metadataTuples.KeyedMetadataTuple;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.stormmq.functions.collections.ListHelper.newArrayList;
import static com.stormmq.llvm.domain.metadata.debugging.dwarfTags.DerivedDwarfTag.*;
import static com.stormmq.string.Formatting.format;
import static java.util.Collections.emptyList;

public final class DIDerivedTypeKeyedMetadataTuple extends KeyedMetadataTuple implements TypeMetadata
{
	@NotNull
	public static DIDerivedTypeKeyedMetadataTuple structMember(@NotNull final ReferenceTracker referenceTracker, @NotNull final DICompositeTypeKeyedMetadataTuple parent, @NotNull final DIFileKeyedMetadataTuple file, final int lineNumber, @NotNull final TypeMetadata underlyingType, final int storageSizeInBits, final int abiAlignmentInBits, @NotNull final String memberName, final int offsetInBits)
	{
		return new DIDerivedTypeKeyedMetadataTuple(referenceTracker, DW_TAG_member, file, lineNumber, underlyingType, storageSizeInBits, abiAlignmentInBits, newArrayList(2, keyWithMetadataFields ->
		{
			keyWithMetadataFields.add(Key.scope.with(parent));

			if (offsetInBits < 0)
			{
				throw new IllegalArgumentException(format("offsetInBits can not be negative ('%1$s')", offsetInBits));
			}

			keyWithMetadataFields.add(Key.name.with(memberName));
			if (offsetInBits != 0)
			{
				keyWithMetadataFields.add(Key.offset.with(offsetInBits));
			}
		}));
	}

	@NotNull
	public static DIDerivedTypeKeyedMetadataTuple constant(@NotNull final ReferenceTracker referenceTracker, @NotNull final DIFileKeyedMetadataTuple file, final int lineNumber, @NotNull final TypeMetadata underlyingType, final int storageSizeInBits, final int abiAlignmentInBits)
	{
		return new DIDerivedTypeKeyedMetadataTuple(referenceTracker, DW_TAG_const_type, file, lineNumber, underlyingType, storageSizeInBits, abiAlignmentInBits, emptyList());
	}

	@NotNull
	public static DIDerivedTypeKeyedMetadataTuple pointer(@NotNull final ReferenceTracker referenceTracker, @NotNull final DIFileKeyedMetadataTuple file, final int lineNumber, @NotNull final TypeMetadata underlyingType, final int storageSizeInBits, final int abiAlignmentInBits)
	{
		return new DIDerivedTypeKeyedMetadataTuple(referenceTracker, DW_TAG_pointer_type, file, lineNumber, underlyingType, storageSizeInBits, abiAlignmentInBits, emptyList());
	}

	@NotNull private final DerivedDwarfTag tag;
	@NotNull private final TypeMetadata underlyingType;
	private final int storageSizeInBits;
	private final int abiAlignmentInBits;

	@SuppressWarnings("OverloadedVarargsMethod")
	private DIDerivedTypeKeyedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @NotNull final DerivedDwarfTag tag, @NotNull final DIFileKeyedMetadataTuple file, final int lineNumber, @NotNull final TypeMetadata underlyingType, final int storageSizeInBits, final int abiAlignmentInBits, @NotNull final List<KeyWithMetadataField> fields)
	{
		super(referenceTracker, false, "DIDerivedType", newArrayList(4 + fields.size(), fieldsX ->
		{
			fieldsX.add(Key.tag.with(tag));
			fieldsX.add(Key.file.with(file));
			fieldsX.add(Key.line.with(lineNumber));
			fieldsX.add(Key.baseType.with(underlyingType));
			if (storageSizeInBits != 0)
			{
				fieldsX.add(Key.size.with(storageSizeInBits));
			}
			fieldsX.add(Key.align.with(abiAlignmentInBits));
			fieldsX.addAll(fields);
		}));
		this.tag = tag;
		this.underlyingType = underlyingType;
		this.storageSizeInBits = storageSizeInBits;
		this.abiAlignmentInBits = abiAlignmentInBits;
	}

	public boolean isConstType()
	{
		return tag == DW_TAG_const_type;
	}

	@Override
	public int storageSizeInBits()
	{
		return storageSizeInBits;
	}

	@Override
	public int abiAlignmentInBits()
	{
		return abiAlignmentInBits;
	}

	public boolean isPointerType()
	{
		return tag == DW_TAG_pointer_type;
	}
}
