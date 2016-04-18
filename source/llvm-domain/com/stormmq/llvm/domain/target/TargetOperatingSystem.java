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
import com.stormmq.string.Formatting;
import org.jetbrains.annotations.*;

import static com.stormmq.llvm.domain.ObjectFileFormat.ELF;
import static com.stormmq.llvm.domain.ObjectFileFormat.MachO;
import static com.stormmq.llvm.domain.target.Vendor.apple;
import static com.stormmq.llvm.domain.target.Vendor.pc;

public enum TargetOperatingSystem
{
	MacOsXMavericks(MachO, apple, "macosx10.9.0"),
	MacOsXYosemite(MachO, apple, "macosx10.10.0"), // AKA x86_64-apple-darwin14.5.0
	MacOsXElCapitan(MachO, apple, "macosx10.11.0"),
	Linux(ELF, pc, "linux"),
	;

	@NotNull private final ObjectFileFormat objectFileFormat;
	@NotNull private final Vendor vendor;
	@NotNull private final String versionedName;
	
	@SuppressWarnings("WeakerAccess")
	TargetOperatingSystem(@NotNull final ObjectFileFormat objectFileFormat, @NotNull final Vendor vendor, @NonNls @NotNull final String versionedName)
	{
		if (versionedName.isEmpty())
		{
			throw new IllegalArgumentException("versionedName is empty");
		}

		if (versionedName.contains("-"))
		{
			throw new IllegalArgumentException("versionedName contains a hyphen");
		}

		this.objectFileFormat = objectFileFormat;
		this.vendor = vendor;
		this.versionedName = versionedName;
	}

	public String toPartialTuple()
	{
		return Formatting.format("%1$s-%2$s", vendor, versionedName);
	}

	public boolean doesNotSupport(@NotNull final Architecture architecture)
	{
		return vendor.doesNotSupport(architecture);
	}
	
	@NotNull
	public ObjectFileFormat objectFileFormat()
	{
		return objectFileFormat;
	}
}
