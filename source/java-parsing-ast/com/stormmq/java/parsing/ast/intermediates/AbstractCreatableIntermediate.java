package com.stormmq.java.parsing.ast.intermediates;

import com.stormmq.java.parsing.ast.details.Detail;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.ReferenceTypeName;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName.EmptyKnownReferenceTypeNames;

public abstract class AbstractCreatableIntermediate<D extends Detail> implements CreatableIntermediate<D>
{
	@NotNull protected final IntermediateCreator intermediateCreator;
	@NotNull protected final Origin origin;
	@NotNull protected final KnownReferenceTypeName ourKnownTypeReferenceName;
	@NotNull protected final Visibility visibility;
	@NotNull protected final AnnotationInstanceIntermediate[] annotations;
	protected final boolean isDeprecatedInByteCode;
	protected final boolean isSynthetic;
	protected final boolean isStatic;

	protected AbstractCreatableIntermediate(@NotNull final IntermediateCreator intermediateCreator, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownTypeReferenceName, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isDeprecatedInByteCode, final boolean isSynthetic, final boolean isStatic)
	{
		this.intermediateCreator = intermediateCreator;
		this.origin = origin;
		this.ourKnownTypeReferenceName = ourKnownTypeReferenceName;
		this.visibility = visibility;
		this.annotations = annotations;
		this.isDeprecatedInByteCode = isDeprecatedInByteCode;
		this.isSynthetic = isSynthetic;
		this.isStatic = isStatic;
	}

	@NotNull
	protected static KnownReferenceTypeName[] resolveReferenceTypeNames(@NotNull final ReferenceTypeName[] referenceTypeNames)
	{
		if (KnownReferenceTypeName[].class.isAssignableFrom(referenceTypeNames.getClass()))
		{
			return (KnownReferenceTypeName[]) referenceTypeNames;
		}

		final int length = referenceTypeNames.length;
		if (length == 0)
		{
			return EmptyKnownReferenceTypeNames;
		}

		final KnownReferenceTypeName[] knownReferenceTypeNames = new KnownReferenceTypeName[length];
		for (int index = 0; index < length; index++)
		{
			knownReferenceTypeNames[index] = referenceTypeNames[index].resolve();
		}
		return knownReferenceTypeNames;
	}

	@Override
	@NotNull
	public final IntermediateCreator intermediateCreator()
	{
		return intermediateCreator;
	}

	@NotNull
	@Override
	public final Origin origin()
	{
		return origin;
	}

	@Override
	public final boolean isStatic()
	{
		return isStatic;
	}
}
