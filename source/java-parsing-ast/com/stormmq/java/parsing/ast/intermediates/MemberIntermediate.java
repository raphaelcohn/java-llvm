package com.stormmq.java.parsing.ast.intermediates;

import com.stormmq.java.parsing.ast.details.MemberDetail;
import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.MemberIntermediateRecorder;
import com.stormmq.java.parsing.ast.intermediateRecorders.SimpleTypeIntermediateRecorder;
import org.jetbrains.annotations.NotNull;

public interface MemberIntermediate<D extends MemberDetail>
{
	@NotNull
	D resolve(@NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder);
}
