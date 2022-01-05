package org.sickert.id3tagger.songinfoservice;

public class Song {

  private Release album;

  private String trackNumber;

  private String title;

  public Release getAlbum() {
    return album;
  }

  public void setAlbum(Release album) {
    this.album = album;
  }

  public String getTrackNumber() {
    return trackNumber;
  }

  public void setTrackNumber(String trackNumber) {
    this.trackNumber = trackNumber;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    if (title != null && trackNumber != null) {
      return trackNumber + " - " + title;
    } else if (title != null) {
      return title;
    } else {
      return "Unknown song";
    }
  }
}
