package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.UnresolvedReferenceTypeName;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.ast.intermediateRecorders.ConstructorIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.FieldIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.MethodIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.*;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.BodyIntermediate;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.ast.intermediates.Origin.Source;

public final class AnyInnerOrLocalClassMemberVoidVisitor extends AbstractInnerOrLocalClassMemberVoidVisitor<BodyDeclarationState>
{
	@NotNull private final TypeDeclarationUser typeDeclarationUser;
	@NotNull private final FieldIntermediateRecorder fieldIntermediateRecorder;
	@NotNull private final ConstructorIntermediateRecorder constructorIntermediateRecorder;
	@NotNull private final MethodIntermediateRecorder methodIntermediateRecorder;

	public AnyInnerOrLocalClassMemberVoidVisitor(@NotNull final TypeDeclarationUser typeDeclarationUser, @NotNull final FieldIntermediateRecorder fieldIntermediateRecorder, @NotNull final ConstructorIntermediateRecorder constructorIntermediateRecorder, @NotNull final MethodIntermediateRecorder methodIntermediateRecorder)
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
	protected AbstractEnumClassMemberVoidVisitor<BodyDeclarationState> useEnum(@NotNull final KnownReferenceTypeName enumName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
	{
		return typeDeclarationUser.useEnum(enumName, annotations, visibility, isStrictFloatingPoint, implementsTypeUsages);
	}

	@Override
	protected void useConstructor(@NotNull final KnownReferenceTypeName typeName, @NotNull final Visibility visibility, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final BodyIntermediate bodyIntermediate, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isVarArgs, @NotNull final TypeUsage constructorTypeUsage)
	{
		final ConstructorIntermediate constructorIntermediate = new ConstructorIntermediate(IntermediateCreator.Class, Source, typeName, visibility, annotations, constructorTypeUsage, false, false, parameterIntermediates, throwsArray, isVarArgs, false, bodyIntermediate);
		constructorIntermediateRecorder.record(typeName, constructorIntermediate);
		xxx; // needs transformation to take a first argument of the parent
	}
}
