package org.sickert.id3tagger.songinfoservice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Release {

  public enum Type {
    ALBUM,
    SINGLE,
    OTHER
  }

  @JsonIgnore private ReleaseId id; // service specific release ID, used for internal purposes only

  @JsonProperty private Artist artist;

  @JsonProperty private Type type;

  @JsonProperty private String name;

  @JsonProperty private String releaseYear;

  public Release() {} // for JSON deserialization

  public Release(ReleaseId id, Artist artist, Type type, String name, String releaseYear) {
    this.id = id;
    this.artist = artist;
    this.type = type;
    this.name = name;
    this.releaseYear = releaseYear;
  }

  public ReleaseId getId() {
    return id;
  }

  public void setId(ReleaseId id) {
    this.id = id;
  }

  public Artist getArtist() {
    return artist;
  }

  public void setArtist(Artist artist) {
    this.artist = artist;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getReleaseYear() {
    return releaseYear;
  }

  public void setReleaseYear(String releaseYear) {
    this.releaseYear = releaseYear;
  }

  @Override
  public String toString() {
    StringBuilder albumStringBuilder = new StringBuilder();

    if (name != null) {
      albumStringBuilder.append(name);
    } else {
      albumStringBuilder.append("unknown album");
    }

    albumStringBuilder.append(" (");
    if (type != null) {
      albumStringBuilder.append(type.name());
    } else {
      albumStringBuilder.append("unknown type");
    }

    albumStringBuilder.append(", ");
    if (releaseYear != null) {
      albumStringBuilder.append(releaseYear);
    } else {
      albumStringBuilder.append("unknown release year");
    }
    albumStringBuilder.append(")");

    return albumStringBuilder.toString();
  }
}
