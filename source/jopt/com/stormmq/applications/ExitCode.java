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

package com.stormmq.applications;

import org.jetbrains.annotations.NotNull;

import static com.stormmq.applications.ExitCode.newShouldHaveExited;
import static java.lang.Runtime.getRuntime;

public enum ExitCode
{
	// Standard C codes
	Success(0),
	Failure(1),

	// bash and some similar shells
	MisuseOfShellBuiltins(2),

	// For diff-like programs
	DiffMismatch(1),
	DiffInabilityToCompare(2),

	// Based on FreeBSD man sysexits(3). BSD has been defining these since BSD 4.3; they have a very long history
	@SuppressWarnings("SpellCheckingInspection")Usage(64), // The command was used incorrectly, e.g., with the wrong number of arguments, a bad flag, a bad syntax in a parameter, or whatever.
	DataError(65), // The input data was incorrect in some way. This should only be used for user's data and not system files.
	NoInput(66), // An input file (not a system file), did not exist or was not readable. This could also include errors like "No message" to a mailer (if it cared to catch it).
	NoUser(67), // The user specified did not exist. This might be used for mail addresses or remote logins.
	NoHost(68), // The host specified did not exist. This is used in mail addresses or network requests.
	Unavailable(69), // A service is unavailable. This can occur if a support program or file does not exist. This can also be used as a catchall message when something you wanted to do does not work, but you do not know why.
	Software(70), // An internal software error has been detected. This should be limited to non-operating system related errors as possible.
	OSError(71), // An operating system error has been detected. This is intended to be used for such things as "cannot fork", "cannot create pipe", or the like. It includes things like getuid returning a user that does not exist in the passwd file.
	OSFile(72), // Some system file (eg /etc/passwd, /var/run/utx.active, etc), does not exist, cannot be opened, or has some sort of error (eg syntax error).
	CanNotCreate(73), // A (user specified), output file cannot be created.
	IOError(74), // An error occurred while doing I/O on some file.
	TemporaryFailure(75), // Temporary failure, indicating something that is not really an error. In sendmail, this means that a mailer (for example) could not create a connection, and the request should be reattempted later.
	Protocol(76), // The remote system returned something that was "not possible " during a protocol exchange.
	NoPermission(77), // You did not have sufficient permission to perform the operation. This is not intended for file system problems, which should use NoInput or CanNotCreate, but rather for higher level permissions.
	Misconfiguration(78), // Something was found in an unconfigured or misconfigured state.

	// POSIX requires these values for shells
	CommandFoundButNotExecutable(126),
	CommandNotFound(127),

	// GNU glibc https://www.gnu.org/software/libc/manual/html_node/Exit-Status.html
	SubprocessFailed(128),
	ShellBuiltinExitPassedInvalidValue(128), // eg in bash, `exit 3.14`

	ExitCodeOutOfRange(255),
	;

	// ksh93 is different enough to most POSIX shells to make it incompatible for anything other than moderately involved shell scripts...
	private static final int ksh93IncrementToExitCodeWithSignal = 256;
	private static final int posixIncrementToExitCodeWithSignal = 128;

	@NotNull
	public static IllegalStateException newShouldHaveExited(@SuppressWarnings("UnusedParameters") @NotNull final Throwable cause)
	{
		return newShouldHaveExited();
	}

	@NotNull
	public static IllegalStateException newShouldHaveExited()
	{
		return new IllegalStateException("Should have exited");
	}

	private final int value;

	ExitCode(final int value)
	{
		this.value = value;
	}

	public void exit()
	{
		System.exit(value);
		throw newShouldHaveExited();
	}

	public void halt()
	{
		getRuntime().halt(value);
		throw newShouldHaveExited();
	}

	// Does not work for Windows
	public static int extractExitCodeIfSignalIncluded(final int exitCode)
	{
		// Compatibility with Single Unix Specification (eg of wait() and waitpid())
		final int lowerEightBits = exitCode & 0xFF;

		if (lowerEightBits > ksh93IncrementToExitCodeWithSignal)
		{
			return lowerEightBits - ksh93IncrementToExitCodeWithSignal;
		}

		if (lowerEightBits > posixIncrementToExitCodeWithSignal)
		{
			return lowerEightBits - posixIncrementToExitCodeWithSignal;
		}

		return lowerEightBits;
	}

	// Values above 128 / 256 can be interpreted as the signal value
	public static boolean wasShellCommandTerminatedBecauseOfSignal(final int exitCode)
	{
		// Compatibility with Single Unix Specification (eg of wait() and waitpid())
		final int lowerEightBits = exitCode & 0xFF;
		return lowerEightBits > ksh93IncrementToExitCodeWithSignal || lowerEightBits > posixIncrementToExitCodeWithSignal;
	}
}
