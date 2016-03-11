package com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors;

import com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationValueItem;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.stormmq.java.parsing.adaptors.asm.helpers.AsmHelper.knownReferenceTypeName;
import static com.stormmq.java.parsing.adaptors.asm.items.AsmEnumConstantItem.asmEnumConstantItem;

public final class ArrayAnnotationVisitorAdaptor implements AnnotationVisitorAdaptor
{
	@NotNull private final List<Object> values;

	@NotNull private final String key;
	@NotNull private final Map<String, Object> definedValues;

	public ArrayAnnotationVisitorAdaptor(@NotNull final String key, @NotNull final Map<String, Object> definedValues)
	{
		this.key = key;
		this.definedValues = definedValues;
		values = new ArrayList<>(8);
	}

	@Override
	public void keyValuePrimitiveStringOrArrayOfThem(@NotNull final String memberName, @NotNull final Object value)
	{
		throw newIllegalArgumentBecauseArrayMembersShouldNotHaveNames();
	}

	@Override
	public void defaultKeyOrArrayValuePrimitiveStringOrArrayOfThem(@NotNull final Object value)
	{
		if (value.getClass().isArray())
		{
			throw newIllegalArgumentBecauseShouldNotHaveNestedArrays();
		}
		values.add(AnnotationVisitorAdaptor.actualValue(value, false));
	}

	@Override
	public void keyValueEnum(@NotNull final String memberName, @NotNull final String enumTypeDescriptor, @NotNull final String enumConstantName)
	{
		throw newIllegalArgumentBecauseArrayMembersShouldNotHaveNames();
	}

	@Override
	public void defaultKeyOrArrayValueEnum(@NotNull final String enumTypeDescriptor, @NotNull final String enumConstantName)
	{
		values.add(asmEnumConstantItem(enumTypeDescriptor, enumConstantName));
	}

	@Nullable
	@Override
	public AnnotationVisitorAdaptor keyValueArray(@Nullable final String memberName)
	{
		throw newIllegalArgumentBecauseShouldNotHaveNestedArrays();
	}

	@Nullable
	@Override
	public AnnotationVisitorAdaptor keyValueAnnotationInstance(@NotNull final String name, @NotNull final String annotationTypeDescriptor)
	{
		throw newIllegalArgumentBecauseArrayMembersShouldNotHaveNames();
	}

	@Nullable
	@Override
	public AnnotationVisitorAdaptor defaultKeyOrArrayValueAnnotationInstance(@NotNull final String annotationTypeDescriptor)
	{
		final KnownReferenceTypeName annotationKnownReferenceTypeName = knownReferenceTypeName(annotationTypeDescriptor);
		return new AnnotationInstanceGatheringAnnotationVisitorAdaptor(values, annotationKnownReferenceTypeName);
	}

	@Override
	public void end()
	{
		AnnotationVisitorAdaptor.record(definedValues, key, values.toArray());
	}

	@NotNull
	private static IllegalArgumentException newIllegalArgumentBecauseArrayMembersShouldNotHaveNames()
	{
		return new IllegalArgumentException("Array members should not have names");
	}

	@NotNull
	private static IllegalArgumentException newIllegalArgumentBecauseShouldNotHaveNestedArrays()
	{
		return new IllegalArgumentException("Should not have nested arrays");
	}

	private static final class AnnotationInstanceGatheringAnnotationVisitorAdaptor extends AbstractGatheringAnnotationVisitorAdaptor
	{
		private final List<Object> values;
		private final KnownReferenceTypeName annotationKnownReferenceTypeName;

		private AnnotationInstanceGatheringAnnotationVisitorAdaptor(@NotNull final List<Object> values, @NotNull final KnownReferenceTypeName annotationKnownReferenceTypeName)
		{
			this.values = values;
			this.annotationKnownReferenceTypeName = annotationKnownReferenceTypeName;
		}

		@Override
		protected void visitEnd(@NotNull final Map<String, Object> definedValues)
		{
			values.add(new AsmAnnotationValueItem(annotationKnownReferenceTypeName, definedValues));
		}
	}
}
