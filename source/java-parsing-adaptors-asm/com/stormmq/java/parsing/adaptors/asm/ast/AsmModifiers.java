package com.stormmq.java.parsing.adaptors.asm.ast;

import com.stormmq.java.parsing.ast.intermediates.IntermediateCreator;
import com.stormmq.java.parsing.ast.modifiers.AbstractModifiers;
import com.stormmq.java.parsing.utilities.FieldFinality;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.ast.intermediates.IntermediateCreator.*;
import static com.stormmq.java.parsing.ast.intermediates.IntermediateCreator.Class;
import static com.stormmq.java.parsing.ast.intermediates.IntermediateCreator.Enum;
import static org.objectweb.asm.Opcodes.*;

public final class AsmModifiers extends AbstractModifiers
{
	// Duplicate of package-private value in org.objectweb.asm.ClassWriter
	private static final int ACC_SYNTHETIC_ATTRIBUTE = 0x40000;

	// What does ACC_SUPER mean?
	private static final int InvalidClassModifiers = invalidModifiersMask(ACC_DEPRECATED, ACC_SYNTHETIC, ACC_SYNTHETIC_ATTRIBUTE, ACC_PUBLIC, ACC_PROTECTED, ACC_PRIVATE, ACC_ABSTRACT, ACC_STATIC, ACC_FINAL, ACC_STRICT, ACC_SUPER);
	private static final int InvalidInterfaceModifiers = invalidModifiersMask(ACC_DEPRECATED, ACC_SYNTHETIC, ACC_SYNTHETIC_ATTRIBUTE, ACC_PUBLIC, ACC_PROTECTED, ACC_PRIVATE, ACC_ABSTRACT, ACC_STATIC, ACC_STRICT, ACC_INTERFACE);
	private static final int InvalidAnnotationModifiers = invalidModifiersMask(ACC_DEPRECATED, ACC_SYNTHETIC, ACC_SYNTHETIC_ATTRIBUTE, ACC_PUBLIC, ACC_PROTECTED, ACC_PRIVATE, ACC_STATIC, ACC_STRICT, ACC_ANNOTATION, ACC_INTERFACE, ACC_ABSTRACT);
	private static final int InvalidEnumModifiers = invalidModifiersMask(ACC_DEPRECATED, ACC_SYNTHETIC, ACC_SYNTHETIC_ATTRIBUTE, ACC_PUBLIC, ACC_PROTECTED, ACC_PRIVATE, ACC_STATIC, ACC_STRICT, ACC_ENUM, ACC_FINAL, ACC_SUPER, ACC_ABSTRACT);

	private static final int InvalidClassFieldModifiers = invalidModifiersMask(ACC_DEPRECATED, ACC_SYNTHETIC, ACC_SYNTHETIC_ATTRIBUTE, ACC_PUBLIC, ACC_PROTECTED, ACC_PRIVATE, ACC_STATIC, ACC_FINAL, ACC_TRANSIENT, ACC_VOLATILE);
	private static final int InvalidInterfaceFieldModifiers = invalidModifiersMask(ACC_DEPRECATED, ACC_SYNTHETIC, ACC_SYNTHETIC_ATTRIBUTE, ACC_PUBLIC, ACC_STATIC, ACC_FINAL);
	private static final int InvalidAnnotationFieldModifiers = invalidModifiersMask(ACC_DEPRECATED, ACC_SYNTHETIC, ACC_SYNTHETIC_ATTRIBUTE, ACC_PUBLIC, ACC_STATIC, ACC_FINAL);
	private static final int InvalidEnumFieldModifiers = invalidModifiersMask(ACC_DEPRECATED, ACC_SYNTHETIC, ACC_SYNTHETIC_ATTRIBUTE, ACC_PUBLIC, ACC_PROTECTED, ACC_PRIVATE, ACC_STATIC, ACC_FINAL, ACC_TRANSIENT, ACC_VOLATILE, ACC_ENUM); // ACC_ENUM is set just for enum constants

	private static final int InvalidClassMethodModifiers = invalidModifiersMask(ACC_DEPRECATED, ACC_SYNTHETIC, ACC_SYNTHETIC_ATTRIBUTE, ACC_PUBLIC, ACC_PROTECTED, ACC_PRIVATE, ACC_STATIC, ACC_FINAL, ACC_STRICT, ACC_ABSTRACT, ACC_BRIDGE, ACC_VARARGS, ACC_SYNCHRONIZED, ACC_NATIVE);
	private static final int InvalidInterfaceMethodModifiers = invalidModifiersMask(ACC_DEPRECATED, ACC_SYNTHETIC, ACC_SYNTHETIC_ATTRIBUTE, ACC_PUBLIC, ACC_ABSTRACT, ACC_VARARGS, ACC_STATIC);
	private static final int InvalidAnnotationMethodModifiers = invalidModifiersMask(ACC_DEPRECATED, ACC_SYNTHETIC, ACC_SYNTHETIC_ATTRIBUTE, ACC_PUBLIC, ACC_ABSTRACT);
	private static final int InvalidEnumMethodModifiers = invalidModifiersMask(ACC_DEPRECATED, ACC_SYNTHETIC, ACC_SYNTHETIC_ATTRIBUTE, ACC_PUBLIC, ACC_PROTECTED, ACC_PRIVATE, ACC_STATIC, ACC_FINAL, ACC_STRICT, ACC_ABSTRACT, ACC_BRIDGE, ACC_VARARGS, ACC_SYNCHRONIZED);

	private static final int InvalidParameterModifiers = invalidModifiersMask(ACC_FINAL | ACC_SYNTHETIC | ACC_MANDATED);

	private static final int InterfaceAndAnnotationFieldAccessAdjustment = ACC_PUBLIC | ACC_STATIC | ACC_FINAL;
	private static final int InterfaceAndAnnotationMethodAccessAdjustment = ACC_PUBLIC | ACC_ABSTRACT;

	@NotNull
	public static AsmModifiers asmModifiersType(final int access)
	{
		return new AsmModifiers(access);
	}

	@NotNull
	public static AsmModifiers asmModifiersField(final int access, @NotNull final IntermediateCreator intermediateCreator)
	{
		final int adjustment = intermediateCreator.isInterfaceLike ? InterfaceAndAnnotationFieldAccessAdjustment : 0;
		return new AsmModifiers(access | adjustment);
	}

	@NotNull
	public static AsmModifiers asmModifiersMethod(final int access, @NotNull final IntermediateCreator intermediateCreator)
	{
		final int adjustment = intermediateCreator.isInterfaceLike ? InterfaceAndAnnotationMethodAccessAdjustment : ((ACC_STATIC & access) == ACC_STATIC) ? ACC_FINAL : 0;
		return new AsmModifiers(access | adjustment);
	}

	@NotNull
	public static AsmModifiers asmModifiersParameters(final int access)
	{
		final AsmModifiers asmModifiers = new AsmModifiers(access);
		asmModifiers.validateAsParameterModifiers();
		return asmModifiers;
	}

	private AsmModifiers(final int access)
	{
		super(access);
	}

	@NotNull
	public IntermediateCreator kindType()
	{
		final boolean isInterface = isInterface();
		final boolean isEnum = isEnum();
		final boolean isAnnotation = isAnnotation();

		if (isAnnotation)
		{
			if (isEnum)
			{
				throw new IllegalArgumentException("Type can not be annotation and enum");
			}
			if (!isInterface)
			{
				throw new IllegalArgumentException("Type can not be annotation and not an interface");
			}
			return Annotation;
		}

		if (isInterface)
		{
			if (isEnum)
			{
				throw new IllegalArgumentException("Type can not be interface and enum");
			}
			return Interface;
		}

		if (isEnum)
		{
			return Enum;
		}

		return Class;
	}

	@Override
	public void validateAsClassModifiers()
	{
		validateModifiers(InvalidClassModifiers);
		super.validateAsClassModifiers();
	}

	@Override
	public void validateAsInterfaceModifiers()
	{
		validateModifiers(InvalidInterfaceModifiers);
	}

	@Override
	public void validateAsAnnotationModifiers()
	{
		validateModifiers(InvalidAnnotationModifiers);
	}

	@Override
	public void validateAsEnumModifiers()
	{
		validateModifiers(InvalidEnumModifiers);
	}

	@Override
	public void validateAsClassFieldModifiers()
	{
		validateModifiers(InvalidClassFieldModifiers);
		super.validateAsClassFieldModifiers();
	}

	@Override
	public void validateAsInterfaceFieldModifiers()
	{
		validateModifiers(InvalidInterfaceFieldModifiers);
	}

	@Override
	public void validateAsAnnotationFieldModifiers()
	{
		validateModifiers(InvalidAnnotationFieldModifiers);
	}

	@Override
	public void validateAsEnumFieldModifiers()
	{
		validateModifiers(InvalidEnumFieldModifiers);
		super.validateAsEnumFieldModifiers();
	}

	@Override
	public void validateAsClassMethodModifiers()
	{
		validateModifiers(InvalidClassMethodModifiers);
		super.validateAsClassMethodModifiers();
	}

	@Override
	public void validateAsInterfaceMethodModifiers()
	{
		validateModifiers(InvalidInterfaceMethodModifiers);
	}

	@Override
	public void validateAsAnnotationMethodModifiers()
	{
		validateModifiers(InvalidAnnotationMethodModifiers);
	}

	@Override
	public void validateAsEnumMethodModifiers()
	{
		validateModifiers(InvalidEnumMethodModifiers);
		super.validateAsEnumMethodModifiers();
	}

	@Override
	public void validateAsParameterModifiers()
	{
		validateModifiers(InvalidParameterModifiers);
	}

	@Override
	protected final boolean isAdditionalChecks()
	{
		return isAnnotation() || isInterface() || isEnum() || isSuper() || isVarArgs();
	}

	public boolean isAnnotation()
	{
		return is(ACC_ANNOTATION);
	}

	public boolean isEnum()
	{
		return is(ACC_ENUM);
	}

	public boolean isInterface()
	{
		return is(ACC_INTERFACE);
	}

	public boolean isSuper()
	{
		return is(ACC_SUPER);
	}

	public boolean isVarArgs()
	{
		return is(ACC_VARARGS);
	}

	@Override
	public boolean isInstance()
	{
		return isNot(ACC_STATIC);
	}

	@Override
	public boolean isAbstract()
	{
		return is(ACC_ABSTRACT);
	}

	@Override
	public boolean isBridge()
	{
		return is(ACC_BRIDGE);
	}

	@Override
	public boolean isDeprecatedInByteCode()
	{
		return is(ACC_DEPRECATED);
	}

	@Override
	public boolean isFinal()
	{
		return is(ACC_FINAL);
	}

	@Override
	public boolean isMandated()
	{
		return is(ACC_MANDATED);
	}

	@Override
	public boolean isNative()
	{
		return is(ACC_NATIVE);
	}

	@Override
	public boolean isPrivate()
	{
		return is(ACC_PRIVATE);
	}

	@Override
	public boolean isProtected()
	{
		return is(ACC_PROTECTED);
	}

	@Override
	public boolean isPublic()
	{
		return is(ACC_PUBLIC);
	}

	@Override
	public boolean isStatic()
	{
		return is(ACC_STATIC);
	}

	@Override
	public boolean isStrictFloatingPoint()
	{
		return is(ACC_STRICT);
	}

	@Override
	public boolean isSynchronized()
	{
		return is(ACC_SYNCHRONIZED);
	}

	@Override
	public boolean isSynthetic()
	{
		return is(ACC_SYNTHETIC);
	}

	@Override
	public boolean isTransient()
	{
		return is(ACC_TRANSIENT);
	}

	@Override
	public boolean isVolatile()
	{
		return is(ACC_VOLATILE);
	}

	private boolean isNot(final int accessOpcode)
	{
		return (modifiers & accessOpcode) != accessOpcode;
	}

	private boolean is(final int accessOpcode)
	{
		return (modifiers & accessOpcode) == accessOpcode;
	}

	@NotNull
	public AsmModifiers removeEnum()
	{
		return new AsmModifiers(modifiers & ~ACC_ENUM);
	}

	@NotNull
	public FieldFinality fieldFinality()
	{
		if (isVolatile())
		{
			if (isFinal())
			{
				throw new IllegalStateException(StringConstants.A_field_can_not_be_both_volatile_and_final);
			}
			return FieldFinality.Volatile;
		}
		if (isFinal())
		{
			return FieldFinality.Final;
		}
		return FieldFinality.Normal;
	}
}
