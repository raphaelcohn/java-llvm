package com.stormmq.java.parsing.adaptors.javaparser.namedTypeDeclarationUsers;

import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.stormmq.java.parsing.adaptors.javaparser.dead.BodyDeclarationsCreator;
import com.stormmq.java.parsing.adaptors.javaparser.ast.JavaparserModifiers;
import com.stormmq.java.parsing.adaptors.javaparser.referenceTypeNameMakers.ReferenceTypeNameMaker;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.TypeDeclarationWrapper;
import com.stormmq.java.parsing.ast.intermediateRecorders.IntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.ast.intermediates.IntermediateCreator.Annotation;
import static com.stormmq.java.parsing.ast.intermediates.Origin.Source;
import static com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName.AnnotationKnownReferenceTypeNames;
import static com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName.JavaLangObject;

public final class AnnotationNamedTypeDeclarationUser implements NamedTypeDeclarationUser<AnnotationDeclaration>
{
	@NotNull private final IntermediateRecorder intermediateRecorder;
	@NotNull private final BodyDeclarationsCreator bodyDeclarationsCreator;

	public AnnotationNamedTypeDeclarationUser(@NotNull final IntermediateRecorder intermediateRecorder, @NotNull final BodyDeclarationsCreator bodyDeclarationsCreator)
	{
		this.intermediateRecorder = intermediateRecorder;
		this.bodyDeclarationsCreator = bodyDeclarationsCreator;
	}

	@Override
	public void use(@NotNull final ReferenceTypeNameMaker referenceTypeNameMaker, @NotNull final KnownReferenceTypeName ourReferenceTypeName, @NotNull final AnnotationDeclaration typeDeclaration)
	{
		final TypeDeclarationWrapper typeDeclarationWrapper = new TypeDeclarationWrapper(typeDeclaration);

		final JavaparserModifiers modifiers = typeDeclarationWrapper.modifiers();
		final AnnotationInstanceIntermediate[] annotationInstanceIntermediates = typeDeclarationWrapper.parseAnnotationInstanceDetails(referenceTypeNameMaker);
		Annotation.createTypeIntermediate(Source, modifiers, ourReferenceTypeName, JavaLangObject, AnnotationKnownReferenceTypeNames, annotationInstanceIntermediates, intermediateRecorder, modifiers.isDeprecatedInByteCode(), modifiers.isSynthetic(), modifiers.visibility(), modifiers.isStrictFloatingPoint());

		bodyDeclarationsCreator.recordClassFieldsAndMethodsAnnotation(referenceTypeNameMaker, typeDeclarationWrapper, ourReferenceTypeName);
	}

}
