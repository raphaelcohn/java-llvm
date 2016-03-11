package com.stormmq.java.parsing.adaptors.asm.items;

import com.stormmq.java.parsing.adaptors.asm.ast.AsmModifiers;
import com.stormmq.java.parsing.ast.intermediates.ParameterIntermediate;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.stormmq.java.parsing.adaptors.asm.ast.AsmModifiers.asmModifiersParameters;
import static com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationItem.parseAnnotationInstanceDetails;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class AsmParameterItem
{
	@NotNull public static final AsmParameterItem[] EmptyAsmParameterItems = {};

	@NotNull private final String parameterName;
	private final boolean isInferredParameterName;
	@NotNull private final List<AsmAnnotationItem> asmAnnotationItems;
	@NotNull private final AsmModifiers asmModifiers;

	public AsmParameterItem(@NotNull final String parameterName, final int access, final boolean isInferredParameterName, @NotNull final List<AsmAnnotationItem> asmAnnotationItems)
	{
		this.parameterName = parameterName;
		this.isInferredParameterName = isInferredParameterName;
		this.asmAnnotationItems = asmAnnotationItems;

		asmModifiers = asmModifiersParameters(access);
	}

	@NotNull
	public ParameterIntermediate toParameterDetails(@NotNull final TypeUsage typeUsage)
	{
		final boolean isFinal = asmModifiers.isFinal();
		final boolean isSynthetic = asmModifiers.isSynthetic();
		final boolean isMandated = asmModifiers.isMandated();

		return new ParameterIntermediate(parameterName, typeUsage, isFinal, isInferredParameterName, isSynthetic, isMandated, parseAnnotationInstanceDetails(asmAnnotationItems));
	}

	@Override
	@NotNull
	public String toString()
	{
		return format(ENGLISH, "%1$s(%2$s, %3$s, %4$s, %5$s)", getClass().getSimpleName(), parameterName, isInferredParameterName, asmAnnotationItems, asmModifiers);
	}

	@Override
	public boolean equals(@Nullable final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		final AsmParameterItem that = (AsmParameterItem) o;

		if (isInferredParameterName != that.isInferredParameterName)
		{
			return false;
		}
		if (!asmAnnotationItems.equals(that.asmAnnotationItems))
		{
			return false;
		}
		if (!asmModifiers.equals(that.asmModifiers))
		{
			return false;
		}
		if (!parameterName.equals(that.parameterName))
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = parameterName.hashCode();
		result = 31 * result + (isInferredParameterName ? 1 : 0);
		result = 31 * result + asmAnnotationItems.hashCode();
		result = 31 * result + asmModifiers.hashCode();
		return result;
	}
}
