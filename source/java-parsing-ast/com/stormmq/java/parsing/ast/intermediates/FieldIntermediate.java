package com.stormmq.java.parsing.ast.intermediates;

import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.fieldDetails.*;
import com.stormmq.java.parsing.ast.details.fieldValueDetails.FieldValueDetail;
import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.MemberIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.*;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.ast.intermediates.IntermediateCreator.Annotation;
import static com.stormmq.java.parsing.ast.intermediates.IntermediateCreator.Enum;
import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AbstractAnnotationInstanceIntermediate.resolveAnnotationInstanceIntermediates;
import static com.stormmq.java.parsing.utilities.FieldFinality.Final;
import static com.stormmq.java.parsing.utilities.Visibility.Public;

public final class FieldIntermediate extends AbstractCreatableIntermediate<FieldDetail> implements MemberIntermediate<FieldDetail>
{
	@NotNull
	public static FieldIntermediate newAnnotationFieldIntermediate(@NotNull final String fieldName, @NotNull final TypeUsage typeUsage, @NotNull final FieldValueDetail fieldValueDetail, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName)
	{
		return new FieldIntermediate(Annotation, origin, ourKnownReferenceTypeName, Public, annotations, fieldName, typeUsage, isDeprecatedInByteCode, isSynthetic, true, Final, false, fieldValueDetail);
	}

	@NotNull
	public static FieldIntermediate newInterfaceFieldIntermediate(@NotNull final String fieldName, @NotNull final TypeUsage typeUsage, @NotNull final FieldValueDetail fieldValueDetail, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName)
	{
		return new FieldIntermediate(IntermediateCreator.Interface, origin, ourKnownReferenceTypeName, Public, annotations, fieldName, typeUsage, isDeprecatedInByteCode, isSynthetic, true, Final, false, fieldValueDetail);
	}

	@NotNull
	public static FieldIntermediate newClassStaticFieldIntermediate(@NotNull final String fieldName, @NotNull final TypeUsage typeUsage, @NotNull final FieldValueDetail fieldValueDetail, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final Visibility visibility, @NotNull final FieldFinality fieldFinality, final boolean isTransient)
	{
		return new FieldIntermediate(IntermediateCreator.Class, origin, ourKnownReferenceTypeName, visibility, annotations, fieldName, typeUsage, isDeprecatedInByteCode, isSynthetic, true, fieldFinality, isTransient, fieldValueDetail);
	}

	@NotNull
	public static FieldIntermediate newClassInstanceFieldIntermediate(@NotNull final String fieldName, @NotNull final TypeUsage typeUsage, @NotNull final FieldValueDetail fieldValueDetail, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final Visibility visibility, @NotNull final FieldFinality fieldFinality, final boolean isTransient)
	{
		return new FieldIntermediate(IntermediateCreator.Class, origin, ourKnownReferenceTypeName, visibility, annotations, fieldName, typeUsage, isDeprecatedInByteCode, isSynthetic, false, fieldFinality, isTransient, fieldValueDetail);
	}

	@NotNull
	public static FieldIntermediate newEnumStaticFieldIntermediate(@NotNull final String fieldName, @NotNull final TypeUsage typeUsage, @NotNull final FieldValueDetail fieldValueDetail, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final Visibility visibility, @NotNull final FieldFinality fieldFinality, final boolean isTransient)
	{
		return new FieldIntermediate(Enum, origin, ourKnownReferenceTypeName, visibility, annotations, fieldName, typeUsage, isDeprecatedInByteCode, isSynthetic, true, fieldFinality, isTransient, fieldValueDetail);
	}

	@NotNull
	public static FieldIntermediate newEnumInstanceFieldIntermediate(@NotNull final String fieldName, @NotNull final TypeUsage typeUsage, @NotNull final FieldValueDetail fieldValueDetail, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final Visibility visibility, @NotNull final FieldFinality fieldFinality, final boolean isTransient)
	{
		return new FieldIntermediate(Enum, origin, ourKnownReferenceTypeName, visibility, annotations, fieldName, typeUsage, isDeprecatedInByteCode, isSynthetic, false, fieldFinality, isTransient, fieldValueDetail);
	}

	@NotNull private final String fieldName;
	@NotNull private final TypeUsage fieldTypeUsage;
	@NotNull private final FieldFinality fieldFinality;
	private final boolean isTransient;
	@NotNull private final FieldValueDetail fieldValueDetail;

	public FieldIntermediate(@NotNull final IntermediateCreator intermediateCreator, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourTypeReferenceName, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final String fieldName, @NotNull final TypeUsage fieldTypeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, final boolean isStatic, @NotNull final FieldFinality fieldFinality, final boolean isTransient, @NotNull final FieldValueDetail fieldValueDetail)
	{
		super(intermediateCreator, origin, ourTypeReferenceName, visibility, annotations, isDeprecatedInByteCode, isSynthetic, isStatic);
		this.fieldName = fieldName;
		this.fieldTypeUsage = fieldTypeUsage;
		this.fieldFinality = fieldFinality;
		this.isTransient = isTransient;
		this.fieldValueDetail = fieldValueDetail;
	}

	@NotNull
	public String fieldName()
	{
		return fieldName;
	}

	@NotNull
	@Override
	public FieldDetail resolve(@NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		final AnnotationInstanceDetail[] annotationInstanceDetails = resolveAnnotationInstanceIntermediates(annotations, memberIntermediateRecorder, simpleTypeIntermediateRecorder);
		final TypeUsage resolvedFieldTypeUsage = fieldTypeUsage.resolve();

		switch(intermediateCreator)
		{
			case Class:
			case Enum:
				if (isStatic)
				{
					return new StaticClassFieldDetail(fieldName, resolvedFieldTypeUsage, isDeprecatedInByteCode, isSynthetic, visibility, fieldFinality, isTransient, fieldValueDetail, annotationInstanceDetails);
				}
				else
				{
					return new InstanceClassFieldDetail(fieldName, resolvedFieldTypeUsage, isDeprecatedInByteCode, isSynthetic, visibility, fieldFinality, isTransient, annotationInstanceDetails);
				}

			case Interface:
				return new InterfaceFieldDetail(fieldName, resolvedFieldTypeUsage, isDeprecatedInByteCode, isSynthetic, fieldValueDetail, annotationInstanceDetails);

			case Annotation:
				return new AnnotationFieldDetail(fieldName, resolvedFieldTypeUsage, isDeprecatedInByteCode, isSynthetic, fieldValueDetail, annotationInstanceDetails);

			default:
				throw new IllegalStateException(StringConstants.Should_not_be_possible);
		}
	}
}
