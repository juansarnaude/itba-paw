package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Director;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class DirectorHibernateDao implements DirectorDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Director> getDirectorById(int directorId) {
        String sql = "SELECT new ar.edu.itba.paw.models.Cast.Director(d.directorId,d.director,(SELECT COUNT(m) FROM Movie m WHERE m.directorId = d.directorId)) FROM Movie d WHERE d.directorId = :directorId GROUP BY d.directorId,d.director";

        TypedQuery<Director> query = em.createQuery(sql, Director.class)
                .setParameter("directorId", directorId);

        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<Director> getDirectorsForQuery(String query, int size) {
        String sql = "SELECT new ar.edu.itba.paw.models.Cast.Director(d.directorId,d.director,(SELECT COUNT(m) FROM Movie m WHERE m.directorId = d.directorId)) FROM Movie d WHERE LOWER(d.director) LIKE LOWER(:query) GROUP BY d.directorId,d.director ORDER BY d.director DESC";

        TypedQuery<Director> q = em.createQuery(sql, Director.class)
                .setParameter("query", "%" + query + "%").setMaxResults(size);

        List<Director> toReturn = q.getResultList();

        return toReturn;
    }
}
