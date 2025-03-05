package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Cast.Director;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;

import java.util.List;

public interface MediaService {

    // Returns a list of media that satisfy the conditions
    // The participant checks in actor OR director OR creator
    // Search is ny name
    // Provider and genres are OR in the list
    List<Media> getMedia(int type, String search, String participant, List<Integer> genres, List<Integer> providers, List<String> status, List<String> lang, String orderBy, String sortOrder, int size, int pageNumber);

    //The amount of results that a query of getMedia will give
    int getMediaCount(int type, String search, String participant, List<Integer> genres, List<Integer> providers, List<String> status, List<String> lang);

    //Return a list of media that are in a moovie list
    List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber);

    //Returns a list of all media for a director
    List<Movie> getMediaForDirectorId(int directorId);


    //get watchlist/watched status for user
    boolean getWatchlistStatus(int mediaId, int userId);
    boolean getWatchedStatus(int mediaId, int userId);

    //Get the Tv or Movie details data
    Media getMediaById(int mediaId);
    Movie getMovieById(int mediaId);
    TVSerie getTvById(int mediaId);

    // search creators
    int getDirectorsForQueryCount(String query, int size);
    List<Director> getDirectorsForQuery(String query, int size);
    }