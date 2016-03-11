package com.stormmq.llvm.api;

import org.jetbrains.annotations.NotNull;

@LlvmIntrinsic(value = "i32")
public final class i32 extends IntegerValue<Integer>
{
    public i32(int value)
    {
        super(value);
    }

    // Fake wrapper
    @LlvmSubstitute(clazz = LlvmOperations.class, methodName = "add", arguments = {i32.class, i32.class})
    @NotNull
    public i32 add(@NotNull final i32 right)
    {
        return LlvmOperations.add(this, right);
    }
}
