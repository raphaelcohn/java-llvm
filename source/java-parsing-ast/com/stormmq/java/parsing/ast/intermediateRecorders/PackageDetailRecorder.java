package com.stormmq.java.parsing.ast.intermediateRecorders;

import com.stormmq.java.parsing.ast.intermediates.packageIntermediates.PackageIntermediate;
import com.stormmq.java.parsing.utilities.names.PackageName;
import org.jetbrains.annotations.NotNull;

public interface PackageDetailRecorder
{
	void ensurePackageIsRepresented(@NotNull PackageName packageName);

	void ensurePackageIsRepresentedAndAddAnnotationInstances(@NotNull final PackageIntermediate packageIntermediate);
}
