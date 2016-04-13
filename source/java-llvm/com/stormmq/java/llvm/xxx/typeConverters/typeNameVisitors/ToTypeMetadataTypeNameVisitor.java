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

package com.stormmq.java.llvm.xxx.typeConverters.typeNameVisitors;

import com.stormmq.java.llvm.xxx.happy.ClassToStructureMap;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.java.parsing.utilities.names.typeNames.TypeNameVisitor;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.llvm.domain.metadata.creation.*;
import com.stormmq.llvm.domain.metadata.debugging.DICompositeTypeKeyedMetadataTuple;
import com.stormmq.llvm.domain.metadata.debugging.TypeMetadata;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.SizedLocallyIdentifiedStructureType;
import com.stormmq.tuples.Triplet;
import org.jetbrains.annotations.NotNull;

public final class ToTypeMetadataTypeNameVisitor implements TypeNameVisitor<TypeMetadata>
{
	@NotNull private final ClassToStructureMap classToStructureMap;
	@NotNull private final DebuggingTypeDefinitions<PackageName> debuggingTypeDefinitions;
	
	public ToTypeMetadataTypeNameVisitor(@NotNull final ClassToStructureMap classToStructureMap, @NotNull final DebuggingTypeDefinitions<PackageName> debuggingTypeDefinitions)
	{
		this.classToStructureMap = classToStructureMap;
		this.debuggingTypeDefinitions = debuggingTypeDefinitions;
	}

	@NotNull
	@Override
	public TypeMetadata useVoid()
	{
		throw new IllegalStateException("void is not permissible in TypeMetadata");
	}

	@NotNull
	@Override
	public TypeMetadata useBoolean()
	{
		return debuggingTypeDefinitions.forBoolean();
	}

	@NotNull
	@Override
	public TypeMetadata useByte()
	{
		return debuggingTypeDefinitions.forByte();
	}

	@NotNull
	@Override
	public TypeMetadata useShort()
	{
		return debuggingTypeDefinitions.forShort();
	}

	@NotNull
	@Override
	public TypeMetadata useChar()
	{
		return debuggingTypeDefinitions.forChar();
	}

	@NotNull
	@Override
	public TypeMetadata useInt()
	{
		return debuggingTypeDefinitions.forInt();
	}

	@NotNull
	@Override
	public TypeMetadata useLong()
	{
		return debuggingTypeDefinitions.forLong();
	}

	@NotNull
	@Override
	public TypeMetadata useFloat()
	{
		return debuggingTypeDefinitions.forFloat();
	}

	@NotNull
	@Override
	public TypeMetadata useDouble()
	{
		return debuggingTypeDefinitions.forDouble();
	}

	@NotNull
	@Override
	public TypeMetadata useReference(@NotNull final KnownReferenceTypeName knownReferenceTypeName)
	{
		final Triplet<SizedLocallyIdentifiedStructureType, NamespacedTypeName<PackageName>, DebuggingFieldDetail<PackageName>[]> triplet = classToStructureMap.lookUpClassDetails(knownReferenceTypeName);
		final DICompositeTypeKeyedMetadataTuple baseType = debuggingTypeDefinitions.structureType(triplet.a, triplet.b, triplet.c);
		return debuggingTypeDefinitions.pointerTo(baseType);
	}
}
