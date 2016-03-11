package com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.compilationUnitWrapperUsers;

import com.stormmq.java.parsing.adaptors.javaparser.ast.importsResolvers.ImportsResolver;
import com.stormmq.java.parsing.adaptors.javaparser.packageDeclarationUsers.PackageDeclarationUser;
import com.stormmq.java.parsing.adaptors.javaparser.referenceTypeNameMakers.ReferenceTypeNameMaker;
import com.stormmq.java.parsing.adaptors.javaparser.typeDeclarationWrapperUsers.TypeDeclarationWrapperUser;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.CompilationUnitWrapper;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public final class RecursiveCompilationUnitWrapperUser implements CompilationUnitWrapperUser
{
	@NotNull private final PackageDeclarationUser packageDeclarationUser;
	@NotNull private final TypeDeclarationWrapperUser typeDeclarationWrapperUser;

	public RecursiveCompilationUnitWrapperUser(@NotNull final PackageDeclarationUser packageDeclarationUser, @NotNull final TypeDeclarationWrapperUser typeDeclarationWrapperUser)
	{
		this.packageDeclarationUser = packageDeclarationUser;
		this.typeDeclarationWrapperUser = typeDeclarationWrapperUser;
	}

	@Override
	public void use(@NotNull final Path relativeFilePath, @NotNull final CompilationUnitWrapper compilationUnitWrapper)
	{
		final ReferenceTypeNameMaker parent = compilationUnitWrapper.newReferenceTypeNameMaker(importsResolver);

		compilationUnitWrapper.usePackageDeclaration(packageDeclarationUser, parent);

		compilationUnitWrapper.useTypeDeclarations(typeDeclarationWrapperUser, parent);
	}
}
