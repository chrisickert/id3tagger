package org.sickert.id3tagger.event;

import java.text.MessageFormat;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.sickert.id3tagger.songinfoservice.Release;

/** @author Christian Sickert */
public class DetermineReleaseEvent extends BaseEvent {

  @Nonnull String albumName;

  @Nullable Release album;

  public DetermineReleaseEvent(
      boolean success, @Nonnull String albumName, @Nullable Release album) {
    super(success);
    this.albumName = albumName;
    this.album = album;
  }

  @Nonnull
  public String getAlbumName() {
    return albumName;
  }

  @Nullable
  public Release getAlbum() {
    return album;
  }

  @Nonnull
  @Override
  public String getSuccessMessage() {
    return MessageFormat.format(
        EVENT_MESSAGES.getString("determineRelease.success"), albumName, album);
  }

  @Nonnull
  @Override
  public String getErrorMessage() {
    return MessageFormat.format(EVENT_MESSAGES.getString("determineRelease.error"), albumName);
  }
}
