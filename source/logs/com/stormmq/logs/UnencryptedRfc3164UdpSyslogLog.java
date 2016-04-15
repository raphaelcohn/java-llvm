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

package com.stormmq.logs;

import com.stormmq.string.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.net.*;
import java.time.*;

import static com.stormmq.logs.LogLevel.Error;
import static com.stormmq.string.Formatting.format;
import static com.stormmq.string.Padding.padAsDecimal;
import static java.lang.System.arraycopy;
import static java.net.AnyLocalAddressHelper.AnyLocalAddress;
import static java.net.InetAddress.getLoopbackAddress;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Clock.systemUTC;

public final class UnencryptedRfc3164UdpSyslogLog implements Log
{
	private static final int SyslogCommonPortNumber = 514;
	private static final int AnyOutboundPort = 0;
	@NotNull private static final InetSocketAddress AnyBindAddress = new InetSocketAddress(AnyLocalAddress, AnyOutboundPort);
	@NotNull private static final InetAddress LocalHost = getLoopbackAddress();


	private static final int TimestampLength = 16;
	private static final int MaximumMessageSize = 1024;
	private static final int MaximumTagLengthInBytes = 32;
	@NotNull private static final byte[] localhost_ = "localhost ".getBytes(US_ASCII);
	@NotNull private static final byte[][] PRIs = calculatePris();
	@NotNull private static final byte[][] Months = months();
	@NotNull private static final byte[][] Days = days();
	@NotNull private static final byte[][] Hours = hours();

	@NotNull
	private static byte[][] calculatePris()
	{
		final int length = 255;
		final byte[][] pris = new byte[length][];
		for (final Rfc3164Facility value : Rfc3164Facility.values())
		{
			final int i = value.rfc3164FacilityCode << 3;

			for (final LogLevel logLevel : LogLevel.values())
			{
				final int priority = i + logLevel.rfc3164Code;
				pris[priority] = format("<%1$s>", priority).getBytes(US_ASCII);
			}
		}
		return pris;
	}

	@SuppressWarnings("MagicNumber")
	@NotNull
	private static byte[][] months()
	{
		@NonNls final byte[][] months = new byte[12][];
		months[0] = "Jan".getBytes(US_ASCII);
		months[1] = "Feb".getBytes(US_ASCII);
		months[2] = "Mar".getBytes(US_ASCII);
		months[3] = "Apr".getBytes(US_ASCII);
		months[4] = "May".getBytes(US_ASCII);
		months[5] = "Jun".getBytes(US_ASCII);
		months[6] = "Jul".getBytes(US_ASCII);
		months[7] = "Aug".getBytes(US_ASCII);
		months[8] = "Sep".getBytes(US_ASCII);
		months[9] = "Oct".getBytes(US_ASCII);
		months[10] = "Nov".getBytes(US_ASCII);
		months[11] = "Dec".getBytes(US_ASCII);
		return months;
	}

	@SuppressWarnings("MagicNumber")
	@NotNull
	private static byte[][] days()
	{
		final byte[][] days = new byte[30][];
		for(int index = 1; index <= 31; index++)
		{
			final byte[] bytes = new byte[3];
			bytes[0] = ' ';
			final byte[] converted = Integer.toString(index).getBytes(US_ASCII);
			if (index < 10)
			{
				bytes[1] = ' ';
				bytes[2] = converted[0];
			}
			else
			{
				bytes[1] = converted[0];
				bytes[2] = converted[1];
			}
			days[index - 1] = bytes;
		}
		return days;
	}

	@SuppressWarnings("MagicNumber")
	@NotNull
	private static byte[][] hours()
	{
		final int length = 24;
		@NonNls final byte[][] hours = new byte[length][];
		for(int index = 0; index < length; index++)
		{
			hours[index] = (' ' + padAsDecimal(index, 2)).getBytes(US_ASCII);
		}
		return hours;
	}

	@NotNull private static final byte[][] MinutesOrSeconds = minutesOrSeconds();

	@SuppressWarnings("MagicNumber")
	@NotNull
	private static byte[][] minutesOrSeconds()
	{
		final int length = 60;
		@NonNls final byte[][] hours = new byte[length][];
		for(int index = 0; index < length; index++)
		{
			hours[index] = (':' + padAsDecimal(index, 2)).getBytes(US_ASCII);
		}
		return hours;
	}

	@NotNull private final DatagramSocket datagramSocket;
	@NotNull private final InetAddress destinationAddress;
	private final int destinationPort;
	@NotNull private final Log failureLog;
	private final int rfc3164FacilityCodeTimesEight;
	@NotNull private final Clock clock = systemUTC();
	@NotNull private final byte[] hostname;
	private final int hostNameLength;
	private final byte[] applicationName;
	private final int applicationNameLength;

	// Localhost logging
	public UnencryptedRfc3164UdpSyslogLog(@NotNull final String applicationName, @NotNull final Rfc3164Facility rfc3164Facility, @NotNull final Log failureLog)
	{
		this(applicationName, rfc3164Facility, new InetSocketAddress(LocalHost, SyslogCommonPortNumber), failureLog, AnyBindAddress);
	}

	public UnencryptedRfc3164UdpSyslogLog(@NotNull final String applicationName, @NotNull final Rfc3164Facility rfc3164Facility, @NotNull final InetAddress destinationAddress, @NotNull final Log failureLog)
	{
		this(applicationName, rfc3164Facility, new InetSocketAddress(destinationAddress, SyslogCommonPortNumber), failureLog, AnyBindAddress);
	}

	public UnencryptedRfc3164UdpSyslogLog(@NotNull final String applicationName, @NotNull final Rfc3164Facility rfc3164Facility, @NotNull final InetSocketAddress destinationAddress, @NotNull final Log failureLog)
	{
		this(applicationName, rfc3164Facility, destinationAddress, failureLog, AnyBindAddress);
	}

	@SuppressWarnings("WeakerAccess")
	public UnencryptedRfc3164UdpSyslogLog(@NotNull final String applicationName, @NotNull final Rfc3164Facility rfc3164Facility, @NotNull final InetSocketAddress destinationAddress, @NotNull final Log failureLog, @NotNull final InetSocketAddress bindAddress)
	{
		this.destinationAddress = destinationAddress.getAddress();
		destinationPort = destinationAddress.getPort();
		this.failureLog = failureLog;
		rfc3164FacilityCodeTimesEight = rfc3164Facility.rfc3164FacilityCode << 3;

		this.applicationName = convertApplicationName(applicationName);
		applicationNameLength = this.applicationName.length;
		hostname = convertHostName(bindAddress);
		hostNameLength = hostname.length;

		try
		{
			datagramSocket = new DatagramSocket(bindAddress);
		}
		catch (final SocketException e)
		{
			throw new IllegalArgumentException(format("Could not bind to UDP socket because '%1$s'", e.getMessage()), e);
		}
		// Slight performance gain as security checks less invasive on each send()
		datagramSocket.connect(this.destinationAddress, destinationPort);
	}

	@NotNull
	private static byte[] convertApplicationName(@NotNull final String applicationName)
	{
		if (applicationName.isEmpty())
		{
			throw new IllegalArgumentException("applicationName should not be empty");
		}

		final int[] lastIndex = new int[1];
		final byte[] codePoints = new byte[MaximumTagLengthInBytes];
		try
		{
			((CodePointUser<IllegalArgumentException>) (index, codePoint) ->
			{
				if (index >= MaximumTagLengthInBytes)
				{
					return;
				}
				if (codePoint >= '0' && codePoint <= '9')
				{
					record(lastIndex, codePoints, index, codePoint);
					return;
				}
				if (codePoint >= 'A' && codePoint <= 'Z')
				{
					record(lastIndex, codePoints, index, codePoint);
					return;
				}
				if (codePoint >= 'a' && codePoint <= 'z')
				{
					record(lastIndex, codePoints, index, codePoint);
				}
			}).iterateOverStringCodePoints(applicationName);
		}
		catch (final InvalidUtf16StringException e)
		{
			throw new IllegalArgumentException("applicationName must be a valid UTF-16 string", e);
		}

		final int length = lastIndex[0] + 1;
		final byte[] applicationNameBytes = new byte[length + 2];
		arraycopy(codePoints, 0, applicationNameBytes, 0, length);
		applicationNameBytes[length] = ':';
		applicationNameBytes[length + 1] = ' ';
		return applicationNameBytes;
	}

	@NotNull
	private static byte[] convertHostName(@NotNull final InetSocketAddress bindAddress)
	{
		@Nullable final String hostString = bindAddress.getHostString();
		return hostString == null ? localhost_ : hostString.replace(' ', '-').getBytes(UTF_8);
	}

	private static void record(final int[] lastIndex, final byte[] codePoints, final int index, final int codePoint)
	{
		//noinspection NumericCastThatLosesPrecision
		codePoints[index] = (byte) codePoint;
		lastIndex[0] = index;
	}

	@Override
	public void log(@NotNull final LogLevel logLevel, @NotNull @NonNls final String utf8SafeMessageWithoutTrailingNewLine)
	{
		// <34>Oct 11 22:14:15 mymachine su: 'su root' failed for lonvick on /dev/pts/8
		final byte[] message = new byte[MaximumMessageSize];
		final int headerAndTagLength = headerAndTag(logLevel, message);
		final TruncatingUtf8ByteUser utf8ByteUser = new TruncatingUtf8ByteUser(headerAndTagLength, message);
		try
		{
			utf8ByteUser.encodeUtf8Bytes(utf8SafeMessageWithoutTrailingNewLine);
		}
		catch (final InvalidUtf16StringException ignored)
		{
			failureLog.log(Error, "Could not safely encode a message as message wasn't valid UTF-16");
		}

		final DatagramPacket sendPacket = new DatagramPacket(message, 0, utf8ByteUser.offset, destinationAddress, destinationPort);
		try
		{
			datagramSocket.send(sendPacket);
		}
		catch (final PortUnreachableException e)
		{
			// TODO: Back off and try again in a while...
			failureLog.log(Error, format("UDP data packet for syslog could not be sent as destination port was unreachable '%1$s'; message was '%2$s':'%3$s'", e.getMessage(), logLevel.name(), utf8SafeMessageWithoutTrailingNewLine));
		}
		catch (final IOException e)
		{
			// something inside the native send() code went wrong. ?recreate the socket?
			failureLog.log(Error, format("UDP data packet for syslog could not be sent due to IOException '%1$s'; message was '%2$s':'%3$s'", e.getMessage(), logLevel.name(), utf8SafeMessageWithoutTrailingNewLine));
		}
	}

	private int headerAndTag(@NotNull final LogLevel logLevel, @NotNull final byte[] message)
	{
		final byte[] PRI = PRIs[rfc3164FacilityCodeTimesEight + logLevel.rfc3164Code];
		final int priLength = PRI.length;
		final int headerAndTagLength = priLength + TimestampLength + hostNameLength + applicationNameLength;
		arraycopy(PRI, 0, message, 0, priLength);
		timestamp(priLength, message);
		arraycopy(hostname, 0, message, priLength + TimestampLength, hostNameLength);
		arraycopy(applicationName, 0, message, priLength + TimestampLength + hostNameLength, applicationNameLength);
		return headerAndTagLength;
	}

	@SuppressWarnings("MagicNumber")
	private void timestamp(final int priLength, @NotNull final byte[] header)
	{
		final ZonedDateTime now = ZonedDateTime.now(clock);
		final byte[] month = Months[now.getMonthValue() - 1];
		final byte[] day = Days[now.getDayOfMonth() - 1];
		final byte[] hour = Hours[now.getHour()];
		final byte[] minute = MinutesOrSeconds[now.getMinute()];
		final byte[] second = MinutesOrSeconds[now.getSecond()];

		arraycopy(month, 0, header, priLength, 3);
		arraycopy(day, 0, header, priLength + 3, 3);
		arraycopy(hour, 0, header, priLength + 6, 3);
		arraycopy(minute, 0, header, priLength + 9, 3);
		arraycopy(second, 0, header, priLength + 12, 3);
		header[priLength + 15] = ' ';
	}

	@Override
	public void close()
	{
		datagramSocket.close();
	}

	private static final class TruncatingUtf8ByteUser implements Utf8ByteUser<RuntimeException>
	{
		@NotNull private final byte[] message;
		private int offset;
		private boolean ignore;

		private TruncatingUtf8ByteUser(final int headerAndTagLength, @NotNull final byte[] message)
		{
			this.message = message;
			offset = headerAndTagLength;
			ignore = false;
		}

		@SuppressWarnings("NumericCastThatLosesPrecision")
		@Override
		public void useUnsignedByte(final int byteIndex, final int sequenceLength, final int utf8Byte)
		{
			if (ignore)
			{
				return;
			}
			if (byteIndex == 0)
			{
				if (offset + sequenceLength >= MaximumMessageSize)
				{
					ignore = true;
					return;
				}
			}
			message[offset] = (byte) utf8Byte;
			offset++;
		}
	}
}
