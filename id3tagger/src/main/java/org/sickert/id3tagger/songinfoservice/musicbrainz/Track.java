package org.sickert.id3tagger.songinfoservice.musicbrainz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {

  private String number;

  private String title;

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public int hashCode() {
    return number.hashCode() + title.toLowerCase().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    return hashCode() == obj.hashCode();
  }
}
