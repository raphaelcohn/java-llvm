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

package com.stormmq.llvm.metadata.debugging;

import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.domain.target.dataLayout.PointerSizing;
import com.stormmq.llvm.metadata.KeyWithMetadataField;
import com.stormmq.llvm.metadata.debugging.dwarfTags.DerivedDwarfTag;
import com.stormmq.llvm.metadata.metadataTuples.KeyedMetadataTuple;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.stormmq.functions.ListHelper.newArrayList;
import static com.stormmq.llvm.metadata.debugging.dwarfTags.DerivedDwarfTag.*;
import static com.stormmq.string.Formatting.format;
import static java.util.Collections.emptyList;

public final class DIDerivedTypeKeyedMetadataTuple extends KeyedMetadataTuple implements TypeMetadata
{
	@NotNull private final DerivedDwarfTag tag;
	@NotNull private final TypeMetadata underlyingType;
	@NotNull private final PointerSizing pointerSizing;

	public DIDerivedTypeKeyedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @NotNull final DIFileKeyedMetadataTuple file, final int lineNumber, @NotNull final TypeMetadata underlyingType, @NotNull final PointerSizing pointerSizing, @NotNull final String memberName, final int offsetInBits)
	{
		this(referenceTracker, DW_TAG_member, file, lineNumber, underlyingType, pointerSizing, newArrayList(2, keyWithMetadataFields ->
		{
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

	public DIDerivedTypeKeyedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @NotNull final DerivedDwarfTag tag, @NotNull final DIFileKeyedMetadataTuple file, final int lineNumber, @NotNull final TypeMetadata underlyingType, @NotNull final PointerSizing pointerSizing)
	{
		// except for pointer-like
		this(referenceTracker, tag, file, lineNumber, underlyingType, pointerSizing, emptyList());
	}

	@SuppressWarnings("OverloadedVarargsMethod")
	private DIDerivedTypeKeyedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @NotNull final DerivedDwarfTag tag, @NotNull final DIFileKeyedMetadataTuple file, final int lineNumber, @NotNull final TypeMetadata underlyingType, @NotNull final PointerSizing pointerSizing, @NotNull final List<KeyWithMetadataField> fields)
	{
		super(referenceTracker, false, "DIDerivedType", newArrayList(4 + fields.size(), fieldsX ->
		{
			fieldsX.add(Key.tag.with(tag));
			fieldsX.add(Key.file.with(file));
			fieldsX.add(Key.line.with(lineNumber));
			fieldsX.add(Key.baseType.with(underlyingType));
			fieldsX.add(Key.size.with(ourSizeInBits(tag, underlyingType, pointerSizing)));
			fieldsX.add(Key.align.with(ourAlignmentInBits(tag, underlyingType, pointerSizing)));
			fieldsX.addAll(fields);
		}));
		this.tag = tag;
		this.underlyingType = underlyingType;
		this.pointerSizing = pointerSizing;
	}

	public void populateScope(@NotNull final DICompositeTypeKeyedMetadataTuple parent)
	{
		populateHackForCircularReferencesInMetadataModel(Key.scope.with(parent));
	}

	public boolean isConstType()
	{
		return tag == DW_TAG_const_type;
	}

	@Override
	public int sizeInBits()
	{
		return ourSizeInBits(tag, underlyingType, pointerSizing);
	}

	@Override
	public int alignmentInBits()
	{
		return ourAlignmentInBits(tag, underlyingType, pointerSizing);
	}

	private static int ourSizeInBits(@NotNull final DerivedDwarfTag tag, @NotNull final TypeMetadata underlyingType, @NotNull final PointerSizing pointerSizing)
	{
		if (tag.isEffectivelyAPointer())
		{
			return pointerSizing.sizeInBits;
		}
		return underlyingType.sizeInBits();
	}

	private static int ourAlignmentInBits(@NotNull final DerivedDwarfTag tag, @NotNull final TypeMetadata underlyingType, @NotNull final PointerSizing pointerSizing)
	{
		if (tag.isEffectivelyAPointer())
		{
			return pointerSizing.alignment.abiAlignmentInBits;
		}
		return underlyingType.sizeInBits();
	}
}
