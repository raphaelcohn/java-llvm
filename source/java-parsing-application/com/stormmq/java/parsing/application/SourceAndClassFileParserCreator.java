package com.stormmq.java.parsing.application;

import com.stormmq.java.parsing.adaptors.asm.ast.AsmClassFileParser;
import com.stormmq.java.parsing.adaptors.asm.classReaderUsers.ClassReaderUser;
import com.stormmq.java.parsing.adaptors.javaparser.ast.JavaparserSourceFileParser;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.compilationUnitWrapperUsers.CompilationUnitWrapperUser;
import com.stormmq.java.parsing.adaptors.javaparser.dead.wrappers.CompilationUnitWrapper;
import com.stormmq.java.parsing.fileParsers.FileParser;
import com.stormmq.java.parsing.fileParsers.MultiplePathsParser;
import com.stormmq.java.parsing.fileParsers.caches.Cache;
import com.stormmq.java.parsing.fileParsers.caches.CacheMaker;
import org.jetbrains.annotations.NotNull;

public final class SourceAndClassFileParserCreator
{
	@NotNull private final Cache<CompilationUnitWrapper> compilationUnitHelperCache;
	@NotNull private final Cache<byte[]> codeCache;

	public SourceAndClassFileParserCreator(@NotNull final CacheMaker cacheMaker)
	{
		compilationUnitHelperCache = cacheMaker.makeCache();
		codeCache = cacheMaker.makeCache();
	}

	@NotNull
	public MultiplePathsParser createMultiplePathsParser(@NotNull final CompilationUnitWrapperUser compilationUnitWrapperUser, @NotNull final ClassReaderUser classReaderUser)
	{
		final FileParser javaSourceFileParser = new JavaparserSourceFileParser(compilationUnitHelperCache, compilationUnitWrapperUser);
		final FileParser javaClassFileParser = new AsmClassFileParser(codeCache, classReaderUser);
		return new MultiplePathsParser(javaSourceFileParser, javaClassFileParser);
	}
}
