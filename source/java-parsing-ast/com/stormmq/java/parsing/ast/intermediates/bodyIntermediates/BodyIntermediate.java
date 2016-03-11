package com.stormmq.java.parsing.ast.intermediates.bodyIntermediates;

import com.stormmq.java.parsing.ast.details.bodyDetails.BodyDetail;
import org.jetbrains.annotations.NotNull;

public interface BodyIntermediate
{
	boolean isConcrete();

	boolean isAbstract();

	boolean isNative();

	@NotNull
	BodyDetail resolve();
}
