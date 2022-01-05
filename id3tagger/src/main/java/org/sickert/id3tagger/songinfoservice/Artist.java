package org.sickert.id3tagger.songinfoservice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;

public class Artist {

  @Nullable @JsonProperty private String name;

  @Nullable @JsonIgnore
  private ArtistId id; // the service specific ID, used for internal purposes only

  public Artist() {} // for JSON deserialization

  public Artist(@Nullable ArtistId id, @Nullable String name) {
    this.id = id;
    this.name = name;
  }

  @Nullable
  public String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  @Nullable
  public ArtistId getId() {
    return id;
  }

  public void setId(@Nullable ArtistId id) {
    this.id = id;
  }

  @Override
  public String toString() {
    if (name != null) {
      return name;
    }
    return super.toString();
  }
}
