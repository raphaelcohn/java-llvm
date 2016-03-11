package com.stormmq.java.parsing.ast.intermediates;

import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.fieldDetails.FieldDetail;
import com.stormmq.java.parsing.ast.details.initializersDetails.InitializersDetail;
import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.details.typeDetails.*;
import com.stormmq.java.parsing.ast.intermediateRecorders.MemberIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.IntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument.*;
import static com.stormmq.java.parsing.ast.intermediates.IntermediateCreator.Annotation;
import static com.stormmq.java.parsing.ast.intermediates.IntermediateCreator.Enum;
import static com.stormmq.java.parsing.ast.intermediates.IntermediateCreator.Interface;
import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AbstractAnnotationInstanceIntermediate.resolveAnnotationInstanceIntermediates;
import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate.EmptyAnnotations;
import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate.EmptyEmptyAnnotations;
import static com.stormmq.java.parsing.utilities.Completeness.Abstract;
import static com.stormmq.java.parsing.utilities.Completeness.Normal;

public final class TypeIntermediate extends AbstractCreatableIntermediate<TypeDetail>
{
	@NotNull
	public static TypeIntermediate newAnnotationTypeIntermediate(@NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownTypeReferenceName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isDeprecatedInByteCode, final boolean isStrictFloatingPoint, final boolean isSynthetic)
	{
		return new TypeIntermediate(Annotation, origin, ourKnownTypeReferenceName, visibility, annotations, isDeprecatedInByteCode, isSynthetic, true, isStrictFloatingPoint, JavaLangAnnotationAnnotationTypeUsages, JavaLangAnnotationAnnotationTypeArgument, Abstract);
	}

	@NotNull
	public static TypeIntermediate newInterfaceTypeIntermediate(@NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownTypeReferenceName, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] interfacesExtended, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isDeprecatedInByteCode, final boolean isStrictFloatingPoint, final boolean isSynthetic)
	{
		return new TypeIntermediate(Interface, origin, ourKnownTypeReferenceName, visibility, annotations, isDeprecatedInByteCode, isSynthetic, true, isStrictFloatingPoint, interfacesExtended, JavaLangObjectTypeArgument, Abstract);
	}

	@NotNull
	public static TypeIntermediate newStaticClassTypeIntermediate(@NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownTypeReferenceName, @NotNull final ClassOrInterfaceIntermediateTypeArgument classExtended, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] interfacesExtended, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isDeprecatedInByteCode, final boolean isStrictFloatingPoint, final boolean isSynthetic, @NotNull final Completeness completeness)
	{
		return new TypeIntermediate(IntermediateCreator.Class, origin, ourKnownTypeReferenceName, visibility, annotations, isDeprecatedInByteCode, isSynthetic, true, isStrictFloatingPoint, interfacesExtended, classExtended, completeness);
	}

	@NotNull
	public static TypeIntermediate newInnerClassTypeIntermediate(@NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownTypeReferenceName, @NotNull final ClassOrInterfaceIntermediateTypeArgument classExtended, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] interfacesExtended, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isDeprecatedInByteCode, final boolean isStrictFloatingPoint, final boolean isSynthetic, @NotNull final Completeness completeness)
	{
		return new TypeIntermediate(IntermediateCreator.Class, origin, ourKnownTypeReferenceName, visibility, annotations, isDeprecatedInByteCode, isSynthetic, false, isStrictFloatingPoint, interfacesExtended, classExtended, completeness);
	}

	@NotNull
	public static TypeIntermediate newEnumTypeIntermediate(@NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownTypeReferenceName, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] interfacesExtended, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isDeprecatedInByteCode, final boolean isStrictFloatingPoint, final boolean isSynthetic)
	{
		// eg public final class com.stormmq.java.parsing.adaptors.javac.TEST2 extends java.lang.Enum<com.stormmq.java.parsing.adaptors.javac.TEST2>
		final ClassOrInterfaceIntermediateTypeArgument us = new ClassOrInterfaceIntermediateTypeArgument(EmptyAnnotations, EmptyEmptyAnnotations, null, ourKnownTypeReferenceName.fullyQualifiedNameUsingDotsAndDollarSigns(), EmptyIntermediateTypeArguments);

		final IntermediateTypeArgument[] upperBounds =
		{
			us
		};
		final ClassOrInterfaceIntermediateTypeArgument javaLangEnumTypeArgument = new ClassOrInterfaceIntermediateTypeArgument(EmptyAnnotations, EmptyEmptyAnnotations, null, "java.lang.Enum", upperBounds);

		return new TypeIntermediate(Enum, origin, ourKnownTypeReferenceName, visibility, annotations, isDeprecatedInByteCode, isSynthetic, true, isStrictFloatingPoint, interfacesExtended, javaLangEnumTypeArgument, Normal);
	}

	private final boolean isStrictFloatingPoint;
	@NotNull private final ClassOrInterfaceIntermediateTypeArgument[] interfacesExtended;
	@NotNull private final ClassOrInterfaceIntermediateTypeArgument classExtended;
	@NotNull private final Completeness completeness;

	public TypeIntermediate(@NotNull final IntermediateCreator intermediateCreator, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourTypeReferenceName, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isDeprecatedInByteCode, final boolean isSynthetic, final boolean isStatic, final boolean isStrictFloatingPoint, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] interfacesExtended, @NotNull final ClassOrInterfaceIntermediateTypeArgument classExtended, @NotNull final Completeness completeness)
	{
		super(intermediateCreator, origin, ourTypeReferenceName, visibility, annotations, isDeprecatedInByteCode, isSynthetic, isStatic);
		this.isStrictFloatingPoint = isStrictFloatingPoint;
		this.interfacesExtended = interfacesExtended;
		this.classExtended = classExtended;
		this.completeness = completeness;
	}

	@NotNull
	public TypeDetail resolve(@NotNull final InitializersDetail[] initializersDetails, @NotNull final FieldDetail[] fieldDetails, @NotNull final MethodDetail[] methodDetails, @NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		final AnnotationInstanceDetail[] annotationInstanceDetails = resolveAnnotationInstanceIntermediates(annotations, memberIntermediateRecorder, simpleTypeIntermediateRecorder);

		switch(intermediateCreator)
		{
			case Class:
				final TypeUsage resolve = classExtended.resolve();
				throw new UnsupportedOperationException("Implement me correctly, using type usage and not reference type name");
				//return new ClassTypeDetail(origin, ourKnownTypeReferenceName, visibility, isDeprecatedInByteCode, isStrictFloatingPoint, isSynthetic, annotationInstanceDetails, resolveInterfacesExtended(), null, completeness);

			case Interface:
				return new InterfaceTypeDetail(origin, ourKnownTypeReferenceName, visibility, isDeprecatedInByteCode, isStrictFloatingPoint, isSynthetic, annotationInstanceDetails, resolveInterfacesExtended());

			case Annotation:
				return new AnnotationTypeDetail(origin, ourKnownTypeReferenceName, visibility, isDeprecatedInByteCode, isStrictFloatingPoint, isSynthetic, annotationInstanceDetails);

			case Enum:
				return new EnumTypeDetail(origin, ourKnownTypeReferenceName, visibility, isDeprecatedInByteCode, isStrictFloatingPoint, isSynthetic, annotationInstanceDetails, resolveInterfacesExtended());

			default:
				throw new IllegalStateException(com.stormmq.java.parsing.utilities.StringConstants.Should_not_be_possible);
		}
	}

	@NotNull
	private KnownReferenceTypeName[] resolveInterfacesExtended()
	{
		throw new UnsupportedOperationException("Implement me correctly, using type usage and not reference type name");
		//return resolveReferenceTypeNames(interfacesExtended);
	}

}
