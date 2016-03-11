package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.github.javaparser.ast.body.*;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.UsefulVoidVisitor;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.BodyDeclarationState;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.PackageState;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

import static com.github.javaparser.ast.body.ModifierSet.*;

public abstract class AbstractTopLevelTypeDeclarationVoidVisitor<S extends PackageState> extends AbstractTypeDeclarationVoidVisitor<S>
{
	protected AbstractTopLevelTypeDeclarationVoidVisitor()
	{
	}

	@NotNull
	@Override
	protected final AbstractAnnotationMemberVoidVisitor<BodyDeclarationState> visitAnnotation(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final KnownReferenceTypeName annotationName, final boolean isStrictFloatingPoint, @NotNull final Visibility visibility)
	{
		return useAnnotation(annotationName, annotations, visibility, isStrictFloatingPoint);
	}

	@NotNull
	@Override
	protected final AbstractInterfaceMemberVoidVisitor<BodyDeclarationState> visitInterface(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final UsefulTypeParameters typeParameters, final boolean isStrictFloatingPoint, @NotNull final KnownReferenceTypeName interfaceName, @NotNull final Visibility visibility, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] extendsTypeUsages)
	{
		return useInterface(interfaceName, annotations, visibility, isStrictFloatingPoint, typeParameters, extendsTypeUsages);
	}

	@Override
	protected final AbstractStaticClassMemberVoidVisitor<BodyDeclarationState> visitStaticClass(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final UsefulTypeParameters typeParameters, final boolean isStrictFloatingPoint, @NotNull final KnownReferenceTypeName className, @NotNull final Completeness completeness, @NotNull final Visibility visibility, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
	{
		return useStaticClass(className, visibility, annotations, isStrictFloatingPoint, completeness, typeParameters, implementsTypeUsages, extendsTypeUsage);
	}

	@Override
	protected final AbstractClassMemberVoidVisitor<BodyDeclarationState> visitNonStaticClass(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final UsefulTypeParameters typeParameters, final boolean isStrictFloatingPoint, @NotNull final KnownReferenceTypeName className, @NotNull final Completeness completeness, @NotNull final Visibility visibility, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
	{
		throw new IllegalArgumentException("A top-level class can not be static");
	}

	@NotNull
	@Override
	protected final AbstractInnerOrLocalClassMemberVoidVisitor<BodyDeclarationState> useInnerClass(@NotNull final KnownReferenceTypeName className, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isStrictFloatingPoint, @NotNull final Completeness completeness, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage)
	{
		throw new IllegalArgumentException("A top-level class can not be static");
	}

	@NotNull
	@Override
	protected final AbstractEnumClassMemberVoidVisitor<BodyDeclarationState> visitEnum(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final KnownReferenceTypeName enumName, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages)
	{
		return useEnum(enumName, annotations, visibility, isStrictFloatingPoint, implementsTypeUsages);
	}

	@Override
	protected final int additionalModifierGuard(@NotNull final String what, final int modifiers)
	{
		if (isPrivate(modifiers))
		{
			throw new IllegalArgumentException("A top level " + what + " can not be private");
		}

		if (isProtected(modifiers))
		{
			throw new IllegalArgumentException("A top level " + what + " can not be protected");
		}

		if (isStatic(modifiers))
		{
			throw new IllegalArgumentException("A top level " + what + " can not be static");
		}

		return modifiers;
	}

	@Override
	public final void visit(@NotNull final EnumConstantDeclaration enumConstantDeclaration, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final FieldDeclaration fieldDeclaration, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final InitializerDeclaration initializerDeclaration, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final ConstructorDeclaration constructorDeclaration, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final MethodDeclaration methodDeclaration, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final AnnotationMemberDeclaration annotationMemberDeclaration, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}

	@Override
	public final void visit(@NotNull final EmptyMemberDeclaration emptyMemberDeclaration, @NotNull final S state)
	{
		UsefulVoidVisitor.throwShouldNotBeVisited();
	}
}
