package com.stormmq.java.parsing.adaptors.javaparser.typeDeclarationWrapperUsers;

import com.stormmq.java.parsing.adaptors.javaparser.referenceTypeNameMakers.ReferenceTypeNameMaker;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.TypeDeclarationWrapper;
import com.stormmq.java.parsing.utilities.names.parentNames.ParentName;
import org.jetbrains.annotations.NotNull;

public interface TypeDeclarationWrapperUser
{
	void use(@NotNull final ReferenceTypeNameMaker parent, @NotNull final ParentName parentName, @NotNull final TypeDeclarationWrapper typeDeclarationWrapper);
}
