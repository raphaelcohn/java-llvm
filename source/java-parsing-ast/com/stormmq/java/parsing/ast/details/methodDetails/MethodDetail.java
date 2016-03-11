package com.stormmq.java.parsing.ast.details.methodDetails;

import com.stormmq.java.parsing.ast.details.ArrayCreator;
import com.stormmq.java.parsing.ast.details.MemberDetail;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

public interface MethodDetail extends MemberDetail
{
	@NotNull ArrayCreator<MethodDetail> MethodDetailArrayCreator = new ArrayCreator<MethodDetail>()
	{
		@NotNull private final MethodDetail[] Empty = create(0);

		@NotNull
		@Override
		public MethodDetail[] empty()
		{
			return Empty;
		}

		@NotNull
		@Override
		public MethodDetail[] create(final int size)
		{
			return new MethodDetail[size];
		}
	};

	@NotNull
	public Visibility visibility();

	public boolean isStatic();

	@NotNull
	public Completeness completeness();

	boolean isNamed(@NotNull final String name);

	@NotNull
	TypeUsage returnTypeUsage();
}
