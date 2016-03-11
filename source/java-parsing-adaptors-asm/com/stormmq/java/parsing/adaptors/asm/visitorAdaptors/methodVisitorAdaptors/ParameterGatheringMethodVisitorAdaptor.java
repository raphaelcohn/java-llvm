package com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.methodVisitorAdaptors;

import com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationItem;
import com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationItemUser;
import com.stormmq.java.parsing.adaptors.asm.items.AsmParameterItem;
import com.stormmq.java.parsing.adaptors.asm.items.AsmParameterItemsUser;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.AnnotationVisitorAdaptor;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.GatheringAsmAnnotationItemAnnotationVisitorAdaptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationItem.EmptyAsmAnnotationItems;
import static com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationItem.EmptyAsmAnnotationItemsArray;
import static com.stormmq.java.parsing.adaptors.asm.items.AsmParameterItem.EmptyAsmParameterItems;
import static com.stormmq.java.parsing.utilities.ReservedIdentifiers.validateIsJavaIdentifier;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class ParameterGatheringMethodVisitorAdaptor implements MethodVisitorAdaptor
{
	private static final int JavaLanguageSpecificationMaximumNumberOfParameters = 255;
	@NotNull private static final String[] InferredParameterNames = new String[JavaLanguageSpecificationMaximumNumberOfParameters];

	static
	{
		final int length = InferredParameterNames.length;
		for (int index = 0; index < length; index++)
		{
			// Matches java.lang.reflect.Executable.getParameters (see https://docs.oracle.com/javase/tutorial/reflect/member/methodparameterreflection.html)
			InferredParameterNames[index] = format(ENGLISH, "arg%03d", index);
		}
	}

	private final int maximumNumberOfParameters;
	private final Set<String> encounteredParameterNames;
	private final Set<String> usedInferredParameterNames;
	@NotNull private final AsmParameterItemsUser asmParameterItemsUser;
	@NotNull private final List<AsmAnnotationItem>[] asmParameterAnnotationItems;
	@NotNull private final AsmParameterItem[] asmParameterItems;
	private int index;

	public ParameterGatheringMethodVisitorAdaptor(final int maximumNumberOfParameters, @NotNull final AsmParameterItemsUser asmParameterItemsUser)
	{
		this.asmParameterItemsUser = asmParameterItemsUser;

		if (maximumNumberOfParameters > JavaLanguageSpecificationMaximumNumberOfParameters)
		{
			throw new IllegalStateException(format(ENGLISH, "There are more than the Java Language Specification number of parameters ('%1$s') defined for a method", JavaLanguageSpecificationMaximumNumberOfParameters));
		}

		this.maximumNumberOfParameters = maximumNumberOfParameters;
		encounteredParameterNames = new HashSet<>(maximumNumberOfParameters);
		usedInferredParameterNames = new HashSet<>(maximumNumberOfParameters);
		if (maximumNumberOfParameters == 0)
		{
			asmParameterAnnotationItems = EmptyAsmAnnotationItemsArray;
			asmParameterItems = EmptyAsmParameterItems;
		}
		else
		{
			//noinspection unchecked
			asmParameterAnnotationItems = new List[maximumNumberOfParameters];
			for (int index = 0; index < maximumNumberOfParameters; index++)
			{
				asmParameterAnnotationItems[index] = new ArrayList<>(1);
			}
			asmParameterItems = new AsmParameterItem[maximumNumberOfParameters];
		}
		index = 0;
	}

	@Nullable
	@Override
	public AnnotationVisitorAdaptor annotationOnParameter(final int parameter, @NotNull final String annotationTypeDescriptor, @NotNull final RetentionPolicy retentionPolicy)
	{
		if (parameter >= maximumNumberOfParameters)
		{
			throw new IllegalStateException(format(ENGLISH, "There are more than '%1$s' parameters defined for a method", maximumNumberOfParameters));
		}
		if (parameter < 0)
		{
			throw new IllegalStateException("parameter should not be negative");
		}
		return new GatheringAsmAnnotationItemAnnotationVisitorAdaptor(annotationTypeDescriptor, retentionPolicy, new AsmAnnotationItemUser()
		{
			@Override
			public void use(@NotNull final AsmAnnotationItem asmAnnotationItem)
			{
				asmParameterAnnotationItems[index].add(asmAnnotationItem);
			}
		});
	}

	@Override
	public void parameter(@Nullable final String parameterName, final int modifiers)
	{
		if (index >= maximumNumberOfParameters)
		{
			throw new IllegalStateException(format(ENGLISH, "There are more than '%1$s' parameters defined for a method", maximumNumberOfParameters));
		}

		final String parameterNameX;
		final boolean isInferredParameterName;
		if (parameterName == null)
		{
			final String inferredParameterName = InferredParameterNames[index];
			if (encounteredParameterNames.contains(inferredParameterName))
			{
				throw new IllegalStateException(format(ENGLISH, "The inferred parameter '%1$s' at index '%2$s' is identical to a parameter we already encountered", inferredParameterName, index));
			}
			usedInferredParameterNames.add(inferredParameterName);

			parameterNameX = inferredParameterName;
			isInferredParameterName = true;
		}
		else
		{
			validateIsJavaIdentifier(parameterName, false);
			if (usedInferredParameterNames.contains(parameterName))
			{
				throw new IllegalStateException(format(ENGLISH, "The parameter '%1$s' at index '%2$s' is identical to a parameter we inferred the name of", parameterName, index));
			}
			if (encounteredParameterNames.contains(parameterName))
			{
				throw new IllegalArgumentException(format(ENGLISH, "The parameter '%1$s' at index '%2$s' reuses the name of an earlier parameter", parameterName, index));
			}
			encounteredParameterNames.add(parameterName);

			parameterNameX = parameterName;
			isInferredParameterName = false;
		}

		final List<AsmAnnotationItem> asmAnnotationItems = asmParameterAnnotationItems[index];
		asmParameterItems[index] = new AsmParameterItem(parameterNameX, modifiers, isInferredParameterName, asmAnnotationItems.isEmpty() ? EmptyAsmAnnotationItems : asmAnnotationItems);
		index++;
	}

	@Override
	public void end()
	{
		// parameter() may not be called for parameters (? no name and access recorded ?) !!!
		final int length = asmParameterItems.length;
		for (index = 0; index < length; index++)
		{
			if (asmParameterItems[index] == null)
			{
				final int indexToRestore = index;
				parameter(null, 0);
				index = indexToRestore;
			}
		}

		asmParameterItemsUser.use(asmParameterItems);
	}
}
