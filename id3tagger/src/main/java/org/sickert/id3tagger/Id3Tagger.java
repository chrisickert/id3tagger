package org.sickert.id3tagger;

import java.io.File;
import java.util.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.sickert.id3tagger.action.Action;
import org.sickert.id3tagger.event.*;
import org.sickert.id3tagger.event.EventListener;
import org.sickert.id3tagger.similarity.Similarity;
import org.sickert.id3tagger.songinfoservice.Artist;
import org.sickert.id3tagger.songinfoservice.Release;
import org.sickert.id3tagger.songinfoservice.Song;
import org.sickert.id3tagger.songinfoservice.SongInfoService;
import org.sickert.id3tagger.term.Terms;
import org.sickert.id3tagger.term.WordSequence;

/** @author Christian Sickert */
public class Id3Tagger {

  public static void traverseMp3FilesByArtistDirectoriesAndPerformAction(
      @Nonnull File artistBasePath, @Nonnull Action action, @Nonnull EventListener eventListener) {
    SongInfoService sis = ServiceLoader.load(SongInfoService.class);

    for (File artistDirectory : collectDirectSubdirectories(artistBasePath)) {

      // find the artist for the given artist directory
      FindArtistEvent findArtistEvent = findArtist(sis, artistDirectory.getName());
      eventListener.notify(findArtistEvent);
      Artist artist = findArtistEvent.getArtist();
      if (!findArtistEvent.wasSuccessful()) {
        continue;
      }

      // get all releases for the artist found
      FindReleasesEvent findReleasesEvent = findReleases(sis, artist);
      eventListener.notify(findReleasesEvent);
      List<Release> releases = findReleasesEvent.getReleases();
      if (!findReleasesEvent.wasSuccessful() || releases.size() == 0) {
        continue;
      }

      // associate release directories to releases
      for (File releaseDirectory : collectDirectSubdirectories(artistDirectory)) {
        Release release =
            findBestMatchInReleases(releases, releaseDirectory, Similarity.SIMILAR_TERMS_COUNT);
        eventListener.notify(
            new DetermineReleaseEvent(release != null, releaseDirectory.getName(), release));
        if (release == null) {
          continue;
        }

        // get all songs for the release found
        FindSongsEvent findSongsEvent = findSongs(sis, release);
        eventListener.notify(findSongsEvent);
        List<Song> songList = findSongsEvent.getSongs();
        if (!findSongsEvent.wasSuccessful() || songList.size() == 0) {
          continue;
        }

        // associate mp3 files to songs
        Collection<File> mp3Files = Mp3Files.collectMp3Files(releaseDirectory, true);
        List<Integer> unassignedTrackNumbers = createTrackNumberList(mp3Files.size());
        for (File mp3File : mp3Files) {
          Song song = findBestMatchInSongList(songList, mp3File, Similarity.SIMILAR_TERMS_COUNT);
          eventListener.notify(new DetermineSongEvent(song != null, mp3File, song));
          if (song == null) {
            continue;
          }

          // create ID3 tag and execute action
          Id3Tag id3Tag = new Id3Tag();
          id3Tag.setArtist(artist.getName());
          id3Tag.setAlbum(release.getName());
          id3Tag.setTitle(song.getTitle());
          id3Tag.setTrack(song.getTrackNumber());
          id3Tag.setYear(release.getReleaseYear());
          eventListener.notify(action.execute(mp3File, id3Tag));
          removeTrackNumberFromList(song.getTrackNumber(), unassignedTrackNumbers);
        }

        // verify that each track number has been assigned to detect possible mismatches
        if (!unassignedTrackNumbers.isEmpty()) {
          eventListener.notify(new MismatchDetectedEvent(releaseDirectory));
        }
      }
    }
  }

  // public static void traverseMp3FilesByArtistDirectoriesAndPerformActionOld(
  //     @Nonnull File artistBasePath, @Nonnull Action action, @Nonnull EventListener eventListener)
  // {
  //   SongInfoService service = ServiceLoader.load(SongInfoService.class);

  //   Collection<File> artistDirectories = collectDirectSubdirectories(artistBasePath);

  //   artistDirectories.stream()
  //       .forEach(
  //           artistDirectory -> {

  //             // find the artist for the given artist directory
  //             FindArtistEvent findArtistEvent = findArtist(service, artistDirectory.getName());
  //             eventListener.notify(findArtistEvent);
  //             Artist artist = findArtistEvent.getArtist();

  //             // find the releases for the given artist
  //             if (artist != null) {
  //               FindReleasesEvent findReleasesEvent = findReleases(service, artist);
  //               eventListener.notify(findReleasesEvent);
  //               List<Release> releases = findReleasesEvent.getReleases();

  //               // match album names, i.e. releases with album directories
  //               if (releases.size() > 0) {
  //                 Collection<File> albumDirectories =
  // collectDirectSubdirectories(artistDirectory);
  //                 albumDirectories.forEach(
  //                     albumDirectory -> {
  //                       Release album =
  //                           findBestMatchInReleases(
  //                               releases, albumDirectory, Similarity.SIMILAR_TERMS_COUNT);
  //                       eventListener.notify(
  //                           new DetermineReleaseEvent(
  //                               album != null, albumDirectory.getName(), album));

  //                       // find songs of an album
  //                       if (null != album) {
  //                         FindSongsEvent findSongsEvent = findSongs(service, album);
  //                         eventListener.notify(findSongsEvent);
  //                         List<Song> songList = findSongsEvent.getSongs();

  //                         // match song names, i.e. song list entries with
  //                         // MP3 file names
  //                         if (songList.size() > 0) {
  //                           Collection<File> mp3Files =
  //                               Mp3Files.collectMp3Files(albumDirectory, true);
  //                           List<Integer> unassignedTrackNumbers =
  //                               createTrackNumberList(mp3Files.size());

  //                           mp3Files.forEach(
  //                               mp3File -> {
  //                                 Song song =
  //                                     findBestMatchInSongList(
  //                                         songList, mp3File, Similarity.SIMILAR_TERMS_COUNT);
  //                                 eventListener.notify(
  //                                     new DetermineSongEvent(song != null, mp3File, song));

  //                                 // create ID3 tag and execute
  //                                 // action
  //                                 if (song != null) {
  //                                   Id3Tag id3Tag = new Id3Tag();
  //                                   id3Tag.setArtist(artist.getName());
  //                                   id3Tag.setAlbum(album.getName());
  //                                   id3Tag.setTitle(song.getTitle());
  //                                   id3Tag.setTrack(song.getTrackNumber());
  //                                   id3Tag.setYear(album.getReleaseYear());
  //                                   eventListener.notify(action.execute(mp3File, id3Tag));
  //                                   removeTrackNumberFromList(
  //                                       song.getTrackNumber(), unassignedTrackNumbers);
  //                                 }
  //                               });

  //                           // verify that each track number has been
  //                           // assigned to detect possible mismatches
  //                           if (!unassignedTrackNumbers.isEmpty()) {
  //                             eventListener.notify(new MismatchDetectedEvent(albumDirectory));
  //                           }
  //                         }
  //                       }
  //                     });
  //               }
  //             }
  //           });
  // }

  @Nonnull
  private static List<Integer> createTrackNumberList(int size) {
    List<Integer> trackNumberList = new ArrayList<>(size);
    for (int i = 1; i <= size; i++) {
      trackNumberList.add(i);
    }
    return trackNumberList;
  }

  private static void removeTrackNumberFromList(
      String trackNumber, @Nonnull List<Integer> trackNumberList) {
    try {
      trackNumberList.remove((Integer) Integer.parseInt(trackNumber));
    } catch (NumberFormatException e) {
      // ignore this silently
    }
  }

  @Nonnull
  protected static FindArtistEvent findArtist(
      @Nonnull SongInfoService service, @Nonnull String artistName) {
    try {
      Artist artist = service.findArtist(artistName);
      return new FindArtistEvent(artist != null, artistName, artist);
    } catch (Exception e) {
      return new FindArtistEvent(false, artistName, null);
    }
  }

  @Nonnull
  protected static FindReleasesEvent findReleases(
      @Nonnull SongInfoService service, @Nonnull Artist artist) {
    try {
      List<Release> discography = service.findReleases(artist);
      return new FindReleasesEvent(true, artist, discography);
    } catch (Exception e) {
      return new FindReleasesEvent(false, artist, Collections.<Release>emptyList());
    }
  }

  @Nonnull
  protected static FindSongsEvent findSongs(
      @Nonnull SongInfoService service, @Nonnull Release release) {
    try {
      List<Song> songs = service.findSongs(release);
      return new FindSongsEvent(true, release, songs);
    } catch (Exception e) {
      return new FindSongsEvent(false, release, Collections.<Song>emptyList());
    }
  }

  @Nonnull
  protected static Collection<File> collectDirectSubdirectories(@Nonnull File basePath) {
    File[] files =
        basePath.listFiles(
            file -> {
              return file.isDirectory();
            });
    return Arrays.asList(files);
  }

  @Nullable
  protected static Release findBestMatchInReleases(
      @Nonnull List<Release> releases,
      @Nonnull File albumDirectory,
      @Nonnull Similarity similarityMeasure) {
    String releaseYearFromAlbumDirectory = Mp3Files.extractReleaseYear(albumDirectory);
    String albumDirectoryWithoutReleaseYear = Mp3Files.removeReleaseYear(albumDirectory);
    WordSequence albumDirectoryWordSequence =
        new WordSequence(
            Terms.normalizeUmlautsAndAccents(
                Terms.toLowerCase(Terms.splitTextIntoWords(albumDirectoryWithoutReleaseYear))));

    // collect the list of albums with maximum similarity to the given album directory name -
    // there might be multiple top-scoring albums
    double maxSimilarity = 0d;
    List<Release> bestMatches = new LinkedList<>();
    for (Release release : releases) {
      double equalReleaseYearsReward =
          releaseYearFromAlbumDirectory != null
                  && releaseYearFromAlbumDirectory.equals(release.getReleaseYear())
              ? similarityMeasure.getEqualReleaseYearsReward()
              : 0d;
      WordSequence albumWordSequence =
          new WordSequence(
              Terms.normalizeUmlautsAndAccents(
                  Terms.toLowerCase(Terms.splitTextIntoWords(release.getName()))));
      double similarity =
          equalReleaseYearsReward
              + promoteAlbumsDemoteOthers(
                  similarityMeasure.similarity(albumWordSequence, albumDirectoryWordSequence),
                  release.getType());
      if (similarity > 0d && similarity == maxSimilarity) {
        bestMatches.add(release);
      } else if (similarity > maxSimilarity) {
        maxSimilarity = similarity;
        bestMatches.clear();
        bestMatches.add(release);
      }
    }

    // in case there are multiple albums with maximum similarity, chose the one with the lowest
    // amount of "noise" in it - i.e. the one with minimum length
    if (bestMatches.size() == 0) {
      return null;
    } else if (bestMatches.size() == 1) {
      return bestMatches.get(0);
    } else {
      return bestMatches.stream()
          .min(Comparator.comparing(album -> Terms.splitTextIntoWords(album.getName()).length))
          .get();
    }
  }

  private static double promoteAlbumsDemoteOthers(
      double unweightedSimilarity, Release.Type albumType) {
    switch (albumType) {
      case ALBUM:
        return unweightedSimilarity * 1.1;

      case OTHER:
        return unweightedSimilarity * 0.9;

      default:
        return unweightedSimilarity;
    }
  }

  @Nonnull
  protected static Song findBestMatchInSongList(
      @Nonnull List<Song> songList, @Nonnull File mp3File, @Nonnull Similarity similarityMeasure) {
    String trackNumberFromFileName = Mp3Files.extractTrackNumber(mp3File);
    trackNumberFromFileName =
        trackNumberFromFileName != null && trackNumberFromFileName.startsWith("0")
            ? trackNumberFromFileName.substring(1)
            : trackNumberFromFileName;
    String fileNameWithoutTrackNumberAndExtension =
        Mp3Files.removeTrackNumberAndFileExtension(mp3File);
    WordSequence mp3FileWordSequence =
        new WordSequence(
            Terms.normalizeUmlautsAndAccents(
                Terms.toLowerCase(
                    Terms.splitTextIntoWords(fileNameWithoutTrackNumberAndExtension))));

    // try to extract the artist name from the list of songs
    String artistName = null;
    if (songList.size() > 0) {
      Song firstSong = songList.get(0);
      if (null != firstSong.getAlbum() && null != firstSong.getAlbum().getArtist()) {
        artistName = firstSong.getAlbum().getArtist().getName();
      }
    }

    // some MP3 files contain the name of the artist within their file name, removing the artist
    // name improves accuracy
    if (artistName != null) {
      WordSequence artistWordSequence =
          new WordSequence(
              Terms.normalizeUmlautsAndAccents(
                  Terms.toLowerCase(Terms.splitTextIntoWords(artistName))));
      WordSequence mp3FileWordSequenceWithoutArtistPossiblyEmpty =
          mp3FileWordSequence.removeFirstOccurrenceOf(artistWordSequence);
      if (!mp3FileWordSequenceWithoutArtistPossiblyEmpty.isEmpty()) {
        mp3FileWordSequence = mp3FileWordSequenceWithoutArtistPossiblyEmpty;
      }
    }

    // collect the list of songs with maximum similarity to the given mp3 file name - there
    // might be multiple top-scoring songs
    double maxSimilarity = 0d;
    List<Song> bestMatches = new LinkedList<>();
    for (Song song : songList) {
      double equalTrackNumbersReward =
          trackNumberFromFileName != null && trackNumberFromFileName.equals(song.getTrackNumber())
              ? similarityMeasure.getEqualTrackNumbersReward()
              : 0d;
      WordSequence songWordSequence =
          new WordSequence(
              Terms.normalizeUmlautsAndAccents(
                  Terms.toLowerCase(Terms.splitTextIntoWords(song.getTitle()))));
      double similarity =
          equalTrackNumbersReward
              + similarityMeasure.similarity(songWordSequence, mp3FileWordSequence);
      if (similarity > 0d && similarity == maxSimilarity) {
        bestMatches.add(song);
      } else if (similarity > maxSimilarity) {
        maxSimilarity = similarity;
        bestMatches.clear();
        bestMatches.add(song);
      }
    }

    // in case there are multiple songs with maximum similarity, chose the one with the lowest
    // amount of "noise" in it - i.e. the one with minimum length
    if (bestMatches.size() == 0) {
      return null;
    } else if (bestMatches.size() == 1) {
      return bestMatches.get(0);
    } else {
      return bestMatches.stream()
          .min(Comparator.comparing(song -> Terms.splitTextIntoWords(song.getTitle()).length))
          .get();
    }
  }
}
