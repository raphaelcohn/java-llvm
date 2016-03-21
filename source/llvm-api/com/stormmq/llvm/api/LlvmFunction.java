package com.stormmq.llvm.api;


import com.stormmq.llvm.domain.*;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.domain.CallingConvention.ccc;
import static com.stormmq.llvm.domain.LinkageType.external;
import static com.stormmq.llvm.domain.VisibilityStyle._default;

public @interface LlvmFunction
{
	@NotNull LinkageType linkage() default external;

	@NotNull CallingConvention callingConvention() default ccc;

	@NotNull VisibilityStyle visibility() default _default;
}
