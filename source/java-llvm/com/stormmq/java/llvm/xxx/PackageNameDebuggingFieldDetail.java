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

package com.stormmq.java.llvm.xxx;

import com.stormmq.java.classfile.domain.information.FieldInformation;
import com.stormmq.java.classfile.domain.uniqueness.FieldUniqueness;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.llvm.domain.metadata.creation.DebuggingFieldDetail;
import com.stormmq.llvm.domain.metadata.creation.DebuggingTypeDefinitions;
import com.stormmq.llvm.domain.metadata.debugging.TypeMetadata;
import com.stormmq.llvm.domain.types.SizedType;
import org.jetbrains.annotations.*;

public final class PackageNameDebuggingFieldDetail implements DebuggingFieldDetail<PackageName>
{
	@NotNull private final ClassToStructureMap classToStructureMap;
	@NotNull private final TypeConverter<SizedType> sizedTypeTypeConverter;
	@NotNull private final FieldInformation fieldInformation;
	@NotNull private final FieldUniqueness fieldUniqueness;

	public PackageNameDebuggingFieldDetail(@NotNull final ClassToStructureMap classToStructureMap, @NotNull final TypeConverter<SizedType> sizedTypeTypeConverter, @NotNull final FieldInformation fieldInformation)
	{
		this.classToStructureMap = classToStructureMap;
		this.sizedTypeTypeConverter = sizedTypeTypeConverter;
		this.fieldInformation = fieldInformation;
		fieldUniqueness = fieldInformation.fieldUniqueness;
	}

	@NotNull
	@Override
	public String fieldName()
	{
		return fieldUniqueness.uniqueName();
	}

	@Override
	public boolean isConstant()
	{
		return fieldInformation.isFinal;
	}

	@NotNull
	@Override
	public SizedType fieldType()
	{
		return sizedTypeTypeConverter.convertField(fieldUniqueness);
	}

	@NotNull
	@Override
	public TypeMetadata asTypeMetadata(@NotNull final DebuggingTypeDefinitions<PackageName> debuggingTypeDefinitions)
	{
		return new TypeConverter<>(new ToTypeMetadataTypeNameVisitor(classToStructureMap, debuggingTypeDefinitions)).convertField(fieldUniqueness);
	}
}
