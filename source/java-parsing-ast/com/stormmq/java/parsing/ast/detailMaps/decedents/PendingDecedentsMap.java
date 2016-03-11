package com.stormmq.java.parsing.ast.detailMaps.decedents;

import com.stormmq.java.parsing.ast.detailMaps.indices.TypeDetailIndex;
import com.stormmq.java.parsing.ast.details.typeDetails.ClassOrInterfaceOrEnumTypeDetail;
import com.stormmq.java.parsing.ast.details.typeDetails.ClassTypeDetail;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class PendingDecedentsMap
{
	@NotNull private final List<ClassOrInterfaceOrEnumTypeDetail> classOrInterfaceOrEnumTypeDetails;

	public PendingDecedentsMap()
	{
		classOrInterfaceOrEnumTypeDetails = new ArrayList<>();
	}

	public void add(@NotNull final ClassOrInterfaceOrEnumTypeDetail classOrInterfaceOrEnumTypeDetail)
	{
		classOrInterfaceOrEnumTypeDetails.add(classOrInterfaceOrEnumTypeDetail);
	}

	@NotNull
	public DecedentsMaps resolve(@NotNull final TypeDetailIndex typeDetailIndex)
	{
		final DecedentMap<ClassTypeDetail> classTypeDecedentsMap = new DecedentMap<>();
		final DecedentMap<ClassOrInterfaceOrEnumTypeDetail> interfaceTypeDecedentsMap = new DecedentMap<>();
		for (ClassOrInterfaceOrEnumTypeDetail classOrInterfaceOrEnumTypeDetail : classOrInterfaceOrEnumTypeDetails)
		{
			classOrInterfaceOrEnumTypeDetail.addToDecedents(classTypeDecedentsMap, interfaceTypeDecedentsMap);
		}
		return new DecedentsMaps(typeDetailIndex, classTypeDecedentsMap, interfaceTypeDecedentsMap);
	}
}
