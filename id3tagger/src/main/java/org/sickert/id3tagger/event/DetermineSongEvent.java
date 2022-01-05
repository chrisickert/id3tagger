package org.sickert.id3tagger.event;

import java.io.File;
import java.text.MessageFormat;
import javax.annotation.Nonnull;
import org.sickert.id3tagger.songinfoservice.Song;

/** @author Christian Sickert */
public class DetermineSongEvent extends BaseEvent {

  @Nonnull private File mp3File;

  @Nonnull private Song song;

  public DetermineSongEvent(boolean success, @Nonnull File mp3File, @Nonnull Song song) {
    super(success);
    this.mp3File = mp3File;
    this.song = song;
  }

  @Nonnull
  public File getMp3File() {
    return mp3File;
  }

  @Nonnull
  public Song getSong() {
    return song;
  }

  @Nonnull
  @Override
  public String getSuccessMessage() {
    return MessageFormat.format(
        EVENT_MESSAGES.getString("determineSong.success"), mp3File.getName(), song);
  }

  @Nonnull
  @Override
  public String getErrorMessage() {
    return MessageFormat.format(EVENT_MESSAGES.getString("determineSong.error"), mp3File.getName());
  }
}
