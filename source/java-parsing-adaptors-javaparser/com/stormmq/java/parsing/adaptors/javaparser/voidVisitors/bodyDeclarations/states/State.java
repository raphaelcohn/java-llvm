package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.UnresolvedReferenceTypeName;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameter;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.ParameterIntermediate;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface State
{
	@NotNull
	Object unresolvedReferenceTypeNameMaker();

	@NotNull
	UsefulTypeParameters usefulTypeParametersOfEncapsulatingDeclaration();

	@NotNull
	KnownReferenceTypeName typeName(@NotNull TypeDeclaration typeDeclaration);

	@NotNull
	AnnotationInstanceIntermediate[] convertToAnnotations(@Nullable final List<AnnotationExpr> annotations);

	@NotNull
	UnresolvedReferenceTypeName[] convertNameExprToUnresolvedReferenceTypeNames(@Nullable final List<NameExpr> throwsList);

	@SuppressWarnings("MethodCanBeVariableArityMethod")
	@NotNull
	ParameterIntermediate[] convertParameters(@Nullable final List<Parameter> parameters, final boolean[] isVarArgsArray);

	@NotNull
	UsefulTypeParameter[] convertTypeParameters(@NotNull final ClassOrInterfaceDeclaration classOrInterfaceDeclaration);

	void visitMembers(@NotNull final VoidVisitor<BodyDeclarationState> bodyDeclarationsVisitor, @NotNull final List<BodyDeclaration> bodyDeclarations, @NotNull final KnownReferenceTypeName typeName);

	@NotNull
	ClassOrInterfaceIntermediateTypeArgument convertToClassOrInterfaceIntermediateTypeArgument(@Nullable final ClassOrInterfaceType classOrInterfaceType);

	@NotNull
	TypeUsage fieldTypeUsage(@NotNull final Type fieldType, final int cStyleArrayCount);
}
