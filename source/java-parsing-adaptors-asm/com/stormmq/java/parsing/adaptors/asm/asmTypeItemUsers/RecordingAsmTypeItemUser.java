package com.stormmq.java.parsing.adaptors.asm.asmTypeItemUsers;

import com.stormmq.java.parsing.adaptors.asm.items.AsmTypeItem;
import com.stormmq.java.parsing.ast.intermediateRecorders.IntermediateRecorder;
import org.jetbrains.annotations.NotNull;

public final class RecordingAsmTypeItemUser implements AsmTypeItemUser
{
	@NotNull private final IntermediateRecorder intermediateRecorder;

	public RecordingAsmTypeItemUser(@NotNull final IntermediateRecorder intermediateRecorder)
	{
		this.intermediateRecorder = intermediateRecorder;
	}

	@Override
	public void use(@NotNull final AsmTypeItem asmTypeItem)
	{
		asmTypeItem.recordTypeIntermediate(intermediateRecorder);
	}
}
