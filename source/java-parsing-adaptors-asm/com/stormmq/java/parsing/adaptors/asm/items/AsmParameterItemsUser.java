package com.stormmq.java.parsing.adaptors.asm.items;

import com.stormmq.java.parsing.adaptors.asm.items.AsmParameterItem;
import org.jetbrains.annotations.NotNull;

public interface AsmParameterItemsUser
{
	void use(@NotNull final AsmParameterItem[] asmParameterItems);
}
