package com.stormmq.java.parsing.ast.intermediateRecorders;

import com.stormmq.java.parsing.ast.detailMaps.MemberDetailMap;
import com.stormmq.java.parsing.ast.detailMaps.TypeDetailMap;
import com.stormmq.java.parsing.ast.details.fieldDetails.FieldDetail;
import com.stormmq.java.parsing.ast.details.initializersDetails.InitializersDetail;
import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.details.typeDetails.TypeDetail;
import com.stormmq.java.parsing.ast.intermediates.MethodIntermediate;
import com.stormmq.java.parsing.ast.intermediates.Origin;
import com.stormmq.java.parsing.ast.intermediates.TypeIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.stormmq.java.parsing.ast.detailMaps.TypeDetailMap.EmptyTypeDetailMap;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class SimpleTypeIntermediateRecorder implements TypeIntermediateRecorder
{
	@NotNull private final List<String> warnings;
	@NotNull private final Map<KnownReferenceTypeName, TypeIntermediate> map;

	public SimpleTypeIntermediateRecorder(@NotNull final List<String> warnings)
	{
		this.warnings = warnings;
		map = new HashMap<>();
	}

	@Override
	public void record(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final TypeIntermediate typeIntermediate)
	{
		@Nullable final TypeIntermediate existingTypeIntermediate = map.get(ourKnownReferenceTypeName);
		if (existingTypeIntermediate != null)
		{
			warn(ourKnownReferenceTypeName, typeIntermediate, existingTypeIntermediate);
		}
		map.put(ourKnownReferenceTypeName, typeIntermediate);
	}

	@NotNull
	public TypeIntermediate findMustBeExtant(@NotNull final KnownReferenceTypeName knownReferenceTypeName)
	{
		final TypeIntermediate typeIntermediate = map.get(knownReferenceTypeName);
		if (typeIntermediate == null)
		{
			throw new IllegalArgumentException(format(ENGLISH, "No type intermediate for '%1$s'", knownReferenceTypeName));
		}
		return typeIntermediate;
	}

	@NotNull
	public TypeDetailMap resolve(@NotNull final MemberDetailMap<InitializersDetail> initializersDetailMap, @NotNull final MemberDetailMap<FieldDetail> fieldDetailMap, @NotNull final MemberDetailMap<MethodDetail> methodDetailMap, @NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder)
	{
		final int size = map.size();
		if (size == 0)
		{
			return EmptyTypeDetailMap;
		}

		final Map<KnownReferenceTypeName, TypeDetail> resolved = new HashMap<>(size);
		for (final Entry<KnownReferenceTypeName, TypeIntermediate> entry : map.entrySet())
		{
			final KnownReferenceTypeName knownReferenceTypeName = entry.getKey();

			final InitializersDetail[] initializersDetails = initializersDetailMap.findMustBeExtant(knownReferenceTypeName);
			final FieldDetail[] fieldDetails = fieldDetailMap.findMustBeExtant(knownReferenceTypeName);
			final MethodDetail[] methodDetails = methodDetailMap.findMustBeExtant(knownReferenceTypeName);

			final TypeIntermediate typeIntermediate = entry.getValue();
			final TypeDetail typeDetail = typeIntermediate.resolve(initializersDetails, fieldDetails, methodDetails, memberIntermediateRecorder, this);
			resolved.put(knownReferenceTypeName, typeDetail);
		}
		return new TypeDetailMap(resolved);
	}

	private void warn(@NotNull final KnownReferenceTypeName typeReference, @NotNull final TypeIntermediate latest, @NotNull final TypeIntermediate existing)
	{
		final Origin existingOrigin = existing.origin();
		final Origin replacementOrigin = latest.origin();
		final int existingOrdinal = existingOrigin.ordinal();
		final int replacementOrdinal = replacementOrigin.ordinal();
		if (replacementOrdinal < existingOrdinal)
		{
			warnings.add(format(ENGLISH, "There is already a type reference '%1$s' with a superior origin '%2$s'", typeReference, existingOrigin));
		}
		else if (replacementOrdinal == existingOrdinal)
		{
			warnings.add(format(ENGLISH, "There is already a type reference '%1$s' with an equivalent origin '%2$s'", typeReference, existingOrigin));
		}
	}
}
