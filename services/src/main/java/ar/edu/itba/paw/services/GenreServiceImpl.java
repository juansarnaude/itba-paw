package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.ResourceNotFoundException;
import ar.edu.itba.paw.models.Genre.Genre;
import ar.edu.itba.paw.persistence.GenreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService{
    @Autowired
    private GenreDao genreDao;

    @Transactional(readOnly = true)
    @Override
    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Genre> getGenresForMedia(int mediaId) {
        return genreDao.getGenresForMedia(mediaId);
    }

    @Transactional(readOnly = true)
    @Override
    public Genre getGenreById (int genreId) {
        Genre genre = genreDao.getGenreById(genreId);
        if(genre == null){
            throw new ResourceNotFoundException("Genre with id " + genreId + " not found");
        }
        return genre;
    }
}
