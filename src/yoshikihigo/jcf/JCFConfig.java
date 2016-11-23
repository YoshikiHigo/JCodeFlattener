package yoshikihigo.jcf;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class JCFConfig {

	static final public Options OPTIONS = new Options();
	static {
		OPTIONS.addOption(Option.builder("i").longOpt("input").required(true)
				.hasArg(true).argName("dir").desc("input directory").build());

		OPTIONS.addOption(Option.builder("o").longOpt("output").required(true)
				.hasArg(true).argName("dir").desc("output directory").build());

		OPTIONS.addOption(Option.builder("e").longOpt("encoding")
				.required(false).argName("code")
				.desc("encoding of target files").build());

		OPTIONS.addOption(Option.builder("v").longOpt("verbose")
				.required(false).hasArg(false).desc("verbose output").build());

		OPTIONS.addOption(Option.builder("q").longOpt("quiet").required(false)
				.hasArg(false).desc("no progres output").build());

		OPTIONS.addOption(Option.builder("a").longOpt("aggressive")
				.required(false).hasArg(false).desc("aggresively flattening")
				.build());
	}

	static public JCFConfig initialize(final String[] args) {

		JCFConfig config = null;

		try {
			final CommandLineParser parser = new DefaultParser();
			final CommandLine commandLine = parser.parse(OPTIONS, args);
			config = new JCFConfig(commandLine);

			if (config.isVERBOSE() && config.isQUIET()) {
				System.err
						.println("\"-v\" (\"--verbose\") and \"-q\" (\"--quiet\") can not be used together.");
				System.exit(0);
			}

		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(0);
		}

		return config;
	}

	private final CommandLine commandLine;

	private JCFConfig(final CommandLine commandLine) {
		this.commandLine = commandLine;
	}

	public boolean hasINPUT() {
		return this.commandLine.hasOption("i");
	}

	public String getINPUT() {
		return this.hasINPUT() ? this.commandLine.getOptionValue("i") : "";
	}

	public boolean hasOUTPUT() {
		return this.commandLine.hasOption("o");
	}

	public String getOUTPUT() {
		return this.hasOUTPUT() ? this.commandLine.getOptionValue("o") : "";
	}

	public boolean hasENCODING() {
		return this.commandLine.hasOption("encoding");
	}

	public String getENCODING() {
		return this.hasENCODING() ? this.commandLine.getOptionValue("e") : "";
	}

	public boolean isVERBOSE() {
		return this.commandLine.hasOption("v");
	}

	public boolean isQUIET() {
		return this.commandLine.hasOption("q");
	}

	public boolean isAGGRESIVE() {
		return this.commandLine.hasOption("a");
	}
}
