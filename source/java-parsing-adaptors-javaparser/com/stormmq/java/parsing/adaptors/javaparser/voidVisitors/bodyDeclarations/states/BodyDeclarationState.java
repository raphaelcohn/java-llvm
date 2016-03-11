package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states;

import com.github.javaparser.ast.body.*;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

public interface BodyDeclarationState extends State
{
	int nextEnumConstantOrdinal(@NotNull EnumConstantDeclaration enumConstantDeclaration);

	@NotNull
	String fieldName(@NotNull VariableDeclaratorId variableDeclaratorId);

	@NotNull
	String annotationMemberName(@NotNull AnnotationMemberDeclaration annotationMemberDeclaration);

	@NotNull
	KnownReferenceTypeName localClassName(@NotNull ClassOrInterfaceDeclaration classDeclaration);

	@NotNull
	KnownReferenceTypeName anonymousClassName(@NotNull ClassOrInterfaceDeclaration classDeclaration);

	// Just having a body {}, even if empty, is enough to become a class
	@NotNull
	KnownReferenceTypeName enumConstantAsInnerClassName(@NotNull EnumConstantDeclaration enumConstantDeclaration);

	@NotNull
	KnownReferenceTypeName currentTypeName();
}
