package com.stormmq.java.parsing.adaptors.javac;

import org.jetbrains.annotations.NotNull;

public class TEST<XX>
{
	public void x()
	{
	}

	@NotNull
	public XX x2()
	{
		return null;
	}

	public <T extends String & Runnable, Y extends String & Cloneable> Generic<T>.GenericX<Y> x(final T value)
	{

		return null;
	}

	public static class Generic<T extends String & Runnable>
	{
		public interface AAA
		{
			public interface BBB
			{

			}
		}

		public interface XXX
		{
			public class YYY
			{
				public void x()
				{
					final Class<AAA> aaaClass = AAA.class;
					final Class<AAA.BBB> bbbClass = AAA.BBB.class;
					final Class<ZZZ> zzzClass = ZZZ.class;

					class Y
					{
						void x()
						{

						}
					}

					class X
					{
						void x()
						{
							final Class<X> xClass = X.class;
						}
					}
				}
			}

			public class ZZZ
			{

			}
		}

		public class GenericX<Y extends String & Cloneable>
		{
			public <T> void q()
			{
				T s;

				final Class<AAA> aaaClass = AAA.class;
				final Class<AAA> aaaClass1 = Generic.AAA.class;
				final Class<AAA.BBB> bbbClass = AAA.BBB.class;
			}
			T x = null;
		}
	}
}
