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

package com.stormmq.llvm.domain.target.triple;

import com.stormmq.byteWriters.ByteWriter;
import com.stormmq.llvm.domain.Writable;
import com.stormmq.string.Formatting;
import org.jetbrains.annotations.*;

import static com.stormmq.llvm.domain.target.triple.Architecture.x86_64;
import static com.stormmq.llvm.domain.target.triple.TargetOperatingSystem.MacOsXMavericks;
import static com.stormmq.string.StringUtilities.encodeUtf8BytesWithCertaintyValueIsValid;

public final class TargetTriple implements Writable
{
	@NotNull public static final TargetTriple MacOsXMavericksOnX86_64 = new TargetTriple(x86_64, MacOsXMavericks, null);
	@NotNull private static final byte[] TargetTripleStart = encodeUtf8BytesWithCertaintyValueIsValid("target triple = \"");
	@SuppressWarnings("HardcodedLineSeparator") @NotNull private static final byte[] End = encodeUtf8BytesWithCertaintyValueIsValid("\"\n");

	@SuppressWarnings("FieldNotUsedInToString") @NotNull private final Architecture architecture;
	@SuppressWarnings("FieldNotUsedInToString") @NotNull private final TargetOperatingSystem operatingSystem;
	@SuppressWarnings("FieldNotUsedInToString") @Nullable private final String environment;

	public TargetTriple(@NotNull final Architecture architecture, @NotNull final TargetOperatingSystem operatingSystem, @Nullable final String environment)
	{
		if (environment != null)
		{
			if (environment.isEmpty())
			{
				throw new IllegalArgumentException("environment is empty");
			}
			if (environment.contains("-"))
			{
				throw new IllegalArgumentException("environment contains a hyphen");
			}
		}
		if (operatingSystem.doesNotSupport(architecture))
		{
			throw new IllegalArgumentException("operatingSystem does not support architecture");
		}
		this.architecture = architecture;
		this.operatingSystem = operatingSystem;
		this.environment = environment;
	}

	@Override
	public <X extends Exception> void write(@NotNull final ByteWriter<X> byteWriter) throws X
	{
		byteWriter.writeBytes(TargetTripleStart);
		byteWriter.writeUtf8EncodedStringWithCertainty(toTargetTriple());
		byteWriter.writeBytes(End);
	}

	@Override
	@NotNull
	public String toString()
	{
		return toTargetTriple();
	}

	@NonNls
	@NotNull
	private String toTargetTriple()
	{
		return environment == null ? Formatting.format("%1$s-%2$s", architecture, operatingSystem) : Formatting.format("%1$s-%2$s-%3$s", architecture, operatingSystem, environment);
	}

	@SuppressWarnings("RedundantIfStatement")
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

		final TargetTriple that = (TargetTriple) o;

		if (architecture != that.architecture)
		{
			return false;
		}
		if (!operatingSystem.equals(that.operatingSystem))
		{
			return false;
		}
		if (environment != null ? !environment.equals(that.environment) : that.environment != null)
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = architecture.hashCode();
		result = 31 * result + operatingSystem.hashCode();
		result = 31 * result + (environment != null ? environment.hashCode() : 0);
		return result;
	}
}
