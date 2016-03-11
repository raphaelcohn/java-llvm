package com.stormmq.java.parsing.ast.detailMaps;

import com.stormmq.java.parsing.ast.details.Detail;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class MemberDetailMap<D extends Detail>
{
	@NotNull private final Map<KnownReferenceTypeName, D[]> map;

	public MemberDetailMap(@NotNull final Map<KnownReferenceTypeName, D[]> map)
	{
		this.map = map;
	}

	@NotNull
	public D[] findMustBeExtant(@NotNull final KnownReferenceTypeName knownReferenceTypeName)
	{
		@Nullable final D[] details = map.get(knownReferenceTypeName);
		if (details == null)
		{
			throw new IllegalArgumentException(format(ENGLISH, "There are no details for knownReferenceTypeName '%1$s'", knownReferenceTypeName));
		}
		return details;
	}
}
