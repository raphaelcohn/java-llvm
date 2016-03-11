package com.stormmq.java.parsing.adaptors.asm.helpers;

import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.PrimitiveTypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.TypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.VoidTypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.ImplementedTypeUsage;
import com.stormmq.java.parsing.ast.typeUsages.TypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;

import java.util.HashSet;
import java.util.Set;

import static com.stormmq.java.parsing.ast.intermediates.IntermediateCreator.Annotation;
import static com.stormmq.java.parsing.ast.intermediates.IntermediateCreator.Enum;
import static com.stormmq.java.parsing.ast.typeUsages.TypeUsage.EmptyTypeUsages;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Type.*;

public final class AsmHelper
{
	public static final int AsmVersion = ASM5;

	private AsmHelper()
	{
	}

	@NotNull
	public static TypeUsage findReturnTypeUsageFromMethodDescriptor(@NotNull final String methodDescriptor)
	{
		return typeUsageFromAsmType(getReturnType(methodDescriptor));
	}

	@NotNull
	public static TypeUsage[] findParameterTypeUsagesFromMethodDescriptor(@NotNull final String methodDescriptor)
	{
		final Type[] asmParameterTypes = getArgumentTypes(methodDescriptor);
		final int length = asmParameterTypes.length;
		if (length == 0)
		{
			return EmptyTypeUsages;
		}
		else
		{
			final TypeUsage[] parameterTypeUsages = new TypeUsage[length];
			//noinspection ForLoopReplaceableByForEach
			for(int index = 0; index < length; index++)
			{
				final Type asmParameterType = asmParameterTypes[index];
				parameterTypeUsages[index] = typeUsageFromAsmType(asmParameterType);
			}
			return parameterTypeUsages;
		}
	}

	@NotNull
	public static KnownReferenceTypeName[] exceptionInternalNamesToReferenceTypeNames(@Nullable final String[] exceptions)
	{
		if (exceptions == null)
		{
			return EmptyKnownReferenceTypeNames;
		}

		final int length = exceptions.length;
		if (length == 0)
		{
			return EmptyKnownReferenceTypeNames;
		}

		final KnownReferenceTypeName[] exceptionTypeReferences = new KnownReferenceTypeName[length];
		//noinspection ForLoopReplaceableByForEach
		for(int index = 0; index < length; index++)
		{
			final String exceptionInternalName = exceptions[index];

			exceptionTypeReferences[index] = knownReferenceTypeNameFromInternalName(exceptionInternalName);
		}
		return exceptionTypeReferences;
	}

	@NotNull
	public static KnownReferenceTypeName[] parseAsmInterfaces(final IntermediateCreator intermediateCreator, @Nullable final String[] interfaces, @NotNull final KnownReferenceTypeName ourTypeReferenceName, @NotNull final KnownReferenceTypeName superTypeReferenceName)
	{
		if (interfaces == null)
		{
			if (isAnnotation(intermediateCreator))
			{
				throw new IllegalArgumentException("Annotations must implement one interface");
			}
			return EmptyKnownReferenceTypeNames;
		}

		final int length = interfaces.length;
		if (length == 0)
		{
			if (isAnnotation(intermediateCreator))
			{
				throw new IllegalArgumentException("Annotations must implement one interface");
			}
			return EmptyKnownReferenceTypeNames;
		}

		if (isEnum(intermediateCreator))
		{
			return EmptyKnownReferenceTypeNames;
		}

		if (isAnnotation(intermediateCreator))
		{
			if (length != 1)
			{
				throw new IllegalArgumentException("Annotations must only implement one interface");
			}
			final KnownReferenceTypeName o = knownReferenceTypeNameFromInternalName(interfaces[0]);
			if (!JavaLangAnnotationAnnotation.equals(o))
			{
				throw new IllegalArgumentException("Annotations must implement the interface java.lang.annotation.Annotation");
			}
			return AnnotationKnownReferenceTypeNames;
		}

		final Set<KnownReferenceTypeName> encounteredInterfaceTypeReferences = new HashSet<>(length);
		final KnownReferenceTypeName[] interfaceTypeReferences = new KnownReferenceTypeName[length];
		for (int index = 0; index < length; index++)
		{
			final KnownReferenceTypeName interfaceTypeReference = knownReferenceTypeNameFromInternalName(interfaces[index]);
			if (interfaceTypeReference.equals(ourTypeReferenceName))
			{
				throw new IllegalArgumentException("Interface is also our type");
			}
			if (interfaceTypeReference.equals(superTypeReferenceName))
			{
				throw new IllegalArgumentException("Interface is also super type");
			}
			final boolean added = encounteredInterfaceTypeReferences.add(interfaceTypeReference);
			if (!added)
			{
				throw new IllegalArgumentException("Duplicate interface");
			}
			interfaceTypeReferences[index] = interfaceTypeReference;
		}
		return interfaceTypeReferences;
	}

	private static boolean isEnum(@NotNull final IntermediateCreator intermediateCreator)
	{
		return intermediateCreator == Enum;
	}

	private static boolean isAnnotation(@NotNull final IntermediateCreator intermediateCreator)
	{
		return intermediateCreator == Annotation;
	}

	@NotNull
	public static KnownReferenceTypeName knownReferenceTypeName(@NotNull final String typeDescriptor)
	{
		return KnownReferenceTypeName.knownReferenceTypeName(getType(typeDescriptor).getClassName());
	}

	@NotNull
	public static TypeName typeNameFromAsmFieldOrParameterOrMethodReturnType(@NotNull final Type type, final boolean arrayIsNotAllowed)
	{
		final int sort = type.getSort();
		switch(sort)
		{
			case VOID:
				return VoidTypeName._void;

			case BOOLEAN:
				return PrimitiveTypeName._boolean;

			case CHAR:
				return PrimitiveTypeName._char;

			case BYTE:
				return PrimitiveTypeName._byte;

			case SHORT:
				return PrimitiveTypeName._short;

			case INT:
				return PrimitiveTypeName._int;

			case FLOAT:
				return PrimitiveTypeName._float;

			case LONG:
				return PrimitiveTypeName._long;

			case DOUBLE:
				return PrimitiveTypeName._double;

			case ARRAY:
				if (arrayIsNotAllowed)
				{
					throw new IllegalArgumentException(format(ENGLISH, "Type '%1$s' is an array type", type));
				}
				final Type elementType = type.getElementType();
				return typeNameFromAsmFieldOrParameterOrMethodReturnType(elementType, true);

			case OBJECT:
				return knownReferenceTypeNameFromInternalName(type.getInternalName());

			case METHOD:
				throw new IllegalArgumentException(format(ENGLISH, "Type '%1$s' is a method type", type));

			default:
				throw new IllegalArgumentException(format(ENGLISH, "Type '%1$s' is an unexpected type", type));

		}
	}

	@NotNull
	public static TypeUsage typeUsageFromAsmType(@NotNull final Type type)
	{
		final TypeName typeName = typeNameFromAsmFieldOrParameterOrMethodReturnType(type, false);
		final int arrayCount = type.getSort() == ARRAY ? type.getDimensions() : 0;
		return new ImplementedTypeUsage(typeName, new AnnotationInstanceIntermediate[arrayCount][0]);
	}
}
