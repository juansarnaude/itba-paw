package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Actor;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.TV.TVCreators;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class TvCreatorsHibernateDao implements TVCreatorsDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<TVCreators> getTvCreatorsByMediaId(int mediaId) {
        return em.createQuery("SELECT c FROM TVCreators c JOIN c.medias m WHERE m.mediaId = :mediaId", TVCreators.class)
                .setParameter("mediaId", mediaId)
                .getResultList();
    }


    @Override
    public Optional<TVCreators> getTvCreatorById(int creatorId) {
        final TypedQuery<TVCreators> query = em.createQuery("FROM TVCreators WHERE creatorId = :creatorId ", TVCreators.class).setParameter("creatorId", creatorId);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<Media> getMediasForTVCreator(int creatorId, int currentUser) {
        String sql = "SELECT new ar.edu.itba.paw.models.Media.Media(m,"+
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieList.moovieListId WHERE m.mediaId = mlc2.mediaId AND wl.name = 'Watched' AND wl.userId = :userid), " +
                "(SELECT CASE WHEN COUNT(wl3) > 0 THEN true ELSE false END FROM MoovieList wl3 INNER JOIN MoovieListContent mlc3 ON wl3.moovieListId = mlc3.moovieList.moovieListId WHERE m.mediaId = mlc3.mediaId AND wl3.name = 'Watchlist' AND wl3.userId = :userid) ) " +
                "FROM Media m JOIN MediaCreators mc ON m.mediaId = mc.media.mediaId WHERE mc.tvCreators.creatorId = :creatorId" +
                " ORDER BY m.tmdbRating DESC";

        TypedQuery<Media> q = em.createQuery(sql, Media.class)
                .setParameter("creatorId", creatorId)
                .setParameter("userid", currentUser);

        return q.getResultList();
    }


    @Override
    public List<TVCreators> getTVCreatorsForQuery(String query, int size) {
        String sql = "SELECT c FROM TVCreators c WHERE LOWER(c.creatorName) LIKE :query ORDER BY c.medias.size DESC";


        TypedQuery<TVCreators> q = em.createQuery(sql, TVCreators.class)
                .setParameter("query","%"+query+"%").setMaxResults(size)
                ;

        List<TVCreators> toReturn = q.getResultList();

        return toReturn;
    }
}
