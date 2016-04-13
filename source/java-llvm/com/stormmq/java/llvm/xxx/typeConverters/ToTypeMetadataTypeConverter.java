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

package com.stormmq.java.llvm.xxx.typeConverters;

import com.stormmq.java.classfile.domain.InternalTypeName;
import com.stormmq.java.classfile.domain.information.FieldInformation;
import com.stormmq.llvm.domain.metadata.creation.DebuggingTypeDefinitions;
import com.stormmq.llvm.domain.metadata.debugging.TypeMetadata;
import org.jetbrains.annotations.*;

public final class ToTypeMetadataTypeConverter implements TypeConverter<TypeMetadata>
{
	@NotNull private final TypeConverter<TypeMetadata> delegateTypeConverter;
	@NotNull private final DebuggingTypeDefinitions<?> debuggingTypeDefinitions;

	public ToTypeMetadataTypeConverter(@NotNull final TypeConverter<TypeMetadata> delegateTypeConverter, @NotNull final DebuggingTypeDefinitions<?> debuggingTypeDefinitions)
	{
		this.delegateTypeConverter = delegateTypeConverter;
		this.debuggingTypeDefinitions = debuggingTypeDefinitions;
	}

	@NotNull
	@Override
	public TypeMetadata convertField(@NotNull final FieldInformation fieldInformation)
	{
		final boolean isFinal = fieldInformation.isFinal;
		final TypeMetadata typeMetadata = delegateTypeConverter.convertField(fieldInformation);
		return isFinal ? debuggingTypeDefinitions.fieldConstantDerivedType(typeMetadata) : typeMetadata;
	}

	@NotNull
	@Override
	public TypeMetadata convertInternalTypeName(@NotNull final InternalTypeName internalTypeName)
	{
		return delegateTypeConverter.convertInternalTypeName(internalTypeName);
	}
}
