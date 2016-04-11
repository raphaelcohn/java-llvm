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
import com.stormmq.java.classfile.domain.uniqueness.FieldUniqueness;
import com.stormmq.java.classfile.processing.typeInformationUsers.TypeInformationTriplet;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.llvm.domain.identifiers.GlobalIdentifier;
import com.stormmq.llvm.domain.identifiers.LocalIdentifier;
import com.stormmq.llvm.domain.types.*;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes.IdentifiedLocallyIdentifiedStructureType;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.function.Consumer;

import static com.stormmq.functions.CollectionHelper.add;
import static com.stormmq.java.llvm.xxx.Identifiers.createClassIdentifier;
import static com.stormmq.java.llvm.xxx.Identifiers.createStaticFieldGlobalIdentifier;

public final class ReferencedClasses
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
	public IdentifiedLocallyIdentifiedStructureType newIdentifiedStructureType(final boolean isPacked, @NotNull final CanBePointedToType... fieldTypes)
	{
		return new IdentifiedLocallyIdentifiedStructureType(thisIdentifier, isPacked, fieldTypes);
	}

	@NotNull
	public GlobalIdentifier staticFieldGlobalIdentifier(@NotNull final FieldUniqueness fieldUniqueness)
	{
		return createStaticFieldGlobalIdentifier(thisClass, fieldUniqueness);
	}
}
