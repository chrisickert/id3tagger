package org.sickert.id3tagger.event;

import java.io.File;
import java.text.MessageFormat;
import javax.annotation.Nonnull;
import org.sickert.id3tagger.Id3Tag;

/** @author Christian Sickert */
public class TagEvent extends BaseEvent {

  @Nonnull private File mp3File;

  @Nonnull private Id3Tag id3Tag;

  public TagEvent(boolean success, @Nonnull File mp3File, @Nonnull Id3Tag id3Tag) {
    super(success);
    this.mp3File = mp3File;
    this.id3Tag = id3Tag;
  }

  @Nonnull
  public File getMp3File() {
    return mp3File;
  }

  @Nonnull
  public Id3Tag getId3Tag() {
    return id3Tag;
  }

  @Nonnull
  @Override
  public String getSuccessMessage() {
    return MessageFormat.format(EVENT_MESSAGES.getString("tag.success"), mp3File, id3Tag);
  }

  @Nonnull
  @Override
  public String getErrorMessage() {
    return MessageFormat.format(EVENT_MESSAGES.getString("tag.error"), mp3File);
  }
}
