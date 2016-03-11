package com.stormmq.java.parsing.ast.detailMaps.decedents;

import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.details.typeDetails.TypeDetail;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class DecedentMap<T extends TypeDetail>
{
	@NotNull private final Map<KnownReferenceTypeName, Set<T>> typeDecedentsMap;

	public DecedentMap()
	{
		typeDecedentsMap = new HashMap<>();
	}

	public void addDecedent(@NotNull final KnownReferenceTypeName referenceTypeName, @NotNull final T decedent)
	{
		@NotNull final Set<T> decedents = ensureEmptyEntryExists(referenceTypeName);
		decedents.add(decedent);
	}

	@NotNull
	private Set<T> ensureEmptyEntryExists(@NotNull final KnownReferenceTypeName classOrInterfaceTypeReference)
	{
		@Nullable final Set<T> potentialDecedents = typeDecedentsMap.get(classOrInterfaceTypeReference);
		@NotNull final Set<T> decedents;
		if (potentialDecedents == null)
		{
			decedents = new HashSet<>(16);
			typeDecedentsMap.put(classOrInterfaceTypeReference, decedents);
		}
		else
		{
			decedents = potentialDecedents;
		}
		return decedents;
	}

	@Nullable
	public Set<T> decedents(@NotNull final KnownReferenceTypeName classTypeReference)
	{
		return typeDecedentsMap.get(classTypeReference);
	}
}
