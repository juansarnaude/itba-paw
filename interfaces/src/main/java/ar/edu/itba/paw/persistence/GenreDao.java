package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Genre.Genre;

import java.util.List;


public interface GenreDao {
    List<Genre> getAllGenres();
    List<Genre> getGenresForMedia(int mediaId);
    Genre getGenreById (int genreId);
}
