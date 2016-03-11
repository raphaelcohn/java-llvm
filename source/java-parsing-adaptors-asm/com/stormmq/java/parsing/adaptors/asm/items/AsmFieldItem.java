package com.stormmq.java.parsing.adaptors.asm.items;

import com.stormmq.java.parsing.adaptors.asm.ast.AsmModifiers;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.fieldVisitorAdaptors.FieldValueToFieldValueDetails;
import com.stormmq.java.parsing.ast.details.fieldValueDetails.FieldValueDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.FieldIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.FieldIntermediate;
import com.stormmq.java.parsing.ast.intermediates.IntermediateCreator;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.FieldFinality;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.stormmq.java.parsing.adaptors.asm.ast.AsmModifiers.asmModifiersField;
import static com.stormmq.java.parsing.adaptors.asm.helpers.AsmHelper.typeUsageFromAsmType;
import static com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationItem.parseAnnotationInstanceDetails;
import static com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.fieldVisitorAdaptors.FieldValueToFieldValueDetails.Converters;
import static com.stormmq.java.parsing.ast.details.fieldValueDetails.UnassignedFieldValueDetail.Unassigned;
import static com.stormmq.java.parsing.ast.intermediates.FieldIntermediate.*;
import static com.stormmq.java.parsing.ast.intermediates.Origin.Class;
import static com.stormmq.java.parsing.utilities.ReservedIdentifiers.validateIsJavaIdentifier;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Locale.ENGLISH;
import static org.objectweb.asm.Type.getType;

public final class AsmFieldItem
{
	@NotNull public static final List<AsmFieldItem> EmptyAsmFieldItems = emptyList();
	private final int access;
	@NotNull private final String name;
	@NotNull private final String fieldDescriptor;
	@Nullable private final String signature;
	@Nullable private final Object value;
	@NotNull private final List<AsmAnnotationItem> asmAnnotationItems;

	public AsmFieldItem(final int access, @NotNull final String name, @NotNull final String fieldDescriptor, @Nullable final String signature, @Nullable final Object value, @NotNull final List<AsmAnnotationItem> asmAnnotationItems)
	{
		this.asmAnnotationItems = asmAnnotationItems;
		validateIsJavaIdentifier(name, false);

		this.access = access;
		this.name = name;
		this.fieldDescriptor = fieldDescriptor;
		this.signature = signature;
		this.value = value;
	}

	public void toFieldIntermediate(@NotNull final IntermediateCreator intermediateCreator, @NotNull final FieldIntermediateRecorder fieldIntermediateRecorder, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName)
	{
		final AsmModifiers asmModifiers = asmModifiersField(access, intermediateCreator);

		final FieldValueDetail fieldValueDetail = convertFieldValue(fieldDescriptor, value, asmModifiers);

		final TypeUsage typeUsage = typeUsageFromAsmType(getType(fieldDescriptor));

		final AnnotationInstanceIntermediate[] annotations = parseAnnotationInstanceDetails(asmAnnotationItems);

		final boolean isDeprecatedInByteCode = asmModifiers.isDeprecatedInByteCode();
		final boolean isSynthetic = asmModifiers.isSynthetic();
		final boolean isStatic = asmModifiers.isStatic();
		final Visibility visibility = asmModifiers.visibility();
		final FieldFinality fieldFinality = asmModifiers.fieldFinality();
		final boolean isTransient = asmModifiers.isTransient();

		final FieldIntermediate fieldIntermediate;
		switch(intermediateCreator)
		{
			case Annotation:
				asmModifiers.validateAsAnnotationFieldModifiers();
				fieldIntermediate = newAnnotationFieldIntermediate(name, typeUsage, fieldValueDetail, annotations, isDeprecatedInByteCode, isSynthetic, Class, ourKnownReferenceTypeName);
				break;

			case Interface:
				asmModifiers.validateAsInterfaceFieldModifiers();
				fieldIntermediate = newInterfaceFieldIntermediate(name, typeUsage, fieldValueDetail, annotations, isDeprecatedInByteCode, isSynthetic, Class, ourKnownReferenceTypeName);
				break;

			case Class:
				asmModifiers.validateAsClassFieldModifiers();
				if (isStatic)
				{
					fieldIntermediate = newClassStaticFieldIntermediate(name, typeUsage, fieldValueDetail, annotations, isDeprecatedInByteCode, isSynthetic, Class, ourKnownReferenceTypeName, visibility, fieldFinality, isTransient);
				}
				else
				{
					fieldIntermediate = newClassInstanceFieldIntermediate(name, typeUsage, fieldValueDetail, annotations, isDeprecatedInByteCode, isSynthetic, Class, ourKnownReferenceTypeName, visibility, fieldFinality, isTransient);
				}
				break;

			case Enum:
				asmModifiers.validateAsEnumFieldModifiers();
				if (isStatic)
				{
					fieldIntermediate = newEnumStaticFieldIntermediate(name, typeUsage, fieldValueDetail, annotations, isDeprecatedInByteCode, isSynthetic, Class, ourKnownReferenceTypeName, visibility, fieldFinality, isTransient);
				}
				else
				{
					fieldIntermediate = newEnumInstanceFieldIntermediate(name, typeUsage, fieldValueDetail, annotations, isDeprecatedInByteCode, isSynthetic, Class, ourKnownReferenceTypeName, visibility, fieldFinality, isTransient);
				}
				break;

			default:
				throw new IllegalStateException(com.stormmq.java.parsing.utilities.StringConstants.Should_not_be_possible);
		}

		fieldIntermediateRecorder.record(ourKnownReferenceTypeName, fieldIntermediate);
	}

	@NotNull
	private FieldValueDetail convertFieldValue(@NotNull final String fieldDescriptor, @Nullable final Object fieldValue, @NotNull final AsmModifiers asmModifiers)
	{
		if (asmModifiers.isStatic())
		{
			if (fieldValue == null)
			{
				return Unassigned;
			}

			return convertLiteralFieldValue(fieldDescriptor, fieldValue);
		}

		if (fieldValue != null)
		{
			return convertLiteralFieldValue(fieldDescriptor, fieldValue);
		}

		return Unassigned;
	}

	private static FieldValueDetail convertLiteralFieldValue(final String fieldDescriptor, final Object fieldValue)
	{
		final Class<?> aClass = fieldValue.getClass();
		@Nullable final FieldValueToFieldValueDetails fieldValueToFieldValueDetails = Converters.get(aClass);
		if (fieldValueToFieldValueDetails == null)
		{
			throw new IllegalArgumentException("Unexpected field value");
		}
		return fieldValueToFieldValueDetails.convert(fieldValue, fieldDescriptor);
	}

	@Override
	@NotNull
	public String toString()
	{
		return format(ENGLISH, "%1$s(%2$s, %3$s, %4$s)", getClass().getSimpleName(), name, access, fieldDescriptor);
	}

	@Override
	public boolean equals(@Nullable final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		final AsmFieldItem that = (AsmFieldItem) o;

		if (access != that.access)
		{
			return false;
		}
		if (!asmAnnotationItems.equals(that.asmAnnotationItems))
		{
			return false;
		}
		if (!fieldDescriptor.equals(that.fieldDescriptor))
		{
			return false;
		}
		if (!name.equals(that.name))
		{
			return false;
		}
		if (signature != null ? !signature.equals(that.signature) : that.signature != null)
		{
			return false;
		}
		if (value != null ? !value.equals(that.value) : that.value != null)
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = access;
		result = 31 * result + name.hashCode();
		result = 31 * result + fieldDescriptor.hashCode();
		result = 31 * result + (signature != null ? signature.hashCode() : 0);
		result = 31 * result + (value != null ? value.hashCode() : 0);
		result = 31 * result + asmAnnotationItems.hashCode();
		return result;
	}
}
