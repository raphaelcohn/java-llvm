package com.stormmq.java.parsing.ast.typeResolution;

import com.stormmq.java.parsing.ast.intermediates.*;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class SimpleTypeResolver implements TypeResolver
{
	@NotNull private final Map<String, KnownReferenceTypeName> knownReferenceTypeNames;
	@NotNull private final Map<String, LinkedHashSet<KnownReferenceTypeName>> knownReferenceTypesInPackagesAndTypes;

	@NotNull private final Map<KnownReferenceTypeName, LinkedHashSet<String>> knownStaticFields;
	@NotNull private final Map<KnownReferenceTypeName, LinkedHashSet<String>> knownStaticMethods;

	public SimpleTypeResolver()
	{
		knownReferenceTypeNames = new HashMap<>();
		knownReferenceTypesInPackagesAndTypes = new HashMap<>(4096);

		knownStaticFields = new HashMap<>();
		knownStaticMethods = new HashMap<>();
	}

	public void recordType(@NotNull final KnownReferenceTypeName knownReferenceTypeName)
	{
		final String fullyQualifiedNameUsingDotsAndDollarSigns = knownReferenceTypeName.fullyQualifiedNameUsingDotsAndDollarSigns();
		if (knownReferenceTypeNames.containsKey(fullyQualifiedNameUsingDotsAndDollarSigns))
		{
			return;
		}
		knownReferenceTypeNames.put(fullyQualifiedNameUsingDotsAndDollarSigns, knownReferenceTypeName);

		knownStaticFields.put(knownReferenceTypeName, new LinkedHashSet<>());
		knownStaticMethods.put(knownReferenceTypeName, new LinkedHashSet<>());
		final String parent = knownReferenceTypeName.allParents();

		@Nullable final LinkedHashSet<KnownReferenceTypeName> existingReferenceTypeNames = knownReferenceTypesInPackagesAndTypes.get(parent);
		@NotNull final LinkedHashSet<KnownReferenceTypeName> referenceTypeNames;
		if (existingReferenceTypeNames == null)
		{
			referenceTypeNames = new LinkedHashSet<>(256);
			knownReferenceTypesInPackagesAndTypes.put(parent, referenceTypeNames);
		}
		else
		{
			referenceTypeNames = existingReferenceTypeNames;
		}
		referenceTypeNames.add(knownReferenceTypeName);
	}

	public void recordField(@NotNull final KnownReferenceTypeName knownReferenceTypeName, @NotNull final FieldIntermediate fieldIntermediate)
	{
		if (fieldIntermediate.isStatic())
		{
			addMember(knownReferenceTypeName, fieldIntermediate.fieldName(), knownStaticFields);
		}
	}

	public void recordConstructor(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final ConstructorIntermediate constructorIntermediate)
	{
	}

	public void recordMethod(@NotNull final KnownReferenceTypeName knownReferenceTypeName, @NotNull final MethodIntermediate methodIntermediate)
	{
		if (methodIntermediate.isStatic())
		{
			addMember(knownReferenceTypeName, methodIntermediate.methodName(), knownStaticMethods);
		}
	}

	private static void addMember(@NotNull final KnownReferenceTypeName knownReferenceTypeName, @NotNull final String memberName, @NotNull final Map<KnownReferenceTypeName, LinkedHashSet<String>> members)
	{
		@Nullable final Set<String> staticFields = members.get(knownReferenceTypeName);
		if (staticFields == null)
		{
			throw new IllegalStateException("Add reference types before members");
		}
		if (staticFields.contains(memberName))
		{
			return;
		}
		staticFields.add(memberName);
	}

	@Nullable
	@Override
	public LinkedHashSet<KnownReferenceTypeName> get(@NotNull final String fullyQualifiedName)
	{
		return knownReferenceTypesInPackagesAndTypes.get(fullyQualifiedName);
	}

	@Override
	public boolean has(@NotNull final String fullyQualifiedName)
	{
		return knownReferenceTypeNames.containsKey(fullyQualifiedName);
	}

	@Nullable
	@Override
	public KnownReferenceTypeName resolve(@NotNull final String fullyQualifiedOrTrailingPartlyQualifiedName)
	{
		return knownReferenceTypeNames.get(fullyQualifiedOrTrailingPartlyQualifiedName);
	}

	@Nullable
	@Override
	public KnownReferenceTypeName resolve(@NotNull final PackageName packageName, @NotNull final String identifier)
	{
		throw new UnsupportedOperationException("Implement me");
	}

	@Nullable
	@Override
	public KnownReferenceTypeName resolve(@NotNull final KnownReferenceTypeName parent, @NotNull final String identifier)
	{
		throw new UnsupportedOperationException("Implement me");
	}
}
