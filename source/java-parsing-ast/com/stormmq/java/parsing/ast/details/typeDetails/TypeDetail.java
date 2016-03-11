package com.stormmq.java.parsing.ast.details.typeDetails;

import com.stormmq.java.parsing.ast.details.Detail;
import com.stormmq.java.parsing.ast.intermediates.Origin;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

public interface TypeDetail extends Detail
{
	@NotNull
	KnownReferenceTypeName ourTypeReferenceName();

	@NotNull
	Origin origin();
}
