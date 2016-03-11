package com.stormmq.java.parsing.ast.detailMaps;

import com.stormmq.java.parsing.ast.details.packageDetails.PackageDetail;
import com.stormmq.java.parsing.utilities.names.PackageName;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

public final class PackageDetailMap
{
	@NotNull private static final Map<PackageName, PackageDetail> TemporaryEmptyMap = Collections.emptyMap();
	@NotNull public static final PackageDetailMap EmptyPackageDetailMap = new PackageDetailMap(TemporaryEmptyMap);

	@NotNull private final Map<PackageName, PackageDetail> map;

	public PackageDetailMap(@NotNull final Map<PackageName, PackageDetail> map)
	{
		this.map = map;
	}
}
