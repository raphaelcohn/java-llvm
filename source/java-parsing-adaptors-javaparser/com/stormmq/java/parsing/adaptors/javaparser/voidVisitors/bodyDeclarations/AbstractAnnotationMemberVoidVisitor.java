package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.FieldFinality;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.github.javaparser.ast.body.ModifierSet.*;
import static com.stormmq.java.parsing.utilities.StringConstants.Should_be_impossible;
import static com.stormmq.java.parsing.adaptors.javaparser.genericVisitors.MethodReturnMethodParameterFieldAndAnnotationMemberTypeGenericVisitor.methodReturnMethodParameterFieldAndAnnotationMemberType;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.assertNotNull;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.nullToEmptyList;
import static com.stormmq.java.parsing.utilities.FieldFinality.Final;
import static com.stormmq.java.parsing.utilities.FieldFinality.Volatile;
import static com.stormmq.java.parsing.utilities.Visibility.Private;
import static com.stormmq.java.parsing.utilities.Visibility.Protected;
import static com.stormmq.java.parsing.utilities.Visibility.Public;

public abstract class AbstractAnnotationMemberVoidVisitor<S extends BodyDeclarationState> extends AbstractMemberVoidVisitor<S>
{
	protected AbstractAnnotationMemberVoidVisitor()
	{
	}

	protected abstract void useAnnotationMember(@NotNull final KnownReferenceTypeName annotationTypeName, @NotNull final String annotationMemberName, @NotNull final TypeUsage typeUsage, @Nullable final Expression defaultValue, @NotNull final AnnotationInstanceIntermediate[] annotations);

	@NotNull
	@Override
	protected final AbstractAnnotationMemberVoidVisitor<BodyDeclarationState> visitAnnotation(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final KnownReferenceTypeName annotationName, final boolean isStrictFloatingPoint, @NotNull final Visibility visibility)
	{
		return useAnnotation(annotationName, annotations, Public, isStrictFloatingPoint);
	}

	@NotNull
	@Override
	protected final AbstractInterfaceMemberVoidVisitor<BodyDeclarationState> visitInterface(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final UsefulTypeParameters typeParameters, final boolean isStrictFloatingPoint, @NotNull final KnownReferenceTypeName interfaceName, @NotNull final Visibility visibility, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] extendsTypeUsages)
	{
		return useInterface(interfaceName, annotations, Public, isStrictFloatingPoint, typeParameters, extendsTypeUsages);
	}

	@Override
	protected final AbstractStaticClassMemberVoidVisitor<BodyDeclarationState> visitStaticClass(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final UsefulTypeParameters typeParameters, final boolean isStrictFloatingPoint, @NotNull final KnownReferenceTypeName className, @NotNull final Completeness completeness, @NotNull final Visibility visibility, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
	{
		return useStaticClass(className, Public, annotations, isStrictFloatingPoint, completeness, typeParameters, implementsTypeUsages, extendsTypeUsage);
	}

	@Override
	protected final AbstractClassMemberVoidVisitor<BodyDeclarationState> visitNonStaticClass(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final UsefulTypeParameters typeParameters, final boolean isStrictFloatingPoint, @NotNull final KnownReferenceTypeName className, @NotNull final Completeness completeness, @NotNull final Visibility visibility, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
	{
		return useStaticClass(className, Public, annotations, isStrictFloatingPoint, completeness, typeParameters, implementsTypeUsages, extendsTypeUsage);
	}

	@NotNull
	@Override
	protected final AbstractEnumClassMemberVoidVisitor<BodyDeclarationState> visitEnum(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final KnownReferenceTypeName enumName, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
	{
		return useEnum(enumName, annotations, Public, isStrictFloatingPoint, implementsTypeUsages);
	}

	@Override
	public final void visit(@NotNull final EnumConstantDeclaration enumConstantDeclaration, @NotNull final S state)
	{
		throw new IllegalStateException("Enum constant declarations are not allowed in annotations");
	}

	@Override
	protected final int additionalModifierGuard(@NotNull final String what, final int modifiers)
	{
		return guardNestedTypeAccessModifiersWhenNestedInAnAnnotationOrInterface(what, modifiers);
	}

	@Override
	protected final void visitStaticField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldTypeUsage, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		// ignore isStatic; all fields in an annotation are implicitly static
		// ignore isFinal; all fields in an annotation are implicitly final
		// ignore isPublic; all fields in an annotation are implicitly public

		if (isTransient)
		{
			throw new IllegalArgumentException("A field can not be transient in an annotation");
		}

		if (fieldFinality == Volatile)
		{
			throw new IllegalArgumentException("A field can not be volatile in an annotation");
		}

		if (visibility == Protected)
		{
			throw new IllegalArgumentException("A field can not be protected in an annotation");
		}

		if (visibility == Private)
		{
			throw new IllegalArgumentException("A field can not be private in an annotation");
		}

		if (init == null)
		{
			throw new IllegalArgumentException("A field must have an init in an annotation");
		}

		useStaticField(typeName, fieldName, fieldTypeUsage, init, false, Final, annotations, Public);
	}

	@Override
	protected final void visitNonStaticField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldTypeUsage, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		visitStaticField(typeName, fieldName, fieldTypeUsage, init, isTransient, fieldFinality, visibility, annotations);
	}

	@Override
	protected final void useInstanceField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldType, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@Override
	protected final void visitStaticInitializer(@NotNull final List<Statement> body, @NotNull final S state)
	{
		throw new IllegalStateException("Static instance initializers are not allowed in annotations");
	}

	@Override
	protected final void visitInstanceInitializer(@NotNull final List<Statement> body, @NotNull final S state)
	{
		throw new IllegalStateException("Instance initializers are not allowed in annotations");
	}

	@Override
	public final void visit(@NotNull final ConstructorDeclaration constructorDeclaration, @NotNull final S state)
	{
		throw new IllegalStateException("Constructors are not allowed in annotations");
	}

	@Override
	public final void visit(@NotNull final MethodDeclaration methodDeclaration, @NotNull final S state)
	{
		throw new IllegalStateException("Methods are not allowed in annotations");
	}

	@Override
	public final void visit(@NotNull final AnnotationMemberDeclaration annotationMemberDeclaration, @NotNull final S state)
	{
		final int modifiers = annotationMemberDeclaration.getModifiers();

		if (isStrictfp(modifiers))
		{
			throw new IllegalArgumentException("Annotation members may not be strictfp");
		}

		if (isSynchronized(modifiers))
		{
			throw new IllegalArgumentException("Annotation members may not be synchronized");
		}

		if (isFinal(modifiers))
		{
			throw new IllegalArgumentException("Annotation members may not be final");
		}

		if (isStatic(modifiers))
		{
			throw new IllegalArgumentException("Annotation members may not be static");
		}

		if (isNative(modifiers))
		{
			throw new IllegalArgumentException("Annotation members may not be native");
		}

		@NotNull final AnnotationInstanceIntermediate[] annotations = state.convertToAnnotations(nullToEmptyList(annotationMemberDeclaration.getAnnotations()));

		final Type originalType = assertNotNull(annotationMemberDeclaration.getType());
		if (originalType instanceof ReferenceType)
		{
			final ReferenceType referenceType = (ReferenceType) originalType;
			if (referenceType.getArrayCount() > 1)
			{
				throw new IllegalArgumentException("An annotation member may only be a non-array or a one-dimensional array");
			}
			final Type underlying = referenceType.getType();
			if (!((underlying instanceof PrimitiveType) || (underlying instanceof ClassOrInterfaceType)))
			{
				throw new IllegalArgumentException("An annotation member may not have a reference type with a one-dimensional array which is not primitive, an enum or an annotation");
			}
		}
		final TypeUsage typeUsage = methodReturnMethodParameterFieldAndAnnotationMemberType(originalType, 0, state, false);

		final String annotationMemberName = state.annotationMemberName(annotationMemberDeclaration);
		@Nullable final Expression defaultValue = annotationMemberDeclaration.getDefaultValue();

		useAnnotationMember(state.currentTypeName(), annotationMemberName, typeUsage, defaultValue, annotations);
	}

	@NotNull
	@Override
	protected final AbstractInnerOrLocalClassMemberVoidVisitor<BodyDeclarationState> useInnerClass(@NotNull final KnownReferenceTypeName className, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isStrictFloatingPoint, @NotNull final Completeness completeness, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage)
	{
		throw new IllegalStateException(Should_be_impossible);
	}
}
