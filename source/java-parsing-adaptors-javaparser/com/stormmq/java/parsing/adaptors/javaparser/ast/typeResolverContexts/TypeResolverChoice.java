package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.java.parsing.utilities.names.parentNames.ParentName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeResolution.TypeResolver;
import org.jetbrains.annotations.NotNull;

public enum TypeResolverChoice
{
	Package
	{
		@Override
		public KnownReferenceTypeName resolve(@NotNull final TypeResolver typeResolver, @NotNull final ParentName parentName, @NotNull final String identifier)
		{
			return typeResolver.resolve((PackageName) parentName, identifier);
		}
	},
	Type
	{
		@Override
		public KnownReferenceTypeName resolve(@NotNull final TypeResolver typeResolver, @NotNull final ParentName parentName, @NotNull final String identifier)
		{
			return typeResolver.resolve((KnownReferenceTypeName) parentName, identifier);
		}
	},
	;

	public abstract KnownReferenceTypeName resolve(@NotNull final TypeResolver typeResolver, @NotNull final ParentName parentName, @NotNull final String identifier);
}
