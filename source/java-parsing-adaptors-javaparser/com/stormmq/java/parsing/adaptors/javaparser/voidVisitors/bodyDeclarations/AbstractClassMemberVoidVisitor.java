package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

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

import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.assertNotNull;
import static com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.EmptyBodyIntermediate.Abstract;
import static com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.EmptyBodyIntermediate.Native;

public abstract class AbstractClassMemberVoidVisitor<S extends BodyDeclarationState> extends AbstractWithMethodMemberVoidVisitor<S>
{
	protected AbstractClassMemberVoidVisitor()
	{
	}

	protected abstract void useConstructor(@NotNull final KnownReferenceTypeName typeName, @NotNull final Visibility visibility, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final BlockStmt body, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isVarArgs, @NotNull final TypeUsage constructorTypeUsage);

	protected abstract void useInstanceInitializer(@NotNull final List<Statement> body);

	@Override
	protected final AbstractInnerOrLocalClassMemberVoidVisitor<BodyDeclarationState> visitNonStaticClass(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final UsefulTypeParameters typeParameters, final boolean isStrictFloatingPoint, @NotNull final KnownReferenceTypeName className, @NotNull final Completeness completeness, @NotNull final Visibility visibility, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
	{
		return useInnerClass(className, visibility, annotations, isStrictFloatingPoint, completeness, typeParameters, implementsTypeUsages, extendsTypeUsage);
	}

	@Override
	protected final int additionalModifierGuard(@NotNull final String what, final int modifiers)
	{
		return modifiers;
	}

	@Override
	protected final void visitInstanceInitializer(@NotNull final List<Statement> body, @NotNull final S state)
	{
		useInstanceInitializer(body);
	}

	@Override
	protected final void visitNonStaticField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldTypeUsage, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		useInstanceField(typeName, fieldName, fieldTypeUsage, init, isTransient, fieldFinality, annotations, visibility);
	}

	@Override
	protected final void visitConstructor(@NotNull final KnownReferenceTypeName typeName, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final BlockStmt body, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isVarArgs, final int modifiers)
	{
		final Visibility visibility = constructorVisibility(modifiers);

		// this is us with
		final TypeUsage constructorTypeUsage = new ClassOrInterfaceIntermediateTypeArgument(AnnotationInstanceIntermediate.EmptyAnnotations, AnnotationInstanceIntermediate.EmptyEmptyAnnotations, null, name, upperBounds);

		useConstructor(typeName, visibility, typeParameters, parameterIntermediates, throwsArray, body, annotations, isVarArgs, constructorTypeUsage);
	}

	@NotNull
	protected abstract Visibility constructorVisibility(final int modifiers);

	@Override
	protected final void visitDefaultMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final List<Statement> body, final boolean isVarArgs)
	{
		throw new IllegalStateException("Classes do not have default methods");
	}

	@Override
	protected final void visitInstanceNativeMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, @NotNull final Completeness completeness, final boolean isVarArgs)
	{
		useInstanceMethod(typeName, methodName, usefulTypeParameters, returnTypeUsage, parameterIntermediates, throwsArray, annotations, Native, false, false, visibility, completeness, isVarArgs);
	}

	@Override
	protected final void visitInstanceNonNativeMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @Nullable final BlockStmt body, final boolean isStrictFloatingPoint, final boolean isSynchronized, @NotNull final Visibility visibility, @NotNull final Completeness completeness, final boolean isVarArgs)
	{
		final BodyIntermediate bodyIntermediate;
		if (completeness == Completeness.Abstract)
		{
			if (body != null)
			{
				throw new IllegalArgumentException("A class can not have an instance method without a body unless the method is abstract or native");
			}
			bodyIntermediate = Abstract;
		}
		else
		{
			if (body == null)
			{
				throw new IllegalArgumentException("A class can not have an instance method without a body unless the method is abstract or native");
			}
			final TypeResolverContext typeResolverContext = x;
			bodyIntermediate = new JavaparserBodyIntermediate(assertNotNull(body.getStmts()), typeResolverContext);
		}

		useInstanceMethod(typeName, methodName, usefulTypeParameters, returnTypeUsage, parameterIntermediates, throwsArray, annotations, bodyIntermediate, isStrictFloatingPoint, isSynchronized, visibility, completeness, isVarArgs);
	}
}
