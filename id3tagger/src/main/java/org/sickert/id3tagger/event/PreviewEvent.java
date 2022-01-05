package org.sickert.id3tagger.event;

import java.io.File;
import java.text.MessageFormat;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.sickert.id3tagger.Id3Tag;

/** @author Christian Sickert */
public class PreviewEvent extends BaseEvent {

  @Nonnull private File mp3File;

  @Nonnull private Id3Tag id3Tag;

  public PreviewEvent(@Nonnull File mp3File, @Nonnull Id3Tag id3Tag) {
    super(true);
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
    return MessageFormat.format(
        BaseEvent.EVENT_MESSAGES.getString("preview.success"), mp3File.getName(), id3Tag);
  }

  @Nonnull
  @Override
  public String getErrorMessage() {
    // will never happen
    return StringUtils.EMPTY;
  }
}
