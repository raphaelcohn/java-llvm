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

import com.stormmq.functions.SizedIterable;
import com.stormmq.functions.SizedIterator;
import com.stormmq.java.classfile.domain.InternalTypeName;
import com.stormmq.java.classfile.domain.InvalidInternalTypeNameException;
import com.stormmq.java.classfile.domain.descriptors.FieldDescriptor;
import com.stormmq.java.classfile.domain.information.FieldInformation;
import com.stormmq.java.classfile.domain.information.TypeInformation;
import com.stormmq.java.classfile.domain.uniqueness.FieldUniqueness;
import com.stormmq.java.classfile.processing.typeInformationUsers.TypeInformationTriplet;
import com.stormmq.llvm.domain.metadata.creation.DebuggingTypeDefinitions;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.java.parsing.utilities.names.typeNames.TypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.TypeNameCategory;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.llvm.domain.identifiers.LocalIdentifier;
import com.stormmq.llvm.domain.metadata.creation.NamespacedTypeName;
import com.stormmq.llvm.domain.metadata.debugging.DICompositeTypeKeyedMetadataTuple;
import com.stormmq.llvm.domain.types.AddressableIdentifierType;
import com.stormmq.llvm.domain.types.CanBePointedToType;
import com.stormmq.llvm.domain.types.firstClassTypes.PointerValueType;
import com.stormmq.llvm.domain.metadata.debugging.TypeMetadata;
import com.stormmq.tuples.Pair;
import org.jetbrains.annotations.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.stormmq.llvm.domain.AddressSpace.GlobalAddressSpace;
import static com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType.floatingPointValueTypeFromSizeInBits;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.integerValueTypeFromSizeInBits;

public final class TypeConverter
{
	@NotNull private final ReferencedClasses referencedClasses;
	@NotNull private final DebuggingTypeDefinitions<PackageName> debuggingTypeDefinitions;
	@NotNull private final UsefulRecords usefulRecords;

	public TypeConverter(@NotNull final ReferencedClasses referencedClasses, @NotNull final DebuggingTypeDefinitions<PackageName> debuggingTypeDefinitions, @NotNull final UsefulRecords usefulRecords)
	{
		this.referencedClasses = referencedClasses;
		this.debuggingTypeDefinitions = debuggingTypeDefinitions;
		this.usefulRecords = usefulRecords;
	}

	@NotNull
	public <T extends CanBePointedToType> T convertFieldToLlvmType(@NotNull final FieldUniqueness fieldUniqueness)
	{
		final FieldDescriptor fieldDescriptor = fieldUniqueness.fieldDescriptor;
		final InternalTypeName internalTypeName = fieldDescriptor.internalTypeName;
		return convertInternalTypeNameToLlvmType(internalTypeName);
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public <T extends CanBePointedToType> T convertInternalTypeNameToLlvmType(@NotNull final InternalTypeName internalTypeName)
	{
		if (internalTypeName.isArray())
		{
			// TODO: Handle arrays
			System.err.println("Arrays are not yet supported");
		}

		final TypeName typeName = internalTypeName.typeName();
		final int sizeInBits = typeName.sizeInBitsOnASixtyFourBitCpu();
		final TypeNameCategory typeNameCategory = typeName.category();
		switch (typeNameCategory)
		{
			case SignedInteger:
			case UnsignedInteger:
				return (T) integerValueTypeFromSizeInBits(sizeInBits);

			case FloatingPoint:
				return (T) floatingPointValueTypeFromSizeInBits(sizeInBits);

			case Void:
				throw new IllegalStateException("Should not be void");

			case Reference:
				final LocalIdentifier add = referencedClasses.refersTo(internalTypeName);
				final AddressableIdentifierType referencedClass = new AddressableIdentifierType(add);
				return (T) new PointerValueType<>(referencedClass, GlobalAddressSpace);

			default:
				throw new IllegalStateException("Unexpected typeNameCategory");
		}
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public TypeMetadata convertInternalTypeNameToLlvmTypeMetadata(@NotNull final InternalTypeName internalTypeName)
	{
		if (internalTypeName.isArray())
		{
			// TODO: Handle arrays
			System.err.println("Arrays are not yet supported (creation)");
		}

		final TypeName typeName = internalTypeName.typeName();
		final int sizeInBits = typeName.sizeInBitsOnASixtyFourBitCpu();
		final TypeNameCategory typeNameCategory = typeName.category();
		switch (typeNameCategory)
		{
			case SignedInteger:
				return debuggingTypeDefinitions.signedInteger(sizeInBits);

			case UnsignedInteger:
				return debuggingTypeDefinitions.unsignedInteger(sizeInBits);

			case FloatingPoint:
				return debuggingTypeDefinitions.floatingPoint(sizeInBits);

			case Void:
				throw new IllegalStateException("Void should be converted into a constant null; this is a specialist operation");

			case Reference:
				final KnownReferenceTypeName knownReferenceTypeName;
				try
				{
					knownReferenceTypeName = internalTypeName.toKnownReferenceTypeName();
				}
				catch (final InvalidInternalTypeNameException e)
				{
					throw new IllegalStateException(e);
				}

				final TypeInformationTriplet self = usefulRecords.retrieve(knownReferenceTypeName);
				final NamespacedTypeName<PackageName> namespacedTypeName = new NamespacedTypeName<>(knownReferenceTypeName.parent(), knownReferenceTypeName.simpleTypeName());
				final boolean isPacked = usefulRecords.isPacked(self);
				final FieldsSizedIterator fields = new FieldsSizedIterator(this, self);

				return debuggingTypeDefinitions.structureType(namespacedTypeName, isPacked, fields);

			default:
				throw new IllegalStateException("Unexpected typeNameCategory");
		}
	}

	private static final class FieldsSizedIterator implements SizedIterator<Entry<String, TypeMetadata>>
	{
		private final SizedIterator<FieldInformation> fieldInformationIterator;
		@NotNull private final TypeConverter typeConverter;

		private FieldsSizedIterator(@NotNull final TypeConverter typeConverter, @NotNull final TypeInformation typeInformation)
		{
			this.typeConverter = typeConverter;
			fieldInformationIterator = typeInformation.instanceFieldsSizedIterator();
		}

		@Override
		public int size()
		{
			return fieldInformationIterator.size();
		}

		@Override
		public boolean hasNext()
		{
			return fieldInformationIterator.hasNext();
		}

		@Override
		public Entry<String, TypeMetadata> next()
		{
			final FieldInformation next = fieldInformationIterator.next();
			final FieldUniqueness fieldUniqueness = next.fieldUniqueness;

			final InternalTypeName internalTypeName = fieldUniqueness.fieldDescriptor.internalTypeName;
			return new Pair<>(fieldUniqueness.uniqueName(), typeConverter.convertInternalTypeNameToLlvmTypeMetadata(internalTypeName));
		}
	}
}
