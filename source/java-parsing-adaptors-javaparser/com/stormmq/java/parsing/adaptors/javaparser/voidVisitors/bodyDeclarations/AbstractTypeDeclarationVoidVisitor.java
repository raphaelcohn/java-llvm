package com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.states.*;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameter;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.*;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static com.github.javaparser.ast.body.ModifierSet.*;
import static com.stormmq.java.parsing.adaptors.javaparser.StringConstants.*;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.assertNotNull;
import static com.stormmq.java.parsing.adaptors.javaparser.helpers.JavaparserHelper.nullToEmptyList;
import static com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument.EmptyClassOrInterfaceIntermediateTypeUsages;
import static com.stormmq.java.parsing.ast.intermediateTypeArguments.ClassOrInterfaceIntermediateTypeArgument.JavaLangObjectTypeArgument;
import static com.stormmq.java.parsing.utilities.Completeness.*;
import static com.stormmq.java.parsing.utilities.StringConstants._enum;
import static com.stormmq.java.parsing.utilities.string.StringUtilities.aOrAn;
import static com.stormmq.java.parsing.utilities.Visibility.*;

public abstract class AbstractTypeDeclarationVoidVisitor<S extends State> extends AbstractUsefulTypeDeclarationVoidVisitor<S>
{
	protected AbstractTypeDeclarationVoidVisitor()
	{
	}

	@SuppressWarnings("ClassReferencesSubclass")
	@NotNull
	protected abstract AbstractAnnotationMemberVoidVisitor<BodyDeclarationState> visitAnnotation(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final KnownReferenceTypeName annotationName, final boolean isStrictFloatingPoint, @NotNull final Visibility visibility);

	@SuppressWarnings("ClassReferencesSubclass")
	@NotNull
	protected abstract AbstractInterfaceMemberVoidVisitor<BodyDeclarationState> visitInterface(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final UsefulTypeParameters typeParameters, final boolean isStrictFloatingPoint, @NotNull final KnownReferenceTypeName interfaceName, @NotNull final Visibility visibility, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] extendsTypeUsages);

	@SuppressWarnings("ClassReferencesSubclass")
	protected abstract AbstractStaticClassMemberVoidVisitor<BodyDeclarationState> visitStaticClass(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final UsefulTypeParameters typeParameters, final boolean isStrictFloatingPoint, @NotNull final KnownReferenceTypeName className, @NotNull final Completeness completeness, @NotNull final Visibility visibility, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages);

	@SuppressWarnings("ClassReferencesSubclass")
	protected abstract AbstractClassMemberVoidVisitor<BodyDeclarationState> visitNonStaticClass(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final UsefulTypeParameters typeParameters, final boolean isStrictFloatingPoint, @NotNull final KnownReferenceTypeName className, @NotNull final Completeness completeness, @NotNull final Visibility visibility, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages);

	@SuppressWarnings("ClassReferencesSubclass")
	@NotNull
	protected abstract AbstractEnumClassMemberVoidVisitor<BodyDeclarationState> visitEnum(@NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final KnownReferenceTypeName enumName, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages);

	@SuppressWarnings("ClassReferencesSubclass")
	@NotNull
	protected abstract AbstractAnnotationMemberVoidVisitor<BodyDeclarationState> useAnnotation(@NotNull final KnownReferenceTypeName annotationName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint);

	@SuppressWarnings("ClassReferencesSubclass")
	@NotNull
	protected abstract AbstractInterfaceMemberVoidVisitor<BodyDeclarationState> useInterface(@NotNull final KnownReferenceTypeName interfaceName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] extendsTypeUsages);

	@SuppressWarnings("ClassReferencesSubclass")
	@NotNull
	protected abstract AbstractStaticClassMemberVoidVisitor<BodyDeclarationState> useStaticClass(@NotNull final KnownReferenceTypeName className, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isStrictFloatingPoint, @NotNull final Completeness completeness, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage);

	@SuppressWarnings("ClassReferencesSubclass")
	@NotNull
	protected abstract AbstractInnerOrLocalClassMemberVoidVisitor<BodyDeclarationState> useInnerClass(@NotNull final KnownReferenceTypeName className, @NotNull final Visibility visibility, @NotNull final AnnotationInstanceIntermediate[] annotations, final boolean isStrictFloatingPoint, @NotNull final Completeness completeness, @NotNull final UsefulTypeParameters typeParameters, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages, @NotNull final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage);

	@SuppressWarnings("ClassReferencesSubclass")
	@NotNull
	protected abstract AbstractEnumClassMemberVoidVisitor<BodyDeclarationState> useEnum(@NotNull final KnownReferenceTypeName enumName, @NotNull final AnnotationInstanceIntermediate[] annotations, @NotNull final Visibility visibility, final boolean isStrictFloatingPoint, @NotNull final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages);

	@Override
	public final void visit(@NotNull final AnnotationDeclaration annotationDeclaration, @NotNull final S state)
	{
		final KnownReferenceTypeName annotationName = state.typeName(annotationDeclaration);
		final List<BodyDeclaration> bodyDeclarations = bodyDeclarations(annotationDeclaration);
		final AnnotationInstanceIntermediate[] annotations = annotations(annotationDeclaration, state);
		final int modifiers = annotationModifiers(annotationDeclaration);
		final boolean isStrictFloatingPoint = isStrictfp(modifiers);
		final Visibility visibility = visibility(modifiers, annotation);

		@NotNull final VoidVisitor<BodyDeclarationState> bodyDeclarationsVisitor = visitAnnotation(annotations, annotationName, isStrictFloatingPoint, visibility);

		state.visitMembers(bodyDeclarationsVisitor, bodyDeclarations, annotationName);
	}

	@Override
	public final void visit(@NotNull final ClassOrInterfaceDeclaration classOrInterfaceDeclaration, @NotNull final S state)
	{
		final List<BodyDeclaration> bodyDeclarations = bodyDeclarations(classOrInterfaceDeclaration);
		final AnnotationInstanceIntermediate[] annotations = annotations(classOrInterfaceDeclaration, state);

		xxx; // state is wrong place, as the class or interface is now in scope
		final UsefulTypeParameter[] typeParameters1 = state.convertTypeParameters(classOrInterfaceDeclaration);
		final UsefulTypeParameters usefulTypeParameters = state.usefulTypeParametersOfEncapsulatingDeclaration();
		@NotNull final UsefulTypeParameters typeParameters = usefulTypeParameters.descend(typeParameters1);

		final int modifiers = classOrInterfaceDeclaration.getModifiers();
		final boolean isStrictFloatingPoint = isStrictfp(modifiers);
		final KnownReferenceTypeName typeName = state.typeName(classOrInterfaceDeclaration);

		@Nullable final List<ClassOrInterfaceType> extendsList = classOrInterfaceDeclaration.getExtends();
		@Nullable final List<ClassOrInterfaceType> implementsList = classOrInterfaceDeclaration.getImplements();

		@NotNull final VoidVisitor<BodyDeclarationState> bodyDeclarationsVisitor;
		if (classOrInterfaceDeclaration.isInterface())
		{
			interfaceModifiers(classOrInterfaceDeclaration);

			if ((implementsList != null) && !implementsList.isEmpty())
			{
				throw new IllegalArgumentException("An interface should not implement anything");
			}

			final ClassOrInterfaceIntermediateTypeArgument[] extendsTypeUsages = convertToClassOrInterfaceIntermediateTypeArguments(state, extendsList);

			final Visibility visibility = visibility(modifiers, StringConstants._class);

			bodyDeclarationsVisitor = visitInterface(annotations, typeParameters, isStrictFloatingPoint, typeName, visibility, extendsTypeUsages);
		}
		else
		{
			classModifiers(classOrInterfaceDeclaration);
			final Completeness completeness = completeness(modifiers);

			final int size = (extendsList == null) ? 0 : extendsList.size();
			final ClassOrInterfaceIntermediateTypeArgument extendsTypeUsage;
			if (size == 0)
			{
				extendsTypeUsage = JavaLangObjectTypeArgument;
			}
			else if (size == 1)
			{
				extendsTypeUsage = state.convertToClassOrInterfaceIntermediateTypeArgument(extendsList.get(0));
			}
			else
			{
				throw new IllegalArgumentException("A class can not extend more than one class");
			}

			final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages = convertToClassOrInterfaceIntermediateTypeArguments(state, implementsList);

			final Visibility visibility = visibility(modifiers, StringConstants._interface);
			final boolean isStatic = isStatic(modifiers);
			if (isStatic)
			{
				bodyDeclarationsVisitor = visitStaticClass(annotations, typeParameters, isStrictFloatingPoint, typeName, completeness, visibility, extendsTypeUsage, implementsTypeUsages);
			}
			else
			{
				bodyDeclarationsVisitor = visitNonStaticClass(annotations, typeParameters, isStrictFloatingPoint, typeName, completeness, visibility, extendsTypeUsage, implementsTypeUsages);
			}
		}

		state.visitMembers(bodyDeclarationsVisitor, bodyDeclarations, typeName);
	}

	@Override
	public final void visit(@NotNull final EnumDeclaration enumDeclaration, @NotNull final S state)
	{
		final List<EnumConstantDeclaration> enumConstantDeclarations = nullToEmptyList(enumDeclaration.getEntries());
		final List<BodyDeclaration> members = bodyDeclarations(enumDeclaration);
		final List<BodyDeclaration> bodyDeclarations = new ArrayList<>(enumConstantDeclarations.size() + members.size());
		for (final EnumConstantDeclaration enumConstantDeclaration : enumConstantDeclarations)
		{
			bodyDeclarations.add(enumConstantDeclaration);
		}
		for (final BodyDeclaration member : members)
		{
			bodyDeclarations.add(member);
		}

		final int modifiers = enumModifiers(enumDeclaration);
		final boolean isStrictFloatingPoint = isStrictfp(modifiers);
		final KnownReferenceTypeName enumName = state.typeName(enumDeclaration);
		final Visibility visibility = visibility(modifiers, _enum);
		final AnnotationInstanceIntermediate[] annotations = annotations(enumDeclaration, state);

		@Nullable final List<ClassOrInterfaceType> implementsList = enumDeclaration.getImplements();
		final ClassOrInterfaceIntermediateTypeArgument[] implementsTypeUsages = convertToClassOrInterfaceIntermediateTypeArguments(state, implementsList);

		@NotNull final VoidVisitor<BodyDeclarationState> bodyDeclarationsVisitor = visitEnum(annotations, enumName, visibility, isStrictFloatingPoint, implementsTypeUsages);
		state.visitMembers(bodyDeclarationsVisitor, bodyDeclarations, enumName);
	}

	@Override
	public final void visit(@NotNull final EmptyTypeDeclaration emptyTypeDeclaration, @NotNull final S state)
	{
		guardShouldNotHaveAnnotations(emptyTypeDeclaration, "emptyTypeDeclaration");
	}

	protected static void guardShouldNotHaveAnnotations(@NotNull final BodyDeclaration bodyDeclaration, @NonNls @NotNull final String what)
	{
		@Nullable final List<AnnotationExpr> annotations = bodyDeclaration.getAnnotations();
		if ((annotations != null) && !annotations.isEmpty())
		{
			throw new IllegalArgumentException(what + " should not have annotations");
		}
	}

	@NotNull
	protected static List<BodyDeclaration> bodyDeclarations(@NotNull final TypeDeclaration typeDeclaration)
	{
		final List<BodyDeclaration> classBody = nullToEmptyList(typeDeclaration.getMembers());
		for (final BodyDeclaration bodyDeclaration : classBody)
		{
			assertNotNull(bodyDeclaration);
		}
		return classBody;
	}

	@NotNull
	protected final AnnotationInstanceIntermediate[] annotations(@NotNull final BodyDeclaration bodyDeclaration, @NotNull final S state)
	{
		final List<AnnotationExpr> annotations = bodyDeclaration.getAnnotations();
		return state.convertToAnnotations(annotations);
	}

	@NotNull
	private ClassOrInterfaceIntermediateTypeArgument[] convertToClassOrInterfaceIntermediateTypeArguments(@NotNull final S state, @Nullable final List<ClassOrInterfaceType> extendsOrImplements)
	{
		if (extendsOrImplements == null)
		{
			return EmptyClassOrInterfaceIntermediateTypeUsages;
		}

		final int size = extendsOrImplements.size();
		if (size == 0)
		{
			return EmptyClassOrInterfaceIntermediateTypeUsages;
		}

		final ClassOrInterfaceIntermediateTypeArgument[] classOrInterfaceIntermediateTypeArguments = new ClassOrInterfaceIntermediateTypeArgument[size];
		for (int index = 0; index < size; index++)
		{
			classOrInterfaceIntermediateTypeArguments[index] = state.convertToClassOrInterfaceIntermediateTypeArgument(extendsOrImplements.get(index));
		}
		return classOrInterfaceIntermediateTypeArguments;
	}

	@NotNull
	protected static Visibility visibility(final int modifiers, @NonNls @NotNull  final String what)
	{
		guardVisibility(what, modifiers);

		if (isPublic(modifiers))
		{
			return Public;
		}

		if (isPrivate(modifiers))
		{
			return Private;
		}

		if (isProtected(modifiers))
		{
			return Protected;
		}

		return PackageLocal;
	}

	@NotNull
	private static Completeness completeness(final int modifiers)
	{
		if (isAbstract(modifiers))
		{
			return Abstract;
		}

		if (isFinal(modifiers))
		{
			return Final;
		}

		return Normal;
	}

	private int annotationModifiers(@NotNull final AnnotationDeclaration annotationDeclaration)
	{
		return guardNotFinal(annotation, guardTypeModifiers(annotation, annotationDeclaration));
	}

	private int interfaceModifiers(@NotNull final ClassOrInterfaceDeclaration interfaceDeclaration)
	{
		return guardNotFinal(StringConstants._interface, guardTypeModifiers(StringConstants._interface, interfaceDeclaration));
	}

	private int classModifiers(@NotNull final ClassOrInterfaceDeclaration classDeclaration)
	{
		return guardTypeModifiers(StringConstants._class, classDeclaration);
	}

	private int enumModifiers(@NotNull final EnumDeclaration enumDeclaration)
	{
		return guardNotAbstract(_enum, guardNotFinal(_enum, guardTypeModifiers(_enum, enumDeclaration)));
	}

	private int guardTypeModifiers(@NotNull final String what, @NotNull final TypeDeclaration typeDeclaration)
	{
		final int modifiers = typeDeclaration.getModifiers();

		if (isTransient(modifiers))
		{
			throw new IllegalArgumentException(aOrAn(what) + _can_not_be_transient);
		}

		if (isVolatile(modifiers))
		{
			throw new IllegalArgumentException(aOrAn(what) + _can_not_be_volatile);
		}

		if (isNative(modifiers))
		{
			throw new IllegalArgumentException(aOrAn(what) + " can not be native");
		}

		if (isSynchronized(modifiers))
		{
			throw new IllegalArgumentException(aOrAn(what) + " can not be synchronized");
		}

		if (isFinal(modifiers) && isAbstract(modifiers))
		{
			throw new IllegalArgumentException(aOrAn(what) + " can not be both final and abstract");
		}

		guardVisibility(what, modifiers);
		additionalModifierGuard(what, modifiers);
		return modifiers;
	}

	protected static int guardVisibility(@NonNls @NotNull final String what, final int modifiers)
	{
		final boolean isPublic = isPublic(modifiers);
		final boolean isProtected = isProtected(modifiers);
		final boolean isPrivate = isPrivate(modifiers);

		if (isPublic)
		{
			if (isProtected)
			{
				throw new IllegalStateException(aOrAn(what) + " can not be both public and protected");
			}

			if (isPrivate)
			{
				throw new IllegalStateException(aOrAn(what) + " can not be both public and private");
			}
		}

		if (isProtected)
		{
			if (isPrivate)
			{
				throw new IllegalStateException(aOrAn(what) + " can not be both protected and private");
			}
		}

		return modifiers;
	}

	protected static int guardNestedTypeAccessModifiersWhenNestedInAnAnnotationOrInterface(@NotNull final String what, final int modifiers)
	{
		if (isPrivate(modifiers))
		{
			throw new IllegalArgumentException(aOrAn(what) + " nested in an interface or annotation can not be private");
		}

		if (isProtected(modifiers))
		{
			throw new IllegalArgumentException(aOrAn(what) + " nested in an interface or annotation can not be protected");
		}

		return modifiers;
	}

	protected abstract int additionalModifierGuard(@NotNull final String what, final int modifiers);

	private static int guardNotFinal(@NotNull final String what, final int modifiers)
	{
		if (isFinal(modifiers))
		{
			throw new IllegalArgumentException(aOrAn(what) + " can not be final");
		}
		return modifiers;
	}

	private static int guardNotAbstract(@NotNull final String what, final int modifiers)
	{
		if (isAbstract(modifiers))
		{
			throw new IllegalArgumentException(aOrAn(what) + " can not be abstract");
		}
		return modifiers;
	}

}
