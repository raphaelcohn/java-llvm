package com.stormmq.llvm.examples.a;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Documented
@Retention(CLASS)
@Target(PACKAGE)
public @interface a_PackageAnnotation
{
	int y = z_Interface.zz.y;
	int y2 = y;

	final double dd = 10d;

	public final int x = 10;

	Class<?>[] value_xxx() default {Object.class};

	@NotNull int[] value_i_default() default {x, z_Interface.e};

	byte value_a();

	byte value_a_default() default 1;

	short value_b();

	short value_b_default() default 1;

	char value_c();

	char value_c_default() default 1;

	int value_d();

	int value_d_default() default 1;

	long value_e();

	long value_e_default() default 1;

	float value_f();

	float value_f_default() default 1;

	double value_g();

	double value_g_default() default 1;

	@Nullable String value_h();

	@NotNull String value_h_default() default "world";

	@Nullable int[] value_i();
}
