package org.sickert.id3tagger.songinfoservice.musicbrainz;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.collect.ImmutableMap;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.time.DateUtils;
import org.sickert.id3tagger.songinfoservice.Song;
import org.sickert.id3tagger.songinfoservice.SongInfoService;

public class MusicBrainzService implements SongInfoService {

  private static final String SERVICE_URL = "http://musicbrainz.org/ws/2";

  // Custom user-agent string to comply with MusicBrainz' rules
  private static final String USER_AGENT =
      "org.sickert.id3tagger (https://github.com/chrisickert/id3tagger)";
  // TODO: Make this more flexible and add version number

  private static final String ARTIST_SERVICE = SERVICE_URL + "/artist";

  private static final String RELEASE_GROUP_SERVICE = SERVICE_URL + "/release-group";

  private static final String RELEASE_SERVICE = SERVICE_URL + "/release";

  private static final int PAGE_SIZE = 100;

  private static final int RETRY_ATTEMPTS = 3;

  private static final long INITIAL_RETRY_DELAY_MILLIS = 5000;

  private static final int MAX_ARTISTS_TO_CONSIDER = 50;

  private Client client;

  public MusicBrainzService() {
    client = ClientBuilder.newBuilder().register(JacksonJsonProvider.class).build();
  }

  @Override
  @Nullable
  public org.sickert.id3tagger.songinfoservice.Artist findArtist(@Nonnull String name) {
    String query = MessageFormat.format("\"{0}\"", name);
    Map<String, String> queryParams = ImmutableMap.of("query", query);
    ArtistList artistList =
        query(ARTIST_SERVICE, queryParams, ArtistList.class, MAX_ARTISTS_TO_CONSIDER);

    if (artistList == null || !artistList.hasEntities()) {
      return null;
    }

    // Sometimes there is more than one artist with the given name. Collect and use all of them in
    // subsequent queries.
    Artist firstArtist = artistList.getFirstEntity();
    List<String> artistIds = new ArrayList<>(1);
    artistList
        .getEntities()
        .forEach(
            artist -> {
              if (firstArtist.getName().equals(artist.getName())) {
                artistIds.add(artist.getId());
              }
            });
    return new org.sickert.id3tagger.songinfoservice.Artist(
        new ArtistId(artistIds), firstArtist.getName());
  }

  @Override
  @Nonnull
  public List<org.sickert.id3tagger.songinfoservice.Release> findReleases(
      @Nonnull org.sickert.id3tagger.songinfoservice.Artist artist) {
    if (artist.getId() == null || !(artist.getId() instanceof ArtistId)) {
      throw new IllegalArgumentException(
          "Unsupported artist ID! The given artist either has an ID of another"
              + " SongInfoService implementation (i.e. not MusicBrainz) or no ID at"
              + " all.");
    }

    List<String> artistIds = ((ArtistId) artist.getId()).getIds();
    List<org.sickert.id3tagger.songinfoservice.Release> releases = new ArrayList<>();
    for (String artistId : artistIds) {
      Map<String, String> queryParams = ImmutableMap.of("artist", artistId);
      ReleaseGroupList releaseGroupList =
          query(RELEASE_GROUP_SERVICE, queryParams, ReleaseGroupList.class);
      if (releaseGroupList != null && releaseGroupList.hasEntities()) {
        releaseGroupList
            .getEntities()
            .forEach(releaseGroup -> releases.add(toRelease(releaseGroup, artist)));
      }
    }
    return releases;
  }

  @Override
  @Nonnull
  public List<Song> findSongs(@Nonnull org.sickert.id3tagger.songinfoservice.Release release) {
    if (release.getId() == null || !(release.getId() instanceof AlbumId)) {
      throw new IllegalArgumentException(
          "Unsupported album ID! The given album either has an ID of another"
              + " SongInfoService implementation (i.e. not MusicBrainz) or no ID at"
              + " all.");
    }

    AlbumId albumId = (AlbumId) release.getId();
    Map<String, String> queryParams =
        ImmutableMap.of("release-group", albumId.getId(), "inc", "recordings");
    ReleaseList releaseList = query(RELEASE_SERVICE, queryParams, ReleaseList.class);

    if (releaseList == null || !releaseList.hasEntities()) {
      return Collections.<Song>emptyList();
    }

    // normalize track numbers (e.g. A1 gets 1, A2 gets 2, and so on, but 1 stays 1, 2 stays 2,
    // ... )
    List<Medium> allMedia =
        releaseList.getEntities().stream()
            .map(Release::getMedia)
            .flatMap(media -> media.stream())
            .collect(Collectors.toList());
    allMedia.forEach(medium -> medium.renumberTracks());

    // collect all tracks
    Stream<Track> allTracks =
        allMedia.stream().map(Medium::getTracks).flatMap(tracks -> tracks.stream()).distinct();

    // convert to Song instances
    List<Song> songList = new ArrayList<>();
    allTracks.forEach(track -> songList.add(trackToSong(track, release)));
    return songList;
  }

  @Nullable
  private <T extends EntityList<?>> T query(
      @Nonnull String serviceUrl,
      @Nonnull Map<String, String> queryParams,
      @Nonnull Class<T> entityListType) {
    return query(serviceUrl, queryParams, entityListType, Integer.MAX_VALUE);
  }

  @Nullable
  private <T extends EntityList<?>> T query(
      @Nonnull String serviceUrl,
      @Nonnull Map<String, String> queryParams,
      @Nonnull Class<T> entityListType,
      int maxResults) {
    T completeEntityList = null;

    for (int page = 0; true; page++) {

      // prepare query
      WebTarget target = client.target(serviceUrl);
      for (Map.Entry<String, String> queryParam : queryParams.entrySet()) {
        target = target.queryParam(queryParam.getKey(), queryParam.getValue());
      }
      target =
          target
              .queryParam("fmt", "json")
              .queryParam("limit", Integer.toString(Math.min(PAGE_SIZE, maxResults)))
              .queryParam("offset", Integer.toString(page * PAGE_SIZE));

      // execute query
      Response response = null;
      long retryDelayMillis = INITIAL_RETRY_DELAY_MILLIS;
      for (int attemptCount = 0; attemptCount <= RETRY_ATTEMPTS; attemptCount++) {
        response = target.request().header(HttpHeaders.USER_AGENT, USER_AGENT).get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
          break;
        }
        this.sleep(retryDelayMillis);
        retryDelayMillis = 2 * retryDelayMillis;
      }
      if (response.getStatus() != Response.Status.OK.getStatusCode()) {
        // TODO: Provide a hint that the entity list is incomplete
        return completeEntityList; // incomplete actually ...
      }

      // read entity list and merge with previous one, if any
      T entityList = response.readEntity(entityListType);
      if (null == completeEntityList) {
        completeEntityList = entityList;
      } else {
        completeEntityList.addEntities(entityList.getEntities());
      }

      // decide whether further requests are necessary to read all entities and shrink result list,
      // if necessary
      int numOfAllEntities = Integer.parseInt(completeEntityList.getAllEntitiesCount());
      if (completeEntityList.size() > maxResults) {
        completeEntityList.shrink(maxResults);
        break;
      } else if (completeEntityList.size() >= numOfAllEntities) {
        break;
      }
    }

    return completeEntityList;
  }

  private void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      // silently ignore this
    }
  }

  @Nonnull
  private org.sickert.id3tagger.songinfoservice.Release toRelease(
      @Nonnull ReleaseGroup releaseGroup,
      @Nonnull org.sickert.id3tagger.songinfoservice.Artist artist) {
    org.sickert.id3tagger.songinfoservice.Release.Type releaseType;
    if (releaseGroup.getType() != null) {
      switch (releaseGroup.getType()) {
        case "Album":
          releaseType = org.sickert.id3tagger.songinfoservice.Release.Type.ALBUM;
          break;

        case "Single":
          releaseType = org.sickert.id3tagger.songinfoservice.Release.Type.SINGLE;
          break;

        default:
          releaseType = org.sickert.id3tagger.songinfoservice.Release.Type.OTHER;
      }
    } else {
      releaseType = org.sickert.id3tagger.songinfoservice.Release.Type.OTHER;
    }

    String releaseYear = null;
    if (null != releaseGroup.getReleaseDate()) {
      try {
        Date firstReleaseDate =
            DateUtils.parseDate(releaseGroup.getReleaseDate(), "yyyy-MM-dd", "yyyy");
        releaseYear = Integer.toString(DateUtils.toCalendar(firstReleaseDate).get(Calendar.YEAR));
      } catch (ParseException e) {
        releaseYear = null;
      }
    }

    return new org.sickert.id3tagger.songinfoservice.Release(
        new AlbumId(releaseGroup.getId()),
        artist,
        releaseType,
        releaseGroup.getTitle(),
        releaseYear);
  }

  @Nonnull
  private Song trackToSong(
      @Nonnull Track track, @Nonnull org.sickert.id3tagger.songinfoservice.Release release) {
    Song song = new Song();
    song.setAlbum(release);
    song.setTrackNumber(track.getNumber());
    song.setTitle(track.getTitle());
    return song;
  }
}
