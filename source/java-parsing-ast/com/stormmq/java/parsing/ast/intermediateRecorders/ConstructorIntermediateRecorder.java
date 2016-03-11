package com.stormmq.java.parsing.ast.intermediateRecorders;

import com.stormmq.java.parsing.ast.intermediates.ConstructorIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

public interface ConstructorIntermediateRecorder
{
	void record(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final ConstructorIntermediate constructorIntermediate);

	boolean hasNoConstructors(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName);
}
