package com.stormmq.java.parsing.ast.detailMaps.indices;

import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.details.typeDetails.AnnotationTypeDetail;
import com.stormmq.java.parsing.ast.details.typeDetails.ClassTypeDetail;
import com.stormmq.java.parsing.ast.details.typeDetails.EnumTypeDetail;
import com.stormmq.java.parsing.ast.details.typeDetails.InterfaceTypeDetail;
import org.jetbrains.annotations.NotNull;

public interface TypeDetailIndex
{
	void guardInterfacesOnlyImplementInterfaces();

	void guardSuperTypesExistAndAreNotFinal();

	@NotNull
	ClassTypeDetail lookUpClass(@NotNull KnownReferenceTypeName classTypeReference);

	@NotNull
	InterfaceTypeDetail lookUpInterface(@NotNull KnownReferenceTypeName interfaceTypeReference);

	@NotNull
	AnnotationTypeDetail lookUpAnnotation(@NotNull KnownReferenceTypeName annotationTypeReference);

	@NotNull
	EnumTypeDetail lookUpEnum(@NotNull KnownReferenceTypeName enumTypeReference);
}
