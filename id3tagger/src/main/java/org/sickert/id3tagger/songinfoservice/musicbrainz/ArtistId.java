package org.sickert.id3tagger.songinfoservice.musicbrainz;

import java.util.List;
import javax.annotation.Nonnull;

/** Created by Christian Sickert */
public class ArtistId implements org.sickert.id3tagger.songinfoservice.ArtistId {

  @Nonnull private List<String> ids;

  public ArtistId(@Nonnull List<String> ids) {
    this.ids = ids;
  }

  @Nonnull
  public List<String> getIds() {
    return ids;
  }

  public void setIds(@Nonnull List<String> ids) {
    this.ids = ids;
  }
}
