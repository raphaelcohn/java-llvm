package com.stormmq.java.parsing.ast.intermediates;

import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.MemberIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.BodyIntermediate;
import com.stormmq.java.parsing.utilities.*;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.ReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.utilities.ReservedIdentifiers.InstanceInitializerMethodName;

public final class ConstructorIntermediate extends AbstractConstructorOrMethodIntermediate implements MemberIntermediate<MethodDetail>
{
	public ConstructorIntermediate(@NotNull final IntermediateCreator intermediateCreator, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourTypeReferenceName, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotationInstanceIntermediates, @NotNull final TypeUsage constructorTypeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final ReferenceTypeName[] throwsArray, final boolean isVarArgs, final boolean isBridge, @NotNull final BodyIntermediate bodyIntermediate)
	{
		super(intermediateCreator, origin, ourTypeReferenceName, visibility, annotationInstanceIntermediates, constructorTypeUsage, isDeprecatedInByteCode, isSynthetic, false, parameterIntermediates, throwsArray, isVarArgs, isBridge, false, bodyIntermediate, Completeness.Final);
	}

	@SuppressWarnings("MethodMayBeStatic")
	@NotNull
	public String methodName()
	{
		return InstanceInitializerMethodName;
	}

	@NotNull
	@Override
	public MethodDetail resolve(@NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		throw new UnsupportedOperationException("Finish me now!");
	}
}
