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

import static com.stormmq.string.StringUtilities.enumSerializationIsNotSupportedForConstantsInASecureContext;

public enum Sizing
{
	Sixteen
	{
		@Override
		public int choose(final int sixteen, final int thirtyTwo, final int sixtyFour)
		{
			return sixteen;
		}

		@NotNull
		@Override
		public <T> T choose(@NotNull final T sixteen, @NotNull final T thirtyTwo, @NotNull final T sixtyFour)
		{
			return sixteen;
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
	ThirtyTwo
	{
		@Override
		public int choose(final int sixteen, final int thirtyTwo, final int sixtyFour)
		{
			return thirtyTwo;
		}

		@NotNull
		@Override
		public <T> T choose(@NotNull final T sixteen, @NotNull final T thirtyTwo, @NotNull final T sixtyFour)
		{
			return thirtyTwo;
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
	SixtyFour
	{
		@Override
		public int choose(final int sixteen, final int thirtyTwo, final int sixtyFour)
		{
			return sixtyFour;
		}

		@NotNull
		@Override
		public <T> T choose(@NotNull final T sixteen, @NotNull final T thirtyTwo, @NotNull final T sixtyFour)
		{
			return sixtyFour;
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

	public boolean isLessThanThirtyTwo()
	{
		return isSixteen();
	}

	public boolean isSixteen()
	{
		return this == Sixteen;
	}
	public boolean isNotSixteen()
	{
		return this != Sixteen;
	}

	public boolean isNotThirtyTwo()
	{
		return this != ThirtyTwo;
	}

	public boolean isThirtyTwo()
	{
		return this == ThirtyTwo;
	}

	public boolean isSixtyFour()
	{
		return this == SixtyFour;
	}

	public abstract int choose(final int sixteen, final int thirtyTwo, final int sixtyFour);

	@NotNull
	public abstract <T> T choose(@NotNull final T sixteen, @NotNull final T thirtyTwo, @NotNull final T sixtyFour);

	@SuppressWarnings("MagicNumber")
	public int storageSizeInBits()
	{
		return choose(16, 32, 64);
	}
}
