package org.sickert.id3tagger.songinfoservice;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface SongInfoService {

  @Nullable
  public Artist findArtist(@Nonnull String artistName);

  @Nonnull
  public List<Release> findReleases(@Nonnull Artist artist);

  @Nonnull
  public List<Song> findSongs(@Nonnull Release release);
}
