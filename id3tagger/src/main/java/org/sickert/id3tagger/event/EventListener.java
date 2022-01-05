package org.sickert.id3tagger.event;

import javax.annotation.Nonnull;

/** @author Christian Sickert */
public interface EventListener {

  void notify(@Nonnull BaseEvent event);
}
