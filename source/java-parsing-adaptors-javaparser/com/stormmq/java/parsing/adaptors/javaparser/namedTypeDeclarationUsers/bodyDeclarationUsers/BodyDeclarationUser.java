package com.stormmq.java.parsing.adaptors.javaparser.namedTypeDeclarationUsers.bodyDeclarationUsers;

import com.github.javaparser.ast.body.*;
import org.jetbrains.annotations.NotNull;

public interface BodyDeclarationUser
{
	void use(@NotNull final EnumConstantDeclaration enumConstantDeclaration);

	void use(@NotNull final InitializerDeclaration initializerDeclaration);

	void use(@NotNull final FieldDeclaration fieldDeclaration);

	void use(@NotNull final ConstructorDeclaration constructorDeclaration);

	void use(@NotNull final MethodDeclaration methodDeclaration);

	void use(@NotNull final AnnotationMemberDeclaration annotationMemberDeclaration);

	void use(@NotNull final EmptyMemberDeclaration emptyMemberDeclaration);

	void use(@NotNull final AnnotationDeclaration annotationDeclaration);

	void use(@NotNull final ClassOrInterfaceDeclaration classOrInterfaceDeclaration);

	void use(@NotNull final EnumDeclaration enumDeclaration);

	void use(@NotNull final EmptyTypeDeclaration emptyTypeDeclaration);

	void end();
}
