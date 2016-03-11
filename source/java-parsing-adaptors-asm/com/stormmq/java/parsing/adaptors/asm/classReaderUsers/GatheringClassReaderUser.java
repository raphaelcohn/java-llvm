package com.stormmq.java.parsing.adaptors.asm.classReaderUsers;

import com.stormmq.java.parsing.adaptors.asm.visitorAdaptors.typeVisitorAdaptors.GatheringTypeVisitorAdaptor;
import com.stormmq.java.parsing.adaptors.asm.asmTypeItemUsers.AsmTypeItemUser;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;

import java.nio.file.Path;

import static com.stormmq.java.parsing.adaptors.asm.visitors.CombinationClassVisitor.combinationClassVisitor;

public final class GatheringClassReaderUser implements ClassReaderUser
{
	@NotNull private static final Attribute[] NoSpecialAttributes = {};

	@NotNull private final AsmTypeItemUser asmTypeItemUser;

	public GatheringClassReaderUser(@NotNull final AsmTypeItemUser asmTypeItemUser)
	{
		this.asmTypeItemUser = asmTypeItemUser;
	}

	@Override
	public void use(@NotNull final Path relativeFilePath, @NotNull final ClassReader classReader)
	{
		final GatheringTypeVisitorAdaptor gatheringClassVisitorAdaptor = new GatheringTypeVisitorAdaptor(asmTypeItemUser);
		classReader.accept(combinationClassVisitor(gatheringClassVisitorAdaptor), NoSpecialAttributes, 0);
	}
}
