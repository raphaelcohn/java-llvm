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

package com.stormmq.llvm.examples.parsing;

import com.stormmq.java.parsing.fileParsers.FileParser;
import com.stormmq.llvm.examples.parsing.files.ParsableFile;
import com.stormmq.llvm.examples.parsing.parseFailueLogs.ParseFailureLog;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public final class QueueProcessor implements Runnable
{
	@NotNull private final FileParser javaClassFileParser;
	@NotNull private final ConcurrentLinkedQueue<ParsableFile> queue;
	@NotNull private final ParseFailureLog parseFailureLog;
	@NotNull private final Coordination coordination;

	public QueueProcessor(@NotNull final ConcurrentLinkedQueue<ParsableFile> queue, @NotNull final FileParser javaClassFileParser, @NotNull final ParseFailureLog parseFailureLog, @NotNull final Coordination coordination)
	{
		this.javaClassFileParser = javaClassFileParser;
		this.queue = queue;
		this.parseFailureLog = parseFailureLog;
		this.coordination = coordination;
	}

	@Override
	public void run()
	{
		while (coordination.shouldContinue())
		{
			@Nullable final ParsableFile poll = queue.poll();

			if (poll == null)
			{
				try
				{
					sleep(1);
				}
				catch (final InterruptedException ignored)
				{
					Thread.currentThread().interrupt();
				}
				continue;
			}

			poll.process(javaClassFileParser, parseFailureLog);
		}

		do
		{
			@Nullable final ParsableFile poll = queue.poll();

			if (poll == null)
			{
				break;
			}

			poll.process(javaClassFileParser, parseFailureLog);

		} while(true);

		coordination.countDown();
	}
}
