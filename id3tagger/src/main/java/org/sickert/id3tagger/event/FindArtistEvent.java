package org.sickert.id3tagger.event;

import java.text.MessageFormat;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.sickert.id3tagger.songinfoservice.Artist;

/** @author Christian Sickert */
public class FindArtistEvent extends BaseEvent {

  @Nonnull private String artistName;

  @Nullable private Artist artist;

  public FindArtistEvent(boolean success, @Nonnull String artistName, @Nullable Artist artist) {
    super(success);
    this.artistName = artistName;
    this.artist = artist;
  }

  @Nonnull
  public String getArtistName() {
    return artistName;
  }

  @Nullable
  public Artist getArtist() {
    return artist;
  }

  @Nonnull
  @Override
  public String getSuccessMessage() {
    return MessageFormat.format(EVENT_MESSAGES.getString("findArtist.success"), artistName, artist);
  }

  @Nonnull
  @Override
  public String getErrorMessage() {
    return MessageFormat.format(EVENT_MESSAGES.getString("findArtist.error"), artistName);
  }
}
