package com.stormmq.java.parsing.ast.intermediateRecorders;

import com.stormmq.java.parsing.ast.detailMaps.MemberDetailMap;
import com.stormmq.java.parsing.ast.detailMaps.TypeDetailMap;
import com.stormmq.java.parsing.ast.details.fieldDetails.FieldDetail;
import com.stormmq.java.parsing.ast.details.initializersDetails.InitializersDetail;
import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.intermediates.*;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeResolution.SimpleTypeResolver;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.java.parsing.ast.details.fieldDetails.FieldDetail.FieldDetailArrayCreator;
import static com.stormmq.java.parsing.ast.details.initializersDetails.InitializersDetail.InitializersDetailArrayCreator;
import static com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail.MethodDetailArrayCreator;

public final class SimpleIntermediateRecorder implements IntermediateRecorder
{
	@NotNull private final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder;
	@NotNull private final SimpleTypeResolver simpleTypeResolver;
	@NotNull private final MemberIntermediateRecorder<InitializersIntermediate, InitializersDetail> initializersMemberIntermediateRecorder;
	@NotNull private final MemberIntermediateRecorder<FieldIntermediate, FieldDetail> fieldMemberIntermediateRecorder;
	@NotNull private final MemberIntermediateRecorder<ConstructorIntermediate, MethodDetail> constructorMemberIntermediateRecorder;
	@NotNull private final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> methodMemberIntermediateRecorder;

	public SimpleIntermediateRecorder(@NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder, @NotNull final SimpleTypeResolver simpleTypeResolver, @NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> methodIntermediateRecorder)
	{
		this.simpleTypeIntermediateRecorder = simpleTypeIntermediateRecorder;
		this.simpleTypeResolver = simpleTypeResolver;
		initializersMemberIntermediateRecorder = new MemberIntermediateRecorder<>();
		constructorMemberIntermediateRecorder = new MemberIntermediateRecorder<>();
		fieldMemberIntermediateRecorder = new MemberIntermediateRecorder<>();
		methodMemberIntermediateRecorder = methodIntermediateRecorder;
	}

	@Override
	public void record(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final TypeIntermediate typeIntermediate)
	{
		simpleTypeIntermediateRecorder.record(ourKnownReferenceTypeName, typeIntermediate);
		simpleTypeResolver.recordType(ourKnownReferenceTypeName);
	}

	@Override
	public void record(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final InitializersIntermediate initializersIntermediate)
	{
		initializersMemberIntermediateRecorder.add(ourKnownReferenceTypeName, initializersIntermediate);
	}

	@Override
	public void record(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final FieldIntermediate fieldIntermediate)
	{
		fieldMemberIntermediateRecorder.add(ourKnownReferenceTypeName, fieldIntermediate);
		simpleTypeResolver.recordField(ourKnownReferenceTypeName, fieldIntermediate);
	}

	@Override
	public void record(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final ConstructorIntermediate constructorIntermediate)
	{
		constructorMemberIntermediateRecorder.add(ourKnownReferenceTypeName, constructorIntermediate);
		simpleTypeResolver.recordConstructor(ourKnownReferenceTypeName, constructorIntermediate);
	}

	@Override
	public boolean hasNoConstructors(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName)
	{
		return constructorMemberIntermediateRecorder.hasNoRecordOf(ourKnownReferenceTypeName);
	}

	@Override
	public void record(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final MethodIntermediate methodIntermediate)
	{
		methodMemberIntermediateRecorder.add(ourKnownReferenceTypeName, methodIntermediate);
		simpleTypeResolver.recordMethod(ourKnownReferenceTypeName, methodIntermediate);
	}

	@NotNull
	public TypeDetailMap resolve()
	{
		final MemberDetailMap<InitializersDetail> initializersDetailMap = initializersMemberIntermediateRecorder.resolve(InitializersDetailArrayCreator, methodMemberIntermediateRecorder, simpleTypeIntermediateRecorder);
		final MemberDetailMap<FieldDetail> fieldDetailMap = fieldMemberIntermediateRecorder.resolve(FieldDetailArrayCreator, methodMemberIntermediateRecorder, simpleTypeIntermediateRecorder);
		final MemberDetailMap<MethodDetail> methodDetailMap = methodMemberIntermediateRecorder.resolve(MethodDetailArrayCreator, methodMemberIntermediateRecorder, simpleTypeIntermediateRecorder);

		return simpleTypeIntermediateRecorder.resolve(initializersDetailMap, fieldDetailMap, methodDetailMap, methodMemberIntermediateRecorder);
	}

//	@NotNull
//	private DecedentsMaps resolve()
//	{
//		return pendingDecedentsMap.resolve(this);
//	}
//
//	@Override
//	public void guardInterfacesOnlyImplementInterfaces()
//	{
////		for (ActualTypeDetailRecorder classOrInterfaceOrEnumTypeDetailMap : allClassOrInterfaceOrEnumTypeDetails)
////		{
////			for (ClassOrInterfaceOrEnumTypeDetail classOrInterfaceOrEnumTypeDetails : classOrInterfaceOrEnumTypeDetailMap.values())
////			{
////				classOrInterfaceOrEnumTypeDetails.guardInterfacesOnlyImplementInterfaces(this);
////			}
////		}
//	}
//
//	@Override
//	public void guardSuperTypesExistAndAreNotFinal()
//	{
////		for (ClassTypeDetail classTypeDetails : simpleTypeIntermediateRecorder.values())
////		{
////			classTypeDetails.guardSuperTypesExistAndAreNotFinal(this);
////		}
//	}
//
//	@Override
//	@NotNull
//	public ClassTypeDetail lookUpClass(@NotNull final KnownReferenceTypeName classTypeReference)
//	{
//		return simpleTypeIntermediateRecorder.lookUp(classTypeReference);
//	}
//
//	@Override
//	@NotNull
//	public InterfaceTypeDetail lookUpInterface(@NotNull final KnownReferenceTypeName interfaceTypeReference)
//	{
//		return interfaceTypeDetailsMap.lookUp(interfaceTypeReference);
//	}
//
//	@Override
//	@NotNull
//	public AnnotationTypeDetail lookUpAnnotation(@NotNull final KnownReferenceTypeName annotationTypeReference)
//	{
//		return annotationTypeDetailsMap.lookUp(annotationTypeReference);
//	}
//
//	@Override
//	@NotNull
//	public EnumTypeDetail lookUpEnum(@NotNull final KnownReferenceTypeName enumTypeReference)
//	{
//		return enumTypeDetailsMap.lookUp(enumTypeReference);
//	}
}
