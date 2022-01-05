package org.sickert.id3tagger.songinfoservice.musicbrainz;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityList<T> {

  private String allEntitiesCount;

  private List<T> entities;

  public EntityList(String allEntitiesCount, List<T> entities) {
    this.allEntitiesCount = allEntitiesCount;
    this.entities = entities;
  }

  public String getAllEntitiesCount() {
    return allEntitiesCount;
  }

  public List<T> getEntities() {
    return entities;
  }

  public int size() {
    if (this.entities != null) {
      return this.entities.size();
    }
    return 0;
  }

  public boolean hasEntities() {
    return this.entities != null && !this.entities.isEmpty();
  }

  public T getFirstEntity() {
    if (null == entities || entities.isEmpty()) {
      return null;
    }
    return entities.get(0);
  }

  @SuppressWarnings("unchecked")
  public void addEntities(List<?> entities) {
    if (entities == null || entities.size() == 0) {
      return;
    }

    if (this.entities == null) {
      this.entities = new ArrayList<>();
    }

    // TODO: This is not type-safe and might cause a ClassCastException
    entities.forEach(entity -> this.entities.add((T) entity));
  }

  public void shrink(int maxEntities) {
    if (entities != null && this.entities.size() > maxEntities) {
      this.entities = this.entities.subList(0, maxEntities);
    }
  }
}
