package org.sickert.id3tagger.action;

import java.io.File;
import javax.annotation.Nonnull;
import org.sickert.id3tagger.Id3Tag;
import org.sickert.id3tagger.event.BaseEvent;

/** @author Christian Sickert */
public interface Action {

  static final Action TAG = new TagAction();
  static final Action PREVIEW = new PreviewAction();

  BaseEvent execute(@Nonnull File mp3File, @Nonnull Id3Tag id3Tag);
}
