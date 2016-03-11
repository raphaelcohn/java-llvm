package com.stormmq.java.parsing.adaptors.javaparser.namedTypeDeclarationUsers;

import com.github.javaparser.ast.TypeParameter;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.PrimitiveType.Primitive;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.stormmq.java.parsing.adaptors.javaparser.dead.BodyDeclarationsCreator;
import com.stormmq.java.parsing.adaptors.javaparser.ast.JavaparserModifiers;
import com.stormmq.java.parsing.adaptors.javaparser.referenceTypeNameMakers.ReferenceTypeNameMaker;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.EnumConstantDeclarationWrapper;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.EnumDeclarationWrapper;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.TypeDeclarationWrapper;
import com.stormmq.java.parsing.ast.intermediateRecorders.IntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.InitializersIntermediate;
import com.stormmq.java.parsing.ast.intermediates.IntermediateCreator;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.StringConstants;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.ReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.ImplementedTypeUsage;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.stormmq.java.parsing.adaptors.javaparser.ast.JavaparserModifiers.*;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.keep.AnnotationExprHelper.EmptyAnnotationExprList;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.keep.ClassOrInterfaceTypeHelper.convertToReferenceTypeNames;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.FIELDSTUFF.STUFF.knownReferenceTypeNameToClassOrInterfaceTypeScope;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.FIELDSTUFF.FieldInit.fieldInitializer;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.NameExprHelper.parentNameToNameExpr;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.NameExprHelper.EmptyNameExprList;
import static com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.ConstructorDeclarationWrapper.ConstructorName;
import static com.stormmq.java.parsing.ast.details.fieldValueDetails.UnassignedFieldValueDetail.Unassigned;
import static com.stormmq.java.parsing.ast.intermediates.IntermediateCreator.Enum;
import static com.stormmq.java.parsing.ast.intermediates.Origin.Source;
import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate.EmptyAnnotations;
import static com.stormmq.java.parsing.utilities.StringConstants._super;
import static com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName.EmptyKnownReferenceTypeNames;

public final class EnumNamedTypeDeclarationUser implements NamedTypeDeclarationUser<EnumDeclaration>
{
	@NotNull private static final List<Parameter> NoParameters = Collections.emptyList();
	@NotNull private static final ClassOrInterfaceType StringType = knownReferenceTypeNameToClassOrInterfaceTypeScope(JavaLangString);
	@NotNull private static final PrimitiveType intType = new PrimitiveType(Primitive.Int);
	@NotNull @NonNls private static final String InternalNameOfValuesField = "$values";
	@NotNull @NonNls private static final String parameterName = "$enum$name";
	@NotNull @NonNls private static final String parameterOrdinal = "$enum$ordinal";
	@NotNull @NonNls private static final String valueOf = "valueOf";

	@NotNull private final IntermediateRecorder intermediateRecorder;
	@NotNull private final BodyDeclarationsCreator bodyDeclarationsCreator;

	public EnumNamedTypeDeclarationUser(@NotNull final IntermediateRecorder intermediateRecorder, @NotNull final BodyDeclarationsCreator bodyDeclarationsCreator)
	{
		this.intermediateRecorder = intermediateRecorder;
		this.bodyDeclarationsCreator = bodyDeclarationsCreator;
	}

	@Override
	public void use(@NotNull final ReferenceTypeNameMaker referenceTypeNameMaker, @NotNull final KnownReferenceTypeName ourReferenceTypeName, @NotNull final EnumDeclaration typeDeclaration)
	{
		final TypeDeclarationWrapper typeDeclarationWrapper = new TypeDeclarationWrapper(typeDeclaration);

		final EnumDeclarationWrapper enumDeclarationWrapper = new EnumDeclarationWrapper(typeDeclaration);


		final JavaparserModifiers modifiers = typeDeclarationWrapper.modifiers();
		final ReferenceTypeName[] interfaceTypeReferenceNames = convertToReferenceTypeNames(referenceTypeNameMaker, typeDeclaration.getImplements());
		final AnnotationInstanceIntermediate[] annotationInstanceIntermediates = typeDeclarationWrapper.parseAnnotationInstanceDetails(referenceTypeNameMaker);

		Enum.createTypeIntermediate(Source, modifiers, ourReferenceTypeName, JavaLangEnum, interfaceTypeReferenceNames, annotationInstanceIntermediates, intermediateRecorder, modifiers.isDeprecatedInByteCode(), modifiers.isSynthetic(), modifiers.visibility(), modifiers.isStrictFloatingPoint());

		final InitializersIntermediate initializersIntermediate = new InitializersIntermediate(true);

		final List<EnumConstantDeclarationWrapper> enumConstantDeclarationWrappers = enumDeclarationWrapper.enumConstants();

		// We MUST handle these BEFORE mucking with synthetic things so that we get things in the right place
		int ordinal = 0;
		int anonymousClassIndex = 1;
		for (EnumConstantDeclarationWrapper enumConstantDeclarationWrapper : enumConstantDeclarationWrappers)
		{
			anonymousClassIndex = useEnumConstantDeclaration(referenceTypeNameMaker, enumConstantDeclarationWrapper, anonymousClassIndex, initializersIntermediate, ordinal, ourReferenceTypeName);
			ordinal++;
		}

		// https://docs.oracle.com/javase/tutorial/reflect/member/methodparameterreflection.html
		addSyntheticValuesMethod(ourReferenceTypeName, typeDeclarationWrapper);
		addSyntheticValueOfMethod(ourReferenceTypeName, typeDeclarationWrapper);
		addSyntheticValuesField(ourReferenceTypeName, typeDeclarationWrapper, enumConstantDeclarationWrappers);
		addSyntheticConstructors(typeDeclarationWrapper);

		bodyDeclarationsCreator.recordClassFieldsAndMethods(referenceTypeNameMaker, typeDeclarationWrapper, initializersIntermediate, ourReferenceTypeName, Enum);
	}

	private int useEnumConstantDeclaration(@NotNull final ReferenceTypeNameMaker referenceTypeNameMaker, @NotNull final EnumConstantDeclarationWrapper enumConstantDeclarationWrapper, final int anonymousClassIndex, @NotNull final InitializersIntermediate initializersIntermediate, final int ordinal, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName)
	{
		final KnownReferenceTypeName enumConstantClassName = ourKnownReferenceTypeName.anonymous(anonymousClassIndex);
		final boolean doesNotHaveASubClass = enumConstantDeclarationWrapper.hasEmptyBody();
		final boolean hasASubClass = !doesNotHaveASubClass;
		if (hasASubClass)
		{
			IntermediateCreator.Class.createTypeIntermediate(Source, EnumClassConstantModifiers, enumConstantClassName, enumConstantClassName, EmptyKnownReferenceTypeNames, EmptyAnnotations, intermediateRecorder, EnumClassConstantModifiers.isDeprecatedInByteCode(), EnumClassConstantModifiers.isSynthetic(), EnumClassConstantModifiers.visibility(), EnumClassConstantModifiers.isStrictFloatingPoint());
		}

		final String fieldName = enumConstantDeclarationWrapper.constantName();
		final TypeUsage fieldType = new ImplementedTypeUsage(ourKnownReferenceTypeName, 0);
		final AnnotationInstanceIntermediate[] annotationInstanceIntermediates = enumConstantDeclarationWrapper.parseAnnotationInstanceDetails(referenceTypeNameMaker);

		Enum.createFieldIntermediate(fieldName, EnumConstantModifiers, fieldType, Unassigned, annotationInstanceIntermediates, intermediateRecorder, ourKnownReferenceTypeName, Source, EnumConstantModifiers.isDeprecatedInByteCode(), EnumConstantModifiers.isSynthetic());


		final KnownReferenceTypeName constructor = hasASubClass ? enumConstantClassName : ourKnownReferenceTypeName;
		final ClassOrInterfaceType type = knownReferenceTypeNameToClassOrInterfaceTypeScope(constructor);
		final List<Expression> withImplicitArguments = enumConstantDeclarationWrapper.withImplicitArguments(ordinal);
		final Expression init = new ObjectCreationExpr(null, type, withImplicitArguments);
		initializersIntermediate.addStaticInitializer(fieldInitializer(init, fieldName, ourKnownReferenceTypeName));

		if (doesNotHaveASubClass)
		{
			return anonymousClassIndex;
		}

		bodyDeclarationsCreator.recordClassFieldsAndMethods(referenceTypeNameMaker.anonymous(anonymousClassIndex, enumConstantDeclarationWrapper), enumConstantDeclarationWrapper, enumConstantClassName, IntermediateCreator.Class);

		return anonymousClassIndex + 1;
	}

	private void addSyntheticValuesMethod(@NotNull final KnownReferenceTypeName ourReferenceTypeName, @NotNull final TypeDeclarationWrapper typeDeclarationWrapper)
	{
		/*
    		public static Colors[] values()
    		{
        		return $values;
    		}
    	*/

		final Type returnType = ourEnumAsArrayType(ourReferenceTypeName);
		final List<TypeParameter> typeParameters = null;
		final List<Parameter> parameters = NoParameters;

		final BlockStmt body = new BlockStmt(Collections.<Statement>singletonList(new ReturnStmt(new FieldAccessExpr(null, InternalNameOfValuesField)))); // ==>  return $values;

		final MethodDeclaration methodDeclaration = new MethodDeclaration(PublicStaticMethodModifiers.toOpcodes(), AnnotationExprHelper.EmptyAnnotationExprList, typeParameters, returnType, "values", parameters, 0, EmptyNameExprList, body);

		typeDeclarationWrapper.addMethod(methodDeclaration);
	}

	private void addSyntheticValueOfMethod(@NotNull final KnownReferenceTypeName ourReferenceTypeName, @NotNull final TypeDeclarationWrapper typeDeclarationWrapper)
	{
		/*
    	public static Colors valueOf(String name)
    	{
        	return (Colors)java.lang.Enum.valueOf(Colors.class, name);
    	}
		*/

		final ClassOrInterfaceType returnType = knownReferenceTypeNameToClassOrInterfaceTypeScope(ourReferenceTypeName);
		final List<TypeParameter> typeParameters = null;

		final Parameter parameter = new Parameter(FinalParameterModifiers.toOpcodes(), StringType, new VariableDeclaratorId("name"));
		final List<Parameter> parameters = Collections.singletonList(parameter);

		final List<Expression> args = new ArrayList<>(2);
		args.add(new ClassExpr(returnType));
		args.add(new NameExpr("name"));
		final BlockStmt body = new BlockStmt(Collections.<Statement>singletonList(new ReturnStmt(new CastExpr(returnType, new MethodCallExpr(parentNameToNameExpr(JavaLangEnum), valueOf, args)))));

		final MethodDeclaration methodDeclaration = new MethodDeclaration(PublicStaticMethodModifiers.toOpcodes(), EmptyAnnotationExprList, typeParameters, returnType, valueOf, parameters, 0, EmptyNameExprList, body);

		typeDeclarationWrapper.addMethod(methodDeclaration);
	}

	private void addSyntheticValuesField(@NotNull final KnownReferenceTypeName ourReferenceTypeName, @NotNull final TypeDeclarationWrapper typeDeclarationWrapper, @NotNull final List<EnumConstantDeclarationWrapper> enumConstantDeclarationWrappers)
	{
		/*
			public static final Colors[] value = {All enum constants}
		 */
		final int modifiers = PublicStaticFinalFieldModifiers.toOpcodes();
		final List<Expression> enumConstants = new ArrayList<>(enumConstantDeclarationWrappers.size());
		for (EnumConstantDeclarationWrapper enumConstantDeclarationWrapper : enumConstantDeclarationWrappers)
		{
			final String constantName = enumConstantDeclarationWrapper.constantName();
			enumConstants.add(new NameExpr(constantName));
		}
		final Expression init = new ArrayInitializerExpr(enumConstants);
		final FieldDeclaration fieldDeclaration = new FieldDeclaration(modifiers, ourEnumAsArrayType(ourReferenceTypeName), new VariableDeclarator(new VariableDeclaratorId(InternalNameOfValuesField), init));

		typeDeclarationWrapper.addFieldAtFront(fieldDeclaration);
	}

	private void addSyntheticConstructors(@NotNull final TypeDeclarationWrapper typeDeclarationWrapper)
	{
		// If not constructors, add one
		final List<ConstructorDeclaration> constructors = typeDeclarationWrapper.constructors();
		if (constructors.isEmpty())
		{
			final List<TypeParameter> typeParameters = null;
			final List<Parameter> parameters = new ArrayList<>(2);
			parameters.add(new Parameter(FinalParameterModifiers.toOpcodes(), StringType, new VariableDeclaratorId(parameterName)));
			parameters.add(new Parameter(FinalParameterModifiers.toOpcodes(), intType, new VariableDeclaratorId(parameterOrdinal)));

			final List<Expression> args = new ArrayList<>(2);
			args.add(new NameExpr(parameterName));
			args.add(new NameExpr(parameterOrdinal));
			// Not 100% sure about this
			final MethodCallExpr aSuper = new MethodCallExpr(null, _super, args);
			final BlockStmt body = new BlockStmt(Collections.<Statement>singletonList(new ReturnStmt(aSuper)));

			final ConstructorDeclaration constructorDeclaration = new ConstructorDeclaration(PrivateConstructorModifiers.toOpcodes(), EmptyAnnotationExprList, typeParameters, ConstructorName, parameters, EmptyNameExprList, body);

			typeDeclarationWrapper.addConstructor(constructorDeclaration);

			return;
		}

		// Extract first line, check if it is 'this' or anything else (except super call, which isn't allowed).
		// If 'this', then re-write this call
		// Otherwise insert a super call
		System.err.println("ENUM: addSyntheticConstructors: re-write existing constructors");
	}

	@NotNull
	private static Type ourEnumAsArrayType(@NotNull final KnownReferenceTypeName ourReferenceTypeName)
	{
		final ClassOrInterfaceType componentType = knownReferenceTypeNameToClassOrInterfaceTypeScope(ourReferenceTypeName);
		return new ReferenceType(componentType, 1);
	}
}
