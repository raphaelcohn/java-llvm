package com.stormmq.llvm.api;

import com.stormmq.llvm.api.CallingConvention;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.llvm.api.CallingConvention.ccc;
import static com.stormmq.llvm.api.LinkageType.external;
import static com.stormmq.llvm.api.VisibilityStyle._default;
import static com.stormmq.llvm.api.DllStorageClass.none;

public @interface LlvmFunction
{
	@NotNull LinkageType linkage() default external;

	@NotNull CallingConvention callingConvention() default ccc;

	@NotNull VisibilityStyle visibility() default _default;

	@NotNull DllStorageClass dllStorageClass() default none;
}
