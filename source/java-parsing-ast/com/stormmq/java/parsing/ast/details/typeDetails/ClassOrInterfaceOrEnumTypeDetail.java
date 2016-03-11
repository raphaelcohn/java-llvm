package com.stormmq.java.parsing.ast.details.typeDetails;

import com.stormmq.java.parsing.ast.detailMaps.decedents.DecedentMap;
import com.stormmq.java.parsing.ast.detailMaps.indices.TypeDetailIndex;
import org.jetbrains.annotations.NotNull;

public interface ClassOrInterfaceOrEnumTypeDetail extends TypeDetail
{
	void addToDecedents(@NotNull final DecedentMap<ClassTypeDetail> classTypeDecedentsMap, @NotNull final DecedentMap<ClassOrInterfaceOrEnumTypeDetail> interfaceTypeDecedentsMap);

	void guardInterfacesOnlyImplementInterfaces(@NotNull final TypeDetailIndex typeDetailIndex);

	boolean isEffectivelyStrictFloatingPoint(@NotNull final TypeDetailIndex typeDetailIndex);
}
