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
import com.stormmq.java.classfile.domain.fieldConstants.FieldConstant;
import com.stormmq.java.classfile.domain.uniqueness.FieldUniqueness;
import com.stormmq.java.classfile.processing.Records;
import com.stormmq.java.classfile.processing.typeInformationUsers.TypeInformationTriplet;
import com.stormmq.java.llvm.api.Packed;
import com.stormmq.java.parsing.utilities.names.typeNames.TypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.TypeNameCategory;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.llvm.domain.typedValues.constantTypedValues.ConstantTypedValue;
import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import com.stormmq.llvm.domain.identifiers.LocalIdentifier;
import com.stormmq.llvm.domain.types.*;
import com.stormmq.llvm.domain.types.firstClassTypes.PointerValueType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.*;
import com.stormmq.llvm.domain.variables.GlobalVariable;
import org.jetbrains.annotations.*;

import java.util.*;

import static com.stormmq.java.llvm.xxx.ToLlvmConstantFieldConstantUser.toLlvmConstant;
import static com.stormmq.llvm.domain.Linkage.external;
import static com.stormmq.llvm.domain.Visibility._default;
import static com.stormmq.llvm.domain.Writable.AutomaticAlignment;
import static com.stormmq.llvm.domain.types.firstClassTypes.FloatingPointValueType.floatingPointValueTypeFromSizeInBits;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.integerValueTypeFromSizeInBits;
import static java.util.Collections.reverse;

public final class ReferencedClasses
{
	@NonNls @NotNull private static final String FieldIdentifierPrefix = "field.";
	@NonNls @NotNull private static final String ClassIdentifierPrefix = "class.";

	private static final int BestGuessOfNumberOfParentFields = 64;
	@NotNull private final KnownReferenceTypeName thisClass;
	@NotNull private final LocalIdentifier thisIdentifier;
	@NotNull private final Set<LocalIdentifier> referencedClassesOtherThanThis;

	public ReferencedClasses(@NotNull final KnownReferenceTypeName thisClass)
	{
		this.thisClass = thisClass;
		thisIdentifier = createLocalIdentifier(thisClass);
		referencedClassesOtherThanThis = new HashSet<>(16);
	}

	@NotNull
	public Set<GlobalVariable<?>> globalVariables(@NotNull final TypeInformationTriplet typeInformationTriplet)
	{
		final Set<GlobalVariable<?>> globals = new HashSet<>(typeInformationTriplet.numberOfStaticAndInstanceFields());
		typeInformationTriplet.forEachInstanceFieldInReverseOrder((fieldUniqueness, fieldInformation) ->
		{
			final GlobalIdentifier globalIdentifier = createFieldIdentifier(fieldUniqueness);

			final Type llvmType = convertFieldToLlvmType(fieldUniqueness);
			@Nullable final FieldConstant constantValue = fieldInformation.constantValue;

			@Nullable final ConstantTypedValue<Type> initializerConstantTypedValue = toLlvmConstant(llvmType, constantValue);
			final boolean isConstant = initializerConstantTypedValue != null;
			globals.add(new GlobalVariable<>(globalIdentifier, external, _default, null, null, false, 0, false, isConstant, llvmType, initializerConstantTypedValue, null, null, AutomaticAlignment));
		});

		return globals;
	}

	@NotNull
	public Set<LocallyIdentifiedStructureType> structureTypes(@NotNull final Records records, @NotNull final TypeInformationTriplet typeInformationTriplet)
	{
		final boolean isPacked = records.hasInheritedAnnotation(typeInformationTriplet, Packed.class);
		final Type[] fieldTypes = fieldTypesIncludingParents(records, typeInformationTriplet);
		final IdentifiedStructureType thisClassIdentifiedStructureType = new IdentifiedStructureType(thisIdentifier, isPacked, fieldTypes);

		final Set<LocallyIdentifiedStructureType> locallyIdentifiedStructureTypes = new HashSet<>(referencedClassesOtherThanThis.size() + 1);
		for (final LocalIdentifier referencedClassOtherThanThis : referencedClassesOtherThanThis)
		{
			locallyIdentifiedStructureTypes.add(new OpaqueStructureType(referencedClassOtherThanThis));
		}
		locallyIdentifiedStructureTypes.add(thisClassIdentifiedStructureType);
		return locallyIdentifiedStructureTypes;
	}

	@NotNull
	private Type[] fieldTypesIncludingParents(@NotNull final Records records, @NotNull final TypeInformationTriplet self)
	{
		final List<Type> fieldTypesInWrongOrder = new ArrayList<>(self.numberOfStaticAndInstanceFields() + BestGuessOfNumberOfParentFields);
		records.loopOverSelfAndParents(self, value ->
		{
			value.forEachStaticFieldInReverseOrder((fieldUniqueness, fieldInformation) ->
			{
				final Type type = convertFieldToLlvmType(fieldUniqueness);
				fieldTypesInWrongOrder.add(type);
			});
			return false;
		});
		reverse(fieldTypesInWrongOrder);
		return fieldTypesInWrongOrder.toArray(new Type[fieldTypesInWrongOrder.size()]);
	}

	@NotNull
	private Type convertFieldToLlvmType(@NotNull final FieldUniqueness fieldUniqueness)
	{
		final FieldDescriptor fieldDescriptor = fieldUniqueness.fieldDescriptor;
		final InternalTypeName internalTypeName = fieldDescriptor.internalTypeName;
		return convertInternalTypeNameToLlvmType(internalTypeName);
	}

	@NotNull
	private LocalIdentifier add(@NotNull final KnownReferenceTypeName referencedTypeName)
	{
		if (thisClass.equals(referencedTypeName))
		{
			return thisIdentifier;
		}

		final LocalIdentifier referencedClass = createLocalIdentifier(referencedTypeName);
		referencedClassesOtherThanThis.add(referencedClass);
		return referencedClass;
	}

	@NotNull
	private Type convertInternalTypeNameToLlvmType(@NotNull final InternalTypeName internalTypeName)
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
				return integerValueTypeFromSizeInBits(sizeInBits);

			case FloatingPoint:
				return floatingPointValueTypeFromSizeInBits(sizeInBits);

			case Void:
				throw new IllegalStateException("Should not be void");

			case Reference:
				final AddressableIdentifierType referencedClass;
				try
				{
					referencedClass = new AddressableIdentifierType(add(internalTypeName.toKnownReferenceTypeName()));
				}
				catch (final InvalidInternalTypeNameException e)
				{
					throw new IllegalStateException("What went wrong, when this typeNameCategory is Reference?", e);
				}
				return new PointerValueType<>(referencedClass, 0);

			default:
				throw new IllegalStateException("Unexpected typeNameCategory");
		}
	}

	@NotNull
	private static LocalIdentifier createLocalIdentifier(@NotNull final KnownReferenceTypeName referencedTypeName)
	{
		return new LocalIdentifier(classIdentifier(referencedTypeName));
	}

	@NotNull
	private GlobalIdentifier createFieldIdentifier(final FieldUniqueness fieldUniqueness)
	{
		return new GlobalIdentifier(fieldIdentifier(thisClass, fieldUniqueness));
	}

	@NotNull
	private static String classIdentifier(@NotNull final KnownReferenceTypeName referencedTypeName)
	{
		return ClassIdentifierPrefix + referencedTypeName.name();
	}

	@NotNull
	private static String fieldIdentifier(@NotNull final KnownReferenceTypeName referencedTypeName, @NotNull final FieldUniqueness fieldUniqueness)
	{
		return FieldIdentifierPrefix + referencedTypeName.name() + '.' + fieldUniqueness.uniqueName();
	}
}
