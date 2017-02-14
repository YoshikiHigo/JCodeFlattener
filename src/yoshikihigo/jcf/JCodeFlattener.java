package yoshikihigo.jcf;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

public class JCodeFlattener {

	private static String[] sourceDirectories = null;
	private static String[] classpathEntries = null;

	@SuppressWarnings("unchecked")
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

		final boolean isNameResolving = !config.isFAST();

		if (isNameResolving && null == classpathEntries) {
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
		final boolean isOnlyFormatting = config.isFORMAT();
		final boolean aggresive = config.isAGGRESIVE();

		final File inputFile = new File(input);
		final File outputFile = new File(output);

		if (inputFile.isFile()) {

			try {

				String text = FileUtils.readFileToString(inputFile, Charset.defaultCharset());
				int pseudVariableID = 0;

				if (input.endsWith(".java")) {

					final Document document = new Document(text);

					while (!isOnlyFormatting) {
						final ASTParser parser = ASTParser.newParser(AST.JLS8);
						parser.setSource(document.get().toCharArray());
						parser.setUnitName(input);
						parser.setKind(ASTParser.K_COMPILATION_UNIT);
						parser.setResolveBindings(isNameResolving);

						if (isNameResolving && null == sourceDirectories) {
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

						final JCFASTVisitor visitor = new JCFASTVisitor(ast, pseudVariableID++, rewriter, aggresive,
								isNameResolving);
						unit.accept(visitor);

						if (!visitor.isChanged()) {
							break;
						}

						// final TextEdit edit1 = rewriter.rewriteAST(document,
						// DefaultCodeFormatterConstants.getEclipseDefaultSettings());
						final TextEdit edit1 = rewriter.rewriteAST(document, null);
						edit1.apply(document);
						text = document.get();
					}

					if (config.isCOMMENT()) {
						final ASTParser parser = ASTParser.newParser(AST.JLS8);
						parser.setSource(document.get().toCharArray());
						parser.setUnitName(input);
						parser.setKind(ASTParser.K_COMPILATION_UNIT);
						parser.setResolveBindings(false);

						final Map<String, String> options = JavaCore.getOptions();
						JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
						parser.setCompilerOptions(options);

						final CompilationUnit unit = (CompilationUnit) parser.createAST(null);
						((List<Comment>) unit.getCommentList()).stream().forEach(e -> {
							e.accept(new JCFCommentVisitor());
							e.delete();
						});

						text = unit.toString();
						document.set(text);
					}

					final CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(null);
					// final TextEdit codeEdit = codeFormatter.format(
					// CodeFormatter.K_COMPILATION_UNIT |
					// CodeFormatter.F_INCLUDE_COMMENTS, document.get(), 0,
					// document.get().length(), 0, null);
					final TextEdit codeEdit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT, document.get(), 0,
							document.get().length(), 0, null);
					codeEdit.apply(document);
					text = document.get();
				}
				FileUtils.write(outputFile, text, Charset.defaultCharset());

			} catch (final IOException | BadLocationException e) {
				e.printStackTrace();
			}
		}

		else if (inputFile.isDirectory()) {

			int index = 0;
			final SortedSet<File> files = FileUtility.getFiles(inputFile);
			if (isNameResolving) {
				sourceDirectories = files.stream().map(f -> f.getParentFile().getAbsolutePath()).toArray(String[]::new);
			}

			final int threads = config.getTHREADS();
			final ExecutorService threadPool = Executors.newFixedThreadPool(threads);
			final List<Future<?>> futures = new ArrayList<>();

			for (final File file : files) {
				final Future<?> future = threadPool.submit(new MyThread(args, file, index++, files.size(), input,
						output, inputFile, outputFile, config.isVERBOSE()));
				futures.add(future);
			}

			try {
				for (final Future<?> future : futures) {
					future.get();
				}
			} catch (final ExecutionException | InterruptedException e) {
				e.printStackTrace();
				System.exit(0);
			} finally {
				threadPool.shutdown();
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

	static class MyThread extends Thread {

		final String[] args;
		final File file;
		final int index;
		final int files;
		final String inputFile;
		final String outputFile;
		final File inputDir;
		final File outputDir;
		final boolean verbose;

		MyThread(final String[] args, final File file, final int index, final int files, final String input,
				final String output, final File inputFile, final File outputFile, final boolean verbose) {
			this.args = args;
			this.file = file;
			this.index = index;
			this.files = files;
			this.inputFile = input;
			this.outputFile = output;
			this.inputDir = inputFile;
			this.outputDir = outputFile;
			this.verbose = verbose;
		}

		@Override
		public void run() {
			if (this.verbose) {
				final StringBuilder text = new StringBuilder();
				text.append(" [");
				text.append(Integer.toString(this.index + 1));
				text.append("/");
				text.append(Integer.toString(this.files));
				text.append("] ");
				text.append(file.getAbsolutePath());
				text.append(" is being flattened by thread ");
				text.append(Long.toString(Thread.currentThread().getId()));
				System.err.println(text.toString());
			}

			final String outputPath = this.file.getAbsolutePath().replace(this.inputDir.getAbsolutePath(),
					this.outputDir.getAbsolutePath());

			final String[] newArgs = new String[this.args.length];
			for (int i = 0; i < this.args.length; i++) {
				if (this.args[i].equals(this.inputFile)) {
					newArgs[i] = this.file.getAbsolutePath();
				} else if (args[i].equals(outputFile)) {
					newArgs[i] = outputPath;
				} else {
					newArgs[i] = this.args[i];
				}
			}

			main(newArgs);
		}
	}

}
