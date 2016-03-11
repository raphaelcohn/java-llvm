package com.stormmq.java.parsing.ast.intermediates;

import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

public enum IntermediateCreator
{
	Annotation(KnownReferenceTypeName.JavaLangObject),
	Interface(KnownReferenceTypeName.JavaLangObject),
	Class(KnownReferenceTypeName.JavaLangObject),
	Enum(KnownReferenceTypeName.JavaLangEnum),
	;

	@NotNull
	public final KnownReferenceTypeName defaultSuperTypeReferenceName;

	IntermediateCreator(@NotNull final KnownReferenceTypeName defaultSuperTypeReferenceName)
	{
		this.defaultSuperTypeReferenceName = defaultSuperTypeReferenceName;
	}
}
