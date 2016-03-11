package com.stormmq.java.parsing.ast.details.typeDetails;

import com.stormmq.java.parsing.ast.intermediates.Origin;
import com.stormmq.java.parsing.ast.detailMaps.decedents.DecedentMap;
import com.stormmq.java.parsing.ast.detailMaps.decedents.DecedentsMaps;
import com.stormmq.java.parsing.ast.detailMaps.indices.TypeDetailIndex;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class ClassTypeDetail extends AbstractClassOrInterfaceOrEnumTypeDetail
{
	@NotNull private final KnownReferenceTypeName superTypeReferenceName;
	@NotNull private final Completeness completeness;
	private final boolean isJavaLangObject;

	public ClassTypeDetail(@NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourTypeReferenceName, @NotNull final Visibility visibility, final boolean isDeprecatedInByteCode, final boolean isStrictFloatingPoint, final boolean isSynthetic, @NotNull final AnnotationInstanceDetail[] annotationInstanceIntermediates, @NotNull final KnownReferenceTypeName[] interfaceTypeReferenceNames, @NotNull final KnownReferenceTypeName superTypeReferenceName, @NotNull final Completeness completeness)
	{
		super(origin, ourTypeReferenceName, visibility, isDeprecatedInByteCode, isStrictFloatingPoint, isSynthetic, annotationInstanceIntermediates, interfaceTypeReferenceNames);

		isJavaLangObject = ourTypeReferenceName.isJavaLangObject();
		if (superTypeReferenceName.equals(ourTypeReferenceName) && !isJavaLangObject)
		{
			throw new IllegalArgumentException(format(ENGLISH, "superTypeReferenceName '%1$s' can not match type name", superTypeReferenceName));
		}

		this.superTypeReferenceName = superTypeReferenceName;
		this.completeness = completeness;
	}

	public void guardSuperTypesExistAndAreNotFinal(@NotNull final TypeDetailIndex typeDetailIndex)
	{
		KnownReferenceTypeName currentSuperTypeReferenceName = superTypeReferenceName;
		while (!currentSuperTypeReferenceName.isJavaLangObject())
		{
			final ClassTypeDetail superClassTypeDetails = typeDetailIndex.lookUpClass(currentSuperTypeReferenceName);
			if (superClassTypeDetails.completeness.isFinal())
			{
				throw new IllegalStateException(format(ENGLISH, "The class type details '%1$s' has a super type '%2$s' in its chain that is final", ourTypeReferenceName, currentSuperTypeReferenceName));
			}
			currentSuperTypeReferenceName = superClassTypeDetails.superTypeReferenceName;
		}
	}

	@Override
	public void addToDecedents(@NotNull final DecedentMap<ClassTypeDetail> classTypeDecedentsMap, @NotNull final DecedentMap<ClassOrInterfaceOrEnumTypeDetail> interfaceTypeDecedentsMap)
	{
		super.addToDecedents(classTypeDecedentsMap, interfaceTypeDecedentsMap);

		classTypeDecedentsMap.addDecedent(superTypeReferenceName, this);
	}

	public boolean isEffectivelyStrictFloatingPoint(@NotNull final TypeDetailIndex typeDetailIndex)
	{
		final boolean superIsEffectivelyStrictFloatingPoint = super.isEffectivelyStrictFloatingPoint(typeDetailIndex);
		if (superIsEffectivelyStrictFloatingPoint)
		{
			return true;
		}

		if (isJavaLangObject)
		{
			return false;
		}

		final ClassTypeDetail superTypeDetails = typeDetailIndex.lookUpClass(superTypeReferenceName);
		if (superTypeDetails.isEffectivelyStrictFloatingPoint(typeDetailIndex))
		{
			return true;
		}

		return false;
	}

	public boolean isOrCanBeFinal(@NotNull final DecedentsMaps decedentsMaps)
	{
		if (completeness.isFinal())
		{
			return true;
		}

		final Set<ClassTypeDetail> decedents = decedentsMaps.decedentsOfClass(ourTypeReferenceName);
		return decedents.size() == 0;
	}

}
