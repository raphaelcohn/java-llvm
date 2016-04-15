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

import org.jetbrains.annotations.NotNull;

public enum Rfc3164Facility
{
	// These are RFC 3164 definitions
	kern(0),
	user(1),
	mail(2),
	daemon(3),
	@SuppressWarnings("SpellCheckingInspection")authpriv(10),
	auth(4, authpriv),
	syslog(5),
	lpr(6),
	news(7),
	@SuppressWarnings("SpellCheckingInspection")uucp(8),
	@SuppressWarnings("SpellCheckingInspection")clockdaemon(9, daemon),
	ftp(11),
	ntp(12, daemon),
	logAudit(13, daemon),
	logAlert(14, daemon),
	cron(15),
	local0(16),
	local1(17),
	local2(18),
	local3(19),
	local4(20),
	local5(21),
	local6(22),
	local7(23),
	;

	public final int rfc3164FacilityCode;
	@NotNull public final Rfc3164Facility linuxPreferred;

	Rfc3164Facility(final int rfc3164FacilityCode)
	{
		this.rfc3164FacilityCode = rfc3164FacilityCode;
		linuxPreferred = this;
	}

	Rfc3164Facility(final int rfc3164FacilityCode, @NotNull final Rfc3164Facility linuxPreferred)
	{
		this.rfc3164FacilityCode = rfc3164FacilityCode;
		this.linuxPreferred = linuxPreferred;
	}
}
