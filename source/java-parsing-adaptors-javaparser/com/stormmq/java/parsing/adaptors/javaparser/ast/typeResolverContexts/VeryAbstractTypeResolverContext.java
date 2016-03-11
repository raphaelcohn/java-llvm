package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.ReferenceType;
import com.stormmq.java.parsing.adaptors.javaparser.ast.ExpressionIntermediate;
import com.stormmq.java.parsing.adaptors.javaparser.ast.JavaparserAnnotationInstanceIntermediate;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.UnresolvedReferenceTypeName.convertNameExprToUnresolveReferenceTypeName;
import static com.stormmq.java.parsing.adaptors.javaparser.genericVisitors.ConvertingAnnotationExprGenericVisitor.convertAnnotationValues;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.assertNotNull;
import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate.EmptyAnnotations;
import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate.EmptyEmptyAnnotations;

public abstract class VeryAbstractTypeResolverContext implements TypeResolverContext
{
	protected VeryAbstractTypeResolverContext()
	{
	}

	@NotNull
	@Override
	public final AnnotationInstanceIntermediate[][] convertReferenceTypeArrayAnnotations(@NotNull final ReferenceType referenceType, final int additionalArrayCount)
	{
		final int arrayCount = referenceType.getArrayCount();
		if (arrayCount < 0)
		{
			throw new IllegalArgumentException("ReferenceType array count should not be negative");
		}

		@Nullable final List<List<AnnotationExpr>> arraysAnnotations = referenceType.getArraysAnnotations();
		if (arrayCount == 0)
		{
			if (additionalArrayCount == 0)
			{
				if (arraysAnnotations == null)
				{
					return EmptyEmptyAnnotations;
				}

				if (arraysAnnotations.isEmpty())
				{
					return EmptyEmptyAnnotations;
				}

				throw new IllegalArgumentException("Array count and arrays annotations mismatch (should not be present)");
			}

			return new AnnotationInstanceIntermediate[additionalArrayCount][0];
		}

		if (arraysAnnotations == null)
		{
			throw new IllegalArgumentException("Array count and arrays annotations mismatch");
		}

		final int size = arraysAnnotations.size();
		if (size != arrayCount)
		{
			throw new IllegalArgumentException("Array count and arrays annotations mismatch");
		}

		final int actualSize = size + additionalArrayCount;
		final AnnotationInstanceIntermediate[][] annotationInstanceIntermediates = new AnnotationInstanceIntermediate[actualSize][];
		int index;
		for (index = 0; index < size; index++)
		{
			@Nullable final List<AnnotationExpr> value = arraysAnnotations.get(index);
			annotationInstanceIntermediates[index] = convertToAnnotations(value);
		}

		// TODO: It is not clear what the javac compiler actually does in this case
		for (index = size; index < actualSize; index++)
		{
			annotationInstanceIntermediates[index] = EmptyAnnotations;
		}

		return annotationInstanceIntermediates;
	}

	@NotNull
	@Override
	public final AnnotationInstanceIntermediate[] convertToAnnotations(@Nullable final List<AnnotationExpr> annotations)
	{
		if (annotations == null)
		{
			return EmptyAnnotations;
		}

		final int size = annotations.size();
		if (size == 0)
		{
			return EmptyAnnotations;
		}

		@SuppressWarnings("unchecked") final AnnotationInstanceIntermediate[] annotationInstanceIntermediates = new AnnotationInstanceIntermediate[size];
		for (int index = 0; index < size; index++)
		{
			final AnnotationExpr annotation = assertNotNull(annotations.get(index));

			final NameExpr nameExpr = assertNotNull(annotation.getName());
			final UnresolvedReferenceTypeName unresolvedReferenceTypeName = convertNameExprToUnresolveReferenceTypeName(nameExpr, this);

			final Map<String, ExpressionIntermediate> values = convertAnnotationValues(annotation, this);
			annotationInstanceIntermediates[index] = new JavaparserAnnotationInstanceIntermediate(unresolvedReferenceTypeName, values);
			index++;
		}
		return annotationInstanceIntermediates;
	}
}
