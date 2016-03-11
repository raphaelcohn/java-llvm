package com.stormmq.java.parsing.ast.intermediates;

import com.stormmq.java.parsing.ast.details.Detail;
import org.jetbrains.annotations.NotNull;

public interface CreatableIntermediate<D extends Detail> extends Intermediate<D>
{
	@NotNull
	IntermediateCreator intermediateCreator();

	@NotNull
	Origin origin();

	boolean isStatic();
}
