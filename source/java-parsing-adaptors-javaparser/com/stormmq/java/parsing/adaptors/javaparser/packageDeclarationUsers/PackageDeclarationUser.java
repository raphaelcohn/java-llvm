package com.stormmq.java.parsing.adaptors.javaparser.packageDeclarationUsers;

import com.github.javaparser.ast.PackageDeclaration;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverContext;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.java.parsing.adaptors.javaparser.referenceTypeNameMakers.ReferenceTypeNameMaker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface PackageDeclarationUser
{
	void use(@NotNull final PackageName packageName, @Nullable final PackageDeclaration packageDeclaration, @NotNull final TypeResolverContext typeResolverContext);
}
