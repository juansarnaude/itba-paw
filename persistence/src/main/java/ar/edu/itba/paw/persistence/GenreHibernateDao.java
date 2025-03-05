package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Genre.Genre;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Primary
@Repository
public class GenreHibernateDao implements GenreDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Genre> getAllGenres() {
        return em.createQuery("SELECT g FROM Genre g JOIN g.medias GROUP BY g.genreId, g.genreName ORDER BY g.genreId ASC", Genre.class)
                .getResultList();
    }

    @Override
    public List<Genre> getGenresForMedia(int mediaId) {
        return em.createQuery(
                        "SELECT g FROM Genre g JOIN g.medias m WHERE m.id = :mediaId GROUP BY g.genreId, g.genreName", Genre.class)
                .setParameter("mediaId", mediaId)
                .getResultList();
    }

    @Override
    public Genre getGenreById(int genreId) {
        List<Genre> results = em.createQuery(
                        "SELECT g FROM Genre g WHERE g.genreId = :genreId", Genre.class
                )
                .setParameter("genreId", genreId)
                .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

}
