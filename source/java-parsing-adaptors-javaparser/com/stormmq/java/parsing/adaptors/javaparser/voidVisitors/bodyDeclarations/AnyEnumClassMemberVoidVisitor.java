package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.stormmq.java.parsing.adaptors.javaparser.ast.JavaparserBodyIntermediate;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.UnresolvedReferenceTypeName;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverContext;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulFieldValueDetail;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.ast.details.fieldValueDetails.FieldValueDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.ConstructorIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.FieldIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.MethodIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.*;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.ast.intermediates.bodyIntermediates.BodyIntermediate;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.FieldFinality;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.PrimitiveTypeUsage;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters.NoUsefulTypeParameters;
import static com.stormmq.java.parsing.ast.details.fieldValueDetails.UnassignedFieldValueDetail.Unassigned;
import static com.stormmq.java.parsing.ast.intermediateTypeArguments.IntermediateTypeArgument.EmptyIntermediateTypeArguments;
import static com.stormmq.java.parsing.ast.intermediates.FieldIntermediate.newEnumStaticFieldIntermediate;
import static com.stormmq.java.parsing.ast.intermediates.MethodIntermediate.newEnumInstanceMethodIntermediate;
import static com.stormmq.java.parsing.ast.intermediates.MethodIntermediate.newEnumStaticMethodIntermediate;
import static com.stormmq.java.parsing.ast.intermediates.Origin.Source;
import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate.EmptyAnnotations;
import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate.EmptyEmptyAnnotations;
import static com.stormmq.java.parsing.utilities.Visibility.Private;
import static com.stormmq.java.parsing.utilities.names.typeNames.PrimitiveTypeName._int;
import static com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName.EmptyKnownReferenceTypeNames;
import static java.lang.System.arraycopy;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public final class AnyEnumClassMemberVoidVisitor extends AbstractEnumClassMemberVoidVisitor<BodyDeclarationState>
{
	@NotNull private static final ParameterIntermediate NameParameter = new ParameterIntermediate("$enum$name", new ClassOrInterfaceIntermediateTypeArgument(EmptyAnnotations, EmptyEmptyAnnotations, null, "java.lang.String", EmptyIntermediateTypeArguments), true, true, true, true, EmptyAnnotations);
	@NotNull private static final ParameterIntermediate OrdinalParameter = new ParameterIntermediate("$enum$ordinal", new PrimitiveTypeUsage(EmptyAnnotations, _int), true, true, true, true, EmptyAnnotations);
	@NotNull private static final ParameterIntermediate[] DefaultConstructorParameters =
	{
		NameParameter,
		OrdinalParameter
	};

	@NotNull private final TypeDeclarationUser typeDeclarationUser;
	@NotNull private final FieldIntermediateRecorder fieldIntermediateRecorder;
	@NotNull private final ConstructorIntermediateRecorder constructorIntermediateRecorder;
	@NotNull private final MethodIntermediateRecorder methodIntermediateRecorder;

	public AnyEnumClassMemberVoidVisitor(@NotNull final TypeDeclarationUser typeDeclarationUser, @NotNull final FieldIntermediateRecorder fieldIntermediateRecorder, @NotNull final ConstructorIntermediateRecorder constructorIntermediateRecorder, @NotNull final MethodIntermediateRecorder methodIntermediateRecorder)
	{
		this.typeDeclarationUser = typeDeclarationUser;
		this.fieldIntermediateRecorder = fieldIntermediateRecorder;
		this.constructorIntermediateRecorder = constructorIntermediateRecorder;
		this.methodIntermediateRecorder = methodIntermediateRecorder;
	}

	@NotNull
	@Override
	protected AbstractAnnotationMemberVoidVisitor<BodyDeclarationState> useAnnotation(@NotNull final KnownReferenceTypeName annotationName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint)
	{
		return typeDeclarationUser.useAnnotation(annotationName, annotations, visibility, isStrictFloatingPoint);
	}

	@NotNull
	@Override
	protected AbstractInterfaceMemberVoidVisitor<BodyDeclarationState> useInterface(@NotNull final KnownReferenceTypeName interfaceName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] extendsTypeUsages)
	{
		return typeDeclarationUser.useInterface(interfaceName, annotations, visibility, isStrictFloatingPoint, extendsTypeUsages, typeParameters);
	}

	@NotNull
	@Override
	protected AbstractStaticClassMemberVoidVisitor<BodyDeclarationState> useStaticClass(@NotNull final KnownReferenceTypeName className, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isStrictFloatingPoint, @NotNull final Completeness completeness, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage)
	{
		return typeDeclarationUser.useStaticClass(className, visibility, annotations, isStrictFloatingPoint, completeness, implementsTypeUsages, extendsTypeUsage, typeParameters);
	}

	@NotNull
	@Override
	protected AbstractInnerOrLocalClassMemberVoidVisitor<BodyDeclarationState> useInnerClass(@NotNull final KnownReferenceTypeName className, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isStrictFloatingPoint, @NotNull final Completeness completeness, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage)
	{
		return typeDeclarationUser.useInnerClass(className, visibility, annotations, isStrictFloatingPoint, completeness, typeParameters, implementsTypeUsages, extendsTypeUsage);
	}

	@NotNull
	@Override
	protected AbstractEnumClassMemberVoidVisitor<BodyDeclarationState> useEnum(@NotNull final KnownReferenceTypeName enumName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
	{
		return typeDeclarationUser.useEnum(enumName, annotations, visibility, isStrictFloatingPoint, implementsTypeUsages);
	}

	@Override
	protected void useEnumConstant(@NotNull final String enumConstantName, @Nullable final KnownReferenceTypeName enumConstantClassName, @NotNull final List<Expression> arguments, final int ordinal, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final List<BodyDeclaration> classBody)
	{
		xxx;
	}

	@Override
	protected void useStaticField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldType, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility)
	{
		final FieldValueDetail fieldValueDetail = init == null ? Unassigned : new UsefulFieldValueDetail(init, false, false);
		final FieldIntermediate fieldIntermediate = newEnumStaticFieldIntermediate(fieldName, fieldType, fieldValueDetail, annotations, false, false, Source, typeName, visibility, fieldFinality, isTransient);
		fieldIntermediateRecorder.record(typeName, fieldIntermediate);

		// needs access to an static init
	}

	@Override
	protected void useInstanceField(@NotNull final KnownReferenceTypeName typeName, @NotNull final String fieldName, @NotNull final TypeUsage fieldType, @Nullable final Expression init, final boolean isTransient, @NotNull final FieldFinality fieldFinality, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility)
	{
		final FieldValueDetail fieldValueDetail = init == null ? Unassigned : new UsefulFieldValueDetail(init, false, false);
		final FieldIntermediate fieldIntermediate = newEnumStaticFieldIntermediate(fieldName, fieldType, fieldValueDetail, annotations, false, false, Source, typeName, visibility, fieldFinality, isTransient);
		fieldIntermediateRecorder.record(typeName, fieldIntermediate);

		// needs access to an instance init
	}

	@Override
	protected void useStaticInitializer(@NotNull final List<Statement> body)
	{
		xxx;
		// needs work to interop with fields
	}

	@Override
	protected void useInstanceInitializer(@NotNull final List<Statement> body)
	{
		xxx;//
	}

	@Override
	protected void useConstructor(@NotNull final KnownReferenceTypeName typeName, @NotNull final Visibility visibility, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final BlockStmt body, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isVarArgs, @NotNull final TypeUsage constructorTypeUsage)
	{
		final int length = parameterIntermediates.length;
		final ParameterIntermediate[] adjustedParameters = new ParameterIntermediate[length + 2];

		// slight chance of collision
		adjustedParameters[0] = NameParameter;
		adjustedParameters[1] = OrdinalParameter;
		arraycopy(parameterIntermediates, 0, adjustedParameters, 2, length);


		final List<Statement> statements = body.getStmts();
		if (statements == null || statements.isEmpty())
		{
			throw new IllegalArgumentException("body must have statements");
		}
		final Statement statement = statements.get(0);


		final ConstructorIntermediate constructorIntermediate = new ConstructorIntermediate(IntermediateCreator.Enum, Source, typeName, visibility, annotations, constructorTypeUsage, false, false, parameterIntermediates, throwsArray, isVarArgs, false, bodyIntermediate, typeParameters);
		constructorIntermediateRecorder.record(typeName, constructorIntermediate);

		xxx; // needs transformation [super call / this call]
	}

	@Override
	protected void useStaticMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final BodyIntermediate bodyIntermediate, final boolean isStrictFloatingPoint, final boolean isSynchronized, @NotNull final Visibility visibility, final boolean isVarArgs)
	{
		final MethodIntermediate methodIntermediate = newEnumStaticMethodIntermediate(methodName, returnTypeUsage, parameterIntermediates, false, false, throwsArray, isVarArgs, bodyIntermediate, annotations, Source, typeName, visibility, false, false, isNative);
		methodIntermediateRecorder.record(typeName, methodIntermediate);
	}

	@Override
	protected void useInstanceMethod(@NotNull final KnownReferenceTypeName typeName, @NotNull final String methodName, @NotNull final UsefulTypeParameters usefulTypeParameters, @NotNull final TypeUsage returnTypeUsage, @NotNull final ParameterIntermediate[] parameterIntermediates, @NotNull final UnresolvedReferenceTypeName[] throwsArray, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final BodyIntermediate bodyIntermediate, final boolean isStrictFloatingPoint, final boolean isSynchronized, @NotNull final Visibility visibility, @NotNull final Completeness completeness, final boolean isVarArgs)
	{
		final MethodIntermediate methodIntermediate = newEnumInstanceMethodIntermediate(methodName, returnTypeUsage, parameterIntermediates, false, false, throwsArray, isVarArgs, bodyIntermediate, annotations, Source, typeName, visibility, completeness, false, false, isNative);
		methodIntermediateRecorder.record(typeName, methodIntermediate);
	}

	protected void end(@NotNull final KnownReferenceTypeName typeName, @NotNull final TypeUsage constructorTypeUsage)
	{
		// finalize initializers
		x;

		// Add a constructor
		if (constructorIntermediateRecorder.hasNoConstructors(typeName))
		{
			final List<Statement> statements = new ArrayList<>(2);

			final List<Expression> args = new ArrayList<>(2);
			args.add(new NameExpr(NameParameter.parameterName()));
			args.add(new NameExpr(OrdinalParameter.parameterName()));
			final ExplicitConstructorInvocationStmt explicitConstructorInvocationStmt = new ExplicitConstructorInvocationStmt(false, null, args);
			explicitConstructorInvocationStmt.setTypeArgs(emptyList());
			statements.add(explicitConstructorInvocationStmt);

			statements.add(new ReturnStmt());

			final TypeResolverContext typeResolverContext;
			final BodyIntermediate bodyIntermediate = new JavaparserBodyIntermediate(statements, typeResolverContext);
			final ConstructorIntermediate constructorIntermediate = new ConstructorIntermediate(IntermediateCreator.Enum, Source, typeName, Private, EmptyAnnotations, constructorTypeUsage, false, false, DefaultConstructorParameters, EmptyKnownReferenceTypeNames, false, false, bodyIntermediate, NoUsefulTypeParameters);
			constructorIntermediateRecorder.record(typeName, constructorIntermediate);
		}
	}

	@NotNull
	private static ClassOrInterfaceType classOrInterfaceTypeWithNoTypeArguments(@NotNull final String name)
	{
		final ClassOrInterfaceType classOrInterfaceType = new ClassOrInterfaceType(null, name);
		classOrInterfaceType.setTypeArgs(emptyList());
		return classOrInterfaceType;
	}

	@NotNull
	private static ClassOrInterfaceType classOrInterfaceTypeWithOneTypeArguments(@NotNull final String name, @NotNull final ClassOrInterfaceType typeArgument)
	{
		final ClassOrInterfaceType classOrInterfaceType = new ClassOrInterfaceType(null, name);
		classOrInterfaceType.setTypeArgs(singletonList(typeArgument));
		return classOrInterfaceType;
	}
}
