package com.stormmq.java.parsing.adaptors.javaparser.namedTypeDeclarationUsers.bodyDeclarationUsers;

import com.github.javaparser.ast.body.*;
import com.stormmq.java.parsing.adaptors.javaparser.dead.FieldCreator;
import com.stormmq.java.parsing.adaptors.javaparser.ast.JavaparserInitializerIntermediate;
import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.declarations.AbstractEmptyIgnoringBodyDeclarationUser;
import com.stormmq.java.parsing.adaptors.javaparser.referenceTypeNameMakers.ReferenceTypeNameMaker;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.FieldDeclarationWrapper;
import com.stormmq.java.parsing.ast.intermediateRecorders.IntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.InitializerIntermediate;
import com.stormmq.java.parsing.ast.intermediates.InitializersIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractRecordingBodyDeclarationUser extends AbstractEmptyIgnoringBodyDeclarationUser
{
	@NotNull private final FieldCreator fieldCreator;
	@NotNull protected final ReferenceTypeNameMaker referenceTypeNameMaker;
	@NotNull private final InitializersIntermediate initializersIntermediate;
	@NotNull protected final IntermediateRecorder intermediateRecorder;
	@NotNull protected final IntermediateCreator intermediateCreator;
	private final boolean instanceInitializersDisallowed;
	@NotNull protected final KnownReferenceTypeName ourKnownReferenceTypeName;

	protected AbstractRecordingBodyDeclarationUser(@NotNull final FieldCreator fieldCreator, @NotNull final ReferenceTypeNameMaker referenceTypeNameMaker, @NotNull final InitializersIntermediate initializersIntermediate, @NotNull final IntermediateRecorder intermediateRecorder, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final IntermediateCreator intermediateCreator)
	{
		super();
		this.fieldCreator = fieldCreator;
		this.referenceTypeNameMaker = referenceTypeNameMaker;
		this.initializersIntermediate = initializersIntermediate;
		this.intermediateRecorder = intermediateRecorder;

		instanceInitializersDisallowed = intermediateCreator.isInterfaceLike;
		this.intermediateCreator = intermediateCreator;
		this.ourKnownReferenceTypeName = ourKnownReferenceTypeName;
	}

	@Override
	public final void use(@NotNull final InitializerDeclaration initializerDeclaration)
	{
		final boolean isStatic = initializerDeclaration.isStatic();
		final InitializerIntermediate initializerIntermediate = new JavaparserInitializerIntermediate(initializerDeclaration.getBlock());
		if (isStatic)
		{
			initializersIntermediate.addStaticInitializer(initializerIntermediate);
		}
		else
		{
			if (instanceInitializersDisallowed)
			{
				throw new IllegalStateException("Instance initializers are not allowed for interfaces and annotations");
			}
			initializersIntermediate.addInstanceInitializer(initializerIntermediate);
		}
	}

	@Override
	public final void use(@NotNull final FieldDeclaration fieldDeclaration)
	{
	}

	@Override
	public void use(@NotNull final ConstructorDeclaration constructorDeclaration)
	{
		throw new IllegalArgumentException("Constructors are not allowed for this type");
	}

	@Override
	public void use(@NotNull final MethodDeclaration methodDeclaration)
	{
		throw new IllegalArgumentException("Methods are not allowed for this type");
	}

	@Override
	public void use(@NotNull final AnnotationMemberDeclaration annotationMemberDeclaration)
	{
		throw new IllegalArgumentException("Annotation members are not allowed for this type");
	}

	@Override
	public final void use(@NotNull final AnnotationDeclaration annotationDeclaration)
	{
	}

	@Override
	public final void use(@NotNull final ClassOrInterfaceDeclaration classOrInterfaceDeclaration)
	{
	}

	@Override
	public final void use(@NotNull final EnumDeclaration enumDeclaration)
	{
	}

	@Override
	public final void end()
	{
		intermediateRecorder.record(ourKnownReferenceTypeName, initializersIntermediate);
	}
}
