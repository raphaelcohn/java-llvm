package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeResolution.TypeResolver;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverChoice.Type;

public final class ConstructorOrMethodTypeResolverContext extends AbstractTypeResolverContext
{
	public ConstructorOrMethodTypeResolverContext(@NotNull final TypeResolver typeResolver, @NotNull final TypeResolverContext parent, @NotNull final KnownReferenceTypeName ourContainingTypeParentKnownReferenceTypeName, @NotNull final String ourContainingTypeSimpleTypeName, @NotNull final UsefulTypeParameters constructorOrMethodTypeParameters, final int blockDepth)
	{
		super(typeResolver, Type, parent, ourContainingTypeParentKnownReferenceTypeName, ourContainingTypeSimpleTypeName, constructorOrMethodTypeParameters);
		predecessors = new ArrayList<>(4);
	}

	@NotNull
	public TypeResolverContext block()
	{
		final List<NestedLocalClassTypeResolverContext> ourPredecessors = new ArrayList<>(predecessors.size());
		Collections.copy(ourPredecessors, predecessors);
		return new BlockTypeResolverContext();
	}

	@NotNull
	@Override
	public TypeResolverContext nestedLocalClass(@NotNull final KnownReferenceTypeName knownReferenceTypeName, @NotNull final String rawLocalClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		// scoping is a bit weird; only predecessors are in scope
		// blocks affect visibility

		// a local class is not allowed anything static

		class A
		{
			void xxx()
			{
				final Class<B> bClass = B.class;
			}
		}

		class B
		{
			void xxx()
			{
				final Class<A> aClass = A.class;
			}
		}
		final List<NestedLocalClassTypeResolverContext> ourPredecessors = new ArrayList<>(predecessors.size());
		Collections.copy(ourPredecessors, predecessors);
		final NestedLocalClassTypeResolverContext nestedLocalClassTypeResolverContext = new NestedLocalClassTypeResolverContext(this, ourPredecessors);
		predecessors.add(nestedLocalClassTypeResolverContext);
		return nestedLocalClassTypeResolverContext;
	}

	@NotNull
	@Override
	public TypeResolverContext nestedAnonymousClass(final int oneBasedOrdinal)
	{
		xxx;
	}

	@NotNull
	@Override
	public TypeResolverContext nestedInterface(@NotNull final String interfaceName, @NotNull final UsefulTypeParameters interfaceTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedInnerClass(@NotNull final String rawInnerClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedStaticClass(@NotNull final String rawStaticClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedAnnotation(@NotNull final String annotationName)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedEnum(@NotNull final String enumName)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext enumConstantClassBody(@NotNull final String enumConstantName)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext constructor(@NotNull final UsefulTypeParameters constructorTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}

	@NotNull
	@Override
	public TypeResolverContext method(@NotNull final UsefulTypeParameters methodTypeParameters)
	{
		throw new IllegalStateException(StringConstants.Should_not_be_possible);
	}
}
