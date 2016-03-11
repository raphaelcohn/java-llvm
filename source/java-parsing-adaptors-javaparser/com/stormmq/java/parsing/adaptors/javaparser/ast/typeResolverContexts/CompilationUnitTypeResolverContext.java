package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.stormmq.java.parsing.adaptors.javaparser.helpers.NameExprHelper;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeResolution.TypeResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.assertNotNull;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.NameExprHelper.parentNameToNameExpr;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.NameExprHelper.qualifiedName;
import static com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverChoice.Package;
import static com.stormmq.java.parsing.utilities.names.PackageName.*;
import static com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName.knownReferenceTypeName;

public final class CompilationUnitTypeResolverContext extends VeryAbstractTypeResolverContext implements TypeResolverContext
{
	@NotNull private static final ImportDeclaration JavaLangImport = new ImportDeclaration(parentNameToNameExpr(JavaLangPackageName), false, true);

	@NotNull private final TypeResolver typeResolver;
	@NotNull private final PackageName packageName;
	@NotNull private final Map<String, KnownReferenceTypeName> instanceSimpleImports;
	@NotNull private final List<ImportDeclaration> instanceAsteriskImports;
	@NotNull private final List<ImportDeclaration> staticImports;

	public CompilationUnitTypeResolverContext(@NotNull final TypeResolver typeResolver, @NotNull final CompilationUnit compilationUnit)
	{
		this.typeResolver = typeResolver;
		final List<ImportDeclaration> importDeclarations = assertNotNull(compilationUnit.getImports());
		final Map<String, KnownReferenceTypeName> instanceSimpleImports = new HashMap<>(importDeclarations.size() + 2);
		final ArrayList<ImportDeclaration> instanceAsteriskImports = new ArrayList<>(importDeclarations.size() + 2);
		final ArrayList<ImportDeclaration> staticImports = new ArrayList<>(importDeclarations.size() + 2);

		instanceAsteriskImports.add(JavaLangImport);

		@Nullable final PackageDeclaration packageDeclaration = compilationUnit.getPackage();
		if (packageDeclaration == null)
		{
			packageName = NoPackageName;
		}
		else
		{
			final NameExpr nameExpr = assertNotNull(packageDeclaration.getName());
			instanceAsteriskImports.add(new ImportDeclaration(nameExpr, false, true));

			packageName = NameExprHelper.packageName(nameExpr);
		}

		for (ImportDeclaration importDeclaration : importDeclarations)
		{
			if (importDeclaration.isStatic())
			{
				staticImports.add(importDeclaration);
			}
			else
			{
				if (importDeclaration.isAsterisk())
				{
					instanceAsteriskImports.add(importDeclaration);
				}
				else
				{
					final KnownReferenceTypeName knownReferenceTypeName = knownReferenceTypeName(qualifyName(importDeclaration));
					instanceSimpleImports.put(knownReferenceTypeName.simpleTypeName(), knownReferenceTypeName);
				}
			}
		}

		instanceAsteriskImports.trimToSize();
		staticImports.trimToSize();

		this.instanceSimpleImports = instanceSimpleImports;
		this.instanceAsteriskImports = instanceAsteriskImports;
		this.staticImports = staticImports;
	}

	@Nullable
	@Override
	public KnownReferenceTypeName resolveAsTypeParameter(@NotNull final String identifier)
	{
		return null;
	}

	@NotNull
	@Override
	public KnownReferenceTypeName resolveAsType(@NotNull final String identifier)
	{
		@Nullable final KnownReferenceTypeName knownReferenceTypeName = instanceSimpleImports.get(identifier);
		if (knownReferenceTypeName != null)
		{
			return knownReferenceTypeName;
		}

		@Nullable KnownReferenceTypeName resolved = null;
		for (ImportDeclaration instanceAsteriskImport : instanceAsteriskImports)
		{
			final PackageName packageName = packageName(qualifyName(instanceAsteriskImport));
			@Nullable KnownReferenceTypeName potential = typeResolver.resolve(packageName, identifier);
			if (potential == null)
			{
				// packageName may actually be a class or type name, and the asterisk refers to inner classes or types
				potential = typeResolver.resolve(packageNameAsTypeName(packageName), identifier);
			}
			if (potential == null)
			{
				continue;
			}
			if (resolved != null)
			{
				throw new IllegalArgumentException("Multiply resolved identifier");
			}
			resolved = potential;
		}

		if (resolved == null)
		{
			throw new IllegalArgumentException("Could not resolve identifier in any package");
		}

		return resolved;
	}

	@NotNull
	private static KnownReferenceTypeName packageNameAsTypeName(@NotNull final PackageName packageName) {return knownReferenceTypeName(packageName.fullyQualifiedNameUsingDotsAndDollarSigns());}

	@NotNull
	@Override
	public KnownReferenceTypeName resolve(@NotNull final String identifier)
	{
		return resolveAsType(identifier);
	}

	@NotNull
	private static String qualifyName(@NotNull final ImportDeclaration importDeclaration)
	{
		final NameExpr nameExpr = assertNotNull(importDeclaration.getName());
		return qualifiedName(nameExpr);
	}

	@Override
	@NotNull
	public TypeResolverContext topLevelAnnotation(@NotNull final String annotationName)
	{
		return new AnnotationTypeResolverContext(typeResolver, Package, this, packageName, annotationName);
	}

	@Override
	@NotNull
	public TypeResolverContext topLevelInterface(@NotNull final String interfaceName, @NotNull final UsefulTypeParameters interfaceTypeParameters)
	{
		return new InterfaceTypeResolverContext(typeResolver, Package, this, packageName, interfaceName, interfaceTypeParameters);
	}

	@Override
	@NotNull
	public TypeResolverContext topLevelClass(@NotNull final String className, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		return new StaticClassTypeResolverContext(typeResolver, Package, this, packageName, className, classTypeParameters);
	}

	@Override
	@NotNull
	public TypeResolverContext topLevelEnum(@NotNull final String enumName)
	{
		return new EnumTypeResolverContext(typeResolver, Package, this, packageName, enumName);
	}

	@Override
	@NotNull
	public TypeResolverContext nestedAnnotation(@NotNull final String annotationName)
	{
		throw new UnsupportedOperationException("Not possible for a compilation unit context");
	}

	@Override
	@NotNull
	public TypeResolverContext nestedInterface(@NotNull final String interfaceName, @NotNull final UsefulTypeParameters interfaceTypeParameters)
	{
		throw new UnsupportedOperationException("Not possible for a compilation unit context");
	}

	// raw is that without any '$' if it happens to start with '$'
	@Override
	@NotNull
	public TypeResolverContext nestedStaticClass(@NotNull final String rawStaticClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		throw new UnsupportedOperationException("Not possible for a compilation unit context");
	}

	// raw is that without any '$' if it happens to start with '$'
	@Override
	@NotNull
	public TypeResolverContext nestedInnerClass(@NotNull final String rawInnerClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		throw new UnsupportedOperationException("Not possible for a compilation unit context");
	}

	// Only within a method
	// raw is that without any '$' if it happens to start with '$'
	@Override
	@NotNull
	public TypeResolverContext nestedLocalClass(@NotNull final KnownReferenceTypeName knownReferenceTypeName, @NotNull final String rawLocalClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		throw new UnsupportedOperationException("Not possible for a compilation unit context");
	}

	// Only within a method or field or initializer
	@Override
	@NotNull
	public TypeResolverContext nestedAnonymousClass(final int ordinal)
	{
		throw new UnsupportedOperationException("Not possible for a compilation unit context");
	}

	@Override
	@NotNull
	public TypeResolverContext nestedEnum(@NotNull final String enumName)
	{
		throw new UnsupportedOperationException("Not possible for a compilation unit context");
	}

	@Override
	@NotNull
	public TypeResolverContext enumConstantClassBody(@NotNull final String enumConstantName)
	{
		throw new UnsupportedOperationException("Not possible for a compilation unit context");
	}

	@Override
	@NotNull
	public TypeResolverContext constructor(@NotNull final UsefulTypeParameters constructorTypeParameters)
	{
		throw new UnsupportedOperationException("Not possible for a compilation unit context");
	}

	@Override
	@NotNull
	public TypeResolverContext method(@NotNull final UsefulTypeParameters methodTypeParameters)
	{
		throw new UnsupportedOperationException("Not possible for a compilation unit context");
	}
}
