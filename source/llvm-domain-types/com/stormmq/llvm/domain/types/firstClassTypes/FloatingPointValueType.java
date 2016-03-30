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

package com.stormmq.llvm.domain.types.firstClassTypes;

import com.stormmq.byteWriters.ByteWriter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import static com.stormmq.string.Formatting.format;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public enum FloatingPointValueType implements PrimitiveSingleValueType
{
	half(16),
	_float(32),
	_double(64),
	fp128(128), // AKA quad
	x86_fp80(80), // AKA long double
	ppc_fp128(128), // AKA double-double
	;

	@SuppressWarnings("MagicNumber")
	@NotNull
	public static FloatingPointValueType floatingPointValueTypeFromSizeInBits(final int sizeInBits)
	{
		switch (sizeInBits)
		{
			case 16:
				return half;

			case 32:
				return _float;

			case 64:
				return _double;

			case 80:
				return x86_fp80;

			case 128:
				return fp128;

			default:
				throw new IllegalArgumentException(format("floating point sizeInBits can not be '%1$s'", sizeInBits));
		}
	}

	@NotNull private final byte[] llvmAssemblyEncoding;
	private final int numberOfBits;

	FloatingPointValueType(final int numberOfBits)
	{
		this.numberOfBits = numberOfBits;
		final String name = name();
		@NotNull @NonNls final String stringValue = name.charAt(0) == '_' ? name.substring(1) : name;
		llvmAssemblyEncoding = encodeUtf8BytesWithCertaintyValueIsValid(stringValue);
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(llvmAssemblyEncoding);
	}
}
