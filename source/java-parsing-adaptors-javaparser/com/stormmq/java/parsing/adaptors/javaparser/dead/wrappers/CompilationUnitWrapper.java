package com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.stormmq.java.parsing.adaptors.javaparser.ast.importsResolvers.ImportsResolver;
import com.stormmq.java.parsing.adaptors.javaparser.helpers.NameExprHelper;
import com.stormmq.java.parsing.adaptors.javaparser.packageDeclarationUsers.PackageDeclarationUser;
import com.stormmq.java.parsing.adaptors.javaparser.referenceTypeNameMakers.CompilationUnitReferenceTypeNameMaker;
import com.stormmq.java.parsing.adaptors.javaparser.referenceTypeNameMakers.ReferenceTypeNameMaker;
import com.stormmq.java.parsing.adaptors.javaparser.typeDeclarationWrapperUsers.TypeDeclarationWrapperUser;
import com.stormmq.java.parsing.utilities.names.PackageName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.nullToEmptyList;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.NameExprHelper.qualifiedName;

public final class CompilationUnitWrapper
{
	@NotNull private static final String NoPackageName = "";

	@NotNull private final CompilationUnit compilationUnit;
	@NotNull private final PackageName packageName;

	public CompilationUnitWrapper(@NotNull final CompilationUnit compilationUnit)
	{
		this.compilationUnit = compilationUnit;
		packageName = PackageName.packageName(packageNameString());
	}

	@Nullable
	public <V> V retrieve(@NotNull final IdentityHashMap<?, V> index)
	{
		return index.get(objectIdentity());
	}

	@SuppressWarnings("unchecked")
	public <V> void store(@NotNull final IdentityHashMap<?, V> index, @NotNull final V value)
	{
		((Map) index).put(objectIdentity(), value);
	}

	@NotNull
	public List<ImportDeclaration> imports()
	{
		return nullToEmptyList(compilationUnit.getImports());
	}

	@NotNull
	public PackageName packageName()
	{
		return packageName;
	}

	@NotNull
	public ImportDeclaration packageNameAsImportDeclaration()
	{
		return new ImportDeclaration(NameExprHelper.parentNameToNameExpr(packageName), false, true);
	}

	public void usePackageDeclaration(@NotNull final PackageDeclarationUser packageDeclarationUser, @NotNull final ReferenceTypeNameMaker parent)
	{
		packageDeclarationUser.use(packageName(), packageDeclaration(), parent);
	}

	public void useTypeDeclarations(@NotNull final TypeDeclarationWrapperUser typeDeclarationWrapperUser, @NotNull final ReferenceTypeNameMaker parent)
	{
		@Nullable final List<TypeDeclaration> typeDeclarations = compilationUnit.getTypes();

		if (typeDeclarations == null)
		{
			return;
		}

		if (typeDeclarations.isEmpty())
		{
			return;
		}

		for (TypeDeclaration typeDeclaration : typeDeclarations)
		{
			typeDeclarationWrapperUser.use(parent, packageName(), new TypeDeclarationWrapper(typeDeclaration));
		}
	}

	@NotNull
	public ReferenceTypeNameMaker newReferenceTypeNameMaker(@NotNull final ImportsResolver importsResolver)
	{
		return new CompilationUnitReferenceTypeNameMaker(importsResolver, this);
	}

	@NotNull
	private Object objectIdentity()
	{
		return compilationUnit;
	}

	@NotNull
	private String packageNameString()
	{
		@Nullable final PackageDeclaration packageDeclaration = packageDeclaration();
		if (packageDeclaration == null)
		{
			return NoPackageName;
		}
		@NotNull final NameExpr packageNameExpr = packageDeclaration.getName();
		return qualifiedName(packageNameExpr);
	}

	@Nullable
	private PackageDeclaration packageDeclaration()
	{
		return compilationUnit.getPackage();
	}
}
