package org.sickert.id3tagger.cli;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import org.apache.commons.cli.*;

/** @author Christian Sickert */
public class CommandLineParser {

  protected static final String DEFAULT_BASE_PATH = System.getProperty("user.dir");

  protected static final Operation DEFAULT_OPERATION_MODE = Operation.TAG;

  protected static final Map<String, Operation> COMMAND_OPMODE_MAP;

  static {
    COMMAND_OPMODE_MAP = new HashMap<>();
    COMMAND_OPMODE_MAP.put("tag", Operation.TAG);
    COMMAND_OPMODE_MAP.put("preview", Operation.PREVIEW);
  }

  @SuppressWarnings("static-access")
  protected static Option basePathOption =
      OptionBuilder.withArgName("directory")
          .hasArg()
          .withLongOpt("basePath")
          .withDescription(
              "Specifies the base path where the MP3 files are located whose ID3 tags"
                  + " are to be fixed. The current directory is used if omitted.")
          .create("b");

  @SuppressWarnings("static-access")
  protected static Option quietOption =
      OptionBuilder.withLongOpt("quiet")
          .withDescription("Run in quiet-mode, i.e. don't print any messages.")
          .create("q");

  protected Options options;

  protected CommandLine commandLine;

  protected Operation operationMode;

  public CommandLineParser() {
    options = createOptions();
  }

  protected Options createOptions() {
    Options options = new Options();
    options.addOption(CommandLineParser.basePathOption);
    options.addOption(CommandLineParser.quietOption);
    return options;
  }

  public void parse(String[] args) throws CommandLineParsingException {
    org.apache.commons.cli.CommandLineParser cliParser = new BasicParser();
    try {
      commandLine = cliParser.parse(options, args);
      operationMode = parseOperationMode(commandLine.getArgs());
    } catch (ParseException e) {
      throw new CommandLineParsingException(e.getMessage());
    }
  }

  protected Operation parseOperationMode(String[] args) throws CommandLineParsingException {
    if ((args == null) || (args.length == 0)) {
      return DEFAULT_OPERATION_MODE;
    }
    String givenOperationMode = args[0];
    for (Entry<String, Operation> modeCommandEntry :
        CommandLineParser.COMMAND_OPMODE_MAP.entrySet()) {
      if (modeCommandEntry.getKey().equalsIgnoreCase(givenOperationMode)) {
        return modeCommandEntry.getValue();
      }
    }
    throw new CommandLineParsingException("Illegal operation mode: " + givenOperationMode);
  }

  public void printUsage() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.setWidth(120);
    StringBuilder usageText = new StringBuilder();
    usageText.append(Id3TaggerCommandLineClient.APPLICATION_NAME).append(" ");
    usageText.append("[OPTIONS] [OPERATION]\n");
    usageText.append("OPERATION must be one of\n");
    for (Entry<String, Operation> modeCommandEntry :
        CommandLineParser.COMMAND_OPMODE_MAP.entrySet()) {
      usageText.append(modeCommandEntry.getKey()).append(" - ");
      switch (modeCommandEntry.getValue()) {
        case TAG:
          usageText.append(
              "(Default) Sets the MP3 files ID3 tags according to the data of the"
                  + " song info service.\n");
          break;
        case PREVIEW:
          usageText.append(
              "Only previews what the MP3 files would be tagged. Does not change any"
                  + " ID3 tags.\n");
          break;
      }
    }
    formatter.printHelp(usageText.toString(), options);
  }

  @Nonnull
  public File getBasePath() {
    String basePath = commandLine.getOptionValue(CommandLineParser.basePathOption.getOpt());
    if (null == basePath) {
      return new File(CommandLineParser.DEFAULT_BASE_PATH);
    }
    return new File(basePath);
  }

  public boolean isQuiet() {
    return commandLine.hasOption(CommandLineParser.quietOption.getOpt());
  }

  @Nonnull
  public Operation getOperation() {
    return operationMode;
  }
}
