package com.stormmq.java.parsing.adaptors.javac;

import com.sun.tools.javac.main.JavaCompiler;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

import javax.tools.JavaFileObject;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class NaiveParser
{
	public static void main(String[] args)
	{
		final JavaCompiler javaCompiler = new JavaCompiler(new Context());

		final Path sourceFilePath = Paths.get("hello.java");
		final JavaFileObject javaFileObject = new PathSourceJavaFileObject(sourceFilePath);
		final JCTree.JCCompilationUnit parse = javaCompiler.parse(javaFileObject);

		final List<JCTree.JCImport> imports = parse.getImports();
		for (JCTree.JCImport anImport : imports)
		{
			anImport.getQualifiedIdentifier();
		}

		final JCTree.JCExpression packageName = parse.getPackageName();
		final List<JCTree> typeDecls = parse.getTypeDecls();
		for (JCTree typeDecl : typeDecls)
		{
			final JCTree.JCClassDecl typeDecl1 = (JCTree.JCClassDecl) typeDecl;
			final List<JCTree> members = typeDecl1.getMembers();
			for (JCTree member : members)
			{
				final JCTree.JCMethodDecl member1 = (JCTree.JCMethodDecl) member;
				final JCTree returnType = member1.getReturnType();

				final JCTree.JCBlock body = member1.getBody();
				final List<JCTree.JCStatement> statements = body.getStatements();
			}
		}
	}
}
