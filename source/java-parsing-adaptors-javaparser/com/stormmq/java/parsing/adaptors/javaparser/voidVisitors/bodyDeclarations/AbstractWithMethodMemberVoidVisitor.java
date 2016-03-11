package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import com.stormmq.java.parsing.adaptors.javaparser.StringConstants;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.UnresolvedReferenceTypeName;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameter;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.ast.intermediates.ParameterIntermediate;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.BodyIntermediate;
import com.stormmq.java.parsing.utilities.*;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import com.stormmq.java.parsing.utilities.string.StringUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.github.javaparser.ast.body.ModifierSet.*;
import static com.stormmq.java.parsing.adaptors.javaparser.StringConstants.constructor;
import static com.stormmq.java.parsing.adaptors.javaparser.StringConstants.method;
import static com.stormmq.java.parsing.adaptors.javaparser.genericVisitors.MethodReturnMethodParameterFieldAndAnnotationMemberTypeGenericVisitor.methodReturnMethodParameterFieldAndAnnotationMemberType;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.assertNotNull;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.validateName;
import static com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameter.convertTypeParameters;

public abstract class AbstractWithMethodMemberVoidVisitor<S extends BodyDeclarationState> extends AbstractMemberVoidVisitor<S>
{
	protected AbstractWithMethodMemberVoidVisitor()
	{
	}

	protected abstract void visitConstructor(@NotNull final KnownReferenceTypeName typeName, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final BlockStmt body, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isVarArgs, final int modifiers);

	protected abstract void visitDefaultMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final List<Statement> body, final boolean isVarArgs);

	protected abstract void visitStaticNonNativeMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final List<Statement> body, final boolean isStrictFloatingPoint, final boolean isSynchronized, @NotNull final Visibility visibility, final boolean isVarArgs);

	protected abstract void visitStaticNativeMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isFinal, final boolean isVarArgs);

	protected abstract void visitInstanceNativeMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, @NotNull final Completeness completeness, final boolean isVarArgs);

	protected abstract void visitInstanceNonNativeMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @Nullable final BlockStmt body, final boolean isStrictFloatingPoint, final boolean isSynchronized, @NotNull final Visibility visibility, @NotNull final Completeness completeness, final boolean isVarArgs);

	protected abstract void visitEnumConstant(@NotNull final EnumConstantDeclaration enumConstantDeclaration, @NotNull final S state, @NotNull final AnnotationInstanceIntermediate[] annotations);

	protected abstract void useStaticMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final BodyIntermediate bodyIntermediate, final boolean isStrictFloatingPoint, final boolean isSynchronized, @NotNull final Visibility visibility, final boolean isVarArgs);

	protected abstract void useInstanceMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final BodyIntermediate bodyIntermediate, final boolean isStrictFloatingPoint, final boolean isSynchronized, @NotNull final Visibility visibility, @NotNull final Completeness completeness, final boolean isVarArgs);

	@Override
	public void visit(@NotNull final ConstructorDeclaration constructorDeclaration, @NotNull final S state)
	{
		final int modifiers = guardVisibility(constructor, guardConstructorOrMethod(constructor, constructorDeclaration.getModifiers()));

		if (isAbstract(modifiers))
		{
			throw new IllegalArgumentException("A constructor can not be abstract");
		}

		if (isFinal(modifiers))
		{
			throw new IllegalArgumentException("A constructor can not be final");
		}

		if (isStrictfp(modifiers))
		{
			throw new IllegalArgumentException("A constructor can not be strictfp");
		}

		if (isNative(modifiers))
		{
			throw new IllegalArgumentException("A constructor can not be native");
		}

		if (isSynchronized(modifiers))
		{
			throw new IllegalArgumentException("A constructor can not be synchronized");
		}

		if (isStatic(modifiers))
		{
			throw new IllegalArgumentException("A constructor can not be static");
		}

		final UsefulTypeParameters parent = state.usefulTypeParametersOfEncapsulatingDeclaration();
		final UsefulTypeParameter[] typeParameters = convertTypeParameters(unresolvedReferenceTypeNameMaker, constructorDeclaration.getTypeParameters());

		final boolean[] isVarArgsArray = new boolean[1];
		final ParameterIntermediate[] parameterIntermediates = state.convertParameters(constructorDeclaration.getParameters(), isVarArgsArray);
		final boolean isVarArgs = isVarArgsArray[0];

		final UnresolvedReferenceTypeName[] throwsArray = state.convertNameExprToUnresolvedReferenceTypeNames(constructorDeclaration.getThrows());
		@Nullable final BlockStmt body = constructorDeclaration.getBlock();
		if (body == null)
		{
			throw new IllegalArgumentException("A constructor can not be missing a body");
		}

		final AnnotationInstanceIntermediate[] annotations = state.convertToAnnotations(constructorDeclaration.getAnnotations());
		final KnownReferenceTypeName typeName = state.currentTypeName();

		assertNotNull(body.getStmts());

		visitConstructor(typeName, parent.descend(typeParameters), parameterIntermediates, throwsArray, body, annotations, isVarArgs, modifiers);
	}

	@Override
	public final void visit(@NotNull final MethodDeclaration methodDeclaration, @NotNull final S state)
	{
		final String methodName = methodName(methodDeclaration);

		final int modifiers = guardVisibility(method, guardConstructorOrMethod(method, methodDeclaration.getModifiers()));

		final boolean isFinal = isFinal(modifiers);
		final boolean isAbstract = isAbstract(modifiers);
		final boolean isNative = isNative(modifiers);
		final boolean isStatic = isStatic(modifiers);
		final boolean isStrictFloatingPoint = isStrictfp(modifiers);
		final boolean isSynchronized = isSynchronized(modifiers);

		if (isAbstract)
		{
			if (isFinal)
			{
				throw new IllegalArgumentException("A method can not be final and abstract");
			}

			if (isNative)
			{
				throw new IllegalArgumentException("A method can not be native and abstract");
			}

			if (isStatic)
			{
				throw new IllegalArgumentException("A method can not be static and abstract");
			}
		}

		if (isNative)
		{
			if (isStrictFloatingPoint)
			{
				throw new IllegalArgumentException("A method can not be native and strictfp");
			}

			if (isSynchronized)
			{
				throw new IllegalArgumentException("A method can not be native and synchronized");
			}
		}

		final boolean isProtected = isProtected(modifiers);
		final boolean isPrivate = isPrivate(modifiers);

		@Nullable final BlockStmt body = methodDeclaration.getBody();

		final UsefulTypeParameters parent = state.usefulTypeParametersOfEncapsulatingDeclaration();
		final UsefulTypeParameter[] typeParameters = state.convertTypeParameters(methodDeclaration);

		final UnresolvedReferenceTypeName[] throwsArray = state.convertNameExprToUnresolvedReferenceTypeNames(methodDeclaration.getThrows());
		final AnnotationInstanceIntermediate[] annotations = state.convertToAnnotations(methodDeclaration.getAnnotations());

		final Type returnType = assertNotNull(methodDeclaration.getType());
		final TypeUsage returnTypeUsage = methodReturnMethodParameterFieldAndAnnotationMemberType(returnType, methodDeclaration.getArrayCount(), unresolvedReferenceTypeNameMaker, true);

		final boolean[] isVarArgsArray = new boolean[1];
		final ParameterIntermediate[] parameterIntermediates = state.convertParameters(unresolvedReferenceTypeNameMaker, methodDeclaration.getParameters(), isVarArgsArray);
		final boolean isVarArgs = isVarArgsArray[0];

		// a static method can not descend from the class type parameters
		final UsefulTypeParameters staticTypeParameters = x;
		final UsefulTypeParameters descendedTypeParameters = parent.descend(typeParameters);
		final KnownReferenceTypeName typeName = state.currentTypeName();

		if (isNative)
		{
			if (body != null)
			{
				throw new IllegalArgumentException("A body is not allowed for native methods");
			}
		}

		if (isAbstract)
		{
			if (body != null)
			{
				throw new IllegalArgumentException("A body is not allowed for abstract methods");
			}
		}

		if (methodDeclaration.isDefault())
		{
			if (isStatic)
			{
				throw new IllegalArgumentException("Default methods can not be static");
			}
			if (isAbstract)
			{
				throw new IllegalArgumentException("Default methods can not be abstract");
			}
			if (isFinal)
			{
				throw new IllegalArgumentException("Default methods can not be final");
			}
			if (isSynchronized)
			{
				throw new IllegalArgumentException("Default methods can not be synchronized");
			}
			if (isStrictFloatingPoint)
			{
				throw new IllegalArgumentException("Default methods can not be strictfp");
			}
			if (isProtected)
			{
				throw new IllegalArgumentException("Default methods can not be protected");
			}
			if (isPrivate)
			{
				throw new IllegalArgumentException("Default methods can not be private");
			}
			if (isNative)
			{
				throw new IllegalArgumentException("Default methods can not be native");
			}
			if (body == null)
			{
				throw new IllegalArgumentException("Default methods must have a body");
			}

			visitDefaultMethod(typeName, methodName, descendedTypeParameters, returnTypeUsage, parameterIntermediates, throwsArray, annotations, assertNotNull(body.getStmts()), isVarArgs);
		}
		else
		{
			final Visibility visibility = visibility(modifiers, method);

			if (isStatic)
			{
				if (isNative)
				{
					visitStaticNativeMethod(typeName, methodName, staticTypeParameters, returnTypeUsage, parameterIntermediates, throwsArray, annotations, visibility, isFinal, isVarArgs);
				}
				else
				{
					if (body == null)
					{
						throw new IllegalArgumentException("Static non-native methods must have a body");
					}
					visitStaticNonNativeMethod(typeName, methodName, staticTypeParameters, returnTypeUsage, parameterIntermediates, throwsArray, annotations, assertNotNull(body.getStmts()), isStrictFloatingPoint, isSynchronized, visibility, isVarArgs);
				}
			}
			else
			{
				final Completeness completeness;
				if (isAbstract)
				{
					completeness = Completeness.Abstract;
				}
				else
				{
					if (isFinal)
					{
						completeness = Completeness.Final;
					}
					else
					{
						completeness = Completeness.Normal;
					}
				}

				if (isNative)
				{
					visitInstanceNativeMethod(typeName, methodName, descendedTypeParameters, returnTypeUsage, parameterIntermediates, throwsArray, annotations, visibility, completeness, isVarArgs);
				}
				else
				{
					visitInstanceNonNativeMethod(typeName, methodName, descendedTypeParameters, returnTypeUsage, parameterIntermediates, throwsArray, annotations, body, isStrictFloatingPoint, isSynchronized, visibility, completeness, isVarArgs);
				}
			}
		}
	}

	private static int guardConstructorOrMethod(@NotNull final String what, final int modifiers)
	{
		if (isTransient(modifiers))
		{
			throw new IllegalArgumentException(StringUtilities.aOrAn(what) + StringConstants._can_not_be_transient);
		}

		if (isVolatile(modifiers))
		{
			throw new IllegalArgumentException(StringUtilities.aOrAn(what) + StringConstants._can_not_be_volatile);
		}

		return modifiers;
	}

	@Override
	public final void visit(@NotNull final AnnotationMemberDeclaration annotationMemberDeclaration, @NotNull final S state)
	{
		throw new IllegalArgumentException("Only annotations can have annotation members");
	}

	@Override
	public void visit(@NotNull final EnumConstantDeclaration enumConstantDeclaration, @NotNull final S state)
	{
		visitEnumConstant(enumConstantDeclaration, state, state.convertToAnnotations(enumConstantDeclaration.getAnnotations()));
	}

	@NotNull
	private static String methodName(@NotNull final MethodDeclaration methodDeclaration)
	{
		return validateName(methodDeclaration.getName(), false);
	}

}
