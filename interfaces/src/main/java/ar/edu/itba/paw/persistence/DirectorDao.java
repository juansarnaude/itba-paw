package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorDao {
    Optional<Director> getDirectorById(int directorId);
    List<Director> getDirectorsForQuery(String query, int size);
}
