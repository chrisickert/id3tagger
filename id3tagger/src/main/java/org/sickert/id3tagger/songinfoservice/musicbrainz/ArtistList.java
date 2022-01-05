package org.sickert.id3tagger.songinfoservice.musicbrainz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistList extends EntityList<Artist> {

  public ArtistList(
      @JsonProperty("count") String allEntitiesCount,
      @JsonProperty("artists") List<Artist> entities) {
    super(allEntitiesCount, entities);
  }
}
