package com.stormmq.java.parsing.ast.intermediates;

import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.bodyDetails.BodyDetail;
import com.stormmq.java.parsing.ast.details.methodDetails.*;
import com.stormmq.java.parsing.ast.details.parameterDetails.ParameterDetail;
import com.stormmq.java.parsing.ast.details.valueDetails.ValueDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.MemberIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.BodyIntermediate;
import com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.EmptyBodyIntermediate;
import com.stormmq.java.parsing.utilities.*;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.ReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.ast.details.parameterDetails.ParameterDetail.EmptyParameterDetails;
import static com.stormmq.java.parsing.ast.intermediates.DefaultValueIntermediate.NoDefaultValueIntermediate;
import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AbstractAnnotationInstanceIntermediate.resolveAnnotationInstanceIntermediates;
import static com.stormmq.java.parsing.utilities.Completeness.Abstract;
import static com.stormmq.java.parsing.utilities.Completeness.Final;
import static com.stormmq.java.parsing.utilities.Visibility.Public;

public final class MethodIntermediate extends AbstractConstructorOrMethodIntermediate implements MemberIntermediate<MethodDetail>
{
	@NotNull
	public static MethodIntermediate newAnnotationMethodIntermediate(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final DefaultValueIntermediate annotationDefaultValueIntermediate, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName)
	{
		if (returnTypeUsage.isMultiDimensionalArray())
		{
			throw new IllegalArgumentException("Annotations are not allowed to have methods with multidimensional arrays");
		}
		return new MethodIntermediate(IntermediateCreator.Annotation, origin, ourKnownReferenceTypeName, Public, annotations, methodName, returnTypeUsage, isDeprecatedInByteCode, isSynthetic, true, ParameterIntermediate.EmptyParameterIntermediates, throwsArray, false, false, false, EmptyBodyIntermediate.Abstract, Abstract, annotationDefaultValueIntermediate);
	}

	@NotNull
	public static MethodIntermediate newInterfaceInstanceMethodIntermediate(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ReferenceTypeName[] throwsArray, final boolean isVarArgs, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName)
	{
		return new MethodIntermediate(IntermediateCreator.Interface, origin, ourKnownReferenceTypeName, Public, annotations, methodName, returnTypeUsage, isDeprecatedInByteCode, isSynthetic, true, parameterIntermediates, throwsArray, isVarArgs, false, false, EmptyBodyIntermediate.Abstract, Abstract, NoDefaultValueIntermediate);
	}

	@NotNull
	public static MethodIntermediate newInterfaceDefaultMethodIntermediate(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ReferenceTypeName[] throwsArray, final boolean isVarArgs, @NotNull final BodyIntermediate bodyIntermediate, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName)
	{
		if (bodyIntermediate instanceof EmptyBodyIntermediate)
		{
			throw new IllegalArgumentException("bodyIntermediate can not be empty for a default method");
		}
		return new MethodIntermediate(IntermediateCreator.Interface, origin, ourKnownReferenceTypeName, Public, annotations, methodName, returnTypeUsage, isDeprecatedInByteCode, isSynthetic, true, parameterIntermediates, throwsArray, isVarArgs, false, false, bodyIntermediate, Abstract, NoDefaultValueIntermediate);
	}

	@NotNull
	public static MethodIntermediate newInterfaceStaticMethodIntermediate(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ReferenceTypeName[] throwsArray, final boolean isVarArgs, @NotNull final BodyIntermediate bodyIntermediate, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName)
	{
		if (bodyIntermediate instanceof EmptyBodyIntermediate)
		{
			throw new IllegalArgumentException("bodyIntermediate can not be empty for a static method");
		}
		return new MethodIntermediate(IntermediateCreator.Interface, origin, ourKnownReferenceTypeName, Public, annotations, methodName, returnTypeUsage, isDeprecatedInByteCode, isSynthetic, true, parameterIntermediates, throwsArray, isVarArgs, false, false, bodyIntermediate, Abstract, NoDefaultValueIntermediate);
	}

	@NotNull
	public static MethodIntermediate newClassStaticMethodIntermediate(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ReferenceTypeName[] throwsArray, final boolean isVarArgs, @NotNull final BodyIntermediate bodyIntermediate, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final Visibility visibility, final boolean isBridge, final boolean isSynchronized, final boolean isNative)
	{
		return newClassOrEnumStaticMethodIntermediate(IntermediateCreator.Class, methodName, returnTypeUsage, parameterIntermediates, isDeprecatedInByteCode, isSynthetic, throwsArray, isVarArgs, bodyIntermediate, annotations, origin, ourKnownReferenceTypeName, visibility, Final, isBridge, isSynchronized, true, isNative);
	}

	@NotNull
	public static MethodIntermediate newClassInstanceMethodIntermediate(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ReferenceTypeName[] throwsArray, final boolean isVarArgs, @NotNull final BodyIntermediate bodyIntermediate, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final Visibility visibility, @NotNull final Completeness completeness, final boolean isBridge, final boolean isSynchronized, final boolean isNative)
	{
		return newClassOrEnumStaticMethodIntermediate(IntermediateCreator.Class, methodName, returnTypeUsage, parameterIntermediates, isDeprecatedInByteCode, isSynthetic, throwsArray, isVarArgs, bodyIntermediate, annotations, origin, ourKnownReferenceTypeName, visibility, completeness, isBridge, isSynchronized, false, isNative);
	}

	@NotNull
	public static MethodIntermediate newEnumStaticMethodIntermediate(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ReferenceTypeName[] throwsArray, final boolean isVarArgs, @NotNull final BodyIntermediate bodyIntermediate, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final Visibility visibility, final boolean isBridge, final boolean isSynchronized, final boolean isNative)
	{
		return newClassOrEnumStaticMethodIntermediate(IntermediateCreator.Enum, methodName, returnTypeUsage, parameterIntermediates, isDeprecatedInByteCode, isSynthetic, throwsArray, isVarArgs, bodyIntermediate, annotations, origin, ourKnownReferenceTypeName, visibility, Final, isBridge, isSynchronized, true, isNative);
	}

	@NotNull
	public static MethodIntermediate newEnumInstanceMethodIntermediate(@NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ReferenceTypeName[] throwsArray, final boolean isVarArgs, @NotNull final BodyIntermediate bodyIntermediate, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final Visibility visibility, @NotNull final Completeness completeness, final boolean isBridge, final boolean isSynchronized, final boolean isNative)
	{
		return newClassOrEnumStaticMethodIntermediate(IntermediateCreator.Enum, methodName, returnTypeUsage, parameterIntermediates, isDeprecatedInByteCode, isSynthetic, throwsArray, isVarArgs, bodyIntermediate, annotations, origin, ourKnownReferenceTypeName, visibility, completeness, isBridge, isSynchronized, false, isNative);
	}

	@NotNull
	private static MethodIntermediate newClassOrEnumStaticMethodIntermediate(@NotNull final IntermediateCreator intermediateCreator, @NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, final boolean isDeprecatedInByteCode, final boolean isSynthetic, @NotNull final ReferenceTypeName[] throwsArray, final boolean isVarArgs, @NotNull final BodyIntermediate bodyIntermediate, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final Visibility visibility, @NotNull final Completeness completeness, final boolean isBridge, final boolean isSynchronized, final boolean isStatic, final boolean isNative)
	{
		visibility.validateIsNotPrivateIfIsAbstract(bodyIntermediate.isAbstract());

		if (isStatic)
		{
			if (bodyIntermediate.isAbstract())
			{
				throw new IllegalArgumentException("static class method bodyIntermediates can not be abstract");
			}
			if (completeness.isAbstract())
			{
				throw new IllegalArgumentException("completeness must not be abstract for a static method");
			}
		}
		else
		{
			if (completeness.isAbstract() && bodyIntermediate.isConcrete())
			{
				throw new IllegalArgumentException("bodyIntermediates can not be concrete for an abstract method");
			}
			if (!completeness.isAbstract() && bodyIntermediate.isAbstract())
			{
				throw new IllegalArgumentException("bodyIntermediates can not be abstract for a normal or final method");
			}
		}
		return new MethodIntermediate(intermediateCreator, origin, ourKnownReferenceTypeName, visibility, annotations, methodName, returnTypeUsage, isDeprecatedInByteCode, isSynthetic, isStatic, parameterIntermediates, throwsArray, isVarArgs, isBridge, isSynchronized, bodyIntermediate, completeness, NoDefaultValueIntermediate);
	}

	@NotNull private final String methodName;
	@NotNull private final DefaultValueIntermediate annotationDefaultValueIntermediate;

	public MethodIntermediate(@NotNull final IntermediateCreator intermediateCreator, @NotNull final Origin origin, @NotNull final KnownReferenceTypeName ourTypeReferenceName, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotationInstanceIntermediates, @NotNull final String methodName, @NotNull final TypeUsage returnTypeUsage, final boolean isDeprecatedInByteCode, final boolean isSynthetic, final boolean isStatic, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final ReferenceTypeName[] throwsArray, final boolean isVarArgs, final boolean isBridge, final boolean isSynchronized, @NotNull final BodyIntermediate bodyIntermediate, @NotNull final Completeness completeness, @NotNull final DefaultValueIntermediate annotationDefaultValueIntermediate)
	{
		super(intermediateCreator, origin, ourTypeReferenceName, visibility, annotationInstanceIntermediates, returnTypeUsage, isDeprecatedInByteCode, isSynthetic, isStatic, parameterIntermediates, throwsArray, isVarArgs, isBridge, isSynchronized, bodyIntermediate, completeness);
		this.methodName = methodName;
		this.annotationDefaultValueIntermediate = annotationDefaultValueIntermediate;
	}

	@NotNull
	public String methodName()
	{
		return methodName;
	}

	@NotNull
	@Override
	public MethodDetail resolve(@NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		final ParameterDetail[] parameterDetails = resolveParameterDetails(memberIntermediateRecorder, simpleTypeIntermediateRecorder);
		final KnownReferenceTypeName[] resolvedExceptionTypeReferenceNames = resolveExceptionReferenceTypeNames();
		final AnnotationInstanceDetail[] annotationInstanceDetails = resolveAnnotationInstanceIntermediates(annotations, memberIntermediateRecorder, simpleTypeIntermediateRecorder);
		final TypeUsage resolvedReturnTypeUsage = resolvedReturnTypeUsage();

		switch (intermediateCreator)
		{
			case Class:
			case Enum:
				final BodyDetail bodyDetail = bodyIntermediate.resolve();
				if (isStatic)
				{
					return new StaticClassMethodDetail(methodName, resolvedReturnTypeUsage, isDeprecatedInByteCode, isSynthetic, parameterDetails, resolvedExceptionTypeReferenceNames, isVarArgs, visibility, isBridge, isSynchronized, bodyDetail, annotationInstanceDetails);
				}
				else
				{
					return new InstanceClassMethodDetail(methodName, resolvedReturnTypeUsage, isDeprecatedInByteCode, isSynthetic, parameterDetails, resolvedExceptionTypeReferenceNames, isVarArgs, visibility, isBridge, isSynchronized, bodyDetail, completeness, annotationInstanceDetails);
				}

			case Interface:
				return new InstanceInterfaceMethodDetail<>(methodName, resolvedReturnTypeUsage, isDeprecatedInByteCode, isSynthetic, parameterDetails, resolvedExceptionTypeReferenceNames, isVarArgs, annotationInstanceDetails);

			case Annotation:
				final ValueDetail valueDetail = annotationDefaultValueIntermediate.resolve(resolvedReturnTypeUsage, simpleTypeIntermediateRecorder);
				return new AnnotationMethodDetail(methodName, resolvedReturnTypeUsage, isDeprecatedInByteCode, isSynthetic, annotationInstanceDetails, valueDetail);

			default:
				throw new IllegalStateException(StringConstants.Should_not_be_possible);
		}
	}

	@NotNull
	private ParameterDetail[] resolveParameterDetails(@NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		final int length = parameterIntermediates.length;
		if (length == 0)
		{
			return EmptyParameterDetails;
		}

		final ParameterDetail[] parameterDetails = new ParameterDetail[length];
		for (int index = 0; index < length; index++)
		{
			parameterDetails[index] = parameterIntermediates[index].resolve(memberIntermediateRecorder, simpleTypeIntermediateRecorder);
		}
		return parameterDetails;
	}

	@NotNull
	private KnownReferenceTypeName[] resolveExceptionReferenceTypeNames()
	{
		return resolveReferenceTypeNames(throwsArray);
	}

	public boolean isNamed(@NotNull final String name)
	{
		return methodName.equals(name);
	}

	@NotNull
	public TypeUsage resolvedReturnTypeUsage()
	{
		return returnTypeUsage.resolve();
	}
}
