package com.stormmq.java.parsing.adaptors.javaparser.ast;

import com.github.javaparser.ast.body.*;
import com.stormmq.java.parsing.ast.modifiers.AbstractModifiers;
import org.jetbrains.annotations.NotNull;

import static com.github.javaparser.ast.body.ModifierSet.*;

public final class JavaparserModifiers extends AbstractModifiers
{
	private static final int InvalidClassModifiers = invalidModifiersMask(PUBLIC, PROTECTED, PRIVATE, ABSTRACT, STATIC, FINAL, STRICTFP);
	private static final int InvalidInterfaceModifiers = invalidModifiersMask(PUBLIC, PROTECTED, PRIVATE, ABSTRACT, STATIC, STRICTFP);
	private static final int InvalidAnnotationModifiers = invalidModifiersMask(PUBLIC, PROTECTED, PRIVATE, STATIC, STRICTFP);
	private static final int InvalidEnumModifiers = invalidModifiersMask(PUBLIC, PROTECTED, PRIVATE, STATIC, STRICTFP);

	private static final int InvalidClassFieldModifiers = invalidModifiersMask(PUBLIC, PROTECTED, PRIVATE, STATIC, FINAL, TRANSIENT, VOLATILE);
	private static final int InvalidInterfaceFieldModifiers = invalidModifiersMask(PUBLIC, STATIC, FINAL);
	private static final int InvalidAnnotationFieldModifiers = invalidModifiersMask(PUBLIC, STATIC, FINAL);
	private static final int InvalidEnumFieldModifiers = invalidModifiersMask(PUBLIC, PROTECTED, PRIVATE, STATIC, FINAL, TRANSIENT, VOLATILE);

	private static final int InvalidClassMethodModifiers = invalidModifiersMask(PUBLIC, PROTECTED, PRIVATE, STATIC, FINAL, STRICTFP, ABSTRACT, SYNCHRONIZED, NATIVE);
	private static final int InvalidInterfaceMethodModifiers = invalidModifiersMask(PUBLIC, ABSTRACT);
	private static final int InvalidAnnotationMethodModifiers = invalidModifiersMask(PUBLIC, ABSTRACT);
	private static final int InvalidEnumMethodModifiers = invalidModifiersMask(PUBLIC, PROTECTED, PRIVATE, STATIC, FINAL, STRICTFP, ABSTRACT, SYNCHRONIZED, NATIVE);

	private static final int InvalidParameterModifiers = invalidModifiersMask(FINAL);

	private static final int InterfaceAndAnnotationFieldAccessAdjustment = PUBLIC | STATIC | FINAL;
	private static final int InterfaceAndAnnotationMethodAccessAdjustment = PUBLIC | ABSTRACT;

	@NotNull public static final JavaparserModifiers EnumConstantModifiers = new JavaparserModifiers(PUBLIC | STATIC | FINAL);
	@NotNull public static final JavaparserModifiers EnumClassConstantModifiers = new JavaparserModifiers(PRIVATE | STATIC | FINAL);
	@NotNull public static final JavaparserModifiers PublicStaticMethodModifiers = new JavaparserModifiers(PUBLIC | STATIC);
	@NotNull public static final JavaparserModifiers FinalParameterModifiers = new JavaparserModifiers(FINAL);
	@NotNull public static final JavaparserModifiers PublicStaticFinalFieldModifiers = new JavaparserModifiers(PUBLIC | STATIC | FINAL);
	@NotNull public static final JavaparserModifiers PrivateConstructorModifiers = new JavaparserModifiers(PRIVATE);

	@NotNull
	public static JavaparserModifiers javaparserModifiersField(@NotNull final FieldDeclaration fieldDeclaration, @NotNull final IntermediateCreator intermediateCreator)
	{
		final int adjustment = intermediateCreator.isInterfaceLike ? InterfaceAndAnnotationFieldAccessAdjustment : 0;
		final int modifiers = fieldDeclaration.getModifiers();
		return new JavaparserModifiers(modifiers | adjustment);
	}

	@NotNull
	public static JavaparserModifiers javaparserModifiersMethod(@NotNull final MethodDeclaration methodDeclaration, @NotNull final IntermediateCreator intermediateCreator)
	{
		final int modifiers = methodDeclaration.getModifiers();
		final int staticAdjustment = ((modifiers & STATIC) == STATIC) ? FINAL : 0;
		return javaparserModifiersMethodInternal(modifiers | staticAdjustment, intermediateCreator);
	}

	@NotNull
	public static JavaparserModifiers javaparserModifiersConstructor(@NotNull final ConstructorDeclaration constructorDeclaration, @NotNull final IntermediateCreator intermediateCreator)
	{
		final int modifiers = constructorDeclaration.getModifiers();
		return javaparserModifiersMethodInternal(modifiers, intermediateCreator);
	}

	@NotNull
	public static JavaparserModifiers javaparserModifiersMethod(@NotNull final AnnotationMemberDeclaration annotationMemberDeclaration, @NotNull final IntermediateCreator intermediateCreator)
	{
		final int modifiers = annotationMemberDeclaration.getModifiers();
		return javaparserModifiersMethodInternal(modifiers, intermediateCreator);
	}

	@NotNull
	private static JavaparserModifiers javaparserModifiersMethodInternal(final int modifiers, @NotNull final IntermediateCreator intermediateCreator)
	{
		final int adjustment = intermediateCreator.isInterfaceLike ? InterfaceAndAnnotationMethodAccessAdjustment : 0;
		return new JavaparserModifiers(modifiers | adjustment);
	}

	public static JavaparserModifiers javaparserModifiersParameter(@NotNull final Parameter parameter)
	{
		final JavaparserModifiers modifiers = new JavaparserModifiers(parameter.getModifiers());
		modifiers.validateAsParameterModifiers();
		return modifiers;
	}

	public JavaparserModifiers(final int modifiers)
	{
		super(modifiers);
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
	protected boolean isAdditionalChecks()
	{
		return false;
	}

	@Override
	public boolean isInstance()
	{
		return !isStatic();
	}

	@Override
	public boolean isAbstract()
	{
		return ModifierSet.isAbstract(modifiers);
	}

	@Override
	public boolean isBridge()
	{
		return false;
	}

	@Override
	public boolean isDeprecatedInByteCode()
	{
		return false;
	}

	@Override
	public boolean isFinal()
	{
		return ModifierSet.isFinal(modifiers);
	}

	@Override
	public boolean isMandated()
	{
		return false;
	}

	@Override
	public boolean isNative()
	{
		return ModifierSet.isNative(modifiers);
	}

	@Override
	public boolean isPrivate()
	{
		return ModifierSet.isPrivate(modifiers);
	}

	@Override
	public boolean isProtected()
	{
		return ModifierSet.isProtected(modifiers);
	}

	@Override
	public boolean isPublic()
	{
		return ModifierSet.isPublic(modifiers);
	}

	@Override
	public boolean isStatic()
	{
		return ModifierSet.isStatic(modifiers);
	}

	@Override
	public boolean isStrictFloatingPoint()
	{
		return ModifierSet.isStrictfp(modifiers);
	}

	@Override
	public boolean isSynchronized()
	{
		return ModifierSet.isSynchronized(modifiers);
	}

	@Override
	public boolean isSynthetic()
	{
		return false;
	}

	@Override
	public boolean isTransient()
	{
		return ModifierSet.isTransient(modifiers);
	}

	@Override
	public boolean isVolatile()
	{
		return ModifierSet.isVolatile(modifiers);
	}

	public boolean isPrivateOrInstance()
	{
		return isPrivate() || !isStatic();
	}

	public int toOpcodes()
	{
		return modifiers;
	}
}
