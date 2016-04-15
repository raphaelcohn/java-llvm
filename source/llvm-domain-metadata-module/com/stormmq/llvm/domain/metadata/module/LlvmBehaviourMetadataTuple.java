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

package com.stormmq.llvm.domain.metadata.module;

import com.stormmq.functions.collections.ListHelper;
import com.stormmq.llvm.domain.ReferenceTracker;
import com.stormmq.llvm.domain.metadata.*;
import com.stormmq.llvm.domain.metadata.metadataTuples.AnonymousMetadataTuple;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.Map.Entry;

import static com.stormmq.llvm.domain.typedValues.constantTypedValues.simpleConstantExpressions.IntegerConstantTypedValue.i32;
import static com.stormmq.llvm.domain.metadata.module.LlvmModuleFlagsBehaviourFlag.*;
import static com.stormmq.llvm.domain.metadata.module.LlvmModuleFlagsBehaviourFlag.Error;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;

public final class LlvmBehaviourMetadataTuple extends AnonymousMetadataTuple
{
	@NotNull
	private static LlvmBehaviourMetadataTuple llvmBehaviourMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @NotNull final LlvmModuleFlagsBehaviourFlag behaviourFlag, @NonNls @NotNull final String key, final int value)
	{
		return new LlvmBehaviourMetadataTuple(referenceTracker, behaviourFlag, key, new ConstantMetadata(i32(value)));
	}

	@NotNull
	public static LlvmBehaviourMetadataTuple DwarfVersion2(@NotNull final ReferenceTracker referenceTracker)
	{
		return llvmBehaviourMetadataTuple(referenceTracker, Warning, "Dwarf Version", 2);
	}

	@NotNull
	public static LlvmBehaviourMetadataTuple DebugInfoVersion(@NotNull final ReferenceTracker referenceTracker)
	{
		return llvmBehaviourMetadataTuple(referenceTracker, Warning, "Debug Info Version", 3);
	}

	@NotNull
	public static LlvmBehaviourMetadataTuple PicLevel2(@NotNull final ReferenceTracker referenceTracker)
	{
		return llvmBehaviourMetadataTuple(referenceTracker, Error, "PIC Level", 2);
	}

	@NotNull
	public static LlvmBehaviourMetadataTuple armCTypeWidthEnum(@NotNull final ReferenceTracker referenceTracker, @NotNull final WcharWidth wcharWidth)
	{
		return llvmBehaviourMetadataTuple(referenceTracker, Error, "short_wchar", wcharWidth.value);
	}

	@NotNull
	public static LlvmBehaviourMetadataTuple armCTypeWidthEnum(@NotNull final ReferenceTracker referenceTracker, @NotNull final EnumWidth enumWidth)
	{
		return llvmBehaviourMetadataTuple(referenceTracker, Error, "short_enum", enumWidth.value);
	}

	@NotNull
	public static LlvmBehaviourMetadataTuple linkerLibraries(@NotNull final ReferenceTracker referenceTracker, @NonNls @NotNull final String... libraries)
	{
		final Collection<String> unique = new LinkedHashSet<>(asList(libraries));
		@NonNls final String[] options = new String[unique.size()];
		int index = 0;
		for (final String library : unique)
		{
			options[index] = "-l" + library;
			index++;
		}
		return linkerOptionsForTarget(referenceTracker, emptyMap(), options);
	}

	@NotNull
	private static LlvmBehaviourMetadataTuple linkerOptionsForTarget(@NotNull final ReferenceTracker referenceTracker, @NotNull final Map<String, String> parameterizedOptions, @NotNull final String... options)
	{
		final int length = options.length;
		final List<Metadata> tupleX = ListHelper.newArrayList(parameterizedOptions.size() + length, tuple ->
		{
			for (final Entry<String, String> entry : parameterizedOptions.entrySet())
			{
				tuple.add(new AnonymousMetadataTuple(referenceTracker, ListHelper.newArrayList(2, keyedTuple ->
				{
					keyedTuple.add(new StringConstantMetadata(entry.getKey()));
					keyedTuple.add(new StringConstantMetadata(entry.getValue()));
				})));
			}

			for (final String option : options)
			{
				final List<Metadata> unkeyedTuple = singletonList(new StringConstantMetadata(option));
				tuple.add(new AnonymousMetadataTuple(referenceTracker, unkeyedTuple));
			}
		});

		return new LlvmBehaviourMetadataTuple(referenceTracker, AppendUnique, "Linker Options", new AnonymousMetadataTuple(referenceTracker, tupleX));
	}

	private LlvmBehaviourMetadataTuple(@NotNull final ReferenceTracker referenceTracker, @NotNull final LlvmModuleFlagsBehaviourFlag behaviourFlag, @NonNls @NotNull final String key, @NotNull final Metadata value)
	{
		super(referenceTracker, convert(behaviourFlag, key, value));
	}

	@NotNull
	private static List<? extends Metadata> convert(@NotNull final LlvmModuleFlagsBehaviourFlag behaviourFlag, @NonNls @NotNull final String key, @NotNull final Metadata value)
	{
		return ListHelper.newArrayList(3, tuple ->
		{
			tuple.add(behaviourFlag.constantMetadataField);
			tuple.add(new StringConstantMetadata(key));
			tuple.add(value);
		});
	}
}
