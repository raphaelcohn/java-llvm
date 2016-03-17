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

package com.stormmq.llvm.api.metadata.specializedMetadataNodes;

import com.stormmq.llvm.api.metadataWriters.MetadataNodeIndexProvider;
import com.stormmq.llvm.api.metadataWriters.SpecializedLabelledFieldsMetadataWriter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public final class DIFileSpecializedMetadataNode extends AbstractSpecializedMetadataNode
{
	/*
		So, for example, if we decompile 'MyClass$1.class', the source file is either MyClass.java OR MyClass$1.class
		The directory is that of 'MyClass$1.class', usually absolute, but we may want to strip some of that to avoid revealing data
		 	- can it be relative?
	*/

	// simpleSourceFileName can include slashes, but is usually relative
	@NotNull private final String simpleSourceFileName;
	@NotNull private final Path directoryUsageOfSimpleSourceFileEncounteredIn;

	public DIFileSpecializedMetadataNode(@NonNls @NotNull final String simpleSourceFileName, @NotNull final Path directoryUsageOfSimpleSourceFileEncounteredIn)
	{
		this.simpleSourceFileName = simpleSourceFileName;
		this.directoryUsageOfSimpleSourceFileEncounteredIn = directoryUsageOfSimpleSourceFileEncounteredIn;
	}

	@NonNls
	@NotNull
	@Override
	protected String specializedName()
	{
		return "DIFile";
	}

	@Override
	protected <X extends Exception> void writeLabelledFields(@NotNull final MetadataNodeIndexProvider metadataNodeIndexProvider, @NotNull final SpecializedLabelledFieldsMetadataWriter<X> specializedLabelledFieldsMetadataWriter) throws X
	{
		specializedLabelledFieldsMetadataWriter.writeLabelledField("filename", simpleSourceFileName);
		specializedLabelledFieldsMetadataWriter.writeLabelledField("directory", directoryUsageOfSimpleSourceFileEncounteredIn.toString());
	}
}
