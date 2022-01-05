package org.sickert.id3tagger.songinfoservice.musicbrainz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseGroupList extends EntityList<ReleaseGroup> {

  public ReleaseGroupList(
      @JsonProperty("release-group-count") String allEntitiesCount,
      @JsonProperty("release-groups") List<ReleaseGroup> entities) {
    super(allEntitiesCount, entities);
  }
}
