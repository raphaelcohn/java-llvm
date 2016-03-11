package com.stormmq.java.parsing.application;

import com.stormmq.java.parsing.adaptors.asm.asmTypeItemUsers.RecordingAsmTypeItemUser;
import com.stormmq.java.parsing.adaptors.asm.classReaderUsers.ClassReaderUser;
import com.stormmq.java.parsing.adaptors.asm.classReaderUsers.GatheringClassReaderUser;
import com.stormmq.java.parsing.adaptors.javaparser.namedTypeDeclarationUsers.AnnotationNamedTypeDeclarationUser;
import com.stormmq.java.parsing.adaptors.javaparser.namedTypeDeclarationUsers.ClassOrInterfaceNamedTypeDeclarationUser;
import com.stormmq.java.parsing.adaptors.javaparser.namedTypeDeclarationUsers.EnumNamedTypeDeclarationUser;
import com.stormmq.java.parsing.adaptors.javaparser.packageDeclarationUsers.RecordingPackageDeclarationUser;
import com.stormmq.java.parsing.adaptors.javaparser.typeDeclarationWrapperUsers.RecursiveTypeDeclarationWrapperUser;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.compilationUnitWrapperUsers.CompilationUnitWrapperUser;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.compilationUnitWrapperUsers.RecursiveCompilationUnitWrapperUser;
import com.stormmq.java.parsing.ast.detailMaps.PackageDetailMap;
import com.stormmq.java.parsing.ast.detailMaps.TypeDetailMap;
import com.stormmq.java.parsing.ast.details.methodDetails.MethodDetail;
import com.stormmq.java.parsing.ast.intermediateRecorders.*;
import com.stormmq.java.parsing.ast.intermediates.MethodIntermediate;
import com.stormmq.java.parsing.ast.typeResolution.SimpleTypeResolver;
import com.stormmq.java.parsing.fileParsers.MultiplePathsParser;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static com.stormmq.java.parsing.fileParsers.caches.FakeCache.FakeCacheMaker;
import static com.stormmq.nio.FileAndFolderHelper.removeAllFoldersAndFilesBelowPath;

public final class Application
{
	@NotNull private final Path sourceRootPath;
	@NotNull private final Path destinationRootPath;
	@NotNull private final LinkedHashSet<Path> importPaths;
	@NotNull private final SourceAndClassFileParserCreator sourceAndClassFileParserCreator;

	public Application(@NotNull final Path sourceRootPath, @NotNull Path destinationRootPath, @NotNull LinkedHashSet<Path> importPaths)
	{
		this.sourceRootPath = sourceRootPath;
		this.destinationRootPath = destinationRootPath;
		this.importPaths = importPaths;
		sourceAndClassFileParserCreator = new SourceAndClassFileParserCreator(FakeCacheMaker);
	}

	public void run()
	{
		step_1_cleanDestinationRootPathOfAllSubFoldersAndFiles();


		final ArrayList<String> warnings = new ArrayList<>(256);
		final SimpleTypeResolver simpleTypeResolver = new SimpleTypeResolver();
		final PackageIntermediateRecorder packageIntermediateRecorder = new PackageIntermediateRecorder();
		final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder = new SimpleTypeIntermediateRecorder(warnings);
		final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> methodIntermediateRecorder = new MemberIntermediateRecorder<>();
		final SimpleIntermediateRecorder simpleIntermediateRecorder = new SimpleIntermediateRecorder(simpleTypeIntermediateRecorder, simpleTypeResolver, methodIntermediateRecorder);

		step_2_parse(packageIntermediateRecorder, simpleIntermediateRecorder, warnings, simpleTypeResolver);

		step_4_resolveAllImports(packageIntermediateRecorder, simpleIntermediateRecorder, methodIntermediateRecorder, simpleTypeIntermediateRecorder);
	}

	private void step_1_cleanDestinationRootPathOfAllSubFoldersAndFiles()
	{
		removeAllFoldersAndFilesBelowPath(destinationRootPath);
	}

	private void step_2_parse(@NotNull final PackageDetailRecorder packageDetailRecorder, @NotNull final SimpleIntermediateRecorder simpleIntermediateRecorder, @NotNull final List<String> warnings, @NotNull final SimpleTypeResolver simpleTypeResolver)
	{
		final CompilationUnitWrapperUser compilationUnitWrapperUser = new RecursiveCompilationUnitWrapperUser
		(
			new RecordingPackageDeclarationUser(packageDetailRecorder),
			new RecursiveTypeDeclarationWrapperUser
			(
				new AnnotationNamedTypeDeclarationUser(simpleIntermediateRecorder, bodyDeclarationsCreator),
				new ClassOrInterfaceNamedTypeDeclarationUser(simpleIntermediateRecorder, bodyDeclarationsCreator),
				new EnumNamedTypeDeclarationUser(simpleIntermediateRecorder, bodyDeclarationsCreator)
			),
			importsResolver
		);

		final ClassReaderUser classReaderUser = new GatheringClassReaderUser
		(
			new RecordingAsmTypeItemUser(simpleIntermediateRecorder)
		);

		final MultiplePathsParser multiplePathsParser = sourceAndClassFileParserCreator.createMultiplePathsParser(compilationUnitWrapperUser, classReaderUser);
		multiplePathsParser.parse(importPaths);
		multiplePathsParser.parse(sourceRootPath);
		for (String warning : warnings)
		{
			System.err.println(warning);
		}
	}

	private void step_4_resolveAllImports(@NotNull final PackageIntermediateRecorder packageIntermediateRecorder, @NotNull final SimpleIntermediateRecorder simpleIntermediateRecorder, @NotNull final MemberIntermediateRecorder<MethodIntermediate, MethodDetail> memberIntermediateRecorder, @NotNull final SimpleTypeIntermediateRecorder simpleTypeIntermediateRecorder)
	{
		final PackageDetailMap packageDetailMap = packageIntermediateRecorder.resolve(memberIntermediateRecorder, simpleTypeIntermediateRecorder);
		final TypeDetailMap typeDetailMap = simpleIntermediateRecorder.resolve();
	}
}
