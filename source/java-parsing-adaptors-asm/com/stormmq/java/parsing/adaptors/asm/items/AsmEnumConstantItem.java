package com.stormmq.java.parsing.adaptors.asm.items;

import com.stormmq.java.parsing.ast.details.valueDetails.EnumConstantSingleValueDetail;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.java.parsing.adaptors.asm.helpers.AsmHelper.knownReferenceTypeName;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class AsmEnumConstantItem
{
	@NotNull
	public static AsmEnumConstantItem asmEnumConstantItem(@NotNull final String enumTypeDescriptor, @NotNull final String value)
	{
		final KnownReferenceTypeName enumKnownReferenceTypeName = knownReferenceTypeName(enumTypeDescriptor);
		return new AsmEnumConstantItem(enumKnownReferenceTypeName, value);
	}

	@NotNull private final KnownReferenceTypeName enumName;
	@NotNull private final String constantName;

	public AsmEnumConstantItem(@NotNull final KnownReferenceTypeName enumName, @NotNull final String constantName)
	{
		this.enumName = enumName;
		this.constantName = constantName;
	}

	@NotNull
	public EnumConstantSingleValueDetail resolve()
	{
		return new EnumConstantSingleValueDetail(enumName, constantName);
	}

	@Override
	@NotNull
	public String toString()
	{
		return format(ENGLISH, "%1$s(%2$s, %3$s)", getClass().getSimpleName(), enumName, constantName);
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

		final AsmEnumConstantItem that = (AsmEnumConstantItem) o;

		if (!constantName.equals(that.constantName))
		{
			return false;
		}
		if (!enumName.equals(that.enumName))
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = enumName.hashCode();
		result = 31 * result + constantName.hashCode();
		return result;
	}
}
