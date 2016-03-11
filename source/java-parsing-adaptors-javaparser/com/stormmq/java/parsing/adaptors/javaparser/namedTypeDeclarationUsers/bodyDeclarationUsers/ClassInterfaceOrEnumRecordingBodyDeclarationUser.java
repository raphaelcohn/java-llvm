package com.stormmq.java.parsing.adaptors.javaparser.namedTypeDeclarationUsers.bodyDeclarationUsers;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.stormmq.java.parsing.adaptors.javaparser.dead.FieldCreator;
import com.stormmq.java.parsing.adaptors.javaparser.MethodAndConstructorCreator;
import com.stormmq.java.parsing.adaptors.javaparser.referenceTypeNameMakers.ReferenceTypeNameMaker;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.MethodDeclarationWrapper;
import com.stormmq.java.parsing.ast.intermediateRecorders.IntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.InitializersIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.ast.intermediates.IntermediateCreator.Interface;

public class ClassInterfaceOrEnumRecordingBodyDeclarationUser extends AbstractRecordingBodyDeclarationUser
{
	@NotNull private final MethodAndConstructorCreator methodAndConstructorCreator;
	private final boolean isInterface;

	public ClassInterfaceOrEnumRecordingBodyDeclarationUser(@NotNull final FieldCreator fieldCreator, @NotNull final ReferenceTypeNameMaker referenceTypeNameMaker, @NotNull final InitializersIntermediate initializersIntermediate, @NotNull final IntermediateRecorder intermediateRecorder, @NotNull final MethodAndConstructorCreator methodAndConstructorCreator, @NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final IntermediateCreator intermediateCreator)
	{
		super(fieldCreator, referenceTypeNameMaker, initializersIntermediate, intermediateRecorder, ourKnownReferenceTypeName, intermediateCreator);
		this.methodAndConstructorCreator = methodAndConstructorCreator;
		isInterface = this.intermediateCreator == Interface;
	}

	@Override
	public void use(@NotNull final ConstructorDeclaration constructorDeclaration)
	{
		if (isInterface)
		{
			throw new IllegalStateException("Constructors are not allowed for interfaces");
		}
		new ConstructorDeclarationWrapper(constructorDeclaration, intermediateCreator).create(methodAndConstructorCreator, referenceTypeNameMaker, ourKnownReferenceTypeName);
	}

	@Override
	public void use(@NotNull final MethodDeclaration methodDeclaration)
	{
		new MethodDeclarationWrapper(methodDeclaration, intermediateCreator).create(methodAndConstructorCreator, referenceTypeNameMaker, ourKnownReferenceTypeName);
	}
}
