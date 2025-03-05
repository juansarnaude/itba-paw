package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Genre.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getAllGenres();
    List<Genre> getGenresForMedia(int mediaId);
    Genre getGenreById (int genreId);
}
