package com.stormmq.java.parsing.ast.detailMaps.decedents;

import com.stormmq.java.parsing.ast.detailMaps.indices.TypeDetailIndex;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.details.typeDetails.ClassOrInterfaceOrEnumTypeDetail;
import com.stormmq.java.parsing.ast.details.typeDetails.ClassTypeDetail;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public final class DecedentsMaps
{
	@NotNull private final TypeDetailIndex typeDetailIndex;
	@NotNull private final DecedentMap<ClassTypeDetail> classTypeDecedentsMap;
	@NotNull private final DecedentMap<ClassOrInterfaceOrEnumTypeDetail> interfaceTypeDecedentsMap;

	public DecedentsMaps(@NotNull final TypeDetailIndex typeDetailIndex, @NotNull final DecedentMap<ClassTypeDetail> classTypeDecedentsMap, @NotNull final DecedentMap<ClassOrInterfaceOrEnumTypeDetail> interfaceTypeDecedentsMap)
	{
		this.typeDetailIndex = typeDetailIndex;
		this.classTypeDecedentsMap = classTypeDecedentsMap;
		this.interfaceTypeDecedentsMap = interfaceTypeDecedentsMap;
	}

	@NotNull
	public Set<ClassTypeDetail> decedentsOfClass(@NotNull final KnownReferenceTypeName classTypeReference)
	{
		// Guard for existence
		typeDetailIndex.lookUpClass(classTypeReference);

		@Nullable final Set<ClassTypeDetail> decedents = classTypeDecedentsMap.decedents(classTypeReference);
		assert decedents != null;
		//noinspection unchecked
		return decedents;
	}

	@NotNull
	public Set<ClassOrInterfaceOrEnumTypeDetail> decedentsOfInterface(@NotNull final KnownReferenceTypeName interfaceTypeReference)
	{
		// Guard for existence
		typeDetailIndex.lookUpInterface(interfaceTypeReference);

		@Nullable final Set<ClassOrInterfaceOrEnumTypeDetail> decedents = interfaceTypeDecedentsMap.decedents(interfaceTypeReference);
		assert decedents != null;
		return decedents;
	}
}
