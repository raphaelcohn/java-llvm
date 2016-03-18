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

import com.stormmq.llvm.target.writers.TargetTripleWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

public final class TargetTriple
{
	@NotNull private final Architecture architecture;
	@NotNull private final TargetOperatingSystem operatingSystem;
	@Nullable private final String environment;

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

	public <X extends Exception> void write(@NotNull final TargetTripleWriter<X> targetTripleWriter) throws X
	{
		targetTripleWriter.writeTargetTriple(toString());
	}

	@Override
	public String toString()
	{
		if (environment == null)
		{
			return format(ENGLISH, "%1$s-%2$s", architecture, operatingSystem);
		}
		else
		{
			return format(ENGLISH, "%1$s-%2$s-%3$s", architecture, operatingSystem, environment);
		}
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
