package com.stormmq.java.parsing.ast.details.typeDetails;

import com.stormmq.java.parsing.ast.intermediates.Origin;
import com.stormmq.java.parsing.ast.detailMaps.decedents.DecedentMap;
import com.stormmq.java.parsing.ast.detailMaps.indices.TypeDetailIndex;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractClassOrInterfaceOrEnumTypeDetail extends AbstractTypeDetail implements ClassOrInterfaceOrEnumTypeDetail
{
	@NotNull protected final KnownReferenceTypeName[] interfaceTypeReferenceNames;

	protected AbstractClassOrInterfaceOrEnumTypeDetail(@NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourTypeReferenceName, @NotNull final Visibility visibility, final boolean isDeprecatedInByteCode, final boolean isStrictFloatingPoint, final boolean isSynthetic, @NotNull final AnnotationInstanceDetail[] annotationInstanceIntermediates, @NotNull final KnownReferenceTypeName[] interfaceTypeReferenceNames)
	{
		super(origin, ourTypeReferenceName, visibility, isDeprecatedInByteCode, isStrictFloatingPoint, isSynthetic, annotationInstanceIntermediates);
		this.interfaceTypeReferenceNames = interfaceTypeReferenceNames;
	}

	@Override
	public void addToDecedents(@NotNull final DecedentMap<ClassTypeDetail> classTypeDecedentsMap, @NotNull final DecedentMap<ClassOrInterfaceOrEnumTypeDetail> interfaceTypeDecedentsMap)
	{
		for (KnownReferenceTypeName interfaceTypeReferenceName : interfaceTypeReferenceNames)
		{
			interfaceTypeDecedentsMap.addDecedent(interfaceTypeReferenceName, this);
		}
	}

	@Override
	public void guardInterfacesOnlyImplementInterfaces(@NotNull final TypeDetailIndex typeDetailIndex)
	{
		for (KnownReferenceTypeName interfaceTypeReferenceName : interfaceTypeReferenceNames)
		{
			final InterfaceTypeDetail interfaceTypeDetails = typeDetailIndex.lookUpInterface(interfaceTypeReferenceName);
			interfaceTypeDetails.guardInterfacesOnlyImplementInterfaces(typeDetailIndex);
		}
	}

	@Override
	public boolean isEffectivelyStrictFloatingPoint(@NotNull final TypeDetailIndex typeDetailIndex)
	{
		if (isStrictFloatingPoint)
		{
			return true;
		}

		for (KnownReferenceTypeName interfaceTypeReferenceName : interfaceTypeReferenceNames)
		{
			final InterfaceTypeDetail interfaceTypeDetails = typeDetailIndex.lookUpInterface(interfaceTypeReferenceName);
			if (interfaceTypeDetails.isEffectivelyStrictFloatingPoint(typeDetailIndex))
			{
				return true;
			}
		}

		return false;
	}
}
