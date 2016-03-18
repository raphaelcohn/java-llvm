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

package com.stormmq.llvm.metadata.writers;

import com.stormmq.llvm.metadata.Metadata;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface SpecializedLabelledFieldsMetadataWriter<X extends Exception>
{
	void writeLabelledField(@NonNls @NotNull final String label, @NonNls @NotNull final String value) throws X;

	<E extends Enum<E>> void writeLabelledField(@NonNls @NotNull final String label, @NonNls @NotNull final E value) throws X;

	void writeLabelledField(@NonNls @NotNull final String label, final boolean value) throws X;

	void writeLabelledField(@NonNls @NotNull final String label, final int value) throws X;

	<E extends Enum<E>> void writeLabelledField(@NonNls @NotNull final String label, @NotNull final Set<E> values) throws X;

	void writeLabelledField(@NonNls @NotNull final String label, @NotNull final Metadata reference, @NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider) throws X;

	void writeLabelledFieldUnquotedString(@NonNls @NotNull final String label, @NonNls @NotNull final String value) throws X;
}
