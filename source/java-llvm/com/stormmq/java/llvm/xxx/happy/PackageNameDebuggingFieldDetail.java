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

import com.stormmq.java.classfile.domain.information.FieldInformation;
import com.stormmq.java.llvm.xxx.typeConverters.*;
import com.stormmq.java.llvm.xxx.typeConverters.typeNameVisitors.*;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.llvm.domain.metadata.creation.DebuggingFieldDetail;
import com.stormmq.llvm.domain.metadata.creation.DebuggingTypeDefinitions;
import com.stormmq.llvm.domain.metadata.debugging.TypeMetadata;
import com.stormmq.llvm.domain.types.SizedType;
import com.stormmq.string.AbstractToString;
import org.jetbrains.annotations.*;

public final class PackageNameDebuggingFieldDetail extends AbstractToString implements DebuggingFieldDetail<PackageName>
{
	@NotNull private final ClassToStructureMap classToStructureMap;
	@NotNull private final TypeConverter<SizedType> sizedTypeTypeConverter;
	@NotNull private final FieldInformation fieldInformation;

	public PackageNameDebuggingFieldDetail(@NotNull final ClassToStructureMap classToStructureMap, @NotNull final TypeConverter<SizedType> sizedTypeTypeConverter, @NotNull final FieldInformation fieldInformation)
	{
		this.classToStructureMap = classToStructureMap;
		this.sizedTypeTypeConverter = sizedTypeTypeConverter;
		this.fieldInformation = fieldInformation;
	}

	@NotNull
	@Override
	protected Object[] fields()
	{
		return fields(classToStructureMap, sizedTypeTypeConverter, fieldInformation);
	}

	@NotNull
	@Override
	public String fieldName()
	{
		return fieldInformation.fieldName();
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
		return sizedTypeTypeConverter.convertField(fieldInformation);
	}

	@NotNull
	@Override
	public TypeMetadata asTypeMetadata(@NotNull final DebuggingTypeDefinitions<PackageName> debuggingTypeDefinitions)
	{
		return new ToTypeMetadataTypeConverter(new SimpleTypeConverter<>(new ToTypeMetadataTypeNameVisitor(classToStructureMap, debuggingTypeDefinitions)), debuggingTypeDefinitions).convertField(fieldInformation);
	}
}
