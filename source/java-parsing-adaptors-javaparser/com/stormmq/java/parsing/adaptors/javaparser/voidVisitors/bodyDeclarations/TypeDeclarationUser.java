package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.ast.intermediateRecorders.IntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.TypeIntermediate;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.ast.intermediates.Origin.Source;
import static com.stormmq.java.parsing.ast.intermediates.TypeIntermediate.*;

public final class TypeDeclarationUser
{
	@NotNull private final IntermediateRecorder intermediateRecorder;
	@NotNull private final AnyAnnotationMemberVoidVisitor anyAnnotationMemberVoidVisitor;
	@NotNull private final AnyInterfaceMemberVoidVisitor anyInterfaceMemberVoidVisitor;
	@NotNull private final AnyStaticClassMemberVoidVisitor anyStaticClassMemberVoidVisitor;
	@NotNull private final AnyInnerOrLocalClassMemberVoidVisitor anyInnerOrLocalClassMemberVoidVisitor;
	@NotNull private final AnyEnumClassMemberVoidVisitor anyEnumClassMemberVoidVisitor;

	public TypeDeclarationUser(@NotNull final IntermediateRecorder intermediateRecorder)
	{
		this.intermediateRecorder = intermediateRecorder;
		anyAnnotationMemberVoidVisitor = new AnyAnnotationMemberVoidVisitor(this, intermediateRecorder, intermediateRecorder);
		anyInterfaceMemberVoidVisitor = new AnyInterfaceMemberVoidVisitor(this, intermediateRecorder, intermediateRecorder);
		anyStaticClassMemberVoidVisitor = new AnyStaticClassMemberVoidVisitor(this, intermediateRecorder, intermediateRecorder, intermediateRecorder);
		anyInnerOrLocalClassMemberVoidVisitor = new AnyInnerOrLocalClassMemberVoidVisitor(this, intermediateRecorder, intermediateRecorder, intermediateRecorder);
		anyEnumClassMemberVoidVisitor = new AnyEnumClassMemberVoidVisitor(this, intermediateRecorder, intermediateRecorder, intermediateRecorder);
	}

	@NotNull
	public AbstractAnnotationMemberVoidVisitor<BodyDeclarationState> useAnnotation(@NotNull final KnownReferenceTypeName annotationName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint)
	{
		final TypeIntermediate typeIntermediate = newAnnotationTypeIntermediate(Source, annotationName, annotations, visibility, false, isStrictFloatingPoint, false);
		intermediateRecorder.record(annotationName, typeIntermediate);
		return anyAnnotationMemberVoidVisitor;
	}

	@NotNull
	public AbstractInterfaceMemberVoidVisitor<BodyDeclarationState> useInterface(@NotNull final KnownReferenceTypeName interfaceName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] extendsTypeUsages, @NotNull final UsefulTypeParameters typeParameters)
	{
		final TypeIntermediate typeIntermediate = newInterfaceTypeIntermediate(Source, interfaceName, extendsTypeUsages, annotations, visibility, false, isStrictFloatingPoint, false, typeParameters);
		intermediateRecorder.record(interfaceName, typeIntermediate);
		return anyInterfaceMemberVoidVisitor;
	}

	@NotNull
	public AbstractStaticClassMemberVoidVisitor<BodyDeclarationState> useStaticClass(@NotNull final KnownReferenceTypeName className, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isStrictFloatingPoint, @NotNull final Completeness completeness, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage, @NotNull final UsefulTypeParameters typeParameters)
	{
		final TypeIntermediate typeIntermediate = newStaticClassTypeIntermediate(Source, className, extendsTypeUsage, implementsTypeUsages, annotations, visibility, false, isStrictFloatingPoint, false, completeness, typeParameters);
		intermediateRecorder.record(className, typeIntermediate);
		return anyStaticClassMemberVoidVisitor;
	}

	@NotNull
	public AbstractInnerOrLocalClassMemberVoidVisitor<BodyDeclarationState> useInnerClass(@NotNull final KnownReferenceTypeName className, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isStrictFloatingPoint, @NotNull final Completeness completeness, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage)
	{
		final TypeIntermediate typeIntermediate = newInnerClassTypeIntermediate(Source, className, extendsTypeUsage, implementsTypeUsages, annotations, visibility, false, isStrictFloatingPoint, false, completeness, typeParameters);
		intermediateRecorder.record(className, typeIntermediate);
		return anyInnerOrLocalClassMemberVoidVisitor;
	}

	@NotNull
	public AbstractEnumClassMemberVoidVisitor<BodyDeclarationState> useEnum(@NotNull final KnownReferenceTypeName enumName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
	{
		final TypeIntermediate typeIntermediate = newEnumTypeIntermediate(Source, enumName, implementsTypeUsages, annotations, visibility, false, isStrictFloatingPoint, false);
		intermediateRecorder.record(enumName, typeIntermediate);
		return anyEnumClassMemberVoidVisitor;
	}
}
