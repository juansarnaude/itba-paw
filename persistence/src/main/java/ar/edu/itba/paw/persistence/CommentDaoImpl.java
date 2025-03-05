package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.User.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CommentDaoImpl implements CommentDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Comment> getComments(int reviewId, int userId, int size, int pageSize) {
        String sql = "SELECT c" +
                " FROM Comment c WHERE c.reviewId = :reviewId";

        List<Comment> comments = em.createQuery(sql, Comment.class)
                .setParameter("reviewId", reviewId)
                .setFirstResult(pageSize*size)
                .setMaxResults(size)
                .getResultList();

        for (Comment c : comments){
            c.setCurrentUserHasLiked(userHasLiked(c.getCommentId(),userId));
            c.setCurrentUserHasDisliked(userHasDisliked(c.getCommentId(),userId));
        }

        return comments;
    }

    @Override
    public Comment getCommentById(int commentId) {
        return em.find(Comment.class, commentId);
    }

    @Override
    public boolean userHasLiked(int commentId, int userId) {
        String sqlQuery = "SELECT CASE WHEN " +
                "EXISTS( SELECT 1 FROM commentlikes cl WHERE cl.commentid = :commentId AND cl.userid = :userId ) " +
                "THEN true ELSE false END";

        boolean likeState = (boolean) em.createNativeQuery(sqlQuery)
                .setParameter("commentId", commentId)
                .setParameter("userId", userId)
                .getSingleResult();
        return likeState;
    }

    @Override
    public boolean userHasDisliked(int commentId, int userId) {
        String sqlQuery = "SELECT CASE WHEN " +
                "EXISTS( SELECT 1 FROM commentdislikes cl WHERE cl.commentid = :commentId AND cl.userid = :userId ) " +
                "THEN true ELSE false END";

        boolean dislikeState = (boolean) em.createNativeQuery(sqlQuery)
                .setParameter("commentId", commentId)
                .setParameter("userId", userId)
                .getSingleResult();
        return dislikeState;
    }

    @Override
    public void likeComment(int commentId, int userId) {
        Query query = em.createNativeQuery("INSERT INTO commentlikes (commentid, userid) VALUES (:commentId, :userId)");
        query.setParameter("commentId", commentId);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    @Override
    public void removeLikeComment(int commentId, int userId) {
        Query query = em.createNativeQuery("DELETE FROM commentlikes WHERE userid = :userId AND commentId = :commentId");
        query.setParameter("userId", userId);
        query.setParameter("commentId", commentId);
        query.executeUpdate();
    }

    @Override
    public void dislikeComment(int commentId, int userId) {
        Query query = em.createNativeQuery("INSERT INTO commentdislikes (commentid, userid) VALUES (:commentId, :userId)");
        query.setParameter("commentId", commentId);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    @Override
    public void removeDislikeComment(int commentId, int userId) {
        Query query = em.createNativeQuery("DELETE FROM commentdislikes WHERE userid = :userId AND commentId = :commentId");
        query.setParameter("userId", userId);
        query.setParameter("commentId", commentId);
        query.executeUpdate();
    }

    @Override
    public void createComment(int reviewId, String content, User user) {
        em.persist(new Comment(user,reviewId,content));
    }

    @Override
    public void deleteComment(int commentId) {
        Comment toDelete = em.find(Comment.class, commentId);
        if ( toDelete != null ){
            em.remove(toDelete);
        }
    }
}
