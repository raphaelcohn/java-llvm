package com.stormmq.java.parsing.adaptors.javaparser.helpers;

import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.QualifiedNameExpr;
import com.stormmq.java.parsing.utilities.StringConstants;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.java.parsing.utilities.names.parentNames.ParentName;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.stormmq.java.parsing.utilities.StringConstants.ExternalTypeNameSeparator;

public final class NameExprHelper
{
	@NotNull private static final NameExpr EmptyNameExpr = new NameExpr("");

	private NameExprHelper()
	{
	}

	@NotNull
	public static String qualifiedName(@NotNull final NameExpr nameExpression)
	{
		NameExpr current = nameExpression;
		final ArrayList<String> names = new ArrayList<>();
		do
		{
			names.add(current.getName());
			if (current instanceof QualifiedNameExpr)
			{
				current = ((QualifiedNameExpr) current).getQualifier();
			}
			else
			{
				break;
			}
		} while(true);

		final StringBuilder builder = new StringBuilder(4096);
		for(int index = names.size() - 1; index >= 0; index--)
		{
			builder.append(names.get(index));
			if (index != 0)
			{
				builder.append(ExternalTypeNameSeparator);
			}
		}
		return builder.toString();
	}

	@NotNull
	public static PackageName packageName(@NotNull final NameExpr nameExpression)
	{
		return PackageName.packageName(qualifiedName(nameExpression));
	}

	@NotNull
	public static NameExpr parentNameToNameExpr(@NotNull final ParentName parentName)
	{
		final String fullyQualifiedNameUsingDotsAndDollarSigns = parentName.fullyQualifiedNameUsingDotsAndDollarSigns();
		if (fullyQualifiedNameUsingDotsAndDollarSigns.isEmpty())
		{
			return EmptyNameExpr;
		}
		if (fullyQualifiedNameUsingDotsAndDollarSigns.contains(".."))
		{
			throw new IllegalArgumentException("Name must not contain consecutive periods");
		}
		if (fullyQualifiedNameUsingDotsAndDollarSigns.charAt(0) == ExternalTypeNameSeparator || fullyQualifiedNameUsingDotsAndDollarSigns.charAt(fullyQualifiedNameUsingDotsAndDollarSigns.length() - 1) == ExternalTypeNameSeparator)
		{
			throw new IllegalArgumentException("Name must not start or end with a period");
		}

		final String[] names = fullyQualifiedNameUsingDotsAndDollarSigns.split("\\.");

		NameExpr nameInstance = new NameExpr(names[0]);

		final int length = names.length;
		for(int index = 1; index < length; index++)
		{
			final String name = names[index];
			nameInstance = new QualifiedNameExpr(nameInstance, name);
		}
		return nameInstance;
	}
}
