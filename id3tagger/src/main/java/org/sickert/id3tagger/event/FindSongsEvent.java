package org.sickert.id3tagger.event;

import java.text.MessageFormat;
import java.util.List;
import javax.annotation.Nonnull;
import org.sickert.id3tagger.songinfoservice.Release;
import org.sickert.id3tagger.songinfoservice.Song;

/** @author Christian Sickert */
public class FindSongsEvent extends BaseEvent {

  @Nonnull private Release album;

  @Nonnull private List<Song> songs;

  public FindSongsEvent(boolean success, @Nonnull Release album, @Nonnull List<Song> songs) {
    super(success);
    this.album = album;
    this.songs = songs;
  }

  @Nonnull
  public Release getAlbum() {
    return album;
  }

  @Nonnull
  public List<Song> getSongs() {
    return songs;
  }

  @Nonnull
  @Override
  public String getSuccessMessage() {
    return MessageFormat.format(EVENT_MESSAGES.getString("findSongs.success"), album, songs.size());
  }

  @Nonnull
  @Override
  public String getErrorMessage() {
    return MessageFormat.format(EVENT_MESSAGES.getString("findSongs.error"), album);
  }
}
