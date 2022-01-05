package org.sickert.id3tagger.action;

import java.io.File;
import javax.annotation.Nonnull;
import org.sickert.id3tagger.Id3Tag;
import org.sickert.id3tagger.ServiceLoader;
import org.sickert.id3tagger.event.BaseEvent;
import org.sickert.id3tagger.event.TagEvent;
import org.sickert.id3tagger.id3tagservice.Id3TagService;

/** @author Christian Sickert */
public class TagAction implements Action {

  @Override
  public BaseEvent execute(@Nonnull File mp3File, @Nonnull Id3Tag id3Tag) {
    Id3TagService id3TagService = ServiceLoader.load(Id3TagService.class);
    return new TagEvent(id3TagService.writeId3Tag(mp3File, id3Tag), mp3File, id3Tag);
  }
}
