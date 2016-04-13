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

import org.jetbrains.annotations.*;

import java.io.*;

import static com.stormmq.llvm.domain.target.Alignment.OneHundredAndTwentyBitAlignment;
import static com.stormmq.llvm.domain.target.Alignment.SixteenBitAlignment;
import static com.stormmq.llvm.domain.target.Alignment.SixtyFourBitAlignment;
import static com.stormmq.string.StringUtilities.enumSerializationIsNotSupportedForConstantsInASecureContext;

public enum CLongDoublePermutation
{
	// Microsoft Windows, FreeBSD and NetBSD
	SixtyFourBitLongDouble(64, 64, SixtyFourBitAlignment)
	{
		@NotNull
		@Override
		public <T> T choose(@NotNull final T doubleType, @NotNull final T x86BitDoubleType, @NotNull final T powerPcDoubleDoubleType, @NotNull final T quadDoubleType, @NotNull final T mipsLongDoubleType)
		{
			return doubleType;
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

	// 32-bit x86 long double, takes 96-bits to store but with 80-bit precision (GCC and Clang)
	i686NinetySixBitLongDoubleClangAndGcc(80, 96, SixtyFourBitAlignment)
	{
		@NotNull
		@Override
		public <T> T choose(@NotNull final T doubleType, @NotNull final T x86BitDoubleType, @NotNull final T powerPcDoubleDoubleType, @NotNull final T quadDoubleType, @NotNull final T mipsLongDoubleType)
		{
			return x86BitDoubleType;
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

	// 32-bit x86 long double, takes 96-bits to store but with 80-bit precision (Borland / CodeGear C++ Builder)
	i686NinetySixBitLongDoubleCxxBuilder(80, 80, SixtyFourBitAlignment)
	{
		@NotNull
		@Override
		public <T> T choose(@NotNull final T doubleType, @NotNull final T x86BitDoubleType, @NotNull final T powerPcDoubleDoubleType, @NotNull final T quadDoubleType, @NotNull final T mipsLongDoubleType)
		{
			return x86BitDoubleType;
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

	// 32-bit x86 long double, Digital Mars
	i686NinetySixBitLongDoubleDigitalMars(80, 16, SixteenBitAlignment)
	{
		@NotNull
		@Override
		public <T> T choose(@NotNull final T doubleType, @NotNull final T x86BitDoubleType, @NotNull final T powerPcDoubleDoubleType, @NotNull final T quadDoubleType, @NotNull final T mipsLongDoubleType)
		{
			return x86BitDoubleType;
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

	// 64-bit x86-64 long double, takes 128-bits to store but with 80-bit precision
	X86_64LongDouble(80, 128, OneHundredAndTwentyBitAlignment)
	{
		@NotNull
		@Override
		public <T> T choose(@NotNull final T doubleType, @NotNull final T x86BitDoubleType, @NotNull final T powerPcDoubleDoubleType, @NotNull final T quadDoubleType, @NotNull final T mipsLongDoubleType)
		{
			return x86BitDoubleType;
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

	// Now considered legacy with POWER8 and POWER9
	PowerPcDoubleDouble(106, 128, OneHundredAndTwentyBitAlignment)
	{
		@NotNull
		@Override
		public <T> T choose(@NotNull final T doubleType, @NotNull final T x86BitDoubleType, @NotNull final T powerPcDoubleDoubleType, @NotNull final T quadDoubleType, @NotNull final T mipsLongDoubleType)
		{
			return powerPcDoubleDoubleType;
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

	// These values are just a guess
	Mips64LongDouble(106, 128, OneHundredAndTwentyBitAlignment)
	{
		@NotNull
		@Override
		public <T> T choose(@NotNull final T doubleType, @NotNull final T x86BitDoubleType, @NotNull final T powerPcDoubleDoubleType, @NotNull final T quadDoubleType, @NotNull final T mipsLongDoubleType)
		{
			throw new UnsupportedOperationException("MIPS-64 long double isn't supported at this time, but clang prefers to user the newer Quad type");
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

	// SPARC v9, ?AArch64, newer MIPS64
	QuadLongDouble(128, 128, OneHundredAndTwentyBitAlignment)
	{
		@NotNull
		@Override
		public <T> T choose(@NotNull final T doubleType, @NotNull final T x86BitDoubleType, @NotNull final T powerPcDoubleDoubleType, @NotNull final T quadDoubleType, @NotNull final T mipsLongDoubleType)
		{
			return quadDoubleType;
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

	private final int precisionInBits;
	public final int storageSizeInBits;
	@NotNull public final Alignment alignment;

	CLongDoublePermutation(final int precisionInBits, final int storageSizeInBits, @NotNull final Alignment alignment)
	{
		this.precisionInBits = precisionInBits;
		this.storageSizeInBits = storageSizeInBits;
		this.alignment = alignment;
	}

	@NotNull
	public abstract <T> T choose(@NotNull final T doubleType, @NotNull final T x86BitDoubleType, @NotNull final T powerPcDoubleDoubleType, @NotNull final T quadDoubleType, @NotNull final T mipsLongDoubleType);

	@SuppressWarnings("MagicNumber")
	public boolean doesDataModelSupportEightyBitPrecisionFloatingPoint()
	{
		return precisionInBits == 80;
	}
}
