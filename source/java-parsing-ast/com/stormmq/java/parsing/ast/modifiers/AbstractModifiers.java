package com.stormmq.java.parsing.ast.modifiers;

import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.Visibility;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.java.parsing.utilities.Completeness.*;
import static com.stormmq.java.parsing.utilities.Visibility.*;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public abstract class AbstractModifiers implements Modifiers
{
	protected final int modifiers;

	protected AbstractModifiers(final int modifiers)
	{
		this.modifiers = modifiers;
	}

	protected static int invalidModifiersMask(final int... validModifiers)
	{
		int combination = 0;
		for (int validModifier : validModifiers)
		{
			combination = combination | validModifier;
		}
		return ~combination;
	}

	protected final void validateModifiers(final int invalidModifiers)
	{
		if ((modifiers & invalidModifiers) != 0)
		{
			throw new IllegalStateException("Not valid modifiers");
		}
	}

	@Override
	public void validateAsClassModifiers()
	{
		if (isFinal() && isAbstract())
		{
			throw new IllegalStateException("Not valid for a class to be both final and abstract");
		}
	}

	@Override
	public void validateAsClassFieldModifiers()
	{
		if (isFinal() && isVolatile())
		{
			throw new IllegalArgumentException("class field modifiers can not be both final and volatile");
		}
	}

	@Override
	public void validateAsEnumFieldModifiers()
	{
		if (isFinal() && isVolatile())
		{
			throw new IllegalArgumentException("enum field modifiers can not be both final and volatile");
		}
	}

	@Override
	public void validateAsClassMethodModifiers()
	{
		final boolean isAbstract = isAbstract();
		if (isAbstract)
		{
			if (isFinal())
			{
				throw new IllegalStateException("Class method can not be abstract and final");
			}

			if (isStatic())
			{
				throw new IllegalArgumentException("Class method can not be abstract and static");
			}

			if (isNative())
			{
				throw new IllegalArgumentException("Class method can not be abstract and native");
			}
		}
	}

	@Override
	public void validateAsEnumMethodModifiers()
	{
		final boolean isAbstract = isAbstract();
		if (isAbstract)
		{
			if (isFinal())
			{
				throw new IllegalStateException("Enum method can not be abstract and final");
			}

			if (isStatic())
			{
				throw new IllegalArgumentException("Enum method can not be abstract and static");
			}

			if (isNative())
			{
				throw new IllegalArgumentException("Enum method can not be abstract and native");
			}
		}
	}

	@NotNull
	@Override
	public final Visibility visibility()
	{
		final boolean isPublic = isPublic();
		final boolean isProtected = isProtected();
		final boolean isPrivate = isPrivate();

		if (isPublic)
		{
			if (isProtected || isPrivate)
			{
				throw new IllegalArgumentException("Visibility can not be public and either protected or private or both");
			}
			return Public;
		}

		if (isProtected)
		{
			if (isPrivate)
			{
				throw new IllegalArgumentException("Visibility can not be protected and either public or private or both");
			}
			return Protected;
		}

		if (isPrivate)
		{
			return Private;
		}

		return PackageLocal;
	}

	@Override
	@NotNull
	public final Completeness classOrMethodCompleteness()
	{
		final boolean isAbstract = isAbstract();
		final boolean isFinal = isFinal();

		if (isFinal)
		{
			if (isAbstract)
			{
				throw new IllegalArgumentException("Class or method completeness can not be both final and abstract");
			}
			return Final;
		}

		if (isAbstract)
		{
			if (isFinal)
			{
				throw new IllegalArgumentException("Class or method completeness can not be both abstract and final");
			}
			return Abstract;
		}

		return Normal;
	}

	@Override
	public final boolean isValidStaticField()
	{
		//noinspection SimplifiableIfStatement
		if (isInstance())
		{
			return false;
		}
		return isValidField();
	}

	@Override
	public final boolean isValidStaticMethod()
	{
		//noinspection SimplifiableIfStatement
		if (isInstance())
		{
			return false;
		}
		return isValidMethod();
	}

	@Override
	public final boolean isValidField()
	{
		if (isAbstract() || isBridge() || isMandated() || isNative() || isStrictFloatingPoint() || isSynchronized() || isAdditionalChecks())
		{
			return false;
		}
		return true;
	}

	@Override
	public final boolean isValidMethod()
	{
		if (isBridge() || isMandated() || isSynchronized() || isAdditionalChecks())
		{
			return false;
		}
		return true;
	}

	protected abstract boolean isAdditionalChecks();

	@Override
	@NotNull
	public final String toString()
	{
		return format(ENGLISH, "%1$s(%2$s)", getClass().getSimpleName(), modifiers);
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

		final AbstractModifiers that = (AbstractModifiers) o;

		if (modifiers != that.modifiers)
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		return modifiers;
	}
}
