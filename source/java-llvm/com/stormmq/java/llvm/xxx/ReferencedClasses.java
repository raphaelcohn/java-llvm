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

import com.stormmq.java.classfile.domain.InternalTypeName;
import com.stormmq.java.classfile.domain.InvalidInternalTypeNameException;
import com.stormmq.java.classfile.domain.descriptors.FieldDescriptor;
import com.stormmq.java.classfile.domain.uniqueness.FieldUniqueness;
import com.stormmq.java.classfile.processing.typeInformationUsers.TypeInformationTriplet;
import com.stormmq.java.llvm.xxx.debugging.DebuggingTypeDefinitions;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.java.parsing.utilities.names.typeNames.TypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.TypeNameCategory;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import com.stormmq.llvm.domain.identifiers.LocalIdentifier;
import com.stormmq.llvm.domain.types.*;
import com.stormmq.llvm.domain.types.firstClassTypes.PointerValueType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.IdentifiedLocallyIdentifiedStructureType;
import com.stormmq.java.llvm.xxx.debugging.LocalIdentifierRecorder;
import com.stormmq.llvm.metadata.debugging.TypeMetadata;
import org.jetbrains.annotations.*;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static com.stormmq.functions.CollectionHelper.add;
import static com.stormmq.java.llvm.xxx.Identifiers.createClassIdentifier;
import static com.stormmq.java.llvm.xxx.Identifiers.createStaticFieldGlobalIdentifier;
import static com.stormmq.llvm.domain.AddressSpace.DefaultAddressSpace;
import static com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType.floatingPointValueTypeFromSizeInBits;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.integerValueTypeFromSizeInBits;

public final class ReferencedClasses implements LocalIdentifierRecorder
{
	@NotNull private final KnownReferenceTypeName thisClass;
	@NotNull private final LocalIdentifier thisIdentifier;
	@NotNull private final Set<LocalIdentifier> referencedClassesOtherThanThis;

	public ReferencedClasses(@NotNull final TypeInformationTriplet typeInformationTriplet)
	{
		thisClass = typeInformationTriplet.thisClassTypeName();
		thisIdentifier = createClassIdentifier(thisClass);
		referencedClassesOtherThanThis = new HashSet<>(16);
	}

	public void referencedClassesOtherThanThis(@NotNull final Consumer<? super LocalIdentifier> action)
	{
		referencedClassesOtherThanThis.forEach(action);
	}

	public int size()
	{
		return referencedClassesOtherThanThis.size();
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
				final LocalIdentifier add = refersTo(internalTypeName);
				final AddressableIdentifierType referencedClass = new AddressableIdentifierType(add);
				return (T) new PointerValueType<>(referencedClass, DefaultAddressSpace);

			default:
				throw new IllegalStateException("Unexpected typeNameCategory");
		}
	}

	@SuppressWarnings("unchecked")
	@NotNull
	public TypeMetadata convertInternalTypeNameToLlvmTypeMetadata(@NotNull final InternalTypeName internalTypeName, @NotNull final DebuggingTypeDefinitions<PackageName> debuggingTypeDefinitions)
	{
		if (internalTypeName.isArray())
		{
			// TODO: Handle arrays
			System.err.println("Arrays are not yet supported (debugging)");
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
				return debuggingTypeDefinitions.compositeType(knownReferenceTypeName.parent(), knownReferenceTypeName.simpleTypeName(), isPacked);

			default:
				throw new IllegalStateException("Unexpected typeNameCategory");
		}
	}

	@NotNull
	@Override
	public LocalIdentifier refersTo(@NotNull final InternalTypeName classInternalTypeName)
	{
		try
		{
			return refersTo(classInternalTypeName.toKnownReferenceTypeName());
		}
		catch (final InvalidInternalTypeNameException e)
		{
			throw new IllegalStateException("What went wrong, when this typeNameCategory is Reference?", e);
		}
	}

	@NotNull
	private LocalIdentifier refersTo(@NotNull final KnownReferenceTypeName referencedTypeName)
	{
		if (thisClass.equals(referencedTypeName))
		{
			return thisIdentifier;
		}

		return add(referencedClassesOtherThanThis, createClassIdentifier(referencedTypeName));
	}

	@NotNull
	public IdentifiedLocallyIdentifiedStructureType newIdentifiedStructureType(final boolean isPacked, @NotNull final Type... fieldTypes)
	{
		return new IdentifiedLocallyIdentifiedStructureType(thisIdentifier, isPacked, fieldTypes);
	}

	@NotNull
	public GlobalIdentifier staticFieldGlobalIdentifier(@NotNull final FieldUniqueness fieldUniqueness)
	{
		return createStaticFieldGlobalIdentifier(thisClass, fieldUniqueness);
	}
}
