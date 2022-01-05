package org.sickert.id3tagger.action;

import java.io.File;
import javax.annotation.Nonnull;
import org.sickert.id3tagger.Id3Tag;
import org.sickert.id3tagger.event.BaseEvent;
import org.sickert.id3tagger.event.PreviewEvent;

/** @author Christian Sickert */
public class PreviewAction implements Action {

  @Override
  public BaseEvent execute(@Nonnull File mp3File, @Nonnull Id3Tag id3Tag) {
    return new PreviewEvent(mp3File, id3Tag);
  }
}
