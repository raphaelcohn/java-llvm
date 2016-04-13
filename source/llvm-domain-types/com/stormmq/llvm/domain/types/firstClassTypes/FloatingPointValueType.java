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
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.string.StringConstants;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.*;

import static com.stormmq.string.StringUtilities.enumSerializationIsNotSupportedForConstantsInASecureContext;
import static com.stormmq.string.Utf8ByteUser.encodeToUtf8ByteArrayWithCertaintyValueIsValid;

public enum FloatingPointValueType implements PrimitiveSingleValueType
{
	// Support is poor except for ARM; clang convert's C's __fp16 to i16...
	half(16, "half")
	{
		@Override
		public int abiAlignmentInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
		{
			return dataLayoutSpecification.halfAbiAlignmentInBits();
		}

		private void writeObject(@NotNull final ObjectOutputStream out) throws IOException
		{
			throw enumSerializationIsNotSupportedForConstantsInASecureContext();
		}

		private void readObject(@NotNull final ObjectInputStream in) throws IOException
		{
			throw enumSerializationIsNotSupportedForConstantsInASecureContext();
		}
	},

	_float(32, StringConstants._float)
	{
		@Override
		public int abiAlignmentInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
		{
			return dataLayoutSpecification.floatAbiAlignmentInBits();
		}

		private void writeObject(@NotNull final ObjectOutputStream out) throws IOException
		{
			throw enumSerializationIsNotSupportedForConstantsInASecureContext();
		}

		private void readObject(@NotNull final ObjectInputStream in) throws IOException
		{
			throw enumSerializationIsNotSupportedForConstantsInASecureContext();
		}
	},

	_double(64, StringConstants._double)
	{
		@Override
		public int abiAlignmentInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
		{
			return dataLayoutSpecification.doubleAbiAlignmentInBits();
		}

		private void writeObject(@NotNull final ObjectOutputStream out) throws IOException
		{
			throw enumSerializationIsNotSupportedForConstantsInASecureContext();
		}

		private void readObject(@NotNull final ObjectInputStream in) throws IOException
		{
			throw enumSerializationIsNotSupportedForConstantsInASecureContext();
		}
	},

	// AKA Quad. Used in SPARC v9, AArch64 (IIRC), possibly MIPS 64
	fp128(128, "fp128")
	{
		@Override
		public int abiAlignmentInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
		{
			return dataLayoutSpecification.quadAbiAlignmentInBits();
		}

		private void writeObject(@NotNull final ObjectOutputStream out) throws IOException
		{
			throw enumSerializationIsNotSupportedForConstantsInASecureContext();
		}

		private void readObject(@NotNull final ObjectInputStream in) throws IOException
		{
			throw enumSerializationIsNotSupportedForConstantsInASecureContext();
		}
	},

	// X86 only, and only then for Linux, Mac OS X and NetBSD
	@SuppressWarnings("DuplicateStringLiteralInspection")x86_fp80(-1, "x86_fp80")
	{
		@SuppressWarnings("RefusedBequest")
		@Override
		public int storageSizeInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
		{
			return dataLayoutSpecification.longDoubleStorageSizeInBits();
		}

		@Override
		public int abiAlignmentInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
		{
			return dataLayoutSpecification.longDoubleAbiAlignmentInBits();
		}

		private void writeObject(@NotNull final ObjectOutputStream out) throws IOException
		{
			throw enumSerializationIsNotSupportedForConstantsInASecureContext();
		}

		private void readObject(@NotNull final ObjectInputStream in) throws IOException
		{
			throw enumSerializationIsNotSupportedForConstantsInASecureContext();
		}
	},

	// PowerPC double-double only.
	ppc_fp128(128, "ppc_fp128")
	{
		@Override
		public int abiAlignmentInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
		{
			return dataLayoutSpecification.longDoubleStorageSizeInBits();
		}

		private void writeObject(@NotNull final ObjectOutputStream out) throws IOException
		{
			throw enumSerializationIsNotSupportedForConstantsInASecureContext();
		}

		private void readObject(@NotNull final ObjectInputStream in) throws IOException
		{
			throw enumSerializationIsNotSupportedForConstantsInASecureContext();
		}
	},

	;

	@NotNull private final byte[] llvmAssemblyEncoding;
	private final int storageSizeInBits;

	FloatingPointValueType(final int storageSizeInBits, @NotNull @NonNls final String llvmName)
	{
		this.storageSizeInBits = storageSizeInBits;
		llvmAssemblyEncoding = encodeToUtf8ByteArrayWithCertaintyValueIsValid(llvmName);
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification) throws X
	{
		byteWriter.writeBytes(llvmAssemblyEncoding);
	}

	@Override
	public int storageSizeInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification)
	{
		return storageSizeInBits;
	}
}
