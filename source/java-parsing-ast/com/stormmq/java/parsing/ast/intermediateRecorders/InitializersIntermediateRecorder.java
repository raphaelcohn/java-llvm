package com.stormmq.java.parsing.ast.intermediateRecorders;

import com.stormmq.java.parsing.ast.intermediates.InitializersIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

public interface InitializersIntermediateRecorder
{
	void record(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final InitializersIntermediate initializersIntermediate);
}
