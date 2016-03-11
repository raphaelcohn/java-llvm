package com.stormmq.java.parsing.adaptors.javaparser.genericVisitors;

import com.github.javaparser.ast.expr.*;
import com.stormmq.java.parsing.adaptors.javaparser.ast.ExpressionIntermediate;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.assertNotNull;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.validateName;
import static com.stormmq.java.parsing.utilities.ReservedIdentifiers.DefaultAnnotationMemberName;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

public final class ConvertingAnnotationExprGenericVisitor extends AbstractAnnotationExprGenericVisitor<Map<String, ExpressionIntermediate>, TypeResolverContext>
{
	@NotNull private static final Map<String, ExpressionIntermediate> EmptyValues = emptyMap();
	@NotNull private static final ConvertingAnnotationExprGenericVisitor ConvertingInstance = new ConvertingAnnotationExprGenericVisitor();

	@NotNull
	public static Map<String, ExpressionIntermediate> convertAnnotationValues(@NotNull final AnnotationExpr annotation, @NotNull final TypeResolverContext typeResolverContext)
	{
		return annotation.accept(ConvertingInstance, typeResolverContext);
	}

	private ConvertingAnnotationExprGenericVisitor()
	{
	}

	@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
	@NotNull
	@Override
	public Map<String, ExpressionIntermediate> visit(@NotNull final NormalAnnotationExpr normalAnnotationExpression, @NotNull final TypeResolverContext typeResolverContext)
	{
		@Nullable final List<MemberValuePair> memberValuePairs = normalAnnotationExpression.getPairs();
		if (memberValuePairs == null)
		{
			return EmptyValues;
		}

		final int size = memberValuePairs.size();
		if (size == 0)
		{
			return EmptyValues;
		}

		final Map<String, ExpressionIntermediate> values = new HashMap<>(size);
		for (final MemberValuePair memberValuePair : memberValuePairs)
		{
			final String key = validateName(memberValuePair.getName(), false);
			final Expression memberValue = assertNotNull(memberValuePair.getValue());
			values.put(key, new ExpressionIntermediate(memberValue, typeResolverContext));
		}
		return values;
	}

	@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
	@NotNull
	@Override
	public Map<String, ExpressionIntermediate> visit(@NotNull final SingleMemberAnnotationExpr singleMemberAnnotationExpression, @NotNull final TypeResolverContext typeResolverContext)
	{
		final Expression memberValue = assertNotNull(singleMemberAnnotationExpression.getMemberValue());
		return singletonMap(DefaultAnnotationMemberName, new ExpressionIntermediate(memberValue, typeResolverContext));
	}

	@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
	@NotNull
	@Override
	public Map<String, ExpressionIntermediate> visit(@NotNull final MarkerAnnotationExpr markerAnnotationExpression, @NotNull final TypeResolverContext typeResolverContext)
	{
		return EmptyValues;
	}
}
