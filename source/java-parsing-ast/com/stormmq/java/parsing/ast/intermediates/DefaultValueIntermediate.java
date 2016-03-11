package com.stormmq.java.parsing.ast.intermediates;

import com.stormmq.java.parsing.ast.details.valueDetails.ValueDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.ast.details.valueDetails.ValueDetail.NoValueDetail;

public interface DefaultValueIntermediate extends Intermediate<ValueDetail>
{
	@NotNull DefaultValueIntermediate NoDefaultValueIntermediate = new DefaultValueIntermediate()
	{

		@Override
		public boolean isAValue()
		{
			return false;
		}

		@Override
		@NotNull
		public ValueDetail resolve(@NotNull final TypeUsage resolvedReturnTypeUsage, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
		{
			return NoValueDetail;
		}
	};

	boolean isAValue();

	@NotNull
	ValueDetail resolve(@NotNull final TypeUsage resolvedReturnTypeUsage, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder);
}
