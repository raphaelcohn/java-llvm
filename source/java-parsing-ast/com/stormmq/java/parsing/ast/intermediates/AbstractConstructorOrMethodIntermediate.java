package com.stormmq.java.parsing.ast.intermediates;

import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.BodyIntermediate;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.ReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractConstructorOrMethodIntermediate extends AbstractCreatableIntermediate implements MemberIntermediate<MethodDetail>
{
	@NotNull protected final TypeUsage returnTypeUsage;
	@NotNull protected final ParameterIntermediate[] parameterIntermediates;
	@NotNull protected final ReferenceTypeName[] throwsArray;
	protected final boolean isVarArgs;
	protected final boolean isBridge;
	protected final boolean isSynchronized;
	@NotNull protected final BodyIntermediate bodyIntermediate;
	@NotNull protected final Completeness completeness;

	public AbstractConstructorOrMethodIntermediate(@NotNull final IntermediateCreator intermediateCreator, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourTypeReferenceName, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotationInstanceIntermediates, @NotNull final TypeUsage returnTypeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, final boolean isStatic, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final ReferenceTypeName[] throwsArray, final boolean isVarArgs, final boolean isBridge, final boolean isSynchronized, @NotNull final BodyIntermediate bodyIntermediate, @NotNull final Completeness completeness)
	{
		super(intermediateCreator, origin, ourTypeReferenceName, visibility, annotationInstanceIntermediates, isDeprecatedInByteCode, isSynthetic, isStatic);
		this.returnTypeUsage = returnTypeUsage;
		this.parameterIntermediates = parameterIntermediates;
		this.throwsArray = throwsArray;
		this.isVarArgs = isVarArgs;
		this.isBridge = isBridge;
		this.isSynchronized = isSynchronized;
		this.bodyIntermediate = bodyIntermediate;
		this.completeness = completeness;
	}
}
