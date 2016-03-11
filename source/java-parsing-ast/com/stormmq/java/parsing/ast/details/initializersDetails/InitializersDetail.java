package com.stormmq.java.parsing.ast.details.initializersDetails;

import com.stormmq.java.parsing.ast.details.ArrayCreator;
import com.stormmq.java.parsing.ast.details.MemberDetail;
import org.jetbrains.annotations.NotNull;

public interface InitializersDetail extends MemberDetail
{
	@NotNull ArrayCreator<InitializersDetail> InitializersDetailArrayCreator = new ArrayCreator<InitializersDetail>()
	{
		@NotNull private final InitializersDetail[] Empty = create(0);

		@NotNull
		@Override
		public InitializersDetail[] empty()
		{
			return Empty;
		}

		@NotNull
		@Override
		public InitializersDetail[] create(final int size)
		{
			return new InitializersDetail[size];
		}
	};
}
