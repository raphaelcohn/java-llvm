package com.stormmq.java.parsing.ast.intermediates;

import com.stormmq.java.parsing.ast.details.initializersDetails.InitializersDetail;
import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.MemberIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class InitializersIntermediate implements MemberIntermediate<InitializersDetail>
{
	@NotNull private static final List<InitializerIntermediate> Empty = Collections.emptyList();

	@NotNull final List<InitializerIntermediate> staticInitializers;
	@NotNull final List<InitializerIntermediate> instanceInitializers;
	private final boolean staticOnly;

	public InitializersIntermediate(final boolean staticOnly)
	{
		this.staticOnly = staticOnly;
		staticInitializers = new ArrayList<>(8);
		instanceInitializers = staticOnly ? Empty : new ArrayList<InitializerIntermediate>(8);
	}

	public void addStaticInitializer(@NotNull final InitializerIntermediate staticInitializer)
	{
		staticInitializers.add(staticInitializer);
	}

	public void addInstanceInitializer(@NotNull final InitializerIntermediate instanceInitializer)
	{
		if (staticOnly)
		{
			throw new IllegalStateException("There should not be any instance initializers for this type");
		}
		instanceInitializers.add(instanceInitializer);
	}

	@NotNull
	@Override
	public InitializersDetail resolve(@NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		throw new UnsupportedOperationException();
	}
}
