package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states;

import com.github.javaparser.ast.body.*;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverContext;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.*;

import java.util.*;

import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.validateName;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class ImplementedBodyDeclarationState extends AbstractState implements BodyDeclarationState
{
	@NotNull private final KnownReferenceTypeName currentTypeName;
	@NotNull private final String ourSimpleName;
	@NotNull private final Map<String, Integer> localClassIndex;
	@NotNull private final Set<String> encounteredFields;
	@NotNull private final Set<String> alreadyEncounteredAnnotationMembers;
	private int lastAnonymousClassIndex;
	private int lastEnumConstantOrdinal;

	// ourSimpleName has two meanings - with and without any $-like prefixes
	public ImplementedBodyDeclarationState(@NotNull final TypeResolverContext typeResolverContext, @NotNull final KnownReferenceTypeName parentName, @NotNull final String ourSimpleName)
	{
		super(typeResolverContext, parentName);
		currentTypeName = parentName;
		this.ourSimpleName = ourSimpleName;
		localClassIndex = new HashMap<>(16);
		encounteredFields = new LinkedHashSet<>(16);
		alreadyEncounteredAnnotationMembers = new LinkedHashSet<>(16);
		lastAnonymousClassIndex = 0;
		lastEnumConstantOrdinal = 0;
	}

	@Override
	public int nextEnumConstantOrdinal(@NotNull final EnumConstantDeclaration enumConstantDeclaration)
	{
		final int ordinal = lastEnumConstantOrdinal;
		lastEnumConstantOrdinal++;
		final String enumConstantName = validateName(enumConstantDeclaration.getName(), false);
		if (!encounteredFields.add(enumConstantName))
		{
			throw new IllegalStateException(format(ENGLISH, "Enum constant '%1$s' has already been encountered", enumConstantName));
		}
		return ordinal;
	}

	@Override
	@NotNull
	public String fieldName(@NotNull final VariableDeclaratorId variableDeclaratorId)
	{
		final String fieldName = validateName(variableDeclaratorId.getName(), false);

		if (!encounteredFields.add(fieldName))
		{
			throw new IllegalStateException(format(ENGLISH, "Field '%1$s' has already been encountered (perhaps as an enum constant)", fieldName));
		}

		return fieldName;
	}

	@Override
	@NotNull
	public String annotationMemberName(@NotNull final AnnotationMemberDeclaration annotationMemberDeclaration)
	{
		final String annotationMemberName = validateName(annotationMemberDeclaration.getName(), false);

		if (!alreadyEncounteredAnnotationMembers.add(annotationMemberName))
		{
			throw new IllegalStateException(format(ENGLISH, "Annotation member '%1$s' has already been encountered", annotationMemberName));
		}

		return annotationMemberName;
	}

	@Override
	@NotNull
	public KnownReferenceTypeName localClassName(@NotNull final ClassOrInterfaceDeclaration classDeclaration)
	{
		if (classDeclaration.isInterface())
		{
			throw new IllegalArgumentException(com.stormmq.java.parsing.utilities.StringConstants.Should_be_impossible);
		}

		final String typeName = validateName(classDeclaration.getName(), false);

		if (typeName.equals(ourSimpleName))
		{
			throw new IllegalArgumentException("Local class name matches parent name");
		}

		@Nullable final Integer lastIndex = localClassIndex.get(typeName);
		final int index = (lastIndex == null) ? 1 : (lastIndex + 1);
		localClassIndex.put(typeName, index);

		final String localClassName = '$' + Integer.toString(index) + typeName;
		return child(localClassName, "local class");
	}

	@Override
	@NotNull
	public KnownReferenceTypeName anonymousClassName(@NotNull final ClassOrInterfaceDeclaration classDeclaration)
	{
		if (classDeclaration.isInterface())
		{
			throw new IllegalArgumentException(com.stormmq.java.parsing.utilities.StringConstants.Should_be_impossible);
		}

		return anonymousOrEnumConstantClassName("anonymous class");
	}

	// Just having a body {}, even if empty, is enough to become a class
	@Override
	@NotNull
	public KnownReferenceTypeName enumConstantAsInnerClassName(@NotNull final EnumConstantDeclaration enumConstantDeclaration)
	{
		return anonymousOrEnumConstantClassName("enum constant");
	}

	@NotNull
	@Override
	public KnownReferenceTypeName currentTypeName()
	{
		return currentTypeName;
	}

	@Override
	@NotNull
	public KnownReferenceTypeName typeName(@NotNull final TypeDeclaration typeDeclaration)
	{
		if (typeDeclaration instanceof EmptyTypeDeclaration)
		{
			throw new IllegalArgumentException(com.stormmq.java.parsing.utilities.StringConstants.Should_be_impossible);
		}

		final String rawTypeName = typeDeclaration.getName();
		if (rawTypeName.equals(ourSimpleName))
		{
			throw new IllegalArgumentException("Nested type's name matches parent name");
		}

		// To stop clashes with local and anonymous classes
		final String qualifiedRawTypeName = (rawTypeName.charAt(0) == '$') ? ('$' + rawTypeName) : rawTypeName;
		final String typeName = validateName(qualifiedRawTypeName, false);

		return child(typeName, "nested type");
	}

	@NotNull
	private KnownReferenceTypeName anonymousOrEnumConstantClassName(@NonNls @NotNull final String what)
	{
		final int anonymousClassIndex = lastAnonymousClassIndex;
		lastAnonymousClassIndex++;
		final String anonymousClassName = validateName(ourSimpleName + '$' + Integer.toString(anonymousClassIndex), false);

		return child(anonymousClassName, what);
	}
}
