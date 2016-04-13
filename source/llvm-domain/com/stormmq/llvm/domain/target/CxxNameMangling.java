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

import com.stormmq.string.InvalidUtf16StringException;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.stormmq.string.StringUtilities.enumSerializationIsNotSupportedForConstantsInASecureContext;
import static com.stormmq.string.Utf8ByteUser.utf8Length;

// See also http://demangler.com/
public enum CxxNameMangling
{
	// Intel C++ (originally IA-64), GCC3+, Clang, HP IA-64
	Intel
	{
		private void writeObject(@NotNull final ObjectOutputStream out) throws IOException
		{
			throw enumSerializationIsNotSupportedForConstantsInASecureContext();
		}

		private void readObject(@NotNull final ObjectInputStream in) throws IOException
		{
			throw enumSerializationIsNotSupportedForConstantsInASecureContext();
		}

		@NotNull
		private String mangleIdentifier(@NotNull final String identifier)
		{
			// This ABI does not yet specify a mangling for identifiers containing characters outside of _A-Za-z0-9 ...
			final int length;
			try
			{
				length = utf8Length(identifier);
			}
			catch (final InvalidUtf16StringException e)
			{
				throw new IllegalArgumentException(e);
			}
			return Integer.toString(length) + identifier;
		}

		@NotNull
		private <N> String mangleInternal(@NotNull final BiConsumer<N, Consumer<String>> namespaceSplitter, @NonNls @NotNull final String beforeNamespaceStart, @NonNls @NotNull final N namespace, @NonNls @NotNull final String... subNamespaceIdentifiers)
		{
			@NonNls final StringBuilder result = new StringBuilder(4096);
			final Consumer<String> mangler = identifier -> result.append(mangleIdentifier(identifier));

			result.append("_Z");
			result.append(beforeNamespaceStart);
			result.append('N');
			namespaceSplitter.accept(namespace, mangler);
			for (final String subNamespaceIdentifier : subNamespaceIdentifiers)
			{
				mangler.accept(subNamespaceIdentifier);
			}
			result.append('E');
			return result.toString();
		}

		@NotNull
		@Override
		public <N> String mangle(@NotNull final BiConsumer<N, Consumer<String>> namespaceSplitter, @NonNls @NotNull final String beforeNamespaceStart, @NonNls @NotNull final N namespace, @NonNls @NotNull final String simpleTypeName)
		{
			return mangleInternal(namespaceSplitter, beforeNamespaceStart, namespace, simpleTypeName);
		}

		@NotNull
		@Override
		public <N> String mangleIdentifier(@NotNull final BiConsumer<N, Consumer<String>> namespaceSplitter, @NonNls @NotNull final N namespace, @NonNls @NotNull final String simpleTypeName)
		{
			return mangle(namespaceSplitter, "TS", namespace, simpleTypeName);
		}

		@SuppressWarnings("OverloadedVarargsMethod")
		@NotNull
		@Override
		public <N> String mangle(@NotNull final BiConsumer<N, Consumer<String>> namespaceSplitter, @NonNls @NotNull final String beforeNamespaceStart, @NonNls @NotNull final N namespace, @NonNls @NotNull final String simpleTypeName, @NonNls @NotNull final String functionName, @NotNull final Object... formalParameterTypes)
		{
			final String withoutArguments = mangleInternal(namespaceSplitter, beforeNamespaceStart, namespace, simpleTypeName);
			final int length = formalParameterTypes.length;
			if (length == 0)
			{
				return withoutArguments + 'v';
			}

			@NonNls final StringBuilder result = new StringBuilder(withoutArguments.length() + (length << 2));

			/*
			5.1.4.3 Constructors and Destructors
			Constructors and destructors are simply special cases of <unqualified-name>, where the final <unqualified-name> of a nested name is replaced by one of the following:
			<ctor-dtor-name> ::= C1	# complete object constructor
			::= C2	# base object constructor
			::= C3	# complete object allocating constructor
			::= D0	# deleting destructor
			::= D1	# complete object destructor
			::= D2	# base object destructor
			*/
			// See also  https://mentorembedded.github.io/cxx-abi/abi.html#mangling-builtin

			// prefix with 'P' if the value is pointed to
			// prefix with 'PP' if the value is double-pointed to (**) - and so on

			// pointer to 'Depositor' was  PS2_   seems to be P for pointer, then S2_  refers to current namespace?
			// from a function in no namespace, pointer to 'Depositor' was P for pointer then N3com7stormmq11inheritance9DepositorE
			// from a function in the 'com' namespace, pointer to 'Depositor' was P for pointer then NS_7stormmq11inheritance9DepositorE

			// https://mentorembedded.github.io/cxx-abi/abi.html#mangle.seq-id
			// S2_  => seq-id of 4 ie take 4 elements from namespace of function, but then we get the weirdness of the third argument of hold() - no use of P, S3_ which is too long...
			// S_
			// These are substitutions, with a number (eg 2 above) in base 36. Numbers start at '-1' ie unspecified (thus there is S0_); 38 is S10_

			throw new UnsupportedOperationException("Finish me");
		}
	},

	// Windows: Visual C++ and Digital Mars

	// There are other approaches (IBM XL C++, SunPro now Oracle Solaris Studio) but they are not really in the mainstream
	;

	CxxNameMangling()
	{
	}

	@NotNull
	public abstract <N> String mangle(@NotNull final BiConsumer<N, Consumer<String>> namespaceSplitter, @NonNls @NotNull final String beforeNamespaceStart, @NonNls @NotNull final N namespace, @NonNls @NotNull final String simpleTypeName);

	@NotNull
	public abstract <N> String mangleIdentifier(@NotNull final BiConsumer<N, Consumer<String>> namespaceSplitter, @NonNls @NotNull final N namespace, @NonNls @NotNull final String simpleTypeName);

	@SuppressWarnings("OverloadedVarargsMethod")
	@NotNull
	public abstract <N> String mangle(@NotNull final BiConsumer<N, Consumer<String>> namespaceSplitter, @NonNls @NotNull final String beforeNamespaceStart, @NonNls @NotNull final N namespace, @NonNls @NotNull final String simpleTypeName, @NonNls @NotNull final String functionName, @NotNull final Object... formalParameterTypes);
}
