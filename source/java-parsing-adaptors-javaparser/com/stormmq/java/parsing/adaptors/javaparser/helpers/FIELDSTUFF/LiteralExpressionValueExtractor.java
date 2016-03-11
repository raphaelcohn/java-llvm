package com.stormmq.java.parsing.adaptors.javaparser.helpers.FIELDSTUFF;

import com.github.javaparser.ast.expr.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.stormmq.java.parsing.utilities.literalParsers.IntegerLiteralParser.isUnderscore;
import static com.stormmq.java.parsing.utilities.literalParsers.IntegerLiteralParser.parseJava7LiteralAsInteger;
import static com.stormmq.java.parsing.utilities.literalParsers.IntegerLiteralParser.parseJava7LiteralAsLong;
import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;

public interface LiteralExpressionValueExtractor
{
	@NotNull
	Map<Class<? extends LiteralExpr>, LiteralExpressionValueExtractor> Extractors = new HashMap<Class<? extends LiteralExpr>, LiteralExpressionValueExtractor>()
	{{
		put(BooleanLiteralExpr.class, new LiteralExpressionValueExtractor()
		{
			@Nullable
			@Override
			public Object extract(@NotNull final Expression expression)
			{
				return ((BooleanLiteralExpr) expression).getValue();
			}
		});
		put(CharLiteralExpr.class, new LiteralExpressionValueExtractor()
		{
			@Nullable
			@Override
			public Object extract(@NotNull final Expression expression)
			{
				final String value = ((CharLiteralExpr) expression).getValue();
				if (value.length() != 1)
				{
					throw new IllegalArgumentException("char literal must be only one character");
				}
				return value.charAt(0);
			}
		});
		put(DoubleLiteralExpr.class, new LiteralExpressionValueExtractor()
		{
			@Nullable
			@Override
			public Object extract(@NotNull final Expression expression)
			{
				final String valueWithUnderscores = ((DoubleLiteralExpr) expression).getValue();

				final int length = valueWithUnderscores.length();
				if (length == 0)
				{
					throw new IllegalArgumentException("Empty double literals are not allowed");
				}
				if (isUnderscore(valueWithUnderscores.charAt(0)))
				{
					throw new IllegalArgumentException("Double literals can not start with an underscore");
				}

				final int endIndex = length - 1;
				final char suffix = valueWithUnderscores.charAt(endIndex);
				if (isUnderscore(suffix))
				{
					throw new IllegalArgumentException("Double literals can not end with an underscore");
				}

				final String value = valueWithUnderscores.replace("_", "");
				try
				{
					switch (suffix)
					{
						case 'f':
						case 'F':
							return parseFloat(value.substring(0, endIndex));
						case 'd':
						case 'D':
							return parseDouble(value.substring(0, endIndex));
						default:
							return parseDouble(value);
					}
				}
				catch (NumberFormatException e)
				{
					throw new IllegalArgumentException("Double literal was not intelligible");
				}
			}
		});
		put(IntegerLiteralExpr.class, new LiteralExpressionValueExtractor()
		{
			@Nullable
			@Override
			public Object extract(@NotNull final Expression expression)
			{
				final String valueIncludingRadixPrefixAndUnderscores = ((IntegerLiteralExpr) expression).getValue();
				return parseJava7LiteralAsInteger(valueIncludingRadixPrefixAndUnderscores);
			}
		});
		put(IntegerLiteralMinValueExpr.class, new LiteralExpressionValueExtractor()
		{
			@Nullable
			@Override
			public Object extract(@NotNull final Expression expression)
			{
				return Integer.MIN_VALUE;
			}
		});
		put(LongLiteralExpr.class, new LiteralExpressionValueExtractor()
		{
			@Nullable
			@Override
			public Object extract(@NotNull final Expression expression)
			{
				final String valueIncludingRadixPrefixAndUnderscores = ((LongLiteralExpr) expression).getValue();
				return parseJava7LiteralAsLong(valueIncludingRadixPrefixAndUnderscores);
			}
		});
		put(LongLiteralMinValueExpr.class, new LiteralExpressionValueExtractor()
		{
			@Nullable
			@Override
			public Object extract(@NotNull final Expression expression)
			{
				return Long.MIN_VALUE;
			}
		});
		put(NullLiteralExpr.class, new LiteralExpressionValueExtractor()
		{
			@Nullable
			@Override
			public Object extract(@NotNull final Expression expression)
			{
				return null;
			}
		});
		put(StringLiteralExpr.class, new LiteralExpressionValueExtractor()
		{
			@Nullable
			@Override
			public Object extract(@NotNull final Expression expression)
			{
				return ((StringLiteralExpr) expression).getValue();
			}
		});
	}};

	@Nullable
	Object extract(@NotNull final Expression expression);
}
