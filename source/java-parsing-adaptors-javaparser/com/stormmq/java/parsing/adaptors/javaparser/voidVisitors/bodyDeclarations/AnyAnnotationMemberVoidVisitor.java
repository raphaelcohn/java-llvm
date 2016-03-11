package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.github.javaparser.ast.expr.Expression;
import com.stormmq.java.parsing.adaptors.javaparser.ast.JavaparserDefaultValueIntermediate;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulFieldValueDetail;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.ast.details.fieldValueDetails.FieldValueDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.FieldIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.MethodIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.DefaultValueIntermediate;
import com.stormmq.java.parsing.ast.intermediates.FieldIntermediate;
import com.stormmq.java.parsing.ast.intermediates.MethodIntermediate;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.FieldFinality;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.java.parsing.ast.details.fieldValueDetails.UnassignedFieldValueDetail.Unassigned;
import static com.stormmq.java.parsing.ast.intermediates.DefaultValueIntermediate.NoDefaultValueIntermediate;
import static com.stormmq.java.parsing.ast.intermediates.FieldIntermediate.newAnnotationFieldIntermediate;
import static com.stormmq.java.parsing.ast.intermediates.MethodIntermediate.newAnnotationMethodIntermediate;
import static com.stormmq.java.parsing.ast.intermediates.Origin.Source;
import static com.stormmq.java.parsing.utilities.FieldFinality.Final;

public final class AnyAnnotationMemberVoidVisitor extends AbstractAnnotationMemberVoidVisitor<BodyDeclarationState>
{
	@NotNull private final TypeDeclarationUser typeDeclarationUser;
	@NotNull private final FieldIntermediateRecorder fieldIntermediateRecorder;
	@NotNull private final MethodIntermediateRecorder methodIntermediateRecorder;

	public AnyAnnotationMemberVoidVisitor(@NotNull final TypeDeclarationUser typeDeclarationUser, @NotNull final FieldIntermediateRecorder fieldIntermediateRecorder, @NotNull final MethodIntermediateRecorder methodIntermediateRecorder)
	{
		this.typeDeclarationUser = typeDeclarationUser;
		this.fieldIntermediateRecorder = fieldIntermediateRecorder;
		this.methodIntermediateRecorder = methodIntermediateRecorder;
	}

	@NotNull
	@Override
	protected AbstractAnnotationMemberVoidVisitor<BodyDeclarationState> useAnnotation(@NotNull final KnownReferenceTypeName annotationName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint)
	{
		return typeDeclarationUser.useAnnotation(annotationName, annotations, visibility, isStrictFloatingPoint);
	}

	@NotNull
	@Override
	protected AbstractInterfaceMemberVoidVisitor<BodyDeclarationState> useInterface(@NotNull final KnownReferenceTypeName interfaceName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] extendsTypeUsages)
	{
		return typeDeclarationUser.useInterface(interfaceName, annotations, visibility, isStrictFloatingPoint, extendsTypeUsages, typeParameters);
	}

	@NotNull
	@Override
	protected AbstractStaticClassMemberVoidVisitor<BodyDeclarationState> useStaticClass(@NotNull final KnownReferenceTypeName className, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isStrictFloatingPoint, @NotNull final Completeness completeness, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage)
	{
		return typeDeclarationUser.useStaticClass(className, visibility, annotations, isStrictFloatingPoint, completeness, implementsTypeUsages, extendsTypeUsage, typeParameters);
	}

	@NotNull
	@Override
	protected AbstractEnumClassMemberVoidVisitor<BodyDeclarationState> useEnum(@NotNull final KnownReferenceTypeName enumName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
	{
		return typeDeclarationUser.useEnum(enumName, annotations, visibility, isStrictFloatingPoint, implementsTypeUsages);
	}

	@Override
	protected void useAnnotationMember(@NotNull final KnownReferenceTypeName annotationTypeName, @NotNull final String annotationMemberName, @NotNull final TypeUsage typeUsage, @Nullable final Expression defaultValue, @NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		final DefaultValueIntermediate annotationDefaultValueIntermediate = defaultValue == null ? NoDefaultValueIntermediate : new JavaparserDefaultValueIntermediate(new UsefulFieldValueDetail(defaultValue, true, true));

		final MethodIntermediate methodIntermediate = newAnnotationMethodIntermediate(annotationMemberName, typeUsage, false, false, KnownReferenceTypeName.EmptyKnownReferenceTypeNames, annotations, annotationDefaultValueIntermediate, Source, annotationTypeName);
		methodIntermediateRecorder.record(annotationTypeName, methodIntermediate);
	}

	@Override
	protected void useStaticField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldType, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility)
	{
		if (fieldFinality != Final)
		{
			throw new IllegalArgumentException("Annotation fields must be final");
		}
		final FieldValueDetail fieldValueDetail = init == null ? Unassigned : new UsefulFieldValueDetail(init, true, false);
		final FieldIntermediate fieldIntermediate = newAnnotationFieldIntermediate(fieldName, fieldType, fieldValueDetail, annotations, false, false, Source, typeName);
		fieldIntermediateRecorder.record(typeName, fieldIntermediate);
	}
}
