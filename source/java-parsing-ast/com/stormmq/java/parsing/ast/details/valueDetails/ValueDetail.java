package com.stormmq.java.parsing.ast.details.valueDetails;

import com.stormmq.java.parsing.ast.details.Detail;
import org.jetbrains.annotations.NotNull;

public interface ValueDetail extends Detail
{
	@NotNull ValueDetail NoValueDetail = new ValueDetail()
	{

	};
}
