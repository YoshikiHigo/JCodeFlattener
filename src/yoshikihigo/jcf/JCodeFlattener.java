package yoshikihigo.jcf;

import java.io.File;
import java.util.SortedSet;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
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
		if (inputFile.isFile()) {
			final String text = FileUtility.readFile(inputFile,
					config.hasENCODING() ? config.getENCODING() : null);

			final ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setSource(text.toCharArray());
			final CompilationUnit unit = (CompilationUnit) parser
					.createAST(null);
			unit.recordModifications();

			final String normalizedText = getSourceCode(text, unit);

			// File file = new File(DEST_FILE_PATH);
			// FileUtils.writeStringToFile(file, source)

			FileUtility.writeFile(normalizedText, output);
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
