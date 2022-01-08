package org.sickert.id3tagger.cli;

import java.io.File;
import javax.annotation.Nonnull;
import org.sickert.id3tagger.action.Action;
import org.sickert.id3tagger.event.BaseEvent;
import org.sickert.id3tagger.event.DetermineReleaseEvent;
import org.sickert.id3tagger.event.DetermineSongEvent;
import org.sickert.id3tagger.event.EventListener;
import org.sickert.id3tagger.event.FindArtistEvent;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.IExitCodeGenerator;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "id3tagger",
    resourceBundle = "org.sickert.id3tagger.cli.CliMessages",
    mixinStandardHelpOptions = true,
    version = {"v0.1.0"}) // TODO: Provide a dynamic version number.
public class Id3Tagger implements Runnable, IExitCodeGenerator, EventListener {

  boolean hasErrors = false;

  @Parameters(index = "0", arity = "1", descriptionKey = "operationParameter")
  Operation operation;

  @Option(
      names = {"-b", "--basePath"},
      descriptionKey = "basePathOption")
  File basePath = new File(System.getProperty("user.dir"));

  @Override
  public void run() {
    org.sickert.id3tagger.Id3Tagger.traverseMp3FilesByArtistDirectoriesAndPerformAction(
        this.basePath, this.getActionForOperation(this.operation), this);
  }

  @Override
  public int getExitCode() {
    return this.hasErrors ? 1 : 0;
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
      this.hasErrors = true;
    }
  }

  private Action getActionForOperation(@Nonnull Operation operation) {
    switch (operation) {
      case TAG:
        return Action.TAG;
      case PREVIEW:
        return Action.PREVIEW;
      default:
        throw new UnsupportedOperationException(
            "Support for " + operation.name() + " is not implemented.");
    }
  }

  private void printInfo(String message) {
    System.out.println(message);
  }

  private void printError(String message) {
    System.err.println(message);
  }

  public static void main(String[] args) {
    int exitCode =
        new CommandLine(new Id3Tagger()).setCaseInsensitiveEnumValuesAllowed(true).execute(args);
    System.exit(exitCode);
  }
}
