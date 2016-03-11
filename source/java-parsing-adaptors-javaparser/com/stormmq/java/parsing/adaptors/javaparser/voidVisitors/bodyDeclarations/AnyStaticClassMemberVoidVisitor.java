package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.github.javaparser.ast.expr.Expression;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.UnresolvedReferenceTypeName;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulFieldValueDetail;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.ast.details.fieldValueDetails.FieldValueDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.ConstructorIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.FieldIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.MethodIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.ConstructorIntermediate;
import com.stormmq.java.parsing.ast.intermediates.FieldIntermediate;
import com.stormmq.java.parsing.ast.intermediates.ParameterIntermediate;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.BodyIntermediate;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.FieldFinality;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.java.parsing.ast.details.fieldValueDetails.UnassignedFieldValueDetail.Unassigned;
import static com.stormmq.java.parsing.ast.intermediates.FieldIntermediate.newClassStaticFieldIntermediate;
import static com.stormmq.java.parsing.ast.intermediates.Origin.Source;

public final class AnyStaticClassMemberVoidVisitor extends AbstractStaticClassMemberVoidVisitor<BodyDeclarationState>
{
	@NotNull private final TypeDeclarationUser typeDeclarationUser;
	@NotNull private final FieldIntermediateRecorder fieldIntermediateRecorder;
	@NotNull private final ConstructorIntermediateRecorder constructorIntermediateRecorder;
	@NotNull private final MethodIntermediateRecorder methodIntermediateRecorder;

	public AnyStaticClassMemberVoidVisitor(@NotNull final TypeDeclarationUser typeDeclarationUser, @NotNull final FieldIntermediateRecorder fieldIntermediateRecorder, @NotNull final ConstructorIntermediateRecorder constructorIntermediateRecorder, @NotNull final MethodIntermediateRecorder methodIntermediateRecorder)
	{
		this.typeDeclarationUser = typeDeclarationUser;
		this.fieldIntermediateRecorder = fieldIntermediateRecorder;
		this.constructorIntermediateRecorder = constructorIntermediateRecorder;
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
	protected AbstractInnerOrLocalClassMemberVoidVisitor<BodyDeclarationState> useInnerClass(@NotNull final KnownReferenceTypeName className, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isStrictFloatingPoint, @NotNull final Completeness completeness, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage)
	{
		return typeDeclarationUser.useInnerClass(className, visibility, annotations, isStrictFloatingPoint, completeness, typeParameters, implementsTypeUsages, extendsTypeUsage);
	}

	@NotNull
	@Override
	protected AbstractEnumClassMemberVoidVisitor<BodyDeclarationState> useEnum(@NotNull final KnownReferenceTypeName enumName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
	{
		return typeDeclarationUser.useEnum(enumName, annotations, visibility, isStrictFloatingPoint, implementsTypeUsages);
	}

	@Override
	protected void useStaticField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldType, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility)
	{
		final FieldValueDetail fieldValueDetail = init == null ? Unassigned : new UsefulFieldValueDetail(init, false, false);
		final FieldIntermediate fieldIntermediate = newClassStaticFieldIntermediate(fieldName, fieldType, fieldValueDetail, annotations, false, false, Source, typeName, visibility, fieldFinality, isTransient);
		fieldIntermediateRecorder.record(typeName, fieldIntermediate);
	}

	@Override
	protected void useInstanceField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldType, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility)
	{
		final FieldValueDetail fieldValueDetail = init == null ? Unassigned : new UsefulFieldValueDetail(init, false, false);
		final FieldIntermediate fieldIntermediate = newClassStaticFieldIntermediate(fieldName, fieldType, fieldValueDetail, annotations, false, false, Source, typeName, visibility, fieldFinality, isTransient);
		fieldIntermediateRecorder.record(typeName, fieldIntermediate);
	}

	@Override
	protected void useConstructor(@NotNull final KnownReferenceTypeName typeName, @NotNull final Visibility visibility, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final BodyIntermediate bodyIntermediate, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isVarArgs, @NotNull final TypeUsage constructorTypeUsage)
	{
		final ConstructorIntermediate constructorIntermediate = new ConstructorIntermediate(IntermediateCreator.Class, Source, typeName, visibility, annotations, constructorTypeUsage, false, false, parameterIntermediates, throwsArray, isVarArgs, false, bodyIntermediate);
		constructorIntermediateRecorder.record(typeName, constructorIntermediate);
	}
}
