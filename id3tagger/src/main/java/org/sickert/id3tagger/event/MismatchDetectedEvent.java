package org.sickert.id3tagger.event;

import java.io.File;
import java.text.MessageFormat;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;

/** Created by Christian Sickert */
public class MismatchDetectedEvent extends BaseEvent {

  @Nonnull private File albumDirectory;

  public MismatchDetectedEvent(@Nonnull File albumDirectory) {
    super(false);
    this.albumDirectory = albumDirectory;
  }

  @Nonnull
  @Override
  public String getSuccessMessage() {
    // this event will never be successful
    return StringUtils.EMPTY;
  }

  @Nonnull
  @Override
  public String getErrorMessage() {
    return MessageFormat.format(
        BaseEvent.EVENT_MESSAGES.getString("mismatchDetected.error"), albumDirectory.getPath());
  }
}
