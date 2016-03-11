package com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.typeVisitorAdaptors;

import com.stormmq.java.parsing.adaptors.asm.asmTypeItemUsers.AsmTypeItemUser;
import com.stormmq.java.parsing.adaptors.asm.items.*;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.fieldVisitorAdaptors.FieldVisitorAdaptor;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.annotationVisitorAdaptors.*;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.fieldVisitorAdaptors.AnnotationGatheringFieldVisitorAdaptor;
import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.methodVisitorAdaptors.*;
import com.stormmq.java.parsing.adaptors.asm.visitors.CombinationSignatureVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.signature.SignatureReader;

import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationItem.EmptyAsmAnnotationItems;
import static com.stormmq.java.parsing.adaptors.asm.items.AsmFieldItem.EmptyAsmFieldItems;
import static com.stormmq.java.parsing.adaptors.asm.items.AsmMethodItem.EmptyAsmMethodItems;
import static com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.typeVisitorAdaptors.GatheringTypeVisitorAdaptor.AsmTypeParameterItem.EmptyAsmTypeParameterItems;
import static com.stormmq.java.parsing.ast.intermediates.DefaultValueIntermediate.NoDefaultValueIntermediate;
import static org.objectweb.asm.Type.getArgumentTypes;

/*
	Order is normally:-
		visit()
		visitSource() [only if not SKIP_DEBUG and has some debug data]
		visitOuterClass() [only if there is an outer class / method]
		annotationOnMethod() [if any]
		annotationOnType() [if any]
		visitAttribute() [if any unrecognised]
		visitInnerClass() [if any]
		visitField()
			annotationOnMethod() [if any]
			annotationOnType() [if any]
			visitAttribute() [if any unrecognised]
			visitEnd()
		visitMethod()
			parameter()
			annotationDefault()
				visit() / visitEnum() / visitArray() / annotationOnMethod() [ visit a nested annotation ]
				visitEnd()
			annotationOnMethod()
			annotationOnType()
			readParameterAnnotations()
			visitAttribute()
			visitCode() [only if SKIP_CODE]
			visitEnd()
		visitEnd()

	However, visitMethod() can occur before visit() for <clinit> and <init>; there really isn't any g'tee
	Hence this class exists to create a very simple anaemic model which can be used later on
 */
public final class GatheringTypeVisitorAdaptor implements TypeVisitorAdaptor
{
	@NotNull private final ArrayList<AsmAnnotationItem> asmAnnotationItems;
	@NotNull private final ArrayList<AsmFieldItem> asmFieldItems;
	@NotNull private final ArrayList<AsmMethodItem> asmMethodItems;
	@NotNull private final AsmTypeItemUser asmTypeItemUser;
	private int version;
	private int modifiers;
	@Nullable private String typeName;
	@Nullable private String typeParameters;
	@Nullable private String superTypeName;
	@Nullable private String[] interfaceTypeNames;
	private boolean isTopLevelType;

	public GatheringTypeVisitorAdaptor(@NotNull final AsmTypeItemUser asmTypeItemUser)
	{
		this.asmTypeItemUser = asmTypeItemUser;

		asmAnnotationItems = new ArrayList<>(8);
		asmFieldItems = new ArrayList<>(32);
		asmMethodItems = new ArrayList<>(256);
		version = 0;
		modifiers = 0;
		typeName = null;
		typeParameters = null;
		superTypeName = null;
		interfaceTypeNames = null;
		isTopLevelType = true;
	}

	@Override
	public void type(final int version, final int modifiers, @NotNull final String typeName, @Nullable final String typeParameters, @Nullable final String superTypeName, @NotNull final String[] interfaceTypeNames)
	{
		this.version = version;
		this.modifiers = modifiers;
		this.typeName = typeName;
		this.typeParameters = typeParameters;
		this.superTypeName = superTypeName;
		this.interfaceTypeNames = interfaceTypeNames;
	}

	@Override
	public void outerType(@NotNull final String outerTypeInternalTypeName, @NotNull final MethodContainer methodContainer)
	{
		isTopLevelType = false;
	}

	@Nullable
	@Override
	public AnnotationVisitorAdaptor annotationOnType(@NotNull final String annotationTypeDescriptor, @NotNull final RetentionPolicy retentionPolicy)
	{
		return new GatheringAsmAnnotationItemAnnotationVisitorAdaptor(annotationTypeDescriptor, retentionPolicy, asmAnnotationItems::add);
	}

	@NotNull
	@Override
	public FieldVisitorAdaptor[] field(final int modifiers, @NotNull final String fieldName, @NotNull final String fieldTypeDescriptor, @Nullable final String signature, @Nullable final Object fieldValueIfAny)
	{
		final List<AsmAnnotationItem> asmAnnotationItems = new ArrayList<>(4);
		asmFieldItems.add(new AsmFieldItem(modifiers, fieldName, fieldTypeDescriptor, signature, fieldValueIfAny, asmAnnotationItems));
		return new FieldVisitorAdaptor[]
		{
			new AnnotationGatheringFieldVisitorAdaptor(asmAnnotationItems::add)
		};
	}

	public static final class AsmTypeParameterItem
	{
		@NotNull public static final AsmTypeParameterItem[] EmptyAsmTypeParameterItems = new AsmTypeParameterItem[0];

	}

	@NotNull
	@Override
	public MethodVisitorAdaptor[] method(final int modifiers, @NotNull final String methodName, @NotNull final String methodDescriptor, @Nullable final String signature, @NotNull final String[] exceptionInternalTypeNames)
	{
		final List<AsmAnnotationItem> asmAnnotationItems = new ArrayList<>(4);
		asmMethodItems.add(new AsmMethodItem(modifiers, methodName, methodDescriptor, signature, exceptionInternalTypeNames, asmAnnotationItems, NoDefaultValueIntermediate));

		final int maximumNumberOfParameters = getArgumentTypes(methodDescriptor).length;

		final AsmTypeParameterItem[] asmTypeParameterItems;
		if (signature == null)
		{
			asmTypeParameterItems = EmptyAsmTypeParameterItems;
		}
		else
		{
			final SignatureReader signatureReader = new SignatureReader(signature);
			signatureReader.accept(new CombinationSignatureVisitor());
		}

		return new MethodVisitorAdaptor[]
		{
			new ParameterGatheringMethodVisitorAdaptor(maximumNumberOfParameters, new AsmMethodItem(modifiers, methodName, methodDescriptor, signature, exceptionInternalTypeNames, asmAnnotationItems, NoDefaultValueIntermediate)::setAsmParameterItems),
			new MethodAnnotationMethodVisitorAdaptor(asmAnnotationItems),
			new AnnotationMemberDefaultValueMethodVisitorAdaptor(new AsmMethodItem(modifiers, methodName, methodDescriptor, signature, exceptionInternalTypeNames, asmAnnotationItems, NoDefaultValueIntermediate))
		};
	}

	@Override
	public void end()
	{
		if (typeName == null)
		{
			throw new IllegalStateException(com.stormmq.java.parsing.utilities.StringConstants.Should_not_be_possible);
		}

		asmAnnotationItems.trimToSize();
		asmFieldItems.trimToSize();
		asmMethodItems.trimToSize();
		asmTypeItemUser.use(new AsmTypeItem(isTopLevelType, version, modifiers, typeName, typeParameters, superTypeName, interfaceTypeNames, asmAnnotationItems.isEmpty() ? EmptyAsmAnnotationItems : asmAnnotationItems, asmFieldItems.isEmpty() ? EmptyAsmFieldItems : asmFieldItems, asmMethodItems.isEmpty() ? EmptyAsmMethodItems : asmMethodItems));
	}

}
