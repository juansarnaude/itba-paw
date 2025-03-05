package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Cast.Director;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.Media.Movie;
import ar.edu.itba.paw.models.Media.TVSerie;
import ar.edu.itba.paw.models.MoovieList.MoovieListContent;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class MediaHibernateDao implements MediaDao{

    @PersistenceContext
    private EntityManager em;

    private static final String moviesQueryParams = " media.mediaId, type, name, originalLanguage, adult, releaseDate, overview, backdropPath, posterPath, trailerLink, tmdbRating, status, runtime, budget, revenue, directorId, director ";

    @Override
    public List<Media> getMedia(int type, String search, String participant, List<Integer> genres, List<Integer> providers, List<String> status, List<String> lang, String orderBy, String sortOrder, int size, int pageNumber, int currentUserId) {

        ArrayList<String> argtype = new ArrayList<>();
        ArrayList<Object> args = new ArrayList<>();

        String sql = "SELECT new ar.edu.itba.paw.models.Media.Media ( m , " +
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieList.moovieListId WHERE m.mediaId = mlc2.mediaId AND wl.name = 'Watched' AND wl.userId = :userid), " +
                "(SELECT CASE WHEN COUNT(wl3) > 0 THEN true ELSE false END FROM MoovieList wl3 INNER JOIN MoovieListContent mlc3 ON wl3.moovieListId = mlc3.moovieList.moovieListId WHERE m.mediaId = mlc3.mediaId AND wl3.name = 'Watchlist' AND wl3.userId = :userid) ) ";

        argtype.add("userid");
        args.add(currentUserId);

        sql += "FROM Media m ";

        if (type == 0 || type == 1) {
            sql += " WHERE m.type = :type ";
            argtype.add("type");
            args.add(type == 1);
        } else {
            sql += " WHERE m.type IS NOT NULL ";
        }

        if (status != null && !status.isEmpty()){
            sql += " AND m.status IN (:status) ";
            argtype.add("status");
            args.add(status);
        }

        if (lang != null && !lang.isEmpty()){
            sql += " AND m.originalLanguage IN (:lang) ";
            argtype.add("lang");
            args.add(lang);
        }

        if (genres != null && !genres.isEmpty()) {
            sql += " AND m IN (SELECT media FROM Media media JOIN media.genres genre WHERE genre.genreId IN :genres) ";
            argtype.add("genres");
            args.add(genres);
        }

        if (providers != null && !providers.isEmpty()) {
            sql += " AND m IN (SELECT media FROM Media media JOIN media.providers providers WHERE providers.providerId IN :providers) ";
            argtype.add("providers");
            args.add(providers);
        }

        if (search != null && search.length() > 0) {
            sql += " AND LOWER(m.name) LIKE :name ";
            argtype.add("name");
            args.add('%' + search.toLowerCase() + '%');
        }

        if (participant != null && participant.length() > 0) {
            sql += " AND ( m.mediaId IN (SELECT media.mediaId FROM Actor a JOIN a.medias media WHERE LOWER(actorname) LIKE :actor ) ";
            args.add('%' + participant.toLowerCase() + '%');
            argtype.add("actor");

            if (type != MediaTypes.TYPE_TVSERIE.getType()) {
                sql += " OR m.mediaId IN (SELECT mediaId FROM Movie m WHERE LOWER(director) LIKE :director ) ";
                args.add('%' + participant.toLowerCase() + '%');
                argtype.add("director");
            }

            if (type != MediaTypes.TYPE_MOVIE.getType()) {
                sql += " OR m.mediaId IN (SELECT m.mediaId FROM TVCreators c JOIN c.medias WHERE LOWER(creatorname) LIKE :creator ) ";
                args.add('%' + participant.toLowerCase() + '%');
                argtype.add("creator");
            }

            sql += " ) ";
        }

        if (orderBy!=null && sortOrder!=null) {
            sql += " ORDER BY " + orderBy + " " + sortOrder;
        }


        TypedQuery<Media> query = em.createQuery(sql, Media.class);

        for(int i=0; i<args.size() ; i++){
            query.setParameter(argtype.get(i), args.get(i));
        }


        return query.setFirstResult(pageNumber * size).setMaxResults(size).getResultList();
    }

    @Override
    public List<Media> getMediaInMoovieList(int moovieListId, int size, int pageNumber) {

        return em.createQuery("SELECT m FROM MoovieListContent mlcE INNER JOIN Media m ON mlcE.mediaId = m.mediaId " +
                        "WHERE mlcE.moovieListId = :moovieListId", Media.class)
                .setParameter("moovieListId", moovieListId)
                .setFirstResult(size*pageNumber).setMaxResults(size)
                .getResultList();
    }

    @Override
    public List<Movie> getMediaForDirectorId(int directorId, int currentUser) {
//
        String sql = "SELECT new ar.edu.itba.paw.models.Media.Movie(m,"+
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieList.moovieListId WHERE m.mediaId = mlc2.mediaId AND wl.name = 'Watched' AND wl.userId = :userid), " +
                "(SELECT CASE WHEN COUNT(wl3) > 0 THEN true ELSE false END FROM MoovieList wl3 INNER JOIN MoovieListContent mlc3 ON wl3.moovieListId = mlc3.moovieList.moovieListId WHERE m.mediaId = mlc3.mediaId AND wl3.name = 'Watchlist' AND wl3.userId = :userid) ) "+
                "FROM Movie m WHERE directorId = :directorId " +
                "ORDER BY m.tmdbRating DESC";
        final TypedQuery<Movie> query = em.createQuery(sql, Movie.class).setParameter("directorId", directorId).setParameter("userid", currentUser);
        return query.getResultList();
    }

    @Override
    public Optional<Media> getMediaById(int mediaId) {
        return Optional.ofNullable(em.find(Media.class, mediaId));
    }

    @Override
    public Optional<Movie> getMovieById(int mediaId) {
        final Movie aux = em.createQuery("SELECT m FROM Movie m WHERE m.mediaId = :mediaId", Movie.class)
                .setParameter("mediaId",mediaId)
                .getSingleResult();
        return Optional.ofNullable(aux);
    }

    @Override
    public Optional<TVSerie> getTvById(int mediaId) {
        final TVSerie aux = em.createQuery("SELECT tv FROM TVSerie tv WHERE tv.mediaId = :mediaId", TVSerie.class)
                .setParameter("mediaId",mediaId)
                .getSingleResult();
        return Optional.ofNullable(aux);
    }

    @Override
    public int getMediaCount(int type, String search, String participant, List<Integer> genres, List<Integer> providers, List<String> status, List<String> lang){
//        (int type, String search, String participant, List<String> genres, List<String> providers, List<String> status, List<String> lang, String orderBy, String sortOrder, int size, int pageNumber, int currentUserId)
        ArrayList<String> argtype = new ArrayList<>();
        ArrayList<Object> args = new ArrayList<>();

        String sql = "SELECT COUNT(m) FROM Media m ";

        if (type == 0 || type == 1) {
            sql += " WHERE m.type = :type ";
            argtype.add("type");
            args.add(type == 1);
        } else {
            sql += " WHERE m.type IS NOT NULL ";
        }

        if (status != null && !status.isEmpty()){
            sql += " AND m.status IN (:status) ";
            argtype.add("status");
            args.add(status);
        }

        if (lang != null && !lang.isEmpty()){
            sql += " AND m.originalLanguage IN (:lang) ";
            argtype.add("lang");
            args.add(lang);
        }

        if (genres != null && !genres.isEmpty()) {
            sql += " AND m IN (SELECT media FROM Media media JOIN media.genres genre WHERE genre.genreId IN :genres) ";
            argtype.add("genres");
            args.add(genres);
        }

        if (providers != null && !providers.isEmpty()) {
            sql += " AND m IN (SELECT media FROM Media media JOIN media.providers providers WHERE providers.providerId IN :providers) ";
            argtype.add("providers");
            args.add(providers);
        }

        if (search != null && search.length() > 0) {
            sql += " AND LOWER(m.name) LIKE :name ";
            argtype.add("name");
            args.add('%' + search.toLowerCase() + '%');
        }

        if (participant != null && participant.length() > 0) {
            sql += " AND ( m.mediaId IN (SELECT media.mediaId FROM Actor a JOIN a.medias media WHERE LOWER(actorname) LIKE :actor ) ";
            args.add('%' + participant.toLowerCase() + '%');
            argtype.add("actor");

            if (type != MediaTypes.TYPE_TVSERIE.getType()) {
                sql += " OR m.mediaId IN (SELECT mediaId FROM Movie m WHERE LOWER(director) LIKE :director ) ";
                args.add('%' + participant.toLowerCase() + '%');
                argtype.add("director");
            }

            if (type != MediaTypes.TYPE_MOVIE.getType()) {
                sql += " OR m.mediaId IN (SELECT m.mediaId FROM TVCreators c JOIN c.medias WHERE LOWER(creatorname) LIKE :creator ) ";
                args.add('%' + participant.toLowerCase() + '%');
                argtype.add("creator");
            }

            sql += " ) ";

        }

        Query query = em.createQuery(sql);

        for (int i = 0; i < args.size(); i++) {
            query.setParameter(argtype.get(i), args.get(i));
        }

        Long toReturn = (Long) query.getSingleResult();
        return toReturn.intValue();
    }

    @Override
    public boolean getWatchlistStatus(int mediaId, int userId) {
        String sql = "SELECT CASE WHEN COUNT(wl3) > 0 THEN true ELSE false END FROM MoovieList wl3 INNER JOIN MoovieListContent mlc3 ON wl3.moovieListId = mlc3.moovieList.moovieListId WHERE :mediaId = mlc3.mediaId AND wl3.name = 'Watchlist' AND wl3.userId = :userid ";

        return (boolean) em.createQuery(sql)
                .setParameter("userid",userId).setParameter("mediaId",mediaId)
                .getSingleResult();
    }

    @Override
    public boolean getWatchedStatus(int mediaId, int userId) {
        String sql = "SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieList.moovieListId WHERE :mediaId = mlc2.mediaId AND wl.name = 'Watched' AND wl.userId = :userid";

        return (boolean) em.createQuery(sql)
                .setParameter("userid",userId).setParameter("mediaId",mediaId)
                .getSingleResult();
    }

    @Override
    public int getDirectorsForQueryCount(String query, int size) {
        return getDirectorsForQuery(query, size).size();
    }

    @Override
    public List<Director> getDirectorsForQuery(String query, int size) {
        String sql = "SELECT m.directorId, m.director, COUNT(m.mediaId) as mediaCount " +
                "FROM Movie m WHERE m.director LIKE :query " +
                "GROUP BY m.directorId, m.director " +
                "ORDER BY mediaCount DESC";

        TypedQuery<Object[]> q = em.createQuery(sql, Object[].class)
                .setParameter("query", "%" + query + "%");

        List<Object[]> results = q.setMaxResults(size).getResultList();
        List<Director> directors = new ArrayList<>();
        for (Object[] result : results) {
            Long mediaCount = (Long) result[2];
            directors.add(new Director((int)result[0], (String) result[1], mediaCount.intValue()));
        }

        return directors;
    }




}
