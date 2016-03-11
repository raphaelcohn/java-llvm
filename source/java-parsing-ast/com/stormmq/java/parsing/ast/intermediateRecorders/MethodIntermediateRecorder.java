package com.stormmq.java.parsing.ast.intermediateRecorders;

import com.stormmq.java.parsing.ast.intermediates.MethodIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

public interface MethodIntermediateRecorder
{
	void record(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final MethodIntermediate methodIntermediate);
}
