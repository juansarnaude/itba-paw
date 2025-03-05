package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Cast.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorService {
    Director getDirectorById(int directorId);
    List<Director> getDirectorsForQuery(String query, int size);
}
