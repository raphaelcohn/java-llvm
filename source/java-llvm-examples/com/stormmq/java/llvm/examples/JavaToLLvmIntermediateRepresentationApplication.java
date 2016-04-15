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

package com.stormmq.java.llvm.examples;

import com.stormmq.applications.*;
import com.stormmq.java.classfile.processing.*;
import com.stormmq.java.classfile.processing.processLogs.StandardProcessLog;
import com.stormmq.java.llvm.xxx.happy.*;
import com.stormmq.java.parsing.utilities.names.PackageName;
import com.stormmq.applications.uncaughtExceptionHandlers.MustExitBecauseOfFailureException;
import com.stormmq.llvm.domain.metadata.creation.CTypeMappings;
import com.stormmq.llvm.domain.metadata.creation.NamespaceSplitter;
import com.stormmq.llvm.domain.module.ModuleWriter;
import com.stormmq.llvm.domain.module.TargetModuleCreator;
import com.stormmq.llvm.domain.target.DataLayoutSpecification;
import com.stormmq.logs.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.file.Path;

import static com.stormmq.applications.ExitCode.CanNotCreate;
import static com.stormmq.java.classfile.processing.TypeInformationTripletUser.loggingTypeInformationTripletUser;
import static com.stormmq.java.parsing.utilities.names.PackageName.NamespaceSplitter;
import static com.stormmq.string.Formatting.format;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.deleteIfExists;

public final class JavaToLLvmIntermediateRepresentationApplication extends AbstractApplication
{
	@NotNull private final Iterable<Path> sourcePaths;
	@NotNull private final Path outputPath;
	@NotNull private final Processor processor;
	@NotNull private final TypeInformationTripletUser<ClassToStructureMap> typeInformationTripletUser;

	public JavaToLLvmIntermediateRepresentationApplication(@NotNull final Log log, @NotNull final Verbosity verbosity, @NotNull final Iterable<Path> sourcePaths, @NotNull final Path outputPath, final boolean permitConstantsInInstanceFields, final TargetModuleCreator targetModuleCreator)
	{
		super(log);

		this.sourcePaths = sourcePaths;
		this.outputPath = outputPath;

		processor = new Processor(permitConstantsInInstanceFields, new StandardProcessLog(log), exitCodeSettingUncaughtExceptionHandler);
		final ModuleCreatorAndWriter moduleCreatorAndWriter = new ModuleCreatorAndWriter(targetModuleCreator, new ModuleWriter(outputPath));
		final NamespaceSplitter<PackageName> namespaceSplitter = new NamespaceSplitter<>(NamespaceSplitter, targetModuleCreator.cxxNameMangling());
		final DataLayoutSpecification dataLayoutSpecification = targetModuleCreator.dataLayoutSpecification();
		final CTypeMappings cTypeMappings = new CTypeMappings(dataLayoutSpecification);
		final JavaConvertingTypeInformationTripletUser javaConvertingTypeInformationTripletUser = new JavaConvertingTypeInformationTripletUser(moduleCreatorAndWriter, namespaceSplitter, dataLayoutSpecification, cTypeMappings);
		typeInformationTripletUser = verbosity.isAtLeastVerbose() ? loggingTypeInformationTripletUser(javaConvertingTypeInformationTripletUser, log) : javaConvertingTypeInformationTripletUser;
	}

	private void recreateOutputPath(@NotNull final Path outputPath) throws MustExitBecauseOfFailureException
	{
		try
		{
			deleteIfExists(outputPath);
		}
		catch (final IOException ignored)
		{
			// Lots of legitimate reasons; may be like $HOME for instance (can't delete)
		}

		try
		{
			createDirectories(outputPath);
		}
		catch (final IOException e)
		{
			exitCode.set(CanNotCreate);
			throw new MustExitBecauseOfFailureException(format("Could not create output path '%1$s'", outputPath), e);
		}
	}

	@Override
	protected void executeInternally() throws MustExitBecauseOfFailureException
	{
		recreateOutputPath(outputPath);

		final Records records = processor.process(sourcePaths);

		if (mustExitBecauseOfFailure())
		{
			return;
		}

		// TODO: Final class and method analysis

		work(records);
	}

	private void work(final Records records)
	{
		records.iterate(typeInformationTripletUser, records1 -> new ClassToStructureMap(new UsefulRecords(records1)));
	}
}
