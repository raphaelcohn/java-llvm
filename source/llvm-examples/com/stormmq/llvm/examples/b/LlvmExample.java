package com.stormmq.llvm.examples.b;

import com.stormmq.llvm.api.*;
import org.jetbrains.annotations.NotNull;


public final class LlvmExample
{
    private LlvmExample()
    {
    }

    /* becomes:-
			define i32 @add1(i32 %x, i32 %y) {
				%tmp1 = add i32 %x, %y
				ret i32 %tmp1
			}
		*/
	@LlvmFunction()
	// @NotNull is converted into the param attribute 'nonnull' when it is a pointer
	@NotNull
    public i32 add1(@NotNull final i32 x, @NotNull final i32 y)
    {
		@NotNull final i32 tmp1 = LlvmOperations.add(x, y);

        // ret i32 %tmp1
        return tmp1;
    }

	@NotNull
	private i32 add2(@NotNull final i32 x, @NotNull final i32 y)
    {
		@NotNull final i32 tmp1 = LlvmOperations.add(x, y);

        // ret i32 %tmp1
        return tmp1;
    }
}
