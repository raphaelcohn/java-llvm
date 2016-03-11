package com.github.javaparser;

import com.github.javaparser.ast.CompilationUnit;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.nio.charset.Charset;

public final class AstParserAdaptor
{
	private AstParserAdaptor()
	{
	}

	@NotNull
	public static CompilationUnit parse(@NotNull final InputStream inputStream, @NotNull final Charset encoding) throws ParseException
	{
		final ASTParser astParser = new ASTParser(inputStream, encoding.name());
		return astParser.CompilationUnit();
	}
}
