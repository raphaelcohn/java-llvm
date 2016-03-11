package com.stormmq.java.parsing.adaptors.javaparser.dead;

import com.stormmq.java.parsing.adaptors.javaparser.ast.importsResolvers.ImportsResolver;
import com.stormmq.java.parsing.adaptors.javaparser.referenceTypeNameMakers.ImportsReferenceTypeNameMaker;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.CompilationUnitWrapper;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.ReferenceTypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public final class PartlyUnknownReferenceTypeName implements ReferenceTypeName
{
	@NotNull private final CompilationUnitWrapper compilationUnitWrapper;
	@NotNull private final String fullyQualifiedOrTrailingPartlyQualifiedName;
	@NotNull private final ImportsResolver importsResolver;
	private final AtomicReference<KnownReferenceTypeName> resolved;

	public PartlyUnknownReferenceTypeName(@NotNull final String fullyQualifiedOrTrailingPartlyQualifiedName, @NotNull final ImportsResolver importsResolver, @NotNull final CompilationUnitWrapper compilationUnitWrapper)
	{
		this.importsResolver = importsResolver;
		if (fullyQualifiedOrTrailingPartlyQualifiedName.isEmpty())
		{
			throw new IllegalArgumentException("Empty fullyQualifiedOrTrailingPartlyQualifiedName is not allowed");
		}
		this.fullyQualifiedOrTrailingPartlyQualifiedName = fullyQualifiedOrTrailingPartlyQualifiedName;
		this.compilationUnitWrapper = compilationUnitWrapper;
		resolved = new AtomicReference<>(null);
	}

	@Override
	public boolean isJavaLangObject()
	{
		return resolve().isJavaLangObject();
	}

	@Override
	public boolean isVoid()
	{
		return false;
	}

	@NotNull
	@Override
	public KnownReferenceTypeName resolve()
	{
		@Nullable KnownReferenceTypeName knownReferenceTypeName;
		knownReferenceTypeName = resolved.get();
		if (knownReferenceTypeName != null)
		{
			return knownReferenceTypeName;
		}
		final ImportsReferenceTypeNameMaker importsReferenceTypeNameMaker = importsResolver.resolveNonStaticImports(compilationUnitWrapper);
		while(knownReferenceTypeName == null)
		{
			final KnownReferenceTypeName knownReferenceTypeNameX = (KnownReferenceTypeName) importsReferenceTypeNameMaker.make(fullyQualifiedOrTrailingPartlyQualifiedName);
			if (resolved.compareAndSet(null, knownReferenceTypeNameX))
			{
				return knownReferenceTypeNameX;
			}
			else
			{
				knownReferenceTypeName = resolved.get();
			}
		}
		return knownReferenceTypeName;
	}

	@Override
	public boolean isPrimitive()
	{
		return false;
	}

}
