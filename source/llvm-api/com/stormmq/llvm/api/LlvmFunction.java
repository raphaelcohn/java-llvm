package com.stormmq.llvm.api;


import com.stormmq.llvm.domain.function.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.stormmq.llvm.domain.function.CallingConvention.ccc;
import static com.stormmq.llvm.domain.function.LinkageType.external;
import static com.stormmq.llvm.domain.function.VisibilityStyle._default;

public @interface LlvmFunction
{
	@NotNull LinkageType linkage() default external;

	@NotNull CallingConvention callingConvention() default ccc;

	@NotNull VisibilityStyle visibility() default _default;

	@Nullable DllStorageClass dllStorageClass();
}
