package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.stormmq.java.parsing.adaptors.javaparser.ast.JavaparserBodyIntermediate;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.UnresolvedReferenceTypeName;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverContext;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.ParameterIntermediate;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.BodyIntermediate;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.FieldFinality;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.EmptyBodyIntermediate.Abstract;
import static com.stormmq.java.parsing.utilities.FieldFinality.Final;
import static com.stormmq.java.parsing.utilities.FieldFinality.Volatile;
import static com.stormmq.java.parsing.utilities.Visibility.*;

public abstract class AbstractInterfaceMemberVoidVisitor<S extends BodyDeclarationState> extends AbstractWithMethodMemberVoidVisitor<S>
{
	protected AbstractInterfaceMemberVoidVisitor()
	{
	}

	protected abstract void useDefaultMethod(@NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final List<Statement> body, final boolean isVarArgs);

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
	protected final AbstractStaticClassMemberVoidVisitor<BodyDeclarationState> visitNonStaticClass(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final UsefulTypeParameters typeParameters, final boolean isStrictFloatingPoint, @NotNull final KnownReferenceTypeName className, @NotNull final Completeness completeness, @NotNull final Visibility visibility, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
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
	protected final int additionalModifierGuard(@NotNull final String what, final int modifiers)
	{
		return guardNestedTypeAccessModifiersWhenNestedInAnAnnotationOrInterface(what, modifiers);
	}

	@Override
	protected final void visitEnumConstant(@NotNull final EnumConstantDeclaration enumConstantDeclaration, @NotNull final S state, @NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		throw new IllegalStateException("Enum constant declarations are not allowed in classes");
	}

	@Override
	protected final void visitStaticInitializer(@NotNull final List<Statement> body, @NotNull final S state)
	{
		throw new IllegalStateException("Static initializers are not allowed in interfaces");
	}

	@Override
	protected final void visitInstanceInitializer(@NotNull final List<Statement> body, @NotNull final S state)
	{
		throw new IllegalStateException("Instance initializers are not allowed in interfaces");
	}

	@Override
	protected final void visitStaticField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldTypeUsage, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		throw new IllegalArgumentException("A field in an interface is implicitly static");
	}

	@Override
	protected final void visitNonStaticField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldTypeUsage, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		if (isTransient)
		{
			throw new IllegalArgumentException("A field can not be transient in an interface");
		}

		if (fieldFinality == Volatile)
		{
			throw new IllegalArgumentException("A field can not be volatile in an interface");
		}

		if (visibility == Protected)
		{
			throw new IllegalArgumentException("A field can not be protected in an interface");
		}

		if (visibility == Private)
		{
			throw new IllegalArgumentException("A field can not be private in an interface");
		}

		if (init == null)
		{
			throw new IllegalArgumentException("A field must have an init in an interface");
		}

		useStaticField(typeName, fieldName, fieldTypeUsage, init, false, Final, annotations, Public);
	}

	@Override
	protected final void useInstanceField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldType, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@Override
	protected final void visitConstructor(@NotNull final KnownReferenceTypeName typeName, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final BlockStmt body, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isVarArgs, final int modifiers)
	{
		throw new IllegalStateException("Constructors are not allowed in interfaces");
	}

	@Override
	protected final void visitDefaultMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final List<Statement> body, final boolean isVarArgs)
	{
		useDefaultMethod(methodName, usefulTypeParameters, returnTypeUsage, parameterIntermediates, throwsArray, annotations, body, isVarArgs);
	}

	@Override
	protected final void visitStaticNativeMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isFinal, final boolean isVarArgs)
	{
		throw new IllegalStateException("Static native methods are not allowed in interfaces");
	}

	@Override
	protected final void visitStaticNonNativeMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final List<Statement> body, final boolean isStrictFloatingPoint, final boolean isSynchronized, @NotNull final Visibility visibility, final boolean isVarArgs)
	{
		if (isStrictFloatingPoint)
		{
			throw new IllegalArgumentException("Interface static methods can not be strictfp");
		}

		if (isSynchronized)
		{
			throw new IllegalArgumentException("Interface static methods can not be synchronized");
		}

		if (visibility == Protected)
		{
			throw new IllegalArgumentException("Interface static methods can not be protected");
		}

		if (visibility == Private)
		{
			throw new IllegalArgumentException("Interface static methods can not be private");
		}

		final TypeResolverContext typeResolverContext = x;
		final BodyIntermediate bodyIntermediate = new JavaparserBodyIntermediate(body, typeResolverContext);

		useStaticMethod(typeName, methodName, usefulTypeParameters, returnTypeUsage, parameterIntermediates, throwsArray, annotations, bodyIntermediate, false, false, Public, isVarArgs);
	}

	@Override
	protected final void visitInstanceNativeMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, @NotNull final Completeness completeness, final boolean isVarArgs)
	{
		throw new IllegalStateException("Instance native methods are not allowed in interfaces");
	}

	@Override
	protected final void visitInstanceNonNativeMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @Nullable final BlockStmt body, final boolean isStrictFloatingPoint, final boolean isSynchronized, @NotNull final Visibility visibility, @NotNull final Completeness completeness, final boolean isVarArgs)
	{
		if (body != null)
		{
			throw new IllegalStateException("Interface instance methods must not have a body");
		}

		if (isStrictFloatingPoint)
		{
			throw new IllegalArgumentException("Interface instance methods can not be strictfp");
		}

		if (isSynchronized)
		{
			throw new IllegalArgumentException("Interface instance methods can not be synchronized");
		}

		if (visibility == Protected)
		{
			throw new IllegalArgumentException("Interface instance methods can not be protected");
		}

		if (visibility == Private)
		{
			throw new IllegalArgumentException("Interface instance methods can not be private");
		}

		if (completeness == Completeness.Final)
		{
			throw new IllegalArgumentException("Interface instance methods can not be final");
		}

		useInstanceMethod(typeName, methodName, usefulTypeParameters, returnTypeUsage, parameterIntermediates, throwsArray, annotations, Abstract, false, false, Public, Completeness.Abstract, isVarArgs);
	}
}
