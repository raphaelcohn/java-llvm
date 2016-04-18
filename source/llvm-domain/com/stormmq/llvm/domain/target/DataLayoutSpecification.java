// The MIT License (MIT)
//
// Copyright © 2016, Raphael Cohn <raphael.cohn@stormmq.com>
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package com.stormmq.llvm.domain.target;

import com.stormmq.llvm.domain.ObjectFileFormat;
import org.jetbrains.annotations.*;

import java.util.List;
import java.util.Map;

public interface DataLayoutSpecification extends CDataModel
{
	int pointerAbiAlignmentInBits();

	int int1AbiAlignmentInBits();

	int int8AbiAlignmentInBits();

	int int16AbiAlignmentInBits();

	int int32AbiAlignmentInBits();

	int int64AbiAlignmentInBits();

	int int128AbiAlignmentInBits();

	int halfAbiAlignmentInBits();

	int floatAbiAlignmentInBits();

	int doubleAbiAlignmentInBits();

	int longDoubleAbiAlignmentInBits();

	int quadAbiAlignmentInBits();

	int vector64AbiAlignmentInBits();

	int vector128AbiAlignmentInBits();

	int aggregateAbiAlignmentInBits();

	@NotNull
	ObjectFileFormat objectFileFormat();

	boolean doesInt128RequireExplicitPadding();

	@NotNull
	<T> T chooseForArchitecture(@NotNull final Map<Architecture, T> knownValues, @NotNull final T defaultValue);
}
