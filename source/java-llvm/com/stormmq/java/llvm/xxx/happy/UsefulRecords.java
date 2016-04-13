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

import com.stormmq.functions.ToBooleanFunction;
import com.stormmq.java.classfile.processing.Records;
import com.stormmq.java.classfile.processing.TypeInformationTripletUser;
import com.stormmq.java.classfile.processing.typeInformationUsers.TypeInformationTriplet;
import com.stormmq.java.llvm.api.Packed;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.*;

import java.lang.annotation.Annotation;
import java.util.function.Function;

public final class UsefulRecords implements Records
{
	@NotNull private final Records records;

	public UsefulRecords(@NotNull final Records records)
	{
		this.records = records;
	}

	public boolean isPacked(@NotNull final TypeInformationTriplet typeInformationTriplet)
	{
		return records.hasAnnotation(typeInformationTriplet, Packed.class);
	}

	@Override
	public <R> void iterate(@NotNull final TypeInformationTripletUser<R> typeInformationTripletUser, @NotNull final Function<Records, R> usefulRecordsCreator)
	{
		records.iterate(typeInformationTripletUser, usefulRecordsCreator);
	}

	@NotNull
	@Override
	public TypeInformationTriplet retrieve(@NotNull final KnownReferenceTypeName knownReferenceTypeName)
	{
		return records.retrieve(knownReferenceTypeName);
	}

	@Override
	public boolean loopOverSelfAndSuperclasses(@NotNull final TypeInformationTriplet self, @NotNull final ToBooleanFunction<TypeInformationTriplet> user)
	{
		return records.loopOverSelfAndSuperclasses(self, user);
	}

	@Override
	public boolean hasAnnotation(@NotNull final TypeInformationTriplet self, @NotNull final Class<? extends Annotation> annotationClass)
	{
		return records.hasAnnotation(self, annotationClass);
	}
}
