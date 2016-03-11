package com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors;

import com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationValueItem;
import com.stormmq.java.parsing.adaptors.asm.items.AsmEnumConstantItem;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.stormmq.java.parsing.adaptors.asm.helpers.AsmHelper.knownReferenceTypeName;
import static com.stormmq.java.parsing.adaptors.asm.items.AsmEnumConstantItem.asmEnumConstantItem;
import static com.stormmq.java.parsing.utilities.ReservedIdentifiers.DefaultAnnotationMemberName;

public abstract class AbstractGatheringAnnotationVisitorAdaptor implements AnnotationVisitorAdaptor
{

	@NotNull private final Map<String, Object> definedValues;

	protected AbstractGatheringAnnotationVisitorAdaptor()
	{
		definedValues = new HashMap<>(4);
	}

	protected abstract void visitEnd(@NotNull final Map<String, Object> definedValues);

	@Override
	public void keyValuePrimitiveStringOrArrayOfThem(@NotNull final String memberName, @NotNull final Object value)
	{
		final Object actualValue = actualValue(value, true);
		record(memberName, actualValue);
	}

	@Override
	public void defaultKeyOrArrayValuePrimitiveStringOrArrayOfThem(@NotNull final Object value)
	{
		keyValuePrimitiveStringOrArrayOfThem(DefaultAnnotationMemberName, value);
	}

	@Override
	public void keyValueEnum(@NotNull final String memberName, @NotNull final String enumTypeDescriptor, @NotNull final String enumConstantName)
	{
		final AsmEnumConstantItem asmEnumConstantItem = asmEnumConstantItem(enumTypeDescriptor, com.stormmq.java.parsing.utilities.StringConstants.value);
		record(memberName, asmEnumConstantItem);
	}

	@Override
	public void defaultKeyOrArrayValueEnum(@NotNull final String enumTypeDescriptor, @NotNull final String enumConstantName)
	{
		keyValueEnum(DefaultAnnotationMemberName, enumTypeDescriptor, enumConstantName);
	}

	// https://blogs.oracle.com/toddfast/entry/creating_nested_complex_java_annotations
	@Nullable
	@Override
	public AnnotationVisitorAdaptor keyValueAnnotationInstance(@NotNull final String name, @NotNull final String annotationTypeDescriptor)
	{
		final KnownReferenceTypeName annotationKnownReferenceTypeName = knownReferenceTypeName(annotationTypeDescriptor);
		return new AnnotationInstanceGatheringAnnotationVisitorAdaptor(definedValues, name, annotationKnownReferenceTypeName);
	}

	@Nullable
	@Override
	public AnnotationVisitorAdaptor defaultKeyOrArrayValueAnnotationInstance(@NotNull final String annotationTypeDescriptor)
	{
		return keyValueAnnotationInstance(DefaultAnnotationMemberName, annotationTypeDescriptor);
	}

	// Only called if array is empty or is not a primitive type (ie is a String)???
	@Nullable
	@Override
	public AnnotationVisitorAdaptor keyValueArray(@Nullable final String memberName)
	{
		final String key = (memberName == null) ? DefaultAnnotationMemberName : memberName;
		return new ArrayAnnotationVisitorAdaptor(key, definedValues);
	}

	@Override
	public void end()
	{
		visitEnd(definedValues);
	}

	private void record(@NotNull final String name, @NotNull final Object value)
	{
		record(definedValues, name, value);
	}

	private static final class AnnotationInstanceGatheringAnnotationVisitorAdaptor extends AbstractGatheringAnnotationVisitorAdaptor
	{
		private final Map<String, Object> definedValues;
		private final String key;
		private final KnownReferenceTypeName annotationKnownReferenceTypeName;

		private AnnotationInstanceGatheringAnnotationVisitorAdaptor(@NotNull final Map<String, Object> definedValues, @NotNull final String key, @NotNull final KnownReferenceTypeName annotationKnownReferenceTypeName)
		{
			this.definedValues = definedValues;
			this.key = key;
			this.annotationKnownReferenceTypeName = annotationKnownReferenceTypeName;
		}

		@Override
		protected void visitEnd(@NotNull final Map<String, Object> definedValues)
		{
			record(definedValues, key, new AsmAnnotationValueItem(annotationKnownReferenceTypeName, definedValues));
		}
	}
}
