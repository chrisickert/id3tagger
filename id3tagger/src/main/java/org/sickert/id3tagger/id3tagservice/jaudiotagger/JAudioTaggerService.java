package org.sickert.id3tagger.id3tagservice.jaudiotagger;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.reference.GenreTypes;
import org.sickert.id3tagger.Id3Tag;
import org.sickert.id3tagger.id3tagservice.Id3TagService;

/** @author Christian Sickert */
public class JAudioTaggerService implements Id3TagService {

  static {
    // disable JAudioTagger's annoying logging
    Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
  }

  @Nullable
  @Override
  public Id3Tag readId3Tag(@Nonnull File mp3File) {
    try {
      // read existing tag, if any
      AudioFile audioFile = AudioFileIO.read(mp3File);
      Tag tag = audioFile.getTag();
      if (tag == null) {
        return null;
      }

      // create id3 tag from read tag
      Id3Tag id3Tag = new Id3Tag();
      id3Tag.setAlbum(tag.getFirst(FieldKey.ALBUM));
      id3Tag.setArtist(tag.getFirst(FieldKey.ARTIST));
      id3Tag.setTitle(tag.getFirst(FieldKey.TITLE));
      id3Tag.setTrack(tag.getFirst(FieldKey.TRACK));
      id3Tag.setYear(tag.getFirst(FieldKey.YEAR));
      id3Tag.setComment(tag.getFirst(FieldKey.COMMENT));
      id3Tag.setGenre(GenreTypes.getInstanceOf().getIdForName(tag.getFirst(FieldKey.GENRE)));
      return id3Tag;

    } catch (CannotReadException
        | IOException
        | TagException
        | ReadOnlyFileException
        | InvalidAudioFrameException e) {
      return null;
    }
  }

  @Override
  public boolean writeId3Tag(@Nonnull File mp3File, @Nonnull Id3Tag id3Tag) {
    try {
      // create a new tag
      AudioFile audioFile = AudioFileIO.read(mp3File);
      Tag tag = audioFile.createDefaultTag();

      // set the new tag's values to the given id3 tag's values
      if (null != id3Tag.getAlbum()) {
        tag.setField(FieldKey.ALBUM, id3Tag.getAlbum());
      }
      if (null != id3Tag.getArtist()) {
        tag.setField(FieldKey.ARTIST, id3Tag.getArtist());
      }
      if (null != id3Tag.getTitle()) {
        tag.setField(FieldKey.TITLE, id3Tag.getTitle());
      }
      if (null != id3Tag.getTrack()) {
        tag.setField(FieldKey.TRACK, id3Tag.getTrack());
      }
      if (null != id3Tag.getYear()) {
        tag.setField(FieldKey.YEAR, id3Tag.getYear());
      }
      if (null != id3Tag.getComment()) {
        tag.setField(FieldKey.COMMENT, id3Tag.getComment());
      }
      if (null != id3Tag.getGenre()) {
        tag.setField(FieldKey.GENRE, GenreTypes.getInstanceOf().getValueForId(id3Tag.getGenre()));
      }

      // commit the changes
      audioFile.setTag(tag);
      audioFile.commit();

      return true;

    } catch (CannotReadException
        | IOException
        | TagException
        | ReadOnlyFileException
        | InvalidAudioFrameException
        | CannotWriteException e) {
      return false;
    }
  }
}
