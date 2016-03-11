package com.stormmq.java.parsing.ast.modifiers;

import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.Visibility;
import org.jetbrains.annotations.NotNull;

public interface Modifiers
{
	void validateAsClassModifiers();

	void validateAsInterfaceModifiers();

	void validateAsAnnotationModifiers();

	void validateAsEnumModifiers();

	void validateAsClassFieldModifiers();

	void validateAsInterfaceFieldModifiers();

	void validateAsAnnotationFieldModifiers();

	void validateAsEnumFieldModifiers();

	void validateAsClassMethodModifiers();

	void validateAsInterfaceMethodModifiers();

	void validateAsAnnotationMethodModifiers();

	void validateAsEnumMethodModifiers();

	void validateAsParameterModifiers();

	@NotNull
	Visibility visibility();

	@NotNull
	Completeness classOrMethodCompleteness();

	boolean isValidStaticField();

	boolean isValidStaticMethod();

	boolean isValidField();

	boolean isValidMethod();

	boolean isInstance();

	boolean isAbstract();

	boolean isBridge();

	boolean isDeprecatedInByteCode();

	boolean isFinal();

	boolean isMandated();

	boolean isNative();

	boolean isPrivate();

	boolean isProtected();

	boolean isPublic();

	boolean isStatic();

	boolean isStrictFloatingPoint();

	boolean isSynchronized();

	boolean isSynthetic();

	boolean isTransient();

	boolean isVolatile();
}
