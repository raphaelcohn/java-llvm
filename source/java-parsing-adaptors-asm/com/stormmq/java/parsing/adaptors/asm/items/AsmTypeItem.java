package com.stormmq.java.parsing.adaptors.asm.items;

import com.stormmq.java.parsing.adaptors.asm.ast.AsmModifiers;
import com.stormmq.java.parsing.ast.intermediateRecorders.FieldIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.IntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.MethodIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.IntermediateCreator;
import com.stormmq.java.parsing.ast.intermediates.TypeIntermediate;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.Completeness;
import com.stormmq.java.parsing.ast.modifiers.Modifiers;
import com.stormmq.java.parsing.utilities.Visibility;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.stormmq.java.parsing.adaptors.asm.ast.AsmModifiers.asmModifiersType;
import static com.stormmq.java.parsing.adaptors.asm.helpers.AsmHelper.parseAsmInterfaces;
import static com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationItem.parseAnnotationInstanceDetails;
import static com.stormmq.java.parsing.ast.intermediates.Origin.Class;
import static com.stormmq.java.parsing.ast.intermediates.TypeIntermediate.*;
import static com.stormmq.java.parsing.utilities.ReservedIdentifiers.validateIsJavaIdentifier;
import static com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName.JavaLangEnum;
import static com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName.JavaLangObjectInternalName;
import static com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName.knownReferenceTypeNameFromInternalName;

public final class AsmTypeItem
{
	@NotNull
	public static KnownReferenceTypeName parseSuperTypeInternalName(@NotNull final IntermediateCreator intermediateCreator, @NotNull final String internalName, @Nullable final String superInternalName)
	{
		if (superInternalName == null)
		{
			if (isSuperInternalNameNotJavaLangObject(internalName))
			{
				//noinspection HardcodedFileSeparator
				throw new IllegalStateException("superName can only be null if this is java/lang/Object");
			}
			return intermediateCreator.defaultSuperTypeReferenceName;
		}
		else
		{
			return knownReferenceTypeNameFromInternalName(superInternalName);
		}
	}

	private static boolean isSuperInternalNameNotJavaLangObject(@NotNull final String internalName)
	{
		return !internalName.equals(JavaLangObjectInternalName);
	}

	private final boolean isTopLevelType;
	private final int version;
	@NotNull private final String name;
	@Nullable private final String signature;
	@Nullable private final String superName;
	@Nullable private final String[] interfaces;
	private final int access;
	@NotNull private final List<AsmAnnotationItem> asmAnnotationItems;
	@NotNull private final List<AsmFieldItem> asmFieldItems;
	@NotNull private final List<AsmMethodItem> asmMethodItems;
	@NotNull private final KnownReferenceTypeName ourKnownReferenceTypeName;

	public AsmTypeItem(final boolean isTopLevelType, final int version, final int access, @NotNull final String name, @Nullable final String signature, @Nullable final String superName, @Nullable final String[] interfaces, @NotNull final List<AsmAnnotationItem> asmAnnotationItems, @NotNull final List<AsmFieldItem> asmFieldItems, @NotNull final List<AsmMethodItem> asmMethodItems)
	{
		this.isTopLevelType = isTopLevelType;
		this.access = access;
		this.asmAnnotationItems = asmAnnotationItems;
		validateIsJavaIdentifier(name, false);

		this.version = version;
		this.name = name;
		this.signature = signature;
		this.superName = superName;
		this.interfaces = interfaces;

		this.asmFieldItems = asmFieldItems;
		this.asmMethodItems = asmMethodItems;

		ourKnownReferenceTypeName = knownReferenceTypeNameFromInternalName(name);
	}

	@NotNull
	private IntermediateCreator findTypeDetailCreator()
	{
		final IntermediateCreator intermediateCreator = asmModifiersType(access).kindType();

		if (intermediateCreator != IntermediateCreator.Enum)
		{
			return intermediateCreator;
		}

		// We can be 'Enum' flagged in our modifiers for a overridden Enum constant

		if (superName == null)
		{
			return intermediateCreator;
		}

		final KnownReferenceTypeName superTypeReferenceName = parseSuperTypeInternalName(intermediateCreator, name, superName);
		if (superTypeReferenceName.equals(JavaLangEnum))
		{
			return intermediateCreator;
		}

		return IntermediateCreator.Class;
	}

	public void recordTypeIntermediate(@NotNull final IntermediateRecorder intermediateRecorder)
	{
		@NotNull final IntermediateCreator intermediateCreator = findTypeDetailCreator();

		newTypeIntermediate(intermediateRecorder, intermediateCreator);
		recordFieldIntermediates(intermediateRecorder);
		recordMethodIntermediates(intermediateRecorder);
	}

	private void newTypeIntermediate(@NotNull final IntermediateRecorder intermediateRecorder, @NotNull final IntermediateCreator intermediateCreator)
	{
		final KnownReferenceTypeName superTypeReferenceName = parseSuperTypeInternalName(intermediateCreator, name, superName);

		final KnownReferenceTypeName[] interfaceTypeReferenceNames = parseAsmInterfaces(intermediateCreator, interfaces, ourKnownReferenceTypeName, superTypeReferenceName);

		// We can be 'Enum' flagged in our modifiers for a overridden Enum constant
		final Modifiers modifiers = adjustModifiersForEnumsWithConstantsAsSubclasses(intermediateCreator);
		final AnnotationInstanceIntermediate[] annotations = parseAnnotationInstanceDetails(asmAnnotationItems);

		final TypeIntermediate typeIntermediate;
		final Visibility visibility = modifiers.visibility();
		final boolean isDeprecatedInByteCode = modifiers.isDeprecatedInByteCode();
		final boolean isStrictFloatingPoint = modifiers.isStrictFloatingPoint();
		final boolean isSynthetic = modifiers.isSynthetic();

		final boolean isStatic = modifiers.isStatic();
		if (isTopLevelType && isStatic)
		{
			throw new IllegalArgumentException("Static is not allowed for top-level types");
		}

		switch(intermediateCreator)
		{
			case Annotation:
				modifiers.validateAsAnnotationModifiers();
				typeIntermediate = newAnnotationTypeIntermediate(Class, ourKnownReferenceTypeName, annotations, visibility, isDeprecatedInByteCode, isStrictFloatingPoint, isSynthetic);
				break;

			case Interface:
				modifiers.validateAsInterfaceModifiers();
				typeIntermediate = newInterfaceTypeIntermediate(Class, ourKnownReferenceTypeName, interfacesExtended, annotations, visibility, isDeprecatedInByteCode, isStrictFloatingPoint, isSynthetic);
				break;

			case Class:
				modifiers.validateAsClassModifiers();

				final Completeness completeness = modifiers.classOrMethodCompleteness();
				if (isStatic || isTopLevelType)
				{
					typeIntermediate = newStaticClassTypeIntermediate(Class, ourKnownReferenceTypeName, classExtended, interfacesExtended, annotations, visibility, isDeprecatedInByteCode, isStrictFloatingPoint, isSynthetic, completeness);
				}
				else
				{
					typeIntermediate = newInnerClassTypeIntermediate(Class, ourKnownReferenceTypeName, classExtended, interfacesExtended, annotations, visibility, isDeprecatedInByteCode, isStrictFloatingPoint, isSynthetic, completeness);
				}
				break;

			case Enum:
				modifiers.validateAsEnumModifiers();
				typeIntermediate = newEnumTypeIntermediate(Class, ourKnownReferenceTypeName, classExtended, interfacesExtended, annotations, visibility, isDeprecatedInByteCode, isStrictFloatingPoint, isSynthetic, Completeness.Abstract);
				break;

			default:
				throw new IllegalStateException(com.stormmq.java.parsing.utilities.StringConstants.Should_not_be_possible);

		}

		intermediateRecorder.record(ourKnownReferenceTypeName, typeIntermediate);
	}

	@NotNull
	private Modifiers adjustModifiersForEnumsWithConstantsAsSubclasses(@NotNull final IntermediateCreator intermediateCreator)
	{
		final AsmModifiers asmModifiers = asmModifiersType(access);
		if (asmModifiers.isEnum() && intermediateCreator == IntermediateCreator.Class)
		{
			return asmModifiers.removeEnum();
		}
		else
		{
			return asmModifiers;
		}
	}

	private void recordFieldIntermediates(@NotNull final FieldIntermediateRecorder fieldIntermediateRecorder)
	{
		@NotNull final IntermediateCreator intermediateCreator = findTypeDetailCreator();

		final int length = asmFieldItems.size();
		if (length == 0)
		{
			return;
		}

		//noinspection ForLoopReplaceableByForEach
		for (int index = 0; index < length; index++)
		{
			final AsmFieldItem asmFieldItem = asmFieldItems.get(index);
			asmFieldItem.toFieldIntermediate(intermediateCreator, fieldIntermediateRecorder, ourKnownReferenceTypeName);
		}
	}

	private void recordMethodIntermediates(@NotNull final MethodIntermediateRecorder methodIntermediateRecorder)
	{
		@NotNull final IntermediateCreator intermediateCreator = findTypeDetailCreator();

		final int length = asmMethodItems.size();
		if (length == 0)
		{
			return;
		}

		//noinspection ForLoopReplaceableByForEach
		for(int index = 0; index < length; index++)
		{
			final AsmMethodItem asmMethodItem = asmMethodItems.get(index);
			asmMethodItem.toMethodIntermediate(intermediateCreator, methodIntermediateRecorder, ourKnownReferenceTypeName);
		}
	}
}
