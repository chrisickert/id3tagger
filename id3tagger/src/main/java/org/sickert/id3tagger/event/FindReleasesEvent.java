package org.sickert.id3tagger.event;

import java.text.MessageFormat;
import java.util.List;
import javax.annotation.Nonnull;
import org.sickert.id3tagger.songinfoservice.Artist;
import org.sickert.id3tagger.songinfoservice.Release;

/** @author Christian Sickert */
public class FindReleasesEvent extends BaseEvent {

  @Nonnull private Artist artist;

  @Nonnull private List<Release> releases;

  public FindReleasesEvent(
      boolean success, @Nonnull Artist artist, @Nonnull List<Release> releases) {
    super(success);
    this.artist = artist;
    this.releases = releases;
  }

  @Nonnull
  public Artist getArtist() {
    return artist;
  }

  @Nonnull
  public List<Release> getReleases() {
    return releases;
  }

  @Nonnull
  @Override
  public String getSuccessMessage() {
    return MessageFormat.format(
        EVENT_MESSAGES.getString("findDiscography.success"), artist, releases.size());
  }

  @Nonnull
  @Override
  public String getErrorMessage() {
    return MessageFormat.format(EVENT_MESSAGES.getString("findDiscography.error"), artist);
  }
}
