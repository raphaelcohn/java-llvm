package com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.typeVisitorAdaptors;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MethodContainer
{
	@NotNull MethodContainer NoMethodContainer = new MethodContainer()
	{
		@Override
		public boolean isLocalOrAnonymousClassInMethod()
		{
			return false;
		}
	};

	@NotNull
	static MethodContainer localOrAnonymousClassInMethod(@NotNull final String methodName, @NotNull final String methodDescriptor)
	{
		return new MethodContainer()
		{
			@Override
			public boolean isLocalOrAnonymousClassInMethod()
			{
				return true;
			}
		};
	}

	boolean isLocalOrAnonymousClassInMethod();
}
