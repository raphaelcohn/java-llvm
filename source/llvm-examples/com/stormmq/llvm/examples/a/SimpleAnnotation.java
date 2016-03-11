package com.stormmq.llvm.examples.a;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface SimpleAnnotation
{
	public String a();

	public int b();
}
