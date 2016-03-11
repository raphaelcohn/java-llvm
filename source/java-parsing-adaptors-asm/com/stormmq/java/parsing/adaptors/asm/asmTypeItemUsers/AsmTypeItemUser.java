package com.stormmq.java.parsing.adaptors.asm.asmTypeItemUsers;

import com.stormmq.java.parsing.adaptors.asm.items.AsmTypeItem;
import org.jetbrains.annotations.NotNull;

public interface AsmTypeItemUser
{
	void use(@NotNull final AsmTypeItem asmTypeItem);
}
