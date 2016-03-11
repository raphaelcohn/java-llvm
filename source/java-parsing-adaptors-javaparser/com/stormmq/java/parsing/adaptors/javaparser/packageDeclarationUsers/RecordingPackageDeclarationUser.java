package com.stormmq.java.parsing.adaptors.javaparser.packageDeclarationUsers;

import com.github.javaparser.ast.PackageDeclaration;
import com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverContext;
import com.stormmq.java.parsing.ast.intermediates.annotationInstanceIntermediates.AnnotationInstanceIntermediate;
import com.stormmq.java.parsing.ast.intermediateRecorders.PackageDetailRecorder;
import com.stormmq.java.parsing.ast.intermediates.packageIntermediates.PackageIntermediate;
import com.stormmq.java.parsing.utilities.names.PackageName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RecordingPackageDeclarationUser implements PackageDeclarationUser
{
	@NotNull private final PackageDetailRecorder packageDetailRecorder;

	public RecordingPackageDeclarationUser(@NotNull final PackageDetailRecorder packageDetailRecorder)
	{
		this.packageDetailRecorder = packageDetailRecorder;
	}

	@Override
	public void use(@NotNull final PackageName packageName, @Nullable final PackageDeclaration packageDeclaration, @NotNull final TypeResolverContext typeResolverContext)
	{
		if (packageDeclaration == null)
		{
			packageDetailRecorder.ensurePackageIsRepresented(packageName);
			return;
		}

		final AnnotationInstanceIntermediate[] annotationInstanceIntermediates = typeResolverContext.convertToAnnotations(packageDeclaration.getAnnotations());
		packageDetailRecorder.ensurePackageIsRepresentedAndAddAnnotationInstances(new PackageIntermediate(packageName, annotationInstanceIntermediates));
	}
}
