package com.stormmq.java.parsing.adaptors.asm.items;

import com.stormmq.java.parsing.adaptors.asm.items.AsmAnnotationItem;
import org.jetbrains.annotations.NotNull;

public interface AsmAnnotationItemUser
{
	void use(@NotNull final AsmAnnotationItem asmAnnotationItem);
}
