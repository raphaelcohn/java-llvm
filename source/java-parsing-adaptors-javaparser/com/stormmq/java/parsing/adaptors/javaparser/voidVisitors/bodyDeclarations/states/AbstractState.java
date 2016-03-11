package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.stormmq.java.parsing.adaptors.javaparser.ast.JavaparserModifiers;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.UnresolvedReferenceTypeName;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverContext;
import com.stormmq.java.parsing.adaptors.javaparser.genericVisitors.MethodReturnMethodParameterFieldAndAnnotationMemberTypeGenericVisitor;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameter;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.ParameterIntermediate;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.parentNames.ParentName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.*;

import java.util.*;

import static com.stormmq.java.parsing.adaptors.javaparser.ast.JavaparserModifiers.javaparserModifiersParameter;
import static com.stormmq.java.parsing.adaptors.javaparser.genericVisitors.MethodReturnMethodParameterFieldAndAnnotationMemberTypeGenericVisitor.methodReturnMethodParameterFieldAndAnnotationMemberType;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.assertNotNull;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.VariableDeclaratorIdHelper.guardCStyleArrayCount;
import static com.stormmq.java.parsing.ast.intermediates.ParameterIntermediate.EmptyParameterIntermediates;

public abstract class AbstractState implements State
{
	@NotNull private final TypeResolverContext typeResolverContext;
	@NotNull private final ParentName parentName;
	@NotNull protected final Set<String> alreadyVisitedTypeNames;

	protected AbstractState(@NotNull final TypeResolverContext typeResolverContext, @NotNull final ParentName parentName)
	{
		this.typeResolverContext = typeResolverContext;
		this.parentName = parentName;
		alreadyVisitedTypeNames = new LinkedHashSet<>(16);
	}

	@NotNull
	@Override
	public final AnnotationInstanceIntermediate[] convertToAnnotations(@Nullable final List<AnnotationExpr> annotations)
	{
		return typeResolverContext.convertToAnnotations(annotations);
	}

	@NotNull
	@Override
	public final UnresolvedReferenceTypeName[] convertNameExprToUnresolvedReferenceTypeNames(@Nullable final List<NameExpr> throwsList)
	{
		return UnresolvedReferenceTypeName.convertNameExprToUnresolvedReferenceTypeNames(throwsList, typeResolverContext);
	}

	@Override
	@NotNull
	public final ParameterIntermediate[] convertParameters(@Nullable final List<Parameter> parameters, @NotNull final boolean[] isVarArgsArray)
	{
		assert isVarArgsArray.length == 1;

		if (parameters == null)
		{
			return EmptyParameterIntermediates;
		}

		final int size = parameters.size();
		if (size == 0)
		{
			return EmptyParameterIntermediates;
		}

		final Set<String> alreadyEncountered = new HashSet<>(size);

		final ParameterIntermediate[] parameterIntermediates = new ParameterIntermediate[size];
		final int lastIndex = size - 1;
		for (int index = 0; index < size; index++)
		{
			final Parameter parameter = assertNotNull(parameters.get(index));
			final boolean isVarArgs = parameter.isVarArgs();
			final int arrayCountCorrectionForVarArgs;
			if (isVarArgs)
			{
				if (index != lastIndex)
				{
					throw new IllegalArgumentException("Only the last parameter can be varargs '...'");
				}
				isVarArgsArray[0] = true;
				arrayCountCorrectionForVarArgs = 1;
			}
			else
			{
				arrayCountCorrectionForVarArgs = 0;
			}
			final VariableDeclaratorId variableDeclaratorId = parameter.getId();
			final String name = variableDeclaratorId.getName();
			if (!alreadyEncountered.add(name))
			{
				throw new IllegalArgumentException("Duplicate method parameter");
			}

			final JavaparserModifiers javaparserModifiers = javaparserModifiersParameter(parameter);
			javaparserModifiers.validateAsParameterModifiers();
			final boolean isFinal = javaparserModifiers.isFinal();

			final int cStyleArrayCount = guardCStyleArrayCount(variableDeclaratorId) + arrayCountCorrectionForVarArgs;

			final AnnotationInstanceIntermediate[] annotations = typeResolverContext.convertToAnnotations(parameter.getAnnotations());

			final TypeUsage typeUsage = methodReturnMethodParameterFieldAndAnnotationMemberType(parameter.getType(), cStyleArrayCount, typeResolverContext, false);
			parameterIntermediates[index] = new ParameterIntermediate(name, typeUsage, isFinal, false, false, false, annotations);
		}
		return parameterIntermediates;
	}

	@Override
	public final void visitMembers(@NotNull final VoidVisitor<BodyDeclarationState> bodyDeclarationsVisitor, @NotNull final List<BodyDeclaration> bodyDeclarations, @NotNull final KnownReferenceTypeName typeName)
	{
		final BodyDeclarationState state = new ImplementedBodyDeclarationState(typeResolverContext, typeName, typeName.simpleTypeName());
		for (final BodyDeclaration bodyDeclaration : bodyDeclarations)
		{
			bodyDeclaration.accept(bodyDeclarationsVisitor, state);
		}
	}

	@Override
	@NotNull
	public final ClassOrInterfaceIntermediateTypeArgument convertToClassOrInterfaceIntermediateTypeArgument(@Nullable final ClassOrInterfaceType classOrInterfaceType)
	{
		assertNotNull(classOrInterfaceType);

		final MethodReturnMethodParameterFieldAndAnnotationMemberTypeGenericVisitor visitor = new MethodReturnMethodParameterFieldAndAnnotationMemberTypeGenericVisitor(typeResolverContext, false);
		final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage = visitor.visit(classOrInterfaceType, 0);

		if (extendsTypeUsage.isArray())
		{
			throw new IllegalArgumentException("A type can not extend or implement an array");
		}
		return extendsTypeUsage;
	}

	@Override
	@NotNull
	public UsefulTypeParameter[] convertTypeParameters(@NotNull final ClassOrInterfaceDeclaration classOrInterfaceDeclaration)
	{
		return UsefulTypeParameter.convertTypeParameters(typeResolverContext, classOrInterfaceDeclaration.getTypeParameters());
	}

	@NotNull
	@Override
	public TypeUsage fieldTypeUsage(@NotNull final Type fieldType, final int cStyleArrayCount)
	{
		return methodReturnMethodParameterFieldAndAnnotationMemberType(fieldType, cStyleArrayCount, typeResolverContext, false);
	}

	@NotNull
	protected final KnownReferenceTypeName child(@NotNull final String typeName, @NonNls @NotNull final String what)
	{
		if (!alreadyVisitedTypeNames.add(typeName))
		{
			throw new IllegalStateException("Another class explicitly matches a " + what + " name; this shouldn't happen");
		}

		return parentName.child(typeName);
	}
}
