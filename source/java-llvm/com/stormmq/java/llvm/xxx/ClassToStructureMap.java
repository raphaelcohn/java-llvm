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
import com.stormmq.java.classfile.processing.typeInformationUsers.TypeInformationTriplet;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.llvm.domain.identifiers.LocalIdentifier;
import com.stormmq.llvm.domain.metadata.creation.*;
import com.stormmq.llvm.domain.metadata.debugging.DICompositeTypeKeyedMetadataTuple;
import com.stormmq.llvm.domain.types.*;
import com.stormmq.llvm.domain.types.firstClassTypes.PointerValueType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.*;
import com.stormmq.tuples.Triplet;
import org.jetbrains.annotations.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.IntFunction;

import static com.stormmq.functions.ListHelper.listToArray;
import static com.stormmq.functions.ListHelper.newArrayList;
import static com.stormmq.llvm.domain.AddressSpace.GlobalAddressSpace;
import static com.stormmq.llvm.domain.metadata.creation.DebuggingFieldDetail.toFieldTypes;
import static java.util.Collections.reverse;

public final class ClassToStructureMap
{
	private static final int BestGuessOfNumberOfParentFields = 64;
	@SuppressWarnings("unchecked") @NotNull private static final DebuggingFieldDetail<PackageName>[] EmptyDebuggingFieldDetails = new DebuggingFieldDetail[0];
	@SuppressWarnings("rawtypes") @NotNull private static final IntFunction<DebuggingFieldDetail<PackageName>[]> DebuggingFieldDetailsArrayConstructor = DebuggingFieldDetail[]::new;

	@NotNull private final UsefulRecords usefulRecords;
	// We can not have just one map as some classes may have circular references in their fields to themselves
	// Circular references (indeed, all objects) resolve to pointers
	@NotNull private final ConcurrentMap<KnownReferenceTypeName, Triplet<TypeInformationTriplet, LocalIdentifier, PointerValueType<AddressableIdentifiedMixin<LocalIdentifier>>>> classNamesToLocalIdentifiersAndPointers;
	@NotNull private final ConcurrentMap<LocalIdentifier, Triplet<SizedLocallyIdentifiedStructureType, NamespacedTypeName<PackageName>, DebuggingFieldDetail<PackageName>[]>> localIdentifiersToStructureDetails;
	@NotNull private final TypeConverter<SizedType> sizedTypeTypeConverter;

	public ClassToStructureMap(@NotNull final UsefulRecords usefulRecords)
	{
		this.usefulRecords = usefulRecords;

		classNamesToLocalIdentifiersAndPointers = new ConcurrentHashMap<>(100_000);
		localIdentifiersToStructureDetails = new ConcurrentHashMap<>(100_000);
		sizedTypeTypeConverter = new TypeConverter<>(new ToSizedTypeTypeNameVisitor(this));
	}

	@NotNull
	public TypeInformationTriplet typeInformationTriplet(@NotNull final KnownReferenceTypeName className)
	{
		return lookUpClassName(className).a;
	}

	@NotNull
	public LocalIdentifier classIdentifier(@NotNull final KnownReferenceTypeName className)
	{
		return lookUpClassName(className).b;
	}

	@NotNull
	public PointerValueType<AddressableIdentifiedMixin<LocalIdentifier>> pointerType(@NotNull final KnownReferenceTypeName className)
	{
		return lookUpClassName(className).c;
	}

	@NotNull
	private Triplet<TypeInformationTriplet, LocalIdentifier, PointerValueType<AddressableIdentifiedMixin<LocalIdentifier>>> lookUpClassName(@NotNull final KnownReferenceTypeName className)
	{
		return classNamesToLocalIdentifiersAndPointers.computeIfAbsent(className, knownReferenceTypeName ->
		{
			// Done at this point, because it will fail if a className is missing
			final TypeInformationTriplet typeInformationTriplet = usefulRecords.retrieve(className);
			final LocalIdentifier localIdentifier = classIdentifier(knownReferenceTypeName);
			final PointerValueType<AddressableIdentifiedMixin<LocalIdentifier>> addressableIdentifierTypePointerValueType = new PointerValueType<>(new AddressableIdentifierType<>(localIdentifier), GlobalAddressSpace);
			return new Triplet<>(typeInformationTriplet, localIdentifier, addressableIdentifierTypePointerValueType);
		});
	}

	@NotNull
	public Triplet<SizedLocallyIdentifiedStructureType, NamespacedTypeName<PackageName>, DebuggingFieldDetail<PackageName>[]> lookUpClassDetails(@NotNull final KnownReferenceTypeName className)
	{
		final Triplet<TypeInformationTriplet, LocalIdentifier, PointerValueType<AddressableIdentifiedMixin<LocalIdentifier>>> triplet = lookUpClassName(className);

		final LocalIdentifier localIdentifier1 = triplet.b;
		return localIdentifiersToStructureDetails.computeIfAbsent(localIdentifier1, localIdentifier ->
		{
			final TypeInformationTriplet self = triplet.a;
			final boolean isPacked = usefulRecords.isPacked(self);
			final DebuggingFieldDetail<PackageName>[] debuggingFieldDetails = fieldTypesIncludingSuperclasses(self, fieldInformation -> new PackageNameDebuggingFieldDetail(this, sizedTypeTypeConverter, fieldInformation), DebuggingFieldDetailsArrayConstructor, EmptyDebuggingFieldDetails);
			final SizedType[] fieldTypes = toFieldTypes(debuggingFieldDetails);

			final SizedLocallyIdentifiedStructureType sizedLocallyIdentifiedStructureType = new SizedLocallyIdentifiedStructureType(localIdentifier, isPacked, fieldTypes);

			final NamespacedTypeName<PackageName> namespacedTypeName = new NamespacedTypeName<>(className.parent(), className.simpleTypeName());

			return new Triplet<>(sizedLocallyIdentifiedStructureType, namespacedTypeName, debuggingFieldDetails);
		});
	}

	@NotNull
	public DICompositeTypeKeyedMetadataTuple computeClassDebuggingType(@NotNull final KnownReferenceTypeName className, @NotNull final DebuggingTypeDefinitions<PackageName> debuggingTypeDefinitions)
	{
		return (DICompositeTypeKeyedMetadataTuple) new ToTypeMetadataTypeNameVisitor(this, debuggingTypeDefinitions).useReference(className);
	}

	@NotNull
	private <T> T[] fieldTypesIncludingSuperclasses(@NotNull final TypeInformationTriplet self, @NotNull final Function<FieldInformation, T> fieldConverter, @NotNull final IntFunction<T[]> arrayConstructor, @NotNull final T[] emptyArray)
	{
		final List<T> fieldTypesInWrongOrderX = newArrayList(self.numberOfInstanceFields() + BestGuessOfNumberOfParentFields, fieldTypesInWrongOrder ->
		{
			usefulRecords.loopOverSelfAndSuperclasses(self, value ->
			{
				value.forEachInstanceField(fieldInformation ->
				{
					fieldTypesInWrongOrder.add(fieldConverter.apply(fieldInformation));
				});
				return false;
			});
			reverse(fieldTypesInWrongOrder);
		});
		return listToArray(fieldTypesInWrongOrderX, arrayConstructor, emptyArray);
	}

}
