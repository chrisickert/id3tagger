package org.sickert.id3tagger;

import javax.annotation.Nullable;

/** @author Christian Sickert */
public class Id3Tag {

  @Nullable protected String artist;

  @Nullable protected String title;

  @Nullable protected String track;

  @Nullable protected String album;

  @Nullable protected String year;

  @Nullable protected String comment;

  @Nullable protected Integer genre;

  @Nullable
  public String getArtist() {
    return artist;
  }

  public void setArtist(@Nullable String artist) {
    this.artist = artist;
  }

  @Nullable
  public String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  @Nullable
  public String getTrack() {
    return track;
  }

  public void setTrack(@Nullable String track) {
    this.track = track;
  }

  @Nullable
  public String getAlbum() {
    return album;
  }

  public void setAlbum(@Nullable String album) {
    this.album = album;
  }

  @Nullable
  public String getYear() {
    return year;
  }

  public void setYear(@Nullable String year) {
    this.year = year;
  }

  @Nullable
  public Integer getGenre() {
    return genre;
  }

  public void setGenre(@Nullable Integer genre) {
    this.genre = genre;
  }

  @Nullable
  public String getComment() {
    return comment;
  }

  public void setComment(@Nullable String comment) {
    this.comment = comment;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    if (title != null) {
      sb.append("Title: ").append(title);
    }
    if (artist != null) {
      if (sb.length() > 1) {
        sb.append(", ");
      }
      sb.append("Artist: ").append(artist);
    }
    if (album != null) {
      if (sb.length() > 1) {
        sb.append(", ");
      }
      sb.append("Album: ").append(album);
    }
    if (track != null) {
      if (sb.length() > 1) {
        sb.append(", ");
      }
      sb.append("Track: ").append(track);
    }
    if (year != null) {
      if (sb.length() > 1) {
        sb.append(", ");
      }
      sb.append("Year: ").append(year);
    }
    if (comment != null) {
      if (sb.length() > 1) {
        sb.append(", ");
      }
      sb.append("Comment: ").append(comment);
    }
    if (genre != null) {
      if (sb.length() > 1) {
        sb.append(", ");
      }
      sb.append("Genre: ").append(genre);
    }
    sb.append("]");
    return sb.toString();
  }
}
