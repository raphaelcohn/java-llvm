package com.stormmq.java.parsing.adaptors.javaparser.helpers.FIELDSTUFF;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.stormmq.java.parsing.ast.details.fieldValueDetails.FieldValueDetail;
import com.stormmq.java.parsing.ast.details.fieldValueDetails.StringInvariantStaticFieldValueDetail;
import com.stormmq.java.parsing.ast.intermediates.InitializerIntermediate;
import com.stormmq.java.parsing.ast.intermediates.InitializersIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeUsages.ImplementedTypeUsage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.java.parsing.adaptors.javaparser.helpers.FIELDSTUFF.LiteralExpressionValueExtractor.Extractors;
import static com.stormmq.java.parsing.ast.details.fieldValueDetails.UnassignedFieldValueDetail.Unassigned;
import static com.stormmq.java.parsing.utilities.PrimitiveTypeHelper.isNotBoxedPrimitiveExcludingVoid;

public class STUFF
{
	@NotNull
	public static FieldValueDetail literalExpressionToConstant(@NotNull final Expression init, @NotNull final ImplementedTypeUsage fieldTypeUsage, @NotNull final InitializersIntermediate initializersIntermediate, @NotNull final InitializerIntermediate initializerIntermediate)
	{
		final LiteralExpressionValueExtractor literalExpressionValueExtractor = Extractors.get(init.getClass());
		@Nullable final Object extracted = literalExpressionValueExtractor.extract(init);

		if (extracted == null)
		{
			if (fieldTypeUsage.isPrimitive())
			{
				throw new IllegalStateException("Can not assign null to a primitive field");
			}
			return Unassigned;
		}

		if (extracted instanceof String)
		{
			if (fieldTypeUsage.isArray())
			{
				throw new IllegalArgumentException("valueType can not be an array if extracted is a String");
			}
			return new StringInvariantStaticFieldValueDetail((String) extracted);
		}

		final Class<?> extractedClass = extracted.getClass();
		if (isNotBoxedPrimitiveExcludingVoid(extractedClass))
		{
			throw new IllegalStateException("Should not have extracted a non-boxed-primitive value except for null and String");
		}

		if (fieldTypeUsage.isPrimitive())
		{
			final ToFieldValueDetailConverter toFieldValueDetailConverter = ToFieldValueDetailConverter.Converters.get(fieldTypeUsage.typeName);
			return toFieldValueDetailConverter.convert(extracted);
		}

		// requires boxing, etc
		initializersIntermediate.addStaticInitializer(initializerIntermediate);
		return Unassigned;
	}

	@NotNull
	public static ClassOrInterfaceType knownReferenceTypeNameToClassOrInterfaceTypeScope(@NotNull final KnownReferenceTypeName knownReferenceTypeName)
	{
		final String fullyQualifiedNameUsingDotsAndDollarSigns = knownReferenceTypeName.fullyQualifiedNameUsingDotsAndDollarSigns();
		final String[] names = fullyQualifiedNameUsingDotsAndDollarSigns.split("\\.");

		ClassOrInterfaceType nameInstance = new ClassOrInterfaceType(names[0]);

		final int length = names.length;
		for (int index = 1; index < length; index++)
		{
			final String name = names[index];
			nameInstance = new ClassOrInterfaceType(nameInstance, name);
		}
		return nameInstance;
	}
}
