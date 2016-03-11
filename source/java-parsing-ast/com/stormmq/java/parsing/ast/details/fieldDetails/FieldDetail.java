package com.stormmq.java.parsing.ast.details.fieldDetails;

import com.stormmq.java.parsing.ast.details.ArrayCreator;
import com.stormmq.java.parsing.ast.details.MemberDetail;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

public interface FieldDetail extends MemberDetail
{
	@NotNull ArrayCreator<FieldDetail> FieldDetailArrayCreator = new ArrayCreator<FieldDetail>()
	{
		@NotNull private final FieldDetail[] Empty = create(0);

		@NotNull
		@Override
		public FieldDetail[] empty()
		{
			return Empty;
		}

		@NotNull
		@Override
		public FieldDetail[] create(final int size)
		{
			return new FieldDetail[size];
		}
	};

	@NotNull Visibility visibility();

	boolean isStatic();

	boolean isFinal();

	boolean isNamed(@NotNull final String name);

	@NotNull
	TypeUsage fieldTypeUsage();
}
