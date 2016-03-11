package com.stormmq.llvm.api;

import org.jetbrains.annotations.NotNull;

public final class LlvmOperations
{
    private LlvmOperations()
    {
    }

    // Replace %a, %b with actual variables
    @LlvmCode("add i32 %a %b")
    @NotNull
    public static i32 add(@SuppressWarnings("UnusedParameters") @NotNull final i32 a, @SuppressWarnings("UnusedParameters") @NotNull final i32 b)
    {
        throw new IllegalStateException("This is a LLVM intrinsic");
    }
}
