package com.stormmq.llvm.examples.a;

import java.lang.reflect.Constructor;

public class Test
{
	public int[][] x[], y;

	public Test(int x)
	{
		new Integer(4);
	}

	public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException
	{
		final Test test = new Test(4);
		final Constructor<Test> constructor = Test.class.getConstructor(int.class);
		final String name = constructor.getName();
		System.out.println(constructor);
	}
}
