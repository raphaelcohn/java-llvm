package com.stormmq.java.parsing.ast.intermediateRecorders;

import com.stormmq.java.parsing.ast.detailMaps.MemberDetailMap;
import com.stormmq.java.parsing.ast.details.ArrayCreator;
import com.stormmq.java.parsing.ast.details.MemberDetail;
import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.intermediates.MemberIntermediate;
import com.stormmq.java.parsing.ast.intermediates.MethodIntermediate;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class MemberIntermediateRecorder<I extends MemberIntermediate<M>, M extends MemberDetail>
{
	@NotNull private final Map<KnownReferenceTypeName, List<I>> index;

	public MemberIntermediateRecorder()
	{
		index = new HashMap<>();
	}

	public void add(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName, @NotNull final I detail)
	{
		@Nullable final List<I> existingDetailList = index.get(ourKnownReferenceTypeName);
		final List<I> detailList;
		if (existingDetailList == null)
		{
			detailList = new ArrayList<>();
			index.put(ourKnownReferenceTypeName, detailList);
		}
		else
		{
			detailList = existingDetailList;
		}
		detailList.add(detail);
	}

	@NotNull
	public List<I> findMustBeExtant(@NotNull final KnownReferenceTypeName referenceTypeName)
	{
		final List<I> intermediates = index.get(referenceTypeName);
		if (intermediates == null)
		{
			throw new IllegalArgumentException(format(ENGLISH, "Unknown referenceTypeName '%1$s'", referenceTypeName));
		}
		return intermediates;
	}

	@NotNull
	public MemberDetailMap<M> resolve(@NotNull final ArrayCreator<M> arrayCreator, @NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> methodIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		final int size = index.size();
		if (size == 0)
		{
			return new MemberDetailMap<>(Collections.<KnownReferenceTypeName, M[]>emptyMap());
		}

		final Map<KnownReferenceTypeName, M[]> result = new HashMap<>(size);
		for (Map.Entry<KnownReferenceTypeName, List<I>> entry : index.entrySet())
		{
			final KnownReferenceTypeName knownReferenceTypeName = entry.getKey();

			result.put(knownReferenceTypeName, resolveMemberDetails(entry.getValue(), arrayCreator, methodIntermediateRecorder, simpleTypeIntermediateRecorder));
		}

		return new MemberDetailMap<>(result);
	}

	public boolean hasNoRecordOf(@NotNull final KnownReferenceTypeName ourKnownReferenceTypeName)
	{
		return !index.containsKey(ourKnownReferenceTypeName);
	}

	@NotNull
	public static <I extends MemberIntermediate<M>, M extends MemberDetail> M[] resolveMemberDetails(@NotNull final List<I> intermediates, @NotNull final ArrayCreator<M> arrayCreator, @NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> methodIntermediateRecorder,  @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		final int size = intermediates.size();
		final M[] details;
		if (size == 0)
		{
			return arrayCreator.empty();
		}
		details = arrayCreator.create(size);
		for (int index = 0; index < size; index++)
		{
			details[index] = intermediates.get(index).resolve(methodIntermediateRecorder, simpleTypeIntermediateRecorder);
		}
		return details;
	}
}
