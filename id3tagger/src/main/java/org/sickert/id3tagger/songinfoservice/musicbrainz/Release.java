package org.sickert.id3tagger.songinfoservice.musicbrainz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Release {

  private String date;

  private List<Medium> media;

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public List<Medium> getMedia() {
    return media;
  }

  public void setMedia(List<Medium> media) {
    this.media = media;
  }
}
