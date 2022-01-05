package org.sickert.id3tagger.id3tagservice;

import java.io.File;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.sickert.id3tagger.Id3Tag;

public interface Id3TagService {

  @Nullable
  Id3Tag readId3Tag(@Nonnull File mp3File);

  boolean writeId3Tag(@Nonnull File mp3File, @Nonnull Id3Tag id3Tag);
}
