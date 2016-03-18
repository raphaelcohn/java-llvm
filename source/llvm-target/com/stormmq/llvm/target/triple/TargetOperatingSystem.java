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

package com.stormmq.llvm.target.triple;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class TargetOperatingSystem
{
	@NotNull private final Vendor vendor;
	@NotNull private final String versionedName;

	public TargetOperatingSystem(@NotNull final Vendor vendor, @NotNull final String versionedName)
	{
		if (versionedName.isEmpty())
		{
			throw new IllegalArgumentException("versionedName is empty");
		}
		if (versionedName.contains("-"))
		{
			throw new IllegalArgumentException("versionedName contains a hyphen");
		}
		this.vendor = vendor;
		this.versionedName = versionedName;
	}

	@Override
	public String toString()
	{
		return format(ENGLISH, "%1$s-%2$s", vendor, versionedName);
	}

	@Override
	public boolean equals(@Nullable final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		final TargetOperatingSystem that = (TargetOperatingSystem) o;

		if (vendor != that.vendor)
		{
			return false;
		}
		if (!versionedName.equals(that.versionedName))
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = vendor.hashCode();
		result = 31 * result + versionedName.hashCode();
		return result;
	}

	public boolean doesNotSupport(@NotNull final Architecture architecture)
	{
		return vendor.doesNotSupport(architecture);
	}
}
