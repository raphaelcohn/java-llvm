package com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers;

import com.github.javaparser.ast.body.*;
import com.stormmq.java.parsing.adaptors.javaparser.dead.AbstractHasBody;
import com.stormmq.java.parsing.adaptors.javaparser.namedTypeDeclarationUsers.bodyDeclarationUsers.BodyDeclarationUser;
import com.stormmq.java.parsing.adaptors.javaparser.referenceTypeNameMakers.ReferenceTypeNameMaker;
import com.stormmq.java.parsing.utilities.StringConstants;
import com.stormmq.java.parsing.utilities.names.parentNames.ParentName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.stormmq.java.parsing.utilities.StringConstants.ExternalTypeNameSeparator;

public final class TypeDeclarationWrapper extends AbstractHasBody
{
	@NotNull
	public List<ConstructorDeclaration> constructors()
	{
		if (typeDeclaration instanceof ClassOrInterfaceDeclaration)
		{
			final ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) typeDeclaration;
			if (classOrInterfaceDeclaration.isInterface())
			{
				return NoConstructors;
			}
			return findConstructorDeclarations();
		}

		if (typeDeclaration instanceof EnumDeclaration)
		{
			return findConstructorDeclarations();
		}

		return NoConstructors;
	}

	@NotNull
	private List<ConstructorDeclaration> findConstructorDeclarations()
	{
		final List<ConstructorDeclaration> constructorDeclarations = new ArrayList<>();
		visitBodyMembers(new BodyDeclarationUser()
		{
			@Override
			public void use(@NotNull final InitializerDeclaration initializerDeclaration)
			{
			}

			@Override
			public void use(@NotNull final FieldDeclaration fieldDeclaration)
			{
			}

			@Override
			public void use(@NotNull final ConstructorDeclaration constructorDeclaration)
			{
				constructorDeclarations.add(constructorDeclaration);
			}

			@Override
			public void use(@NotNull final MethodDeclaration methodDeclaration)
			{
			}

			@Override
			public void use(@NotNull final AnnotationMemberDeclaration annotationMemberDeclaration)
			{
			}

			@Override
			public void use(@NotNull final EmptyMemberDeclaration emptyMemberDeclaration)
			{
			}

			@Override
			public void use(@NotNull final AnnotationDeclaration annotationDeclaration)
			{
			}

			@Override
			public void use(@NotNull final ClassOrInterfaceDeclaration classOrInterfaceDeclaration)
			{
			}

			@Override
			public void use(@NotNull final EnumDeclaration enumDeclaration)
			{
			}

			@Override
			public void use(@NotNull final EmptyTypeDeclaration emptyTypeDeclaration)
			{
			}

			@Override
			public void end()
			{
			}
		});
		if (constructorDeclarations.isEmpty())
		{
			return NoConstructors;
		}
		return constructorDeclarations;
	}

	@NotNull
	public KnownReferenceTypeName knownReferenceTypeName(@NotNull final ParentName parentName)
	{
		final String simpleTypeName = name();
		final String fullyQualifiedTypeName = qualifyName(parentName.fullyQualifiedNameUsingDotsAndDollarSigns(), simpleTypeName);
		return KnownReferenceTypeName.knownReferenceTypeName(fullyQualifiedTypeName);
	}

	@NotNull
	private static String qualifyName(@NotNull final String qualifier, @NotNull final String simpleTypeName)
	{
		if (qualifier.isEmpty())
		{
			return simpleTypeName;
		}
		return qualifier + ExternalTypeNameSeparator + simpleTypeName;
	}

	public void addMethod(@NotNull final MethodDeclaration methodDeclaration)
	{
		addMember(methodDeclaration, false);
	}

	public void addConstructor(@NotNull final ConstructorDeclaration constructorDeclaration)
	{
		addMember(constructorDeclaration, false);
	}

	public void addFieldAtFront(@NotNull final FieldDeclaration fieldDeclaration)
	{
		addMember(fieldDeclaration, true);
	}

	private void addMember(@NotNull final BodyDeclaration bodyDeclaration, final boolean atFront)
	{
		@Nullable final List<BodyDeclaration> members = typeDeclaration.getMembers();
		final List<BodyDeclaration> replacements;
		if (members == null)
		{
			replacements = new ArrayList<>(1);
			replacements.add(bodyDeclaration);
		}
		else
		{
			replacements = new ArrayList<>(members.size());
			if (atFront)
			{
				replacements.add(bodyDeclaration);
			}
			for (BodyDeclaration member : members)
			{
				replacements.add(member);
			}
			if (!atFront)
			{
				replacements.add(bodyDeclaration);
			}
		}
		typeDeclaration.setMembers(replacements);
	}
}
