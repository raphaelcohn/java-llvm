package com.stormmq.java.parsing.adaptors.javaparser.namedTypeDeclarationUsers;

import com.github.javaparser.ast.body.TypeDeclaration;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.adaptors.javaparser.referenceTypeNameMakers.ReferenceTypeNameMaker;
import org.jetbrains.annotations.NotNull;

public interface NamedTypeDeclarationUser<T extends TypeDeclaration>
{
	void use(@NotNull final ReferenceTypeNameMaker referenceTypeNameMaker, @NotNull final KnownReferenceTypeName ourReferenceTypeName, @NotNull final T typeDeclaration);
}
