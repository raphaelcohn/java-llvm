package com.stormmq.java.parsing.adaptors.javaparser.namedTypeDeclarationUsers;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.stormmq.java.parsing.adaptors.javaparser.dead.BodyDeclarationsCreator;
import com.stormmq.java.parsing.adaptors.javaparser.ast.JavaparserModifiers;
import com.stormmq.java.parsing.adaptors.javaparser.dead.TypeParameterWrapper;
import com.stormmq.java.parsing.adaptors.javaparser.referenceTypeNameMakers.ReferenceTypeNameMaker;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.TypeDeclarationWrapper;
import com.stormmq.java.parsing.ast.intermediateRecorders.TypeIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.IntermediateCreator;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.ReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.stormmq.java.parsing.adaptors.javaparser.helpers.keep.ClassOrInterfaceTypeHelper.convertToReferenceTypeName;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.keep.ClassOrInterfaceTypeHelper.convertToReferenceTypeNames;
import static com.stormmq.java.parsing.adaptors.javaparser.dead.TypeParameterWrapper.typeParameterWrappers;
import static com.stormmq.java.parsing.ast.intermediates.IntermediateCreator.Interface;
import static com.stormmq.java.parsing.ast.intermediates.Origin.Source;
import static com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName.JavaLangObject;

public final class ClassOrInterfaceNamedTypeDeclarationUser implements NamedTypeDeclarationUser<ClassOrInterfaceDeclaration>
{
	@NotNull private final TypeIntermediateRecorder typeIntermediateRecorder;
	@NotNull private final BodyDeclarationsCreator bodyDeclarationsCreator;

	public ClassOrInterfaceNamedTypeDeclarationUser(@NotNull final TypeIntermediateRecorder typeIntermediateRecorder, @NotNull final BodyDeclarationsCreator bodyDeclarationsCreator)
	{
		this.typeIntermediateRecorder = typeIntermediateRecorder;
		this.bodyDeclarationsCreator = bodyDeclarationsCreator;
	}

	@Override
	public void use(@NotNull final ReferenceTypeNameMaker referenceTypeNameMaker, @NotNull final KnownReferenceTypeName ourReferenceTypeName, @NotNull final ClassOrInterfaceDeclaration typeDeclaration)
	{
		final TypeParameterWrapper[] typeParameterWrappers = typeParameterWrappers(typeDeclaration.getTypeParameters());

		@NotNull final ReferenceTypeNameMaker referenceTypeNameMakerWithTypeParameters = referenceTypeNameMaker.withTypeParameters(typeParameterWrappers);

		final TypeDeclarationWrapper typeDeclarationWrapper = new TypeDeclarationWrapper(typeDeclaration);
		final IntermediateCreator intermediateCreator = typeDeclarationWrapper.typeDetailsCreator();

		final JavaparserModifiers modifiers = typeDeclarationWrapper.modifiers();

		final AnnotationInstanceIntermediate[] annotationInstanceIntermediates = typeDeclarationWrapper.parseAnnotationInstanceDetails(referenceTypeNameMakerWithTypeParameters);

		// add to type when creating;
		xxx;

		@Nullable final List<ClassOrInterfaceType> extendsList = typeDeclaration.getExtends();
		@Nullable final List<ClassOrInterfaceType> implementsList = typeDeclaration.getImplements();

		final ReferenceTypeName superTypeReferenceName;
		final ReferenceTypeName[] interfaceTypeReferenceNames;
		if (intermediateCreator == Interface)
		{
			superTypeReferenceName = interfaceSuperType(implementsList);
			interfaceTypeReferenceNames = convertToReferenceTypeNames(referenceTypeNameMakerWithTypeParameters, extendsList);
		}
		else
		{
			superTypeReferenceName = classSuperType(referenceTypeNameMakerWithTypeParameters, extendsList);
			interfaceTypeReferenceNames = convertToReferenceTypeNames(referenceTypeNameMakerWithTypeParameters, implementsList);
		}

		intermediateCreator.createTypeIntermediate(Source, modifiers, ourReferenceTypeName, superTypeReferenceName, interfaceTypeReferenceNames, annotationInstanceIntermediates, typeIntermediateRecorder, modifiers.isDeprecatedInByteCode(), modifiers.isSynthetic(), modifiers.visibility(), modifiers.isStrictFloatingPoint());

		bodyDeclarationsCreator.recordClassFieldsAndMethods(referenceTypeNameMakerWithTypeParameters, typeDeclarationWrapper, ourReferenceTypeName, intermediateCreator);
	}

	@NotNull
	private static KnownReferenceTypeName interfaceSuperType(@Nullable final List<ClassOrInterfaceType> implementsList)
	{
		if (implementsList == null || implementsList.isEmpty())
		{
			return JavaLangObject;
		}

		throw new IllegalStateException("An interface can not implement anything");
	}

	@NotNull
	private static ReferenceTypeName classSuperType(@NotNull final ReferenceTypeNameMaker referenceTypeNameMaker, @Nullable final List<ClassOrInterfaceType> extendsList)
	{
		final ReferenceTypeName superTypeReferenceName;
		if (extendsList == null || extendsList.isEmpty())
		{
			superTypeReferenceName = JavaLangObject;
		}
		else if (extendsList.size() == 1)
		{
			superTypeReferenceName = convertToReferenceTypeName(referenceTypeNameMaker, extendsList.get(0));
		}
		else
		{
			throw new IllegalStateException("Extends can either be empty or have only one implement for a class");
		}
		return superTypeReferenceName;
	}
}
