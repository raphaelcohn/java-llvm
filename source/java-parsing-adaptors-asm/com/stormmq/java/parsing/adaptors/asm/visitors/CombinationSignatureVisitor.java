package com.stormmq.java.parsing.adaptors.asm.visitors;

import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.signatureVisitorAdaptors.SignatureVisitorAdaptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.signature.SignatureVisitor;

import java.lang.reflect.Constructor;
import java.util.List;

import static com.stormmq.java.parsing.adaptors.asm.helpers.AsmHelper.AsmVersion;

public final class CombinationSignatureVisitor extends SignatureVisitor
{
	@NotNull private final Streamable<SignatureVisitorAdaptor> signatureVisitorAdaptors;

	public CombinationSignatureVisitor(@NotNull final List<SignatureVisitorAdaptor> signatureVisitorAdaptors)
	{
		super(AsmVersion);
		this.signatureVisitorAdaptors = new Streamable<>(signatureVisitorAdaptors);
	}

	@Override
	public void visitFormalTypeParameter(@NotNull final String name)
	{
		super.visitFormalTypeParameter(name);
	}

	@Override
	@Nullable
	public SignatureVisitor visitClassBound()
	{
		return super.visitClassBound();
		x;
	}

	@Override
	@Nullable
	public SignatureVisitor visitInterfaceBound()
	{
		return super.visitInterfaceBound();
		x;
	}

	@Override
	@Nullable
	public SignatureVisitor visitSuperclass()
	{
		final Class<SignatureVisitor> signatureVisitorClass = SignatureVisitor.class;
		final Constructor<SignatureVisitor> constructor = signatureVisitorClass.getConstructor(null);
		constructor.toGenericString()
		return super.visitSuperclass();
		x;
	}

	@Override
	@Nullable
	public SignatureVisitor visitInterface()
	{
		return super.visitInterface();
		x;
	}

	@Override
	@Nullable
	public SignatureVisitor visitParameterType()
	{
		return super.visitParameterType();
		x;
	}

	@Override
	@Nullable
	public SignatureVisitor visitReturnType()
	{
		return super.visitReturnType();
		x;
	}

	@Override
	@Nullable
	public SignatureVisitor visitExceptionType()
	{
		return super.visitExceptionType();
		x;
	}

	@Override
	public void visitBaseType(final char descriptor)
	{
		super.visitBaseType(descriptor);
	}

	@Override
	public void visitTypeVariable(@NotNull final String name)
	{
		super.visitTypeVariable(name);
	}

	@Override
	@Nullable
	public SignatureVisitor visitArrayType()
	{
		return super.visitArrayType();
		x;
	}

	@Override
	public void visitClassType(@NotNull final String name)
	{
		super.visitClassType(name);
	}

	@Override
	public void visitInnerClassType(@NotNull final String name)
	{
		super.visitInnerClassType(name);
	}

	@Override
	public void visitTypeArgument()
	{
		super.visitTypeArgument();
	}

	@Override
	@Nullable
	public SignatureVisitor visitTypeArgument(final char wildcard)
	{
		return super.visitTypeArgument(wildcard);
		x;
	}

	@Override
	public void visitEnd()
	{
		super.visitEnd();
	}
}
