package yoshikihigo.jcf;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.SortedSet;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

public class JCodeFlattener {

	private static String[] sourceDirectories = null;
	private static String[] classpathEntries = null;

	public static void main(final String[] args) {

		final JCFConfig config = JCFConfig.initialize(args);

		if (!config.hasINPUT()) {
			System.err.println("option \"-input\" is require to specify your target source files.");
			System.exit(0);
		}
		final String input = config.getINPUT();

		if (!config.hasOUTPUT()) {
			System.err.println("option \"-output\" is require to specify this tool's output directory.");
			System.exit(0);
		}

		if (null == classpathEntries) {
			final String libraries = config.hasLIBRARY() ? config.getLIBRARY() : System.getProperty("java.class.path");
			final String osname = System.getProperty("os.name");
			if (osname.contains("Windows")) {
				classpathEntries = libraries.split(";");
			} else if (osname.contains("Linux") || osname.contains("Mac")) {
				classpathEntries = libraries.split(":");
			} else {
				System.err.println("Your OS (" + osname + ") is not supported by this tool.");
				System.exit(0);
			}
		}

		final String output = config.getOUTPUT();
		final boolean aggresive = config.isAGGRESIVE();

		final File inputFile = new File(input);
		final File outputFile = new File(output);

		if (inputFile.isFile()) {

			try {

				String text = FileUtils.readFileToString(inputFile, Charset.defaultCharset());
				int pseudVariableID = 0;

				if (input.endsWith(".java")) {
					while (true) {
						final Document document = new Document(text);

						final ASTParser parser = ASTParser.newParser(AST.JLS8);
						parser.setSource(document.get().toCharArray());
						parser.setUnitName(input);
						parser.setKind(ASTParser.K_COMPILATION_UNIT);
						parser.setResolveBindings(true);

						if (null == sourceDirectories) {
							sourceDirectories = new String[] { inputFile.getAbsolutePath() };
						}

						parser.setEnvironment(classpathEntries, sourceDirectories, null, true);

						final Map<String, String> options = JavaCore.getOptions();
						JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
						parser.setCompilerOptions(options);

						final CompilationUnit unit = (CompilationUnit) parser.createAST(null);
						final AST ast = unit.getAST();
						final ASTRewrite rewriter = ASTRewrite.create(ast);
						unit.recordModifications();

						final JCFASTVisitor visitor = new JCFASTVisitor(ast, pseudVariableID++, rewriter, aggresive);
						unit.accept(visitor);

						if (!visitor.isChanged()) {
							break;
						}

						final TextEdit edit = rewriter.rewriteAST(document, null);
						edit.apply(document);
						text = document.get();
					}
				}
				FileUtils.write(outputFile, text, Charset.defaultCharset());

			} catch (final IOException | BadLocationException e) {
				e.printStackTrace();
			}
		}

		else if (inputFile.isDirectory()) {

			int index = 0;
			final SortedSet<File> files = FileUtility.getFiles(inputFile);
			sourceDirectories = files.stream().map(f -> f.getParentFile().getAbsolutePath()).toArray(String[]::new);

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

				final String outputPath = file.getAbsolutePath().replace(input, output);

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

	private static String getSourceCode(final String code, final CompilationUnit unit) {
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
