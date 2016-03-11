package com.stormmq.java.parsing.ast.detailMaps;

import com.stormmq.java.parsing.ast.details.typeDetails.TypeDetail;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

public final class TypeDetailMap
{
	@NotNull public static final TypeDetailMap EmptyTypeDetailMap = new TypeDetailMap(Collections.<KnownReferenceTypeName, TypeDetail>emptyMap());
	@NotNull private final Map<KnownReferenceTypeName, TypeDetail> map;

	public TypeDetailMap(@NotNull final Map<KnownReferenceTypeName, TypeDetail> map)
	{
		this.map = map;
	}
}
