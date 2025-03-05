package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Director;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;

import java.util.List;
import java.util.Optional;

public interface MediaDao {
    List<Media> getMedia(int type, String search, String participant, List<Integer> genres, List<Integer> providers, List<String> status, List<String> lang, String orderBy, String sortOrder, int size, int pageNumber, int currentUserId);

    List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber);

    List<Movie> getMediaForDirectorId(int directorId, int currentId);

    Optional<Media> getMediaById(int mediaId);
    Optional<Movie> getMovieById(int mediaId);
    Optional<TVSerie> getTvById(int mediaId);
    int getMediaCount(int type, String search, String participant, List<Integer> genres, List<Integer> providers, List<String> status, List<String> lang);


    //get watchlist/watched status for user
    boolean getWatchlistStatus(int mediaId, int userId);
    boolean getWatchedStatus(int mediaId, int userId);

    // search creators
    int getDirectorsForQueryCount(String query, int size);
    List<Director> getDirectorsForQuery(String query, int size);
}
