package com.stormmq.java.parsing.adaptors.asm.items;

import com.stormmq.java.parsing.adaptors.asm.ast.AsmBodyIntermediate;
import com.stormmq.java.parsing.adaptors.asm.ast.AsmDefaultValueIntermediate;
import com.stormmq.java.parsing.adaptors.asm.ast.AsmModifiers;
import com.stormmq.java.parsing.ast.intermediateRecorders.MethodIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.*;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.BodyIntermediate;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.ast.modifiers.Modifiers;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.stormmq.java.parsing.adaptors.asm.ast.AsmModifiers.asmModifiersMethod;
import static com.stormmq.java.parsing.adaptors.asm.helpers.AsmHelper.*;
import static com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationItem.parseAnnotationInstanceDetails;
import static com.stormmq.java.parsing.adaptors.asm.items.AsmParameterItem.EmptyAsmParameterItems;
import static com.stormmq.java.parsing.ast.intermediates.MethodIntermediate.*;
import static com.stormmq.java.parsing.ast.intermediates.Origin.Class;
import static com.stormmq.java.parsing.ast.intermediates.ParameterIntermediate.EmptyParameterIntermediates;
import static com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.EmptyBodyIntermediate.Abstract;
import static com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.EmptyBodyIntermediate.Native;
import static com.stormmq.java.parsing.utilities.ReservedIdentifiers.validateIsJavaIdentifier;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Locale.ENGLISH;

public final class AsmMethodItem
{
	@NotNull public static final List<AsmMethodItem> EmptyAsmMethodItems = emptyList();

	private final int modifiers;
	@NotNull private final String name;
	@NotNull private final String methodDescriptor;
	@Nullable private final String signature;
	@NotNull private final String[] exceptions;
	@NotNull private final List<AsmAnnotationItem> asmAnnotationItems;
	@NotNull private AsmParameterItem[] asmParameterItems;
	@NotNull private DefaultValueIntermediate annotationDefaultValueIntermediate;

	public AsmMethodItem(final int modifiers, @NotNull final String name, @NotNull final String methodDescriptor, @Nullable final String signature, @NotNull final String[] exceptions, @NotNull final List<AsmAnnotationItem> asmAnnotationItems, @NotNull final DefaultValueIntermediate initialDefaultValueIntermediate)
	{
		validateIsJavaIdentifier(name, true);

		this.modifiers = modifiers;
		this.name = name;
		this.methodDescriptor = methodDescriptor;
		this.signature = signature;
		this.exceptions = exceptions;

		this.asmParameterItems = EmptyAsmParameterItems;
		this.asmAnnotationItems = asmAnnotationItems;
		annotationDefaultValueIntermediate = initialDefaultValueIntermediate;
	}

	public void setAsmParameterItems(@NotNull final AsmParameterItem... asmParameterItems)
	{
		this.asmParameterItems = asmParameterItems;
	}

	public void toMethodIntermediate(@NotNull final IntermediateCreator intermediateCreator, @NotNull final MethodIntermediateRecorder methodIntermediateRecorder, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName)
	{
		final AsmModifiers asmModifiers = asmModifiersMethod(modifiers, intermediateCreator);

		final TypeUsage returnTypeUsage = findReturnTypeUsageFromMethodDescriptor(methodDescriptor);

		final TypeUsage[] parameterTypeUsages = findParameterTypeUsagesFromMethodDescriptor(methodDescriptor);

		final KnownReferenceTypeName[] throwsArray = exceptionInternalNamesToReferenceTypeNames(exceptions);

		final AnnotationInstanceIntermediate[] annotations = parseAnnotationInstanceDetails(asmAnnotationItems);

		final boolean isVarArgs = asmModifiers.isVarArgs();

		final BodyIntermediate bodyIntermediate = createBodyDetails(asmModifiers);

		final int length = asmParameterItems.length;
		final ParameterIntermediate[] parameterIntermediates;
		if (length == 0)
		{
			parameterIntermediates = EmptyParameterIntermediates;
		}
		else
		{
			//noinspection unchecked
			parameterIntermediates = new ParameterIntermediate[length];
			for (int index = 0; index < length; index++)
			{
				final TypeUsage parameterTypeUsage = parameterTypeUsages[index];
				final AsmParameterItem asmParameterItem = asmParameterItems[index];
				parameterIntermediates[index] = asmParameterItem.toParameterDetails(parameterTypeUsage);
			}
		}

		final boolean isDeprecatedInByteCode = asmModifiers.isDeprecatedInByteCode();
		final boolean isSynthetic = asmModifiers.isSynthetic();
		final boolean isStatic = asmModifiers.isStatic();
		final Visibility visibility = asmModifiers.visibility();
		final boolean isNative = asmModifiers.isNative();
		final Completeness completeness = asmModifiers.classOrMethodCompleteness();
		final boolean isSynchronized = asmModifiers.isSynchronized();

		final MethodIntermediate methodIntermediate;
		final boolean isBridge = asmModifiers.isBridge();
		switch (intermediateCreator)
		{
			case Annotation:
				asmModifiers.validateAsAnnotationMethodModifiers();

				if (parameterIntermediates.length != 0)
				{
					throw new IllegalStateException("There are parameters for an annotation member");
				}

				if (!bodyIntermediate.isAbstract())
				{
					throw new IllegalStateException("There is a body for an annotation member");
				}

				if (isVarArgs)
				{
					throw new IllegalStateException("There is varargs for an annotation member");
				}

				if (isNative)
				{
					throw new IllegalStateException("There is native for an annotation member");
				}

				if (throwsArray.length != 0)
				{
					throw new IllegalStateException("There is throws for an annotation member");
				}

				if (returnTypeUsage.isMultiDimensionalArray())
				{
					throw new IllegalStateException("There is a multi-dimensional return type for an annotation member");
				}

				methodIntermediate = newAnnotationMethodIntermediate(name, returnTypeUsage, isDeprecatedInByteCode, isSynthetic, throwsArray, annotations, annotationDefaultValueIntermediate, Class, ourKnownReferenceTypeName);
				break;

			case Interface:
				if (isNative)
				{
					throw new IllegalStateException("There is native for an interface method");
				}

				asmModifiers.validateAsInterfaceMethodModifiers();
				if (isStatic)
				{
					methodIntermediate = newInterfaceStaticMethodIntermediate(name, returnTypeUsage, parameterIntermediates, isDeprecatedInByteCode, isSynthetic, throwsArray, isVarArgs, bodyIntermediate, annotations, Class, ourKnownReferenceTypeName);
				}
				else
				{
					if (bodyIntermediate.isConcrete())
					{
						methodIntermediate = newInterfaceInstanceMethodIntermediate(name, returnTypeUsage, parameterIntermediates, isDeprecatedInByteCode, isSynthetic, throwsArray, isVarArgs, annotations, Class, ourKnownReferenceTypeName);
					}
					else
					{
						methodIntermediate = newInterfaceDefaultMethodIntermediate(name, returnTypeUsage, parameterIntermediates, isDeprecatedInByteCode, isSynthetic, throwsArray, isVarArgs, bodyIntermediate, annotations, Class, ourKnownReferenceTypeName);
					}
				}
				break;

			case Class:
				asmModifiers.validateAsClassMethodModifiers();
				if (isStatic)
				{
					methodIntermediate = newClassStaticMethodIntermediate(name, returnTypeUsage, parameterIntermediates, isDeprecatedInByteCode, isSynthetic, throwsArray, isVarArgs, bodyIntermediate, annotations, Class, ourKnownReferenceTypeName, visibility, isBridge, isSynchronized, isNative);
				}
				else
				{
					methodIntermediate = newClassInstanceMethodIntermediate(name, returnTypeUsage, parameterIntermediates, isDeprecatedInByteCode, isSynthetic, throwsArray, isVarArgs, bodyIntermediate, annotations, Class, ourKnownReferenceTypeName, visibility, completeness, isBridge, isSynchronized, isNative);
				}
				break;

			case Enum:
				asmModifiers.validateAsEnumMethodModifiers();
				if (isStatic)
				{
					methodIntermediate = newEnumStaticMethodIntermediate(name, returnTypeUsage, parameterIntermediates, isDeprecatedInByteCode, isSynthetic, throwsArray, isVarArgs, bodyIntermediate, annotations, Class, ourKnownReferenceTypeName, visibility, isBridge, isSynchronized, isNative);
				}
				else
				{
					methodIntermediate = newEnumInstanceMethodIntermediate(name, returnTypeUsage, parameterIntermediates, isDeprecatedInByteCode, isSynthetic, throwsArray, isVarArgs, bodyIntermediate, annotations, Class, ourKnownReferenceTypeName, visibility, completeness, isBridge, isSynchronized, isNative);
				}
				break;

			default:
				throw new IllegalStateException(com.stormmq.java.parsing.utilities.StringConstants.Should_not_be_possible);
		}

		methodIntermediateRecorder.record(ourKnownReferenceTypeName, methodIntermediate);
	}

	@NotNull
	private static BodyIntermediate createBodyDetails(@NotNull final Modifiers modifiers)
	{
		if (modifiers.isAbstract())
		{
			return Abstract;
		}

		if (modifiers.isNative())
		{
			return Native;
		}

		return new AsmBodyIntermediate();
	}

	@Override
	@NotNull
	public String toString()
	{
		return format(ENGLISH, "%1$s(%2$s, %3$s, %4$s)", getClass().getSimpleName(), name, modifiers, methodDescriptor);
	}

	public void setAnnotationDefaultValueIntermediate(@NotNull final Object value)
	{
		annotationDefaultValueIntermediate = new AsmDefaultValueIntermediate(value);
	}
}
