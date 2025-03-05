package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.MoovieListNotFoundException;
import ar.edu.itba.paw.exceptions.ReviewNotFoundException;
import ar.edu.itba.paw.exceptions.UnableToInsertIntoDatabase;
import ar.edu.itba.paw.models.Review.MoovieListReview;
import ar.edu.itba.paw.models.Review.Review;
import ar.edu.itba.paw.models.Review.ReviewTypes;
import ar.edu.itba.paw.models.User.User;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class ReviewHibernateDao implements ReviewDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CommentDao commentDao;

    @Override
    public Optional<Review> getReviewById(int currentUserId, int reviewId) {
        Review review = Optional.ofNullable(em.find(Review.class, reviewId)).orElseThrow(() -> new ReviewNotFoundException("Review by id: " + reviewId + " not found"));

        String sqlQuery = "SELECT CASE WHEN EXISTS (SELECT 1 FROM reviewslikes rl WHERE rl.reviewid= :reviewId  AND rl.userid = :userId) THEN true ELSE false END";
        Query query = em.createNativeQuery(sqlQuery);
        query.setParameter("reviewId", reviewId);
        query.setParameter("userId", currentUserId);

        query.unwrap(SQLQuery.class);

        Object obj = query.getSingleResult();

        review.setHasLiked((boolean) obj);
        review.setComments(commentDao.getComments(review.getReviewId(),currentUserId,0,0));

        return Optional.of(review);
    }



    @Override
    public List<Review> getReviewsByMediaId(int currentUserId, int mediaId, int size, int pageNumber) {
        final String jpql = "SELECT new ar.edu.itba.paw.models.Review.Review ( " +
                " r , COALESCE((SELECT 1 FROM ReviewsLikes rl WHERE rl.review.reviewId = r.reviewId AND rl.user.userId = :currentUserId), 0) ) " +
                " FROM Review r WHERE r.mediaId = :mediaId";

        TypedQuery<Review> query = em.createQuery(jpql, Review.class)
                .setParameter("mediaId", mediaId).setParameter("currentUserId", currentUserId);

        return query.setFirstResult(pageNumber*size).setMaxResults(size).getResultList();
    }

    public int getReviewsByMediaIdCount(int mediaId) {
        return ((Number) em.createQuery("SELECT COUNT(r) FROM Review r WHERE r.mediaId = :mediaId").setParameter("mediaId", mediaId).getSingleResult()).intValue();
    }


    @Override
    public List<Review> getMovieReviewsFromUser(int currentUserId, int userId, int size, int pageNumber) {
        final String jpql = "SELECT new ar.edu.itba.paw.models.Review.Review ( " +
                " r , COALESCE((SELECT 1 FROM ReviewsLikes rl WHERE rl.review.reviewId = r.reviewId AND rl.user.userId = :currentUserId), 0) ) " +
                " FROM Review r WHERE r.user.userId = :userId";

        TypedQuery<Review> query = em.createQuery(jpql, Review.class)
                .setParameter("userId", userId).setParameter("currentUserId", currentUserId);

        return query.setFirstResult(pageNumber*size).setMaxResults(size).getResultList();
    }

    @Override
    public Optional<MoovieListReview> getMoovieListReviewById(int currentUserId, int moovieListReviewId) {
        MoovieListReview review = Optional.ofNullable(em.find(MoovieListReview.class, moovieListReviewId)).orElseThrow(() -> new MoovieListNotFoundException("Review by id: " + moovieListReviewId + " not found"));

        String sqlQuery = "SELECT CASE WHEN EXISTS (SELECT 1 FROM moovielistsreviewslikes rl WHERE rl.moovieListReviewId= :reviewId  AND rl.userid = :userId) THEN true ELSE false END";
        Query query = em.createNativeQuery(sqlQuery);
        query.setParameter("reviewId", moovieListReviewId);
        query.setParameter("userId", currentUserId);

        query.unwrap(SQLQuery.class);

        Object obj = query.getSingleResult();

        review.setCurrentUserHasLiked((boolean) obj);

        return Optional.of(review);
    }



    @Override
    public List<MoovieListReview> getMoovieListReviewsByMoovieListId(int currentUserId, int moovieListId, int size, int pageNumber) {
        final String jpql = "SELECT new ar.edu.itba.paw.models.Review.MoovieListReview ( " +
                " r , COALESCE((SELECT 1 FROM MoovieListsReviewsLikes rl WHERE rl.moovieListReview.moovieListReviewId = r.moovieListReviewId AND rl.user.userId = :currentUserId), 0) ) " +
                " FROM MoovieListReview r WHERE r.moovieListId = :moovieListId";

        TypedQuery<MoovieListReview> query = em.createQuery(jpql, MoovieListReview.class)
                .setParameter("moovieListId", moovieListId).setParameter("currentUserId", currentUserId);

        return query.setFirstResult(pageNumber*size).setMaxResults(size).getResultList();
    }

    @Override
    public int getMoovieListReviewByMoovieListIdCount(int moovieListId) {
        return ((Number) em.createQuery("SELECT COUNT(r) FROM MoovieListReview r WHERE r.moovieListId = :moovieListId").setParameter("moovieListId", moovieListId).getSingleResult()).intValue();
    }

    @Override
    public List<MoovieListReview> getMoovieListReviewsFromUser(int currentUserId, int userId, int size, int pageNumber) {
        final String jpql = "SELECT new ar.edu.itba.paw.models.Review.MoovieListReview ( " +
                            " r , COALESCE((SELECT 1 FROM MoovieListsReviewsLikes rl WHERE rl.moovieListReview.moovieListReviewId = r.moovieListReviewId AND rl.user.userId = :currentUserId), 0)  ) " +
                            " FROM MoovieListReview r WHERE r.user.userId = :userId ";

        TypedQuery<MoovieListReview> query = em.createQuery(jpql, MoovieListReview.class)
                .setParameter("userId", userId).setParameter("currentUserId", currentUserId);

        return query.setFirstResult(pageNumber*size).setMaxResults(size).getResultList();
    }



    @Override
    public void createReview(User user, int mediaId, int rating, String reviewContent, ReviewTypes type) {
        try {
            if(type.getType() == ReviewTypes.REVIEW_MEDIA.getType()) {
                Review review = new Review(user, mediaId, rating, reviewContent);
                em.persist(review);
            }else{
                MoovieListReview review = new MoovieListReview(user, mediaId,  reviewContent);
                em.persist(review);
            }
        } catch (Exception e) {
            throw new UnableToInsertIntoDatabase("An error occurred while creating the review.", e);
        }
    }

    @Override
    public boolean editReview(int userId, int mediaId, int rating, String reviewContent, ReviewTypes type) {
        if(type.getType() == ReviewTypes.REVIEW_MEDIA.getType()) {
            Review review = em.createQuery("SELECT r FROM Review r WHERE r.user.userId = :userId AND r.mediaId = :mediaId", Review.class)
                    .setParameter("userId", userId)
                    .setParameter("mediaId", mediaId)
                    .getSingleResult();
            if(review != null) {
                review.setRating(rating);
                review.setReviewContent(reviewContent);
                review.setLastModified(LocalDate.now());
                em.merge(review);
                return true;
            }
        }else{
            MoovieListReview review = em.createQuery("SELECT r FROM MoovieListReview r WHERE r.user.userId = :userId AND r.moovieListId = :mediaId", MoovieListReview.class)
                    .setParameter("userId", userId)
                    .setParameter("mediaId", mediaId)
                    .getSingleResult();
            if(review != null) {
                review.setReviewContent(reviewContent);
                review.setLastModified(LocalDate.now());
                em.merge(review);
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteReview(int reviewId, ReviewTypes type) {
        if(type.getType() == ReviewTypes.REVIEW_MEDIA.getType()){
            Review review = em.find(Review.class, reviewId);
            em.remove(review);
        }else{
            MoovieListReview review = em.find(MoovieListReview.class, reviewId);
            em.remove(review);
        }
    }

    @Override
    public void likeReview(int userId, int reviewId, ReviewTypes type) {
        Query query =null;
        if(type.getType() == ReviewTypes.REVIEW_MEDIA.getType()) {
            query = em.createNativeQuery("INSERT INTO reviewslikes (userid, reviewid) VALUES (:userId, :reviewId)");

        }else{
            query = em.createNativeQuery("INSERT INTO moovieListsReviewsLikes (userid, moovieListReviewId) VALUES (:userId, :reviewId)");
        }
        query.setParameter("userId", userId);
        query.setParameter("reviewId", reviewId);
        query.executeUpdate();
    }


    @Override
    public void removeLikeReview(int userId, int reviewId, ReviewTypes type) {
        Query query =null;
        if(type.getType() == ReviewTypes.REVIEW_MEDIA.getType()) {
            query = em.createNativeQuery("DELETE FROM reviewslikes WHERE userid = :userId AND reviewid = :reviewId");
        }else{
            query = em.createNativeQuery("DELETE FROM moovieListsReviewsLikes WHERE userid = :userId AND moovielistreviewid = :reviewId");
        }
        query.setParameter("userId", userId);
        query.setParameter("reviewId", reviewId);
        query.executeUpdate();
    }

    @Override
    public Review getReviewByMediaIdAndUsername(int mediaId, int userId){
        try{
            return em.createQuery("SELECT r FROM Review r WHERE r.user.userId = :userId AND r.mediaId = :mediaId", Review.class)
                    .setParameter("userId", userId)
                    .setParameter("mediaId", mediaId)
                    .getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }

    @Override
    public MoovieListReview getMoovieListReviewByListIdAndUsername(int listId, int userId){
        try{
                return em.createQuery("SELECT r FROM MoovieListReview r WHERE r.user.userId = :userId AND r.moovieListId = :listId", MoovieListReview.class)
                        .setParameter("userId", userId)
                        .setParameter("listId", listId)
                        .getSingleResult();
            }catch(NoResultException e){
            return null;
        }
    }
}
