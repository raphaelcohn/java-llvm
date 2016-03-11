package com.stormmq.java.parsing.ast.intermediateRecorders;

import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.details.packageDetails.PackageDetail;
import com.stormmq.java.parsing.ast.detailMaps.PackageDetailMap;
import com.stormmq.java.parsing.ast.intermediates.MethodIntermediate;
import com.stormmq.java.parsing.ast.intermediates.packageIntermediates.PackageIntermediate;
import com.stormmq.java.parsing.utilities.names.PackageName;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.stormmq.java.parsing.ast.detailMaps.PackageDetailMap.EmptyPackageDetailMap;
import static com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate.EmptyAnnotations;

public final class PackageIntermediateRecorder implements PackageDetailRecorder
{
	@NotNull private final Map<PackageName, PackageIntermediate> index;

	public PackageIntermediateRecorder()
	{
		index = new HashMap<>();
	}

	public void ensurePackageIsRepresented(@NotNull final PackageName packageName)
	{
		ensurePackageIsRepresentedAndAddAnnotationInstances(new PackageIntermediate(packageName, EmptyAnnotations));
	}

	@Override
	public void ensurePackageIsRepresentedAndAddAnnotationInstances(@NotNull final PackageIntermediate packageIntermediate)
	{
		packageIntermediate.record(index);
	}

	@NotNull
	public PackageDetailMap resolve(@NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		final int indexSize = index.size();
		if (indexSize == 0)
		{
			return EmptyPackageDetailMap;
		}

		final Map<PackageName, PackageDetail> resolved = new HashMap<>(indexSize);

		for (Map.Entry<PackageName, PackageIntermediate> entry : index.entrySet())
		{
			final PackageName key = entry.getKey();
			final PackageIntermediate packageIntermediate = entry.getValue();
			resolved.put(key, packageIntermediate.resolve(memberIntermediateRecorder, simpleTypeIntermediateRecorder));
		}

		return new PackageDetailMap(resolved);
	}
}
