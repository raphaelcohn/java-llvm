package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ReferenceType;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface TypeResolver
{
	@NotNull
	AnnotationInstanceIntermediate[][] convertReferenceTypeArrayAnnotations(@NotNull final ReferenceType referenceType, final int additionalArrayCount);

	@NotNull
	AnnotationInstanceIntermediate[] convertToAnnotations(@Nullable final List<AnnotationExpr> annotations);
	
	@Nullable
	KnownReferenceTypeName resolveAsTypeParameter(@NotNull final String identifier);

	@Nullable
	KnownReferenceTypeName resolveAsType(@NotNull final String identifier);

	@Nullable
	KnownReferenceTypeName resolve(@NotNull final String identifier);
}
