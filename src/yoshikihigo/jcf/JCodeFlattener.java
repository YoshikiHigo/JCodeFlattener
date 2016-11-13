package yoshikihigo.jcf;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.SortedSet;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

public class JCodeFlattener {

	public static void main(final String[] args) {

		final JCFConfig config = JCFConfig.initialize(args);

		if (!config.hasINPUT()) {
			System.err
					.println("option \"-input\" is require to specify your target source files.");
			System.exit(0);
		}
		final String input = config.getINPUT();

		if (!config.hasOUTPUT()) {
			System.err
					.println("option \"-output\" is require to specify this tool's output directory.");
			System.exit(0);
		}
		final String output = config.getOUTPUT();

		final File inputFile = new File(input);
		final File outputFile = new File(output);
		if (inputFile.isFile()) {

			try {
				final String text = FileUtils.readFileToString(inputFile,
						Charset.defaultCharset());

				if (input.endsWith(".java")) {
					final Document document = new Document(text);

					final ASTParser parser = ASTParser.newParser(AST.JLS8);
					parser.setSource(document.get().toCharArray());
					parser.setKind(ASTParser.K_COMPILATION_UNIT);

					final CompilationUnit unit = (CompilationUnit) parser
							.createAST(null);
					final AST ast = unit.getAST();
					final ASTRewrite rewriter = ASTRewrite.create(ast);
					unit.recordModifications();

					final JCFASTVisitor visitor = new JCFASTVisitor(ast,
							rewriter);
					unit.accept(visitor);

					final TextEdit edit = rewriter.rewriteAST(document, null);
					edit.apply(document);
					FileUtils.write(outputFile, document.get(),
							Charset.defaultCharset());
				} else {
					FileUtils.write(outputFile, text, Charset.defaultCharset());
				}

			} catch (final IOException | BadLocationException e) {
				e.printStackTrace();
			}
		}

		else if (inputFile.isDirectory()) {

			int index = 0;
			final SortedSet<File> files = FileUtility.getFiles(inputFile);

			for (final File file : files) {

				if (config.isVERBOSE()) {
					final StringBuilder text = new StringBuilder();
					text.append(" [");
					text.append(Integer.toString(index++ + 1));
					text.append("/");
					text.append(Integer.toString(files.size()));
					text.append("] ");
					text.append(file.getAbsolutePath());
					System.err.println(text.toString());
				}

				final String outputPath = file.getAbsolutePath().replace(input,
						output);

				final String[] newArgs = new String[args.length];
				for (int i = 0; i < args.length; i++) {
					if (args[i].equals(input)) {
						newArgs[i] = file.getAbsolutePath();
					} else if (args[i].equals(output)) {
						newArgs[i] = outputPath;
					} else {
						newArgs[i] = args[i];
					}
				}

				main(newArgs);
			}
		}
	}

	private static String getSourceCode(final String code,
			final CompilationUnit unit) {
		final IDocument document = new Document(code);
		final TextEdit edit = unit.rewrite(document, null);
		try {
			edit.apply(document);
			return document.get();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
