package com.stormmq.java.parsing.ast.intermediateRecorders;

import com.stormmq.java.parsing.ast.intermediates.TypeIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

public interface TypeIntermediateRecorder
{
	void record(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final TypeIntermediate typeIntermediate);
}
