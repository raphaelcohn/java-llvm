package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.Statement;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.UnresolvedReferenceTypeName;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.ParameterIntermediate;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.FieldFinality;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.stormmq.java.parsing.utilities.FieldFinality.Final;

public abstract class AbstractInnerLocalOrAnonymousClassMemberVoidVisitor<S extends BodyDeclarationState> extends AbstractClassMemberVoidVisitor<S>
{
	protected AbstractInnerLocalOrAnonymousClassMemberVoidVisitor()
	{
		super();
	}

	@NotNull
	@Override
	protected final AbstractAnnotationMemberVoidVisitor<BodyDeclarationState> visitAnnotation(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final KnownReferenceTypeName annotationName, final boolean isStrictFloatingPoint, @NotNull final Visibility visibility)
	{
		throw new IllegalArgumentException("Inner, local and anonymous classes can not have annotation declarations");
	}

	@NotNull
	@Override
	protected final AbstractInterfaceMemberVoidVisitor<BodyDeclarationState> visitInterface(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final UsefulTypeParameters typeParameters, final boolean isStrictFloatingPoint, @NotNull final KnownReferenceTypeName interfaceName, @NotNull final Visibility visibility, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] extendsTypeUsages)
	{
		throw new IllegalArgumentException("Inner, local and anonymous classes can not have interface declarations");
	}

	@Override
	protected final AbstractStaticClassMemberVoidVisitor<BodyDeclarationState> visitStaticClass(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final UsefulTypeParameters typeParameters, final boolean isStrictFloatingPoint, @NotNull final KnownReferenceTypeName className, @NotNull final Completeness completeness, @NotNull final Visibility visibility, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
	{
		throw new IllegalArgumentException("Inner, local and anonymous classes can not have static class declarations");
	}

	@NotNull
	@Override
	protected final AbstractEnumClassMemberVoidVisitor<BodyDeclarationState> visitEnum(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final KnownReferenceTypeName enumName, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
	{
		throw new IllegalArgumentException("Inner, local and anonymous classes can not have enum declarations");
	}

	@Override
	protected final void visitStaticField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldTypeUsage, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		if (fieldFinality != Final)
		{
			throw new IllegalArgumentException("Static fields are not allowed in inner classes unless final");
		}

		if (!fieldTypeUsage.isPrimitive() || !fieldTypeUsage.couldBeString())
		{
			throw new IllegalArgumentException("Static fields are not allowed in inner classes unless final and primitive or of type String");
		}

		if (init == null)
		{
			throw new IllegalArgumentException("Static fields are not allowed in inner classes unless final with a specified constant value");
		}

		useStaticField(typeName, fieldName, fieldTypeUsage, init, isTransient, fieldFinality, annotations, visibility);
	}

	@NotNull
	@Override
	protected final AbstractStaticClassMemberVoidVisitor<BodyDeclarationState> useStaticClass(@NotNull final KnownReferenceTypeName className, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isStrictFloatingPoint, @NotNull final Completeness completeness, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage)
	{
		throw new IllegalArgumentException("Inner, local and anonymous classes can not have static class declarations");
	}

	@Override
	protected final void visitEnumConstant(@NotNull final EnumConstantDeclaration enumConstantDeclaration, @NotNull final S state, @NotNull final AnnotationInstanceIntermediate[] annotations)
	{
		throw new IllegalStateException("Inner, local and anonymous classes can not have enum constant declarations");
	}

	@Override
	protected final void visitStaticInitializer(@NotNull final List<Statement> body, @NotNull final S state)
	{
		throw new IllegalArgumentException("Static initializers are not allowed in inner, local and anonymous classes");
	}

	@Override
	protected final void visitStaticNativeMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isFinal, final boolean isVarArgs)
	{
		throw new IllegalArgumentException("Static native methods are not allowed in inner, local and anonymous classes");
	}

	@Override
	protected final void visitStaticNonNativeMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final List<Statement> body, final boolean isStrictFloatingPoint, final boolean isSynchronized, @NotNull final Visibility visibility, final boolean isVarArgs)
	{
		throw new IllegalArgumentException("Static non-native methods are not allowed in inner, local and anonymous classes");
	}
}
