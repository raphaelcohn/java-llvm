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

import com.stormmq.functions.*;
import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.domain.metadata.metadataTuples.*;
import com.stormmq.llvm.domain.target.enumSizing.EnumSizeRule;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.*;

import static com.stormmq.functions.ListHelper.newArrayListExceptionally;
import static com.stormmq.llvm.domain.metadata.debugging.DISubrangeKeyedMetadataTuple.zeroBasedArrayDimension;
import static com.stormmq.llvm.domain.metadata.debugging.dwarfTags.CompositeDwarfTag.DW_TAG_array_type;
import static com.stormmq.llvm.domain.metadata.debugging.dwarfTags.CompositeDwarfTag.DW_TAG_enumeration_type;
import static com.stormmq.llvm.domain.metadata.debugging.dwarfTags.CompositeDwarfTag.DW_TAG_structure_type;

public final class DICompositeTypeKeyedMetadataTuple extends KeyedMetadataTuple implements TypeMetadata, ScopeMetadata
{
	private final int storageSizeInBits;
	private final int abiAlignmentInBits;

	public DICompositeTypeKeyedMetadataTuple(@NotNull final ReferenceTracker referenceTracker, final int storageSizeInBits, final int abiAlignmentInBits)
	{
		super(referenceTracker, false, "DICompositeType");
		this.storageSizeInBits = storageSizeInBits;
		this.abiAlignmentInBits = abiAlignmentInBits;
	}

	@NotNull
	public DICompositeTypeKeyedMetadataTuple populateStructure(@NonNls @NotNull final String name, @NotNull final ScopeMetadata scope, @NotNull final DIFileKeyedMetadataTuple file, final int lineNumber, @NotNull @NonNls final String identifier, final boolean isPacked, @NotNull final List<DIDerivedTypeKeyedMetadataTuple> elements)
	{
		// We do not yet support vtableHolder: !"_ZTSN3com7stormmq11inheritance13HasAnnotationE"

		populate(Key.tag.with(DW_TAG_structure_type), Key.name.with(name), Key.scope.with(scope), Key.file.with(file), Key.lineNumber.with(lineNumber), Key.size.with(storageSizeInBits), Key.align.with(abiAlignmentInBits), Key.identifier.with(identifier), Key.elements.with(new AnonymousMetadataTuple(referenceTracker, elements)));
		return this;
	}

	@NotNull
	public DICompositeTypeKeyedMetadataTuple populateEnum(@NotNull final EnumSizeRule enumSizing, @NonNls @NotNull final String name, @NotNull final DIFileKeyedMetadataTuple file, final int lineNumber, @NotNull final SizedIterator<Entry<String, Integer>> enumeratedValues)
	{
		final List<DIEnumeratorKeyedMetadataTuple> elements = newArrayListExceptionally(enumeratedValues.size(), list ->
		{
			final long[] maximumLong = new long[1];
			enumeratedValues.forEachRemaining(new MaximumEnumConsumer(referenceTracker, list, maximumLong));
		});

		populate(Key.tag.with(DW_TAG_enumeration_type), Key.name.with(name), Key.file.with(file), Key.lineNumber.with(lineNumber), Key.size.with(storageSizeInBits), Key.align.with(abiAlignmentInBits), Key.elements.with(new AnonymousMetadataTuple(referenceTracker, elements)));
		return this;
	}

	// !68 = !DICompositeType(tag: DW_TAG_array_type, baseType: !20, size: 1600, align: 32, elements: !69)
	@SuppressWarnings("ForLoopReplaceableByForEach")
	@NotNull
	public DICompositeTypeKeyedMetadataTuple populateArray(@NotNull final TypeMetadata underlyingType, @NotNull final int... dimensionSizes)
	{
		final int length = dimensionSizes.length;
		if (length == 0)
		{
			throw new IllegalArgumentException("There must be at least one dimension (dimensionSizes can not be empty)");
		}

		final List<DISubrangeKeyedMetadataTuple> elements = new ArrayList<>(length);
		int size = underlyingType.storageSizeInBits();
		for (int index = 0; index < length; index++)
		{
			final int dimensionSize = dimensionSizes[index];
			elements.add(zeroBasedArrayDimension(referenceTracker, dimensionSize));
			size *= dimensionSize;
		}
		populate(Key.tag.with(DW_TAG_array_type), Key.baseType.with(underlyingType), Key.size.with(storageSizeInBits), Key.align.with(abiAlignmentInBits), Key.elements.with(new AnonymousMetadataTuple(referenceTracker, elements)));
		return this;
	}

	@Override
	public int abiAlignmentInBits()
	{
		if (abiAlignmentInBits < 0)
		{
			throw new IllegalStateException("Can not ask for abiAlignmentInBits before a structure has been populated");
		}
		return abiAlignmentInBits;
	}

	@Override
	public int storageSizeInBits()
	{
		if (storageSizeInBits < 0)
		{
			throw new IllegalStateException("Can not ask for storageSizeInBits before a structure has been populated");
		}
		return storageSizeInBits;
	}

	private static final class MaximumEnumConsumer implements Consumer<Entry<String, Integer>>
	{
		@NotNull private final ReferenceTracker referenceTracker;
		@NotNull private final Collection<DIEnumeratorKeyedMetadataTuple> list;
		private final long[] maximumLong;

		private MaximumEnumConsumer(@NotNull final ReferenceTracker referenceTracker, @NotNull final Collection<DIEnumeratorKeyedMetadataTuple> list, @NotNull final long[] maximumLong)
		{
			this.referenceTracker = referenceTracker;
			this.list = list;
			this.maximumLong = maximumLong;
			maximumLong[0] = 0L;
		}

		@Override
		public void accept(final Entry<String, Integer> nameValuePair)
		{
			final String name = nameValuePair.getKey();
			final int value = nameValuePair.getValue();
			final long asLong = Integer.toUnsignedLong(value);
			if (asLong > maximumLong[0])
			{
				maximumLong[0] = asLong;
			}
			list.add(new DIEnumeratorKeyedMetadataTuple(referenceTracker, name, value));
		}
	}
}
