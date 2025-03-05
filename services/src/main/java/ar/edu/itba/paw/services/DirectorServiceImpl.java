package ar.edu.itba.paw.services;

import ar.edu.itba.paw.exceptions.DirectorNotFoundException;
import ar.edu.itba.paw.models.Cast.Director;
import ar.edu.itba.paw.persistence.DirectorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorServiceImpl implements DirectorService{
    @Autowired
    private DirectorDao directorDao;

    @Transactional(readOnly = true)
    @Override
    public Director getDirectorById(int directorId) {
        return directorDao.getDirectorById(directorId).orElseThrow(() -> new DirectorNotFoundException("Director was not found for the id: " + directorId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Director> getDirectorsForQuery(String query, int size) {
        return directorDao.getDirectorsForQuery(query, size);
    }
}
