package com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts;

import com.stormmq.java.parsing.adaptors.javaparser.voidVisitors.bodyDeclarations.usefuls.UsefulTypeParameters;
import com.stormmq.java.parsing.utilities.names.parentNames.ParentName;
import com.stormmq.java.parsing.utilities.names.typeNames.referenceTypeNames.KnownReferenceTypeName;
import com.stormmq.java.parsing.ast.typeResolution.TypeResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.java.parsing.adaptors.javaparser.ast.typeResolverContexts.TypeResolverChoice.Type;

public abstract class AbstractTypeResolverContext extends VeryAbstractTypeResolverContext
{
	@NotNull private final TypeResolver typeResolver;
	@NotNull private final TypeResolverChoice typeResolverChoice;
	@NotNull private final TypeResolverContext parent;
	@NotNull private final ParentName ourParentName;
	@NotNull private final String ourSimpleTypeName;
	@NotNull private final UsefulTypeParameters usefulTypeParameters;

	protected AbstractTypeResolverContext(@NotNull final TypeResolver typeResolver, @NotNull final TypeResolverChoice typeResolverChoice, @NotNull final TypeResolverContext parent, @NotNull final ParentName ourParentName, @NotNull final String ourSimpleTypeName, @NotNull final UsefulTypeParameters usefulTypeParameters)
	{
		this.typeResolver = typeResolver;
		this.typeResolverChoice = typeResolverChoice;
		this.parent = parent;
		this.ourParentName = ourParentName;
		this.ourSimpleTypeName = ourSimpleTypeName;
		this.usefulTypeParameters = usefulTypeParameters;
	}

	@Override
	@Nullable
	public final KnownReferenceTypeName resolveAsTypeParameter(@NotNull final String identifier)
	{
		@Nullable final KnownReferenceTypeName resolveAsTypeParameter = usefulTypeParameters.resolve(identifier, this);
		if (resolveAsTypeParameter != null)
		{
			return resolveAsTypeParameter;
		}
		return parent.resolveAsTypeParameter(identifier);
	}

	@Override
	@Nullable
	public final KnownReferenceTypeName resolveAsType(@NotNull final String identifier)
	{
		// Is this us, or one of our siblings?
		@Nullable final KnownReferenceTypeName knownReferenceTypeName = typeResolverChoice.resolve(typeResolver, ourParentName, identifier);
		if (knownReferenceTypeName != null)
		{
			return knownReferenceTypeName;
		}

		return parent.resolveAsType(identifier);
	}

	@NotNull
	public final KnownReferenceTypeName resolve(@NotNull final String identifier)
	{
		@Nullable final KnownReferenceTypeName resolveAsTypeParameter = resolveAsTypeParameter(identifier);
		if (resolveAsTypeParameter != null)
		{
			return resolveAsTypeParameter;
		}
		@Nullable final KnownReferenceTypeName typeName = resolveAsType(identifier);
		if (typeName == null)
		{
			throw new IllegalArgumentException("Could not resolve identifier");
		}
		return typeName;
	}

	@Override
	@NotNull
	public final TypeResolverContext topLevelAnnotation(@NotNull final String annotationName)
	{
		throw new UnsupportedOperationException("Not possible for a non-CompilationUnit context");
	}

	@Override
	@NotNull
	public final TypeResolverContext topLevelInterface(@NotNull final String interfaceName, @NotNull final UsefulTypeParameters interfaceTypeParameters)
	{
		throw new UnsupportedOperationException("Not possible for a non-CompilationUnit context");
	}

	@Override
	@NotNull
	public final TypeResolverContext topLevelClass(@NotNull final String className, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		throw new UnsupportedOperationException("Not possible for a non-CompilationUnit context");
	}

	@Override
	@NotNull
	public final TypeResolverContext topLevelEnum(@NotNull final String enumName)
	{
		throw new UnsupportedOperationException("Not possible for a non-CompilationUnit context");
	}

	@NotNull
	@Override
	public TypeResolverContext nestedAnnotation(@NotNull final String annotationName)
	{
		return new AnnotationTypeResolverContext(typeResolver, Type, this, child(), annotationName);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedInterface(@NotNull final String interfaceName, @NotNull final UsefulTypeParameters interfaceTypeParameters)
	{
		return new InterfaceTypeResolverContext(typeResolver, Type, this, child(), interfaceName, interfaceTypeParameters);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedInnerClass(@NotNull final String rawInnerClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		return new NestedInnerClassTypeResolverContext(typeResolver, this, child(), rawInnerClassName, classTypeParameters);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedStaticClass(@NotNull final String rawStaticClassName, @NotNull final UsefulTypeParameters classTypeParameters)
	{
		return new StaticClassTypeResolverContext(typeResolver, Type, this, child(), rawStaticClassName, classTypeParameters);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedAnonymousClass(final int oneBasedOrdinal)
	{
		return new NestedAnonymousClassTypeResolverContext(typeResolver, this, (KnownReferenceTypeName) ourParentName, ourSimpleTypeName);
	}

	@NotNull
	@Override
	public TypeResolverContext nestedEnum(@NotNull final String enumName)
	{
		return new EnumTypeResolverContext(typeResolver, Type, this, child(), enumName);
	}

	@NotNull
	private KnownReferenceTypeName child() {return ourParentName.child(ourSimpleTypeName);}

	@NotNull
	protected final TypeResolverContext newConstructorOrMethodTypeResolverContext(@NotNull final UsefulTypeParameters constructorOrMethodTypeParameters)
	{
		return new ConstructorOrMethodTypeResolverContext(typeResolver, this, (KnownReferenceTypeName) ourParentName, ourSimpleTypeName, constructorOrMethodTypeParameters);
	}

	@NotNull
	protected final EnumConstantClassBodyTypeResolverContext newEnumConstantClassBodyTypeResolverContext(@NotNull final String enumConstantName)
	{
		return new EnumConstantClassBodyTypeResolverContext(typeResolver, this, (KnownReferenceTypeName) ourParentName, ourSimpleTypeName, enumConstantName);
	}
}
