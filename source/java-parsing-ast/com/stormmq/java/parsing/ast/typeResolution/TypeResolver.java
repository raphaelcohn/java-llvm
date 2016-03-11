package com.stormmq.java.parsing.ast.typeResolution;

import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;

public interface TypeResolver
{
	@Nullable
	LinkedHashSet<KnownReferenceTypeName> get(@NotNull final String fullyQualifiedName);

	boolean has(@NotNull final String fullyQualifiedName);

	@Nullable
	KnownReferenceTypeName resolve(@NotNull final String fullyQualifiedOrTrailingPartlyQualifiedName);

	@Nullable
	KnownReferenceTypeName resolve(@NotNull final PackageName packageName, @NotNull final String identifier);

	@Nullable
	KnownReferenceTypeName resolve(@NotNull final KnownReferenceTypeName parent, @NotNull final String identifier);
}
