package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.PackageState;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

public final class TopLevelTypeDeclarationVoidVisitor<S extends PackageState> extends AbstractTopLevelTypeDeclarationVoidVisitor<S>
{
	@NotNull private final TypeDeclarationUser typeDeclarationUser;

	public TopLevelTypeDeclarationVoidVisitor(@NotNull final TypeDeclarationUser typeDeclarationUser)
	{
		this.typeDeclarationUser = typeDeclarationUser;
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

}
