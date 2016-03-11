package com.stormmq.java.parsing.ast.intermediates.packageIntermediates;

import com.stormmq.java.parsing.ast.details.annotationInstanceDetails.AnnotationInstanceDetail;
import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.details.packageDetails.PackageDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.MemberIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediates.Intermediate;
import com.stormmq.java.parsing.ast.intermediates.MethodIntermediate;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.utilities.names.PackageName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AbstractAnnotationInstanceIntermediate.resolveAnnotationInstanceIntermediates;
import static java.lang.System.arraycopy;

public final class PackageIntermediate implements Intermediate<PackageDetail>
{
	@NotNull private final PackageName packageName;
	@NotNull private final AnnotationInstanceIntermediate[] annotationInstanceIntermediates;

	public PackageIntermediate(@NotNull final PackageName packageName, @NotNull final AnnotationInstanceIntermediate[] annotationInstanceIntermediates)
	{
		this.packageName = packageName;
		this.annotationInstanceIntermediates = annotationInstanceIntermediates;
	}

	public void record(@NotNull final Map<PackageName, PackageIntermediate> index)
	{
		@Nullable final PackageIntermediate existingIfAny = index.get(packageName);
		if (existingIfAny == null)
		{
			index.put(packageName, this);
			return;
		}

		final AnnotationInstanceIntermediate[] ours = annotationInstanceIntermediates;
		final int ourLength = ours.length;
		if (ourLength == 0)
		{
			return;
		}

		final AnnotationInstanceIntermediate[] theirs = existingIfAny.annotationInstanceIntermediates;
		final int theirLength = theirs.length;
		if (theirLength == 0)
		{
			index.put(packageName, this);
		}

		final int length = ourLength + theirLength;
		final AnnotationInstanceIntermediate[] replacement = new AnnotationInstanceIntermediate[length];
		arraycopy(ours, 0, replacement, 0, ourLength);
		arraycopy(theirs, 0, replacement, ourLength, theirLength);
		index.put(packageName, new PackageIntermediate(packageName, replacement));
	}

	@NotNull
	public PackageDetail resolve(@NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		final AnnotationInstanceDetail[] annotationInstanceDetails = resolveAnnotationInstanceIntermediates(annotationInstanceIntermediates, memberIntermediateRecorder, simpleTypeIntermediateRecorder);
		return new PackageDetail(packageName, annotationInstanceDetails);
	}
}
