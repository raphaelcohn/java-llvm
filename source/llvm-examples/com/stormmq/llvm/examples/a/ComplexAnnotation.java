package com.stormmq.llvm.examples.a;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface ComplexAnnotation
{
	public SimpleAnnotation value() default @SimpleAnnotation(a = "...", b = 1);

	public SimpleAnnotation[] value2() default {};

	public SimpleAnnotation[] value3() default {@SimpleAnnotation(a = "aaa", b = 2)};
}
