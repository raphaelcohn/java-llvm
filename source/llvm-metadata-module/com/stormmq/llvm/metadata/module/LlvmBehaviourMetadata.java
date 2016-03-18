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

package com.stormmq.llvm.metadata.module;

import com.stormmq.llvm.metadata.AbstractAnonymousMetadata;
import com.stormmq.llvm.metadata.writers.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.Map.Entry;

import static com.stormmq.llvm.metadata.module.LlvmModuleFlagsBehaviourFlag.AppendUnique;
import static com.stormmq.llvm.metadata.module.LlvmModuleFlagsBehaviourFlag.Error;
import static com.stormmq.llvm.metadata.module.LlvmModuleFlagsBehaviourFlag.Warning;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Locale.ENGLISH;

public class LlvmBehaviourMetadata extends AbstractAnonymousMetadata
{
	@NotNull public static final LlvmBehaviourMetadata DwarfVersion2 = new LlvmBehaviourMetadata(Warning, "Dwarf Version", 2);
	@NotNull public static final LlvmBehaviourMetadata DebugInfoVersion = new LlvmBehaviourMetadata(Warning, "Debug Info Version", 3);
	@NotNull public static final LlvmBehaviourMetadata PicLevel2 = new LlvmBehaviourMetadata(Error, "PIC Level", 2);

	@NotNull
	public static LlvmBehaviourMetadata linkerLibraries(@NonNls @NotNull final String... libraries)
	{
		final LinkedHashSet<String> unique = new LinkedHashSet<>(asList(libraries));
		@NonNls final String[] options = new String[unique.size()];
		int index = 0;
		for (final String library : unique)
		{
			options[index] = "-l" + library;
			index++;
		}
		return linkerOptionsForTarget(emptyMap(), options);
	}

	@NotNull
	public static LlvmBehaviourMetadata linkerOptionsForTarget(@NotNull final Map<String, String> parameterizedOptions, @NotNull final String... options)
	{
		final StringBuilder stringBuilder = new StringBuilder(1024);
		stringBuilder.append("!{ ");

		boolean afterFirst = false;

		for (final Entry<String, String> entry : parameterizedOptions.entrySet())
		{
			if (afterFirst)
			{
				stringBuilder.append(", ");
			}
			else
			{
				afterFirst = true;
			}
			stringBuilder.append(format(ENGLISH, "!{ !\"%1$s\", !\"%2$s\"}", entry.getKey(), entry.getValue()));
		}

		for (final String option : options)
		{
			if (afterFirst)
			{
				stringBuilder.append(", ");
			}
			else
			{
				afterFirst = true;
			}
			// eg -lz
			stringBuilder.append(format(ENGLISH, "!{ !\"%1$s\"}", option));
		}

		stringBuilder.append(" }");

		return new LlvmBehaviourMetadata(AppendUnique, "Linker Options", stringBuilder.toString());
	}

	@NotNull
	public static LlvmBehaviourMetadata armCTypeWidthEnum(@NotNull final WcharWidth wcharWidth)
	{
		return new LlvmBehaviourMetadata(Error, "short_wchar", wcharWidth.value);
	}

	@NotNull
	public static LlvmBehaviourMetadata armCTypeWidthEnum(@NotNull final EnumWidth enumWidth)
	{
		return new LlvmBehaviourMetadata(Error, "short_enum", enumWidth.value);
	}

	@NotNull private final LlvmModuleFlagsBehaviourFlag behaviourFlag;
	@NotNull private final String key;
	@NotNull private final String encodedValue;

	public LlvmBehaviourMetadata(@NotNull final LlvmModuleFlagsBehaviourFlag behaviourFlag, @NonNls @NotNull final String key, @NonNls @NotNull final String encodedValue)
	{
		this.behaviourFlag = behaviourFlag;
		this.key = key;
		this.encodedValue = encodedValue;
	}

	public LlvmBehaviourMetadata(@NotNull final LlvmModuleFlagsBehaviourFlag behaviourFlag, @NonNls @NotNull final String key, final int value)
	{
		this(behaviourFlag, key, "i32 " + Integer.toString(value));
	}

	@Override
	protected final <X extends Exception> void writeAnonymousFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final AnonymousFieldsMetadataWriter<X> anonymousFieldsMetadataWriter) throws X
	{
		anonymousFieldsMetadataWriter.write(behaviourFlag.value);
		anonymousFieldsMetadataWriter.write(key);
		anonymousFieldsMetadataWriter.writeUnquotedString(encodedValue);
	}

	@Override
	protected final <X extends Exception> void assignForwardReferencesForDependentNodes(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final MetadataWriter<X> metadataWriter) throws X, X
	{
	}

	@Override
	protected final <X extends Exception> void writeForDependentNodes(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final MetadataWriter<X> metadataWriter) throws X
	{
	}
}
