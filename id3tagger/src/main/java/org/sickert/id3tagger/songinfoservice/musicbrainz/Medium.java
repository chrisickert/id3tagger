package org.sickert.id3tagger.songinfoservice.musicbrainz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Medium {

  private String position;

  private List<Track> tracks;

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public List<Track> getTracks() {
    return tracks;
  }

  public void setTracks(List<Track> tracks) {
    this.tracks = tracks;
  }

  public void renumberTracks() {
    if (null != tracks) {
      int trackNumber = 1;
      for (Track track : tracks) {
        track.setNumber(Integer.toString(trackNumber));
        trackNumber++;
      }
    }
  }
}
