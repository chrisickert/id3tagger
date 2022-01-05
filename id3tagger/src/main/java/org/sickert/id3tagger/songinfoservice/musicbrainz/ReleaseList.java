package org.sickert.id3tagger.songinfoservice.musicbrainz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseList extends EntityList<Release> {

  public ReleaseList(
      @JsonProperty("release-count") String allEntitiesCount,
      @JsonProperty("releases") List<Release> entities) {
    super(allEntitiesCount, entities);
  }
}
