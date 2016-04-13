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

package com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.structureTypes;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.LlvmWritable;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.llvm.domain.types.SizedType;
import com.stormmq.llvm.domain.types.firstClassTypes.aggregateTypes.ArrayType;
import org.jetbrains.annotations.*;

import java.io.*;

import static com.stormmq.functions.ListHelper.listToArray;
import static com.stormmq.functions.ListHelper.newArrayList;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.i128;
import static com.stormmq.llvm.domain.types.firstClassTypes.IntegerValueType.i8;
import static com.stormmq.string.StringUtilities.enumSerializationIsNotSupportedForConstantsInASecureContext;

public enum StructureDataLayout
{
	Packed
	{
		@Override
		public int storageSizeInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields)
		{
			int currentSize = 0;
			for (final SizedType field : fields)
			{
				currentSize += field.storageSizeInBits(dataLayoutSpecification);
			}
			return currentSize;
		}

		@Override
		public int abiAlignmentInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields)
		{
			return dataLayoutSpecification.aggregateTypeAbiAlignmentInBits();
		}

		@Override
		public <X extends Exception> void writeBody(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields) throws X
		{
			writeBodyInternal(byteWriter, dataLayoutSpecification, fields, LlvmWritable.OpenAngleBracketOpenBracketSpace, LlvmWritable.SpaceCloseBracketCloseAngleBracket);
		}

		@NotNull
		@Override
		public int[] offsets(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields)
		{
			final int length = fields.length;
			final int[] offsets = new int[length];
			int currentSize = 0;
			for(int index = 0; index < length; index++)
			{
				offsets[index] = currentSize;
				final SizedType field = fields[index];
				currentSize += field.storageSizeInBits(dataLayoutSpecification);
			}
			return offsets;
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

	Normal
	{
		@Override
		public int storageSizeInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields)
		{
			int currentSize = 0;
			int largestAlignmentInBits = dataLayoutSpecification.aggregateTypeAbiAlignmentInBits();

			for (final SizedType field : fields)
			{
				final int elementSizeInBits = field.storageSizeInBits(dataLayoutSpecification);
				final int alignmentInBits = field.abiAlignmentInBits(dataLayoutSpecification);

				// For aggregate types, the X86-64 ABI does not require any alignment
				final int remainder = alignmentInBits == 0 ? 0 : currentSize % alignmentInBits;
				if (remainder == 0)
				{
					currentSize += elementSizeInBits;
				}
				else
				{
					currentSize = (currentSize / alignmentInBits + 1) * alignmentInBits + alignmentInBits;
				}

				if (alignmentInBits > largestAlignmentInBits)
				{
					largestAlignmentInBits = alignmentInBits;
				}
			}

			if (currentSize % largestAlignmentInBits == 0)
			{
				return currentSize;
			}
			return (currentSize / largestAlignmentInBits + 1) * largestAlignmentInBits;
		}

		@Override
		public int abiAlignmentInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields)
		{
			int largestAlignmentInBits = dataLayoutSpecification.aggregateTypeAbiAlignmentInBits();

			for (final SizedType field : fields)
			{
				final int alignmentInBits = field.abiAlignmentInBits(dataLayoutSpecification);
				if (alignmentInBits > largestAlignmentInBits)
				{
					largestAlignmentInBits = alignmentInBits;
				}
			}

			return largestAlignmentInBits;
		}

		@Override
		public <X extends Exception> void writeBody(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields) throws X
		{
			writeBodyInternal(byteWriter, dataLayoutSpecification, fields, LlvmWritable.OpenBracketSpace, LlvmWritable.SpaceCloseBracket);
		}

		@NotNull
		@Override
		public int[] offsets(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields)
		{
			final int length = fields.length;
			final int[] offsets = new int[length];

			int currentSize = 0;
			int largestAlignmentInBits = dataLayoutSpecification.aggregateTypeAbiAlignmentInBits();

			final int lastIndex = length - 1;
			for (int index = 0; index < length; index++)
			{
				offsets[index] = currentSize;
				if (index == lastIndex)
				{
					break;
				}

				final SizedType field = fields[index];

				final int elementSizeInBits = field.storageSizeInBits(dataLayoutSpecification);
				final int alignmentInBits = field.abiAlignmentInBits(dataLayoutSpecification);

				// For aggregate types, the X86-64 ABI does not require any alignment
				final int remainder = alignmentInBits == 0 ? 0 : currentSize % alignmentInBits;
				if (remainder == 0)
				{
					currentSize += elementSizeInBits;
				}
				else
				{
					currentSize = (currentSize / alignmentInBits + 1) * alignmentInBits + alignmentInBits;
				}

				if (alignmentInBits > largestAlignmentInBits)
				{
					largestAlignmentInBits = alignmentInBits;
				}
			}

			return offsets;
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

	ExplicitlyPadInt128
	{
		@Override
		public int storageSizeInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields)
		{
			return Normal.storageSizeInBits(dataLayoutSpecification, fields);
		}

		@Override
		public int abiAlignmentInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields)
		{
			return Normal.abiAlignmentInBits(dataLayoutSpecification, fields);
		}

		@Override
		public <X extends Exception> void writeBody(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields) throws X
		{
			Normal.writeBody(byteWriter, dataLayoutSpecification, int128IsExplicitlyPadded(dataLayoutSpecification, fields));
		}

		@NotNull
		@Override
		public int[] offsets(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields)
		{
			return Normal.offsets(dataLayoutSpecification, fields);
		}

		@NotNull
		private SizedType[] int128IsExplicitlyPadded(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields)
		{
			final int length = fields.length;

			return listToArray(newArrayList(length << 1, list ->
			{
				int currentSize = 0;
				for (int index = 0; index < length; index++)
				{
					final SizedType field = fields[index];
					if (index != 0)
					{
						if (field.equals(i128))
						{
							if (currentSize % 128 != 0)
							{
								final int previousSize = currentSize;
								//noinspection MultiplyOrDivideByPowerOfTwo,MagicNumber
								currentSize = ((currentSize / 128) + 1) * 128;
								final int padding = currentSize - previousSize;
								list.add(new ArrayType<>(i8, padding));
							}
						}
					}
					list.add(field);
					currentSize += field.storageSizeInBits(dataLayoutSpecification);
				}
			}), SizedType[]::new, new SizedType[0]);
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

	@NotNull
	public static StructureDataLayout structureDataLayout(final boolean isPacked, @NotNull final DataLayoutSpecification dataLayoutSpecification)
	{
		if (isPacked)
		{
			return Packed;
		}

		if (dataLayoutSpecification.doesInt128RequireExplicitPadding())
		{
			return ExplicitlyPadInt128;
		}

		return Normal;
	}

	public abstract int storageSizeInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields);

	public abstract int abiAlignmentInBits(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields);

	@NotNull
	public abstract int[] offsets(@NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields);

	public abstract <X extends Exception> void writeBody(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType... fields) throws X;

	private static <X extends Exception> void writeBodyInternal(@NotNull final ByteWriter<X> byteWriter, @NotNull final DataLayoutSpecification dataLayoutSpecification, @NotNull final SizedType[] fields, @NotNull final byte[] open, @NotNull final byte[] close) throws X
	{
		byteWriter.writeBytes(open);

		final int length = fields.length;
		for (int index = 0; index < length; index++)
		{
			final SizedType field = fields[index];
			if (index != 0)
			{
				byteWriter.writeBytes(LlvmWritable.CommaSpace);
			}
			field.write(byteWriter, dataLayoutSpecification);
		}

		byteWriter.writeBytes(close);
	}
}
