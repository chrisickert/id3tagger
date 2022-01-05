package org.sickert.id3tagger.cli;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.sickert.id3tagger.Id3Tagger;
import org.sickert.id3tagger.action.Action;
import org.sickert.id3tagger.event.BaseEvent;
import org.sickert.id3tagger.event.DetermineReleaseEvent;
import org.sickert.id3tagger.event.DetermineSongEvent;
import org.sickert.id3tagger.event.EventListener;
import org.sickert.id3tagger.event.FindArtistEvent;

/** @author Christian Sickert */
public class Id3TaggerCommandLineClient implements EventListener {

  public static final String APPLICATION_NAME = "id3tagger";

  private boolean quiet = false;

  private boolean error = false;

  public static void main(String args[]) {
    Id3TaggerCommandLineClient client = new Id3TaggerCommandLineClient();
    CommandLineParser parser = client.parseCommandLine(args);
    if (null == parser) {
      System.exit(-1);
    }
    client.run(parser);
    if (client.error) {
      System.exit(1);
    } else {
      System.exit(0);
    }
  }

  @Nullable
  private CommandLineParser parseCommandLine(String args[]) {
    CommandLineParser parser = new CommandLineParser();

    try {
      parser.parse(args);
      return parser;
    } catch (CommandLineParsingException e) {
      System.err.println(e.getMessage());
      parser.printUsage();
      return null;
    }
  }

  private void run(CommandLineParser parser) {
    this.quiet = parser.isQuiet();
    if (parser.getOperation() == Operation.TAG) {
      Id3Tagger.traverseMp3FilesByArtistDirectoriesAndPerformAction(
          parser.getBasePath(), Action.TAG, this);
    } else if (parser.getOperation() == Operation.PREVIEW) {
      Id3Tagger.traverseMp3FilesByArtistDirectoriesAndPerformAction(
          parser.getBasePath(), Action.PREVIEW, this);
    }
  }

  private void printInfo(@Nonnull String message) {
    if (!quiet) {
      System.out.println(message);
    }
  }

  private void printError(@Nonnull String message) {
    if (!quiet) {
      System.err.println(message);
    }
  }

  @Override
  public void notify(@Nonnull BaseEvent event) {
    if (event.wasSuccessful()) {
      if (event instanceof FindArtistEvent
          || event instanceof DetermineReleaseEvent
          || event instanceof DetermineSongEvent) {
        printInfo(event.toString());
      }
    } else {
      printError(event.toString());
      error = true;
    }
  }
}
