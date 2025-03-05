package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.Media.Media;
import ar.edu.itba.paw.models.Media.MediaTypes;
import ar.edu.itba.paw.models.MoovieList.*;
import ar.edu.itba.paw.models.PagingSizes;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.User.User;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.PreparedStatement;
import java.util.*;

@Primary
@Repository
public class MoovieListHibernateDao implements MoovieListDao{

    @PersistenceContext
    private EntityManager em;

    private static final Logger LOGGER = LoggerFactory.getLogger(MoovieListHibernateDao.class);




    @Override
    public Optional<MoovieList> getMoovieListById(int moovieListId) {
        return Optional.ofNullable(em.find(MoovieList.class, moovieListId));
    }

    @Override
    public MoovieListCard getMoovieListCardById(int moovieListId, int currentUserId) {


        MoovieListCard mlc = Optional.ofNullable(em.find(MoovieListCard.class, moovieListId)).orElseThrow( () -> new MoovieListNotFoundException("Moovie list by id: " + moovieListId + " not found") );

        String sqlQuery = "SELECT " +
                " (SELECT COUNT(*) FROM moovielistsContent mlc " +
                " INNER JOIN moovielistscontent mlc2 ON mlc.mediaid = mlc2.mediaid " +
                " JOIN moovieLists ml ON mlc2.moovieListId = ml.moovieListId " +
                " WHERE mlc.moovieListId = :moovieListId AND ml.name = 'Watched' AND ml.userId = :userId) as currentUserWatchAmount, " +
                " (CASE WHEN EXISTS (SELECT 1 FROM moovielistslikes mll WHERE mll.moovieListId = :moovieListId AND mll.userId = :userId) THEN true ELSE false END ) as currentUserHasLiked, " +
                " (CASE WHEN EXISTS (SELECT 1 FROM moovielistsfollows mlf WHERE mlf.moovieListId = :moovieListId AND mlf.userId = :userId) THEN true ELSE false END ) as currentUserHasFollowed";

        Query query = em.createNativeQuery(sqlQuery);
        query.setParameter("moovieListId", moovieListId);
        query.setParameter("userId", currentUserId);

        query.unwrap(SQLQuery.class);

        Object[] obj = (Object[]) query.getSingleResult();

        //Fill the current user status
        mlc.setUserStatus(((Number)obj[0]).intValue(), (boolean)obj[1], (boolean)obj[2]);

        return mlc;
    }

    @Override
    public List<MoovieListCard> getLikedMoovieListCards(int userId, int type, int size, int pageNumber, int currentUserId) {
        String jpql = "SELECT mlc, " +
                "(SELECT COUNT(mlc2) FROM MoovieListContent mlc2 INNER JOIN MoovieList ml ON mlc2.moovieList.moovieListId = ml.moovieListId WHERE mlc2.moovieList.moovieListId = mlc.id AND ml.name = 'Watched' AND ml.userId = :currentUserId), " +
                "(SELECT COUNT(mll2) > 0 FROM MoovieListLikes mll2 WHERE mll2.moovieList.id = mlc.id), " + // ACA BORRE LOS  AND mlf.user.id = :userId  me parece que no es por ahi
                "(SELECT COUNT(mlf2) > 0 FROM MoovieListFollowers mlf2 WHERE mlf2.moovieList.id = mlc.id) " +
                "FROM MoovieListCard mlc JOIN MoovieListLikes mll " +
                "ON mlc.moovieListId = mll.moovieList.moovieListId " +
                "WHERE mll.user.userId = :userId";

        List<Object[]> results = em.createQuery(jpql)
                .setParameter("userId", userId).setParameter("currentUserId",currentUserId)
                .getResultList();

        List<MoovieListCard> cards = new ArrayList<>();

        //Fill the current user status
        for (Object[] result : results) {
            MoovieListCard mlc = (MoovieListCard) result[0];

            mlc.setUserStatus(((Long) result[1]).intValue(),(boolean) result[2], (boolean) result[3] );

            cards.add(mlc);
        }

        return cards;

    }


    @Override
    public List<MoovieListCard> getFollowedMoovieListCards(int userId, int type, int size, int pageNumber, int currentUserId) {
        String jpql = "SELECT mlc, " +
                "(SELECT COUNT(mlc2) FROM MoovieListContent mlc2 INNER JOIN MoovieList ml ON mlc2.moovieList.moovieListId = ml.moovieListId WHERE mlc2.moovieList.moovieListId = mlc.id AND ml.name = 'Watched' AND ml.userId = :currentUserId), " +
                "(SELECT COUNT(mll2) > 0 FROM MoovieListLikes mll2 WHERE mll2.moovieList.id = mlc.id), " + // ACA BORRE LOS  AND mlf.user.id = :userId  me parece que no es por ahi
                "(SELECT COUNT(mlf2) > 0 FROM MoovieListFollowers mlf2 WHERE mlf2.moovieList.id = mlc.id) " +
                "FROM MoovieListCard mlc JOIN MoovieListFollowers mll " +
                "ON mlc.moovieListId = mll.moovieList.moovieListId " +
                "WHERE mll.user.userId = :userId";

        List<Object[]> results = em.createQuery(jpql)
                .setParameter("userId", userId).setParameter("currentUserId",currentUserId)
                .setFirstResult(size*pageNumber).setMaxResults(size).getResultList();

        //Set the current user status
        List<MoovieListCard> cards = new ArrayList<>();
        for (Object[] result : results) {
            MoovieListCard mlc = (MoovieListCard) result[0];
            mlc.setUserStatus(((Long) result[1]).intValue(), (boolean) result[2], (boolean) result[3] );
            cards.add(mlc);
        }

        return cards;
    }

    @Override
    public List<User> getMoovieListFollowers(int moovieListId) {
        return em.createQuery("SELECT mlf.user " +
                        "FROM MoovieListFollowers mlf " +
                        "WHERE mlf.moovieList.moovieListId = :moovieListId", User.class)
                .setParameter("moovieListId", moovieListId)
                .getResultList();
    }

    @Override
    public int getFollowedMoovieListCardsCount(int userId, int type) {
        String jpql = "SELECT COUNT(mlc) " +
                "FROM MoovieListCard mlc JOIN MoovieListFollowers mlf " +
                "ON mlc.moovieListId = mlf.moovieList.moovieListId " +
                "WHERE mlf.user.userId = :userId AND type = :type";

        Number result = (Number) em.createQuery(jpql)
                .setParameter("userId", userId)
                .setParameter("type", type)
                .getSingleResult();

        return result.intValue();
    }

    @Override
    public List<MoovieListCard> getRecommendedMoovieListCards(int moovieListId, int size, int pageNumber, int currentUserId) {

        String jpql = "SELECT mlc, " +
                "(SELECT COUNT(mlc2) FROM MoovieListContent mlc2 INNER JOIN MoovieList ml ON mlc2.moovieList.moovieListId = ml.moovieListId WHERE mlc2.moovieList.moovieListId = mlc.id AND ml.name = 'Watched' AND ml.userId = :currentUserId), " +
                "(SELECT COUNT(mll2) > 0 FROM MoovieListLikes mll2 WHERE mll2.moovieList.id = mlc.id), " +
                "(SELECT COUNT(mlf2) > 0 FROM MoovieListFollowers mlf2 WHERE mlf2.moovieList.id = mlc.id), " +
                "COUNT(l) AS totallikes" +
                " FROM MoovieListCard mlc LEFT JOIN User u ON mlc.user.userId = u.userId LEFT JOIN MoovieListLikes l ON mlc.moovieListId = l.moovieList.moovieListId " +
                " LEFT JOIN MoovieListFollowers mlf ON mlf.moovieList.moovieListId = mlc.moovieListId " +
                " WHERE type = 1  AND l.user.userId IN (SELECT user.userId FROM MoovieListLikes WHERE moovieList.moovieListId = :moovieListId) AND mlc.moovieListId <> :moovieListId " +
                " GROUP BY mlc.moovieListId, u.userId ORDER BY totallikes ";


        List<Object[]> results = em.createQuery(jpql)
                .setParameter("moovieListId", moovieListId).setParameter("currentUserId",currentUserId)
                .setFirstResult(pageNumber * size).setMaxResults(size).getResultList();

        //Set the current user status
        List<MoovieListCard> cards = new ArrayList<>();
        for (Object[] result : results) {
            MoovieListCard mlc = (MoovieListCard) result[0];
            mlc.setUserStatus(((Long) result[1]).intValue(), (boolean) result[2], (boolean) result[3] );
            cards.add(mlc);
        }

        return cards;
    }


    @Override
    public List<Media> getRecommendedMediaToAdd(int moovieListId, int size) {
        String jpql = "SELECT m.mediaID FROM media m " +
                "LEFT JOIN mediagenres g ON g.mediaid = m.mediaid " +
                "WHERE g.genreid = (SELECT sq1.genreid FROM " +
                "(SELECT mlc.moovielistid, mg.genreid, COUNT(mg.genreid) AS countGenres " +
                "FROM moovielistscontent mlc INNER JOIN mediagenres mg ON mg.mediaid = mlc.mediaid " +
                "WHERE mlc.moovielistid = :moovieListId GROUP BY mg.genreid, mlc.moovielistid " +
                "ORDER BY countGenres DESC LIMIT 1) AS sq1) " +
                "AND m.mediaID NOT IN (SELECT mlc2.mediaid FROM moovielistscontent mlc2 WHERE moovielistid = :moovieListId ) " +
                "ORDER BY m.tmdbrating DESC";

        List<Integer> ids = em.createNativeQuery(jpql).setParameter("moovieListId", moovieListId).setFirstResult(0).setMaxResults(size).getResultList();

        if (ids == null || ids.isEmpty()) {
            // no recommendations
            return Collections.emptyList();
        } else {
            TypedQuery<Media> query = em.createQuery("SELECT m FROM Media m WHERE m.mediaId in (:ids)", Media.class)
                    .setParameter("ids", ids);

            return query.getResultList();
        }

    }

    @Override
    public List<MoovieListCard> getMoovieListCards(String search, String ownerUsername, int type, String orderBy, String order, int size, int pageNumber, int currentUserId) {
        String jpql = "SELECT mlc, " +
                "(SELECT COUNT(mlc2) FROM MoovieListContent mlc2 INNER JOIN MoovieList ml ON mlc2.moovieList.moovieListId = ml.moovieListId WHERE mlc2.moovieList.moovieListId = mlc.id AND ml.name = 'Watched' AND ml.userId = :currentUserId), " +
                "(SELECT COUNT(mll2) > 0 FROM MoovieListLikes mll2 WHERE mll2.moovieList.id = mlc.id), " + // ACA BORRE LOS  AND mlf.user.id = :userId  me parece que no es por ahi
                "(SELECT COUNT(mlf2) > 0 FROM MoovieListFollowers mlf2 WHERE mlf2.moovieList.id = mlc.id) " +
                "FROM MoovieListCard mlc " +
                "WHERE mlc.type = :type ";

        boolean searchFlag = search != null && !search.isEmpty();
        boolean usernameFlag = ownerUsername != null && !ownerUsername.isEmpty();
        boolean orderFlag = order != null && !order.isEmpty();

        if (searchFlag) {
            jpql += "AND LOWER(mlc.name) LIKE :search ";
        }

        if (usernameFlag){
            jpql += "AND LOWER(mlc.user.username) = :ownerUsername ";
        }

        if(orderFlag){
            jpql += "ORDER BY " + orderBy + " " + order;
        }


        Query query = em.createQuery(jpql)
                .setParameter("currentUserId",currentUserId)
                .setParameter("type", type);

        if (searchFlag){
            query.setParameter("search", "%"+search.toLowerCase()+"%");
        }
        if (usernameFlag){
            query.setParameter("ownerUsername", ownerUsername.toLowerCase());
        }

        List<Object[]> results = query.setFirstResult(pageNumber*size).setMaxResults(size).getResultList();

        List<MoovieListCard> cards = new ArrayList<>();
        for (Object[] result : results) {
            MoovieListCard mlc = (MoovieListCard) result[0];
            mlc.setUserStatus(((Long) result[1]).intValue(), (boolean) result[2], (boolean) result[3] );
            cards.add(mlc);
        }

        return cards;
    }

    @Override
    public int getMoovieListCardsCount(String search, String ownerUsername, int type) {
        String jpql = "SELECT COUNT(mlc) " +
                "FROM MoovieListCard mlc "
                +"WHERE mlc.type = :type ";

        boolean searchFlag = search != null && !search.isEmpty();
        boolean usernameFlag = ownerUsername != null && !ownerUsername.isEmpty();

        if (searchFlag) {
            jpql += "AND LOWER(mlc.name) LIKE :search ";
        }

        if (usernameFlag){
            jpql += "AND LOWER(mlc.user.username) = :ownerUsername ";
        }
        Query query = em.createQuery(jpql)
                .setParameter("type", type);

        if (searchFlag){
            query.setParameter("search", "%"+search.toLowerCase()+"%");
        }
        if (usernameFlag){
            query.setParameter("ownerUsername", ownerUsername.toLowerCase());
        }

        Long toReturn = (Long) query.getSingleResult();

        return toReturn.intValue();
    }

    @Override
    public List<Media> getMoovieListContent(int moovieListId, int userid, String orderBy, String sortOrder, int size, int pageNumber) {

        String jpql = "SELECT new ar.edu.itba.paw.models.Media.Media(" +
                "m, " +
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END " +
                "FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieList.moovieListId " +
                "WHERE mlc.mediaId = mlc2.mediaId AND wl.name = 'Watched' AND wl.userId = :userid),  " +
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END " +
                "FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieList.moovieListId " +
                "WHERE mlc.mediaId = mlc2.mediaId AND wl.name = 'Watchlist' AND wl.userId = :userid))  " +
                "FROM MoovieListContent mlc LEFT JOIN Media m ON mlc.mediaId = m.mediaId " +
                "WHERE mlc.moovieList.moovieListId = :moovieListId " +
                "ORDER BY ";


        if (orderBy==null) {
            orderBy = "customOrder";
        }
        if (sortOrder==null) {
            sortOrder = "DESC";
        }


        if(orderBy.equals("customOrder")){
            jpql += " mlc." + orderBy + " " + sortOrder;
        } else{
            jpql += " m." + orderBy + " " + sortOrder;
        }


        TypedQuery<Media> query = em.createQuery(jpql, Media.class);
        query.setParameter("moovieListId", moovieListId);
        query.setParameter("userid", userid);
        query.setFirstResult(pageNumber * size);
        query.setMaxResults(size);
        return query.getResultList();
    }


    @Override
    public List<Media> getFeaturedMoovieListContent( int mediaType, int userid, String featuredListOrder, String orderBy, String sortOrder, int size, int pageNumber) {
        String firstQuery = "SELECT m.mediaId FROM Media m ";

        if (mediaType != MediaTypes.TYPE_ALL.getType()) {
            firstQuery+= " WHERE m.type = :type ";
        }

        firstQuery += " ORDER BY m." + featuredListOrder + " DESC";

        TypedQuery<Integer> q1 = em.createQuery(firstQuery, Integer.class);

        if (mediaType != MediaTypes.TYPE_ALL.getType()) {
            q1 = q1.setParameter("type", mediaType==1 );
        }


        List<Integer> medias = q1.setFirstResult(size*pageNumber)
                .setMaxResults(100).getResultList();


        //Two queryes are needed in order to make a LIMIT 25 inside the LIMI 100

        String jpql = "SELECT new ar.edu.itba.paw.models.Media.Media(" +
                "m, " +
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END " +
                "FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieList.moovieListId " +
                "WHERE m.mediaId = mlc2.mediaId AND wl.name = 'Watched' AND wl.userId = :userid), " +
                "(SELECT CASE WHEN COUNT(wl) > 0 THEN true ELSE false END " +
                "FROM MoovieList wl INNER JOIN MoovieListContent mlc2 ON wl.moovieListId = mlc2.moovieList.moovieListId " +
                "WHERE m.mediaId = mlc2.mediaId AND wl.name = 'Watchlist' AND wl.userId = :userid))" +
                "FROM Media m " +
                "WHERE m.mediaId IN (:medias) ORDER BY ";

        if((orderBy!=null && sortOrder != null) && (!orderBy.isEmpty() && !sortOrder.isEmpty()) && !orderBy.equals("customOrder") ){
            jpql += " m." + orderBy + " " + sortOrder;
        } else{
            jpql += " m." + featuredListOrder + " " + "DESC";
        }


        TypedQuery<Media> query = em.createQuery(jpql, Media.class);
        query.setParameter("medias", medias).setParameter("userid", userid)
                .setFirstResult(pageNumber * size).setMaxResults(size);
        return query.getResultList();
    }


    @Override
    public List<MoovieListContent> getMoovieListContentModel(int moovieListId, int size, int pageNumber) {
        TypedQuery<MoovieListContent> query = em.createQuery(" FROM MoovieListContent m WHERE m.moovieList.moovieListId = :moovieListId ORDER BY m.customOrder", MoovieListContent.class);
        return query.setParameter("moovieListId", moovieListId).setMaxResults(size).setFirstResult(size*pageNumber).getResultList();
    }

    @Override
    public int countWatchedFeaturedMoovieListContent(int mediaType, int userid, String featuredListOrder) {
        String query = "SELECT COUNT(*) FROM ( ";
        query += " SELECT * FROM media WHERE ";

        if(mediaType!=MediaTypes.TYPE_ALL.getType()){
            query += " type = :type AND ";
        }

        query += "  mediaid IN ( SELECT mediaid FROM ( SELECT m2.mediaid, COUNT(r.rating) AS votecount ";
        query += " FROM media m2 LEFT JOIN reviews r ON m2.mediaid = r.mediaid GROUP BY m2.mediaid ORDER BY votecount DESC LIMIT 100) sq1 ) ";
        query += " AND mediaid IN ( SELECT mlc.mediaid FROM moovielists ml LEFT JOIN moovielistscontent mlc ON ml.moovielistid = mlc.moovielistid ";
        query += " WHERE ml.name = 'Watched' AND userid = :userid )) AS sq2; ";

        Query q1 = em.createNativeQuery(query).setParameter("userid", userid);

        if (mediaType != MediaTypes.TYPE_ALL.getType()) {
            q1 = q1.setParameter("type", mediaType==1 );
        }

        Number toReturn = (Number) q1.getSingleResult();

        return toReturn.intValue();
    }



    @Override
    public MoovieList createMoovieList(int userId, String name, int type, String description) {
        MoovieList newMoovieList = new MoovieList(userId, name, description, type);
        try{
            TypedQuery<MoovieList> query = em.createQuery(
                    "SELECT m FROM MoovieList m WHERE m.name = :name", MoovieList.class);
            query.setParameter("name", newMoovieList.getName());

            List<MoovieList> results = query.getResultList();

            if (!results.isEmpty() && (type==(MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PUBLIC.getType()) || type==(MoovieListTypes.MOOVIE_LIST_TYPE_STANDARD_PRIVATE.getType())) ) {
                throw new UnableToInsertIntoDatabase("Create MoovieList failed, already have a table with the given name");
            }
            em.persist(newMoovieList);
            return newMoovieList;
        } catch(DuplicateKeyException e){
            throw new UnableToInsertIntoDatabase("Create MoovieList failed, already have a table with the given name");
        }

    }

    @Override
    public void editMoovieList(int listId, String newName, String newDescription) {
        try {
            MoovieList moovieList = em.find(MoovieList.class, listId);

            moovieList.setName(newName);
            moovieList.setDescription(newDescription);
            em.merge(moovieList);
        } catch (DuplicateKeyException e) {
            throw new UnableToInsertIntoDatabase("You already have a MoovieList with name: " + newName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to edit MoovieList.");
        }
    }

    @Override
    public int isMediaInMoovieList(int mediaId, int moovieListId) {
        String query = "SELECT customOrder FROM moovielistscontent WHERE mediaid = ? AND moovielistid = ? LIMIT 1";

        try {
            Query q1 = em.createNativeQuery(query)
                    .setParameter(1, mediaId)
                    .setParameter(2, moovieListId);

            Object result = q1.getSingleResult();
            return result != null ? (int)((Integer) result) : -1;
        } catch (Exception e) {
            return -1;
        }
    }



    @Override
    public MoovieList insertMediaIntoMoovieList(int moovieListid, List<Integer> mediaIdList) {
        //Remove the duplicates, just for security, we cant use an unique contraint for the update moovie list order to work!
        HashSet<Integer> uniqueSet = new HashSet<>(mediaIdList);
        mediaIdList = new ArrayList<>(uniqueSet);

        MoovieList updatedMoovieList = em.find(MoovieList.class, moovieListid);
        if (updatedMoovieList != null) {
            Integer maxCustomOrder = (Integer) em.createQuery("SELECT MAX(mlc.customOrder) FROM MoovieListContent mlc WHERE mlc.moovieList.moovieListId = :moovieListId")
                    .setParameter("moovieListId", moovieListid)
                    .getSingleResult();

            if (maxCustomOrder == null) {
                maxCustomOrder = 0;
            }

            for (Integer mediaId : mediaIdList) {
                Boolean isPresent = (Boolean) em.createQuery("SELECT CASE WHEN COUNT(mlc) > 0 THEN true ELSE false END FROM MoovieListContent mlc" +
                                " WHERE mlc.moovieList.moovieListId = :moovieListId AND mlc.mediaId = :mediaId", Boolean.class )
                        .setParameter("mediaId", mediaId).setParameter("moovieListId", updatedMoovieList.getMoovieListId()).getSingleResult();
                if(!isPresent){
                    MoovieListContent newMoovieListContent = new MoovieListContent(updatedMoovieList, mediaId, maxCustomOrder);
                    maxCustomOrder++;
                    em.persist(newMoovieListContent);
                } else {
                    throw new UnableToInsertIntoDatabase("Media already in list.");
                }
            }
        }

        return updatedMoovieList;
    }


    @Override
    public void deleteMediaFromMoovieList(int moovieListId, int mediaId) {
        int currentOrder = isMediaInMoovieList(mediaId,moovieListId);
        MoovieListContent toRemove = em.createQuery("SELECT mlc FROM MoovieListContent mlc WHERE mlc.moovieList.moovieListId = :moovieListId AND mlc.mediaId = :mediaId", MoovieListContent.class).setParameter("moovieListId", moovieListId).setParameter("mediaId",mediaId).getSingleResult();
        em.remove(toRemove);
        em.createNativeQuery("UPDATE moovielistscontent SET customorder = customorder - 1 " +
                "WHERE customorder > ?").setParameter(1, currentOrder)
                .executeUpdate();

    }

    @Override
    public void deleteMoovieList(int moovieListId) {
        MoovieList toRemove = em.find(MoovieList.class, moovieListId);
        em.remove(toRemove);
    }

    @Value("classpath:functions.sql")
    private Resource functions;
    public void executeFunctionScript() {
        try {
            // Read the SQL script from the functions.sql file
            byte[] scriptBytes = FileCopyUtils.copyToByteArray(functions.getInputStream());
            String scriptContent = new String(scriptBytes);

            // Execute the script using JdbcTemplate
            em.createNativeQuery(scriptContent).executeUpdate();



        } catch (Exception e) {
            String lets = e.getMessage();
            // Handle any exceptions
        }
    }



    @Override
    public void updateMoovieListOrder(int moovieListId, int mediaId, int prevOrder, int newOrder) {
        Query query;

        if (newOrder > prevOrder) {
            query = em.createNativeQuery("UPDATE moovielistscontent SET customorder = customorder - 1 " +
                    "WHERE customorder > ? AND customorder <= ?");
        } else {
            query = em.createNativeQuery("UPDATE moovielistscontent SET customorder = customorder + 1 " +
                    "WHERE customorder < ? AND customorder >= ?");
        }

        query.setParameter(1, prevOrder)
                .setParameter(2, newOrder)
                .executeUpdate();

        em.createNativeQuery("UPDATE moovielistscontent SET customorder = ? " +
                        "WHERE moovielistid = ? AND mediaid = ?")
                .setParameter(1, newOrder)
                .setParameter(2, moovieListId)
                .setParameter(3, mediaId)
                .executeUpdate();
    }


    @Override
    public void removeLikeMoovieList(int userId, int moovieListId) {
        MoovieListLikes like = em.createQuery("SELECT mll" +
                        " FROM MoovieListLikes mll" +
                        " WHERE mll.user.id = :userId AND mll.moovieList.id = :moovieListId"
                        , MoovieListLikes.class)
                .setParameter("userId", userId)
                .setParameter("moovieListId", moovieListId)
                .getSingleResult();

        // Si la entidad existe, eliminar el like
        if (like != null) {
            em.remove(like);
        }
    }

    @Override
    public void likeMoovieList(int userId, int moovieListId) {

        User user = em.find(User.class, userId);
        MoovieList moovieList = em.find(MoovieList.class, moovieListId);
        MoovieListLikes newLike = new MoovieListLikes(moovieList, user);
        em.persist(newLike);
    }

    @Override
    public void removeFollowMoovieList(int userId, int moovieListId) {
        MoovieListFollowers follow = em.createQuery("SELECT mll" +
                                " FROM MoovieListFollowers mll" +
                                " WHERE mll.user.id = :userId AND mll.moovieList.id = :moovieListId"
                        , MoovieListFollowers.class)
                .setParameter("userId", userId)
                .setParameter("moovieListId", moovieListId)
                .getSingleResult();

        // Si la entidad existe, eliminar el follow
        if (follow != null) {
            em.remove(follow);
        }
    }

    @Override
    public void followMoovieList(int userId, int moovieListId) {

        User user = em.find(User.class, userId);
        MoovieList moovieList = em.find(MoovieList.class, moovieListId);
        MoovieListFollowers newFollow = new MoovieListFollowers(moovieList, user);
        em.persist(newFollow);
    }

    @Override
    public void deleteListReview(int moovieListReviewId){
        MoovieListReview toRemove = em.find(MoovieListReview.class, moovieListReviewId);
        em.remove(toRemove);
    }
}
