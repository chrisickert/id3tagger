package org.sickert.id3tagger.songinfoservice.musicbrainz;

import javax.annotation.Nonnull;

/** Created by Christian Sickert */
public class AlbumId implements org.sickert.id3tagger.songinfoservice.ReleaseId {

  @Nonnull private String id;

  public AlbumId(@Nonnull String id) {
    this.id = id;
  }

  @Nonnull
  public String getId() {
    return id;
  }

  public void setId(@Nonnull String id) {
    this.id = id;
  }
}
