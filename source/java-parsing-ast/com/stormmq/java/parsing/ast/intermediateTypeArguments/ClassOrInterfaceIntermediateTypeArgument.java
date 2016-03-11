package com.stormmq.java.parsing.ast.intermediateTypeArguments;

import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate.EmptyAnnotations;
import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate.EmptyEmptyAnnotations;
import static java.lang.System.arraycopy;

public final class ClassOrInterfaceIntermediateTypeArgument extends AbstractArrayPossibleIntermediateTypeArgument
{
	@NotNull public static final ClassOrInterfaceIntermediateTypeArgument JavaLangObjectTypeArgument = new ClassOrInterfaceIntermediateTypeArgument(EmptyAnnotations, EmptyEmptyAnnotations, null, "java.lang.Object", EmptyIntermediateTypeArguments);
	@NotNull public static final ClassOrInterfaceIntermediateTypeArgument JavaLangAnnotationAnnotationTypeArgument = new ClassOrInterfaceIntermediateTypeArgument(EmptyAnnotations, EmptyEmptyAnnotations, null, "java.lang.annotation.Annotation", EmptyIntermediateTypeArguments);

	@NotNull public final static ClassOrInterfaceIntermediateTypeArgument[] EmptyClassOrInterfaceIntermediateTypeUsages = {};
	@NotNull public static final ClassOrInterfaceIntermediateTypeArgument[] JavaLangAnnotationAnnotationTypeUsages = {JavaLangAnnotationAnnotationTypeArgument};

	@NotNull private final AnnotationInstanceIntermediate[] annotations;
	@NotNull private final AnnotationInstanceIntermediate[][] arrayAnnotations;
	@Nullable private final ClassOrInterfaceIntermediateTypeArgument scope;
	@NotNull private final String name;
	@NotNull private final IntermediateTypeArgument[] upperBounds;

	public ClassOrInterfaceIntermediateTypeArgument(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final AnnotationInstanceIntermediate[][] arrayAnnotations, @Nullable final ClassOrInterfaceIntermediateTypeArgument scope, @NotNull final String name, @NotNull final IntermediateTypeArgument[] upperBounds)
	{
		super(annotations, arrayAnnotations);
		this.annotations = annotations;
		this.arrayAnnotations = arrayAnnotations;
		this.scope = scope;
		this.name = name;
		this.upperBounds = upperBounds;
	}

	@NotNull
	@Override
	public TypeUsage resolve()
	{
		throw new UnsupportedOperationException("Resolution not yet supported");
	}

	@Override
	public boolean isPrimitive()
	{
		return false;
	}

	@Override
	public boolean isVoid()
	{
		return false;
	}

	@Override
	public boolean couldBeString()
	{
		return name.equals("String") || name.endsWith(".String");
	}

	@NotNull
	public ClassOrInterfaceIntermediateTypeArgument merge(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final AnnotationInstanceIntermediate[][] arrayAnnotations)
	{
		if (isArray())
		{
			throw new IllegalArgumentException("Already an array");
		}

		final AnnotationInstanceIntermediate[] combined = mergeAnnotations(annotations, this.annotations);

		return new ClassOrInterfaceIntermediateTypeArgument(combined, arrayAnnotations, scope, name, upperBounds);
	}

	@NotNull
	public static AnnotationInstanceIntermediate[] mergeAnnotations(@NotNull final AnnotationInstanceIntermediate[] underlyingAnnotations, @NotNull final AnnotationInstanceIntermediate[] overlyingAnnotations)
	{
		final int thisLength = overlyingAnnotations.length;
		final int thatLength = underlyingAnnotations.length;
		final int newLength = thisLength + thatLength;
		final AnnotationInstanceIntermediate[] combined = new AnnotationInstanceIntermediate[newLength];
		arraycopy(underlyingAnnotations, 0, combined, 0, thatLength);
		arraycopy(overlyingAnnotations, 0, combined, thatLength, thisLength);
		return combined;
	}
}
