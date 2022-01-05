package org.sickert.id3tagger;

import java.io.File;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;

public class Mp3Files {

  private static final String MP3_FILE_EXTENSION = ".mp3";

  private static final Pattern TRACK_NUMBER_IN_THE_MIDDLE_PATTERN =
      Pattern.compile("\\D\\d{1,2}\\D");
  private static final Pattern TRACK_NUMBER_IN_FRONT_PATTERN = Pattern.compile("\\d{1,2}\\D");
  private static final Pattern RELEASE_YEAR_PATTERN = Pattern.compile("\\d{4}\\D");

  @Nonnull
  public static Collection<File> collectMp3Files(@Nonnull File baseDirectory, boolean recursive) {
    IOFileFilter dirFilter = (recursive ? TrueFileFilter.TRUE : null);
    return (Collection<File>)
        FileUtils.listFiles(
            baseDirectory,
            new WildcardFileFilter("*" + MP3_FILE_EXTENSION, IOCase.INSENSITIVE),
            dirFilter);
  }

  @Nullable
  public static String extractTrackNumber(@Nonnull File mp3File) {
    String mp3FileName = mp3File.getName().trim();
    String trackNumber = null;

    Matcher trackNumberInTheMiddleMatcher = TRACK_NUMBER_IN_THE_MIDDLE_PATTERN.matcher(mp3FileName);
    Matcher trackNumberInFrontMatcher =
        TRACK_NUMBER_IN_FRONT_PATTERN
            .matcher(mp3FileName)
            .region(0, Math.min(3, mp3FileName.length()));

    if (mp3FileName.length() > 0) {
      if (Character.isDigit(mp3FileName.charAt(0))) {
        if (trackNumberInFrontMatcher.find()) {
          trackNumber =
              mp3FileName.substring(
                  trackNumberInFrontMatcher.start(), trackNumberInFrontMatcher.end() - 1);
        }
      } else {
        if (trackNumberInTheMiddleMatcher.find()) {
          trackNumber =
              mp3FileName.substring(
                  trackNumberInTheMiddleMatcher.start() + 1,
                  trackNumberInTheMiddleMatcher.end() - 1);
        }
      }
    }

    return trackNumber;
  }

  @Nonnull
  public static String removeTrackNumberAndFileExtension(@Nonnull File mp3File) {
    String mp3FileName = mp3File.getName().trim();
    String mp3FileNameWithoutExtension =
        StringUtils.endsWithIgnoreCase(mp3FileName, MP3_FILE_EXTENSION)
            ? mp3FileName.substring(0, mp3FileName.length() - MP3_FILE_EXTENSION.length())
            : mp3FileName;
    String trackNumber = extractTrackNumber(mp3File);
    return trackNumber != null
        ? mp3FileNameWithoutExtension.replaceFirst(trackNumber, "")
        : mp3FileNameWithoutExtension;
  }

  @Nullable
  public static String extractReleaseYear(@Nonnull File albumDirectory) {
    String albumDirectoryName = albumDirectory.getName().trim();
    if (Character.isDigit(albumDirectoryName.charAt(0))) {
      Matcher releaseYearMatcher =
          RELEASE_YEAR_PATTERN
              .matcher(albumDirectoryName)
              .region(0, Math.min(5, albumDirectoryName.length()));
      if (releaseYearMatcher.find()) {
        return albumDirectoryName.substring(
            releaseYearMatcher.start(), releaseYearMatcher.end() - 1);
      }
    }
    return null;
  }

  @Nonnull
  public static String removeReleaseYear(@Nonnull File albumDirectory) {
    String albumDirectoryName = albumDirectory.getName().trim();
    String releaseYear = extractReleaseYear(albumDirectory);
    return releaseYear != null
        ? albumDirectoryName.replaceFirst(releaseYear, "").trim()
        : albumDirectoryName;
  }
}
