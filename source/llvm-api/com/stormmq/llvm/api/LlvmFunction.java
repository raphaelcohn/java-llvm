package com.stormmq.llvm.api;


import com.stormmq.llvm.domain.*;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.CallingConvention.ccc;
import static com.stormmq.llvm.domain.Linkage.external;
import static com.stormmq.llvm.domain.Visibility._default;

public @interface LlvmFunction
{
	@NotNull Linkage linkage() default external;

	@NotNull CallingConvention callingConvention() default ccc;

	@NotNull Visibility visibility() default _default;
}
