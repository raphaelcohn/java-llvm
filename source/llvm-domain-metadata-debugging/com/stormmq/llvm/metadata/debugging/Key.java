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

import com.stormmq.llvm.domain.constants.Constant;
import com.stormmq.llvm.domain.identifiers.AbstractIdentifier;
import com.stormmq.llvm.metadata.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public enum Key
{
	align,
	arg,
	baseType,
	column,
	containingType,
	declaration,
	directory,
	discriminator,
	dwoId,
	element,
	emissionKind,
	encoding,
	entity,
	enums,
	file,
	filename,
	flags,
	functionName,
	globals,
	identifier,
	imports,
	isDefinition,
	isLocal,
	isOptimized,
	language,
	line,
	lineNumber,
	linkageName,
	macinfo,
	macros,
	name,
	nodes,
	producer,
	retainedTypes,
	runtimeVersion,
	scope,
	scopeLine,
	size,
	subprograms,
	tag,
	templateParams,
	type,
	types,
	value,
	variable,
	variables,
	virtualIndex,
	virtuality,
	;

	@NotNull
	public <E extends Enum<E>> KeyWithMetadataField with(@NotNull final Set<E> value)
	{
		return with(new RawMetadata(value));
	}

	@NotNull
	public KeyWithMetadataField with(@NotNull final Enum<?> value)
	{
		return with(new RawMetadata(value));
	}

	@NotNull
	public KeyWithMetadataField with(@NotNull final AbstractIdentifier identifier)
	{
		return with(new RawMetadata(identifier));
	}

	@NotNull
	public KeyWithMetadataField with(@NonNls @NotNull final Constant<?> value)
	{
		return with(new ConstantMetadata(value));
	}

	@NotNull
	public KeyWithMetadataField with(@NonNls @NotNull final String value)
	{
		return with(new RawMetadata('"' + value + '"'));
	}

	@NotNull
	public KeyWithMetadataField withUnquotedString(@NonNls @NotNull final String value)
	{
		return with(new RawMetadata(value));
	}

	@NotNull
	public KeyWithMetadataField with(final boolean value)
	{
		return with(new RawMetadata(value));
	}

	@NotNull
	public KeyWithMetadataField with(final int value)
	{
		return with(new RawMetadata(value));
	}

	@NotNull
	public KeyWithMetadataField with(@NotNull final Metadata value)
	{
		return new KeyWithMetadataField(toString(), value);
	}
}