package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comments.Comment;
import ar.edu.itba.paw.models.User.User;

import java.util.List;

public interface CommentDao {

    public List<Comment> getComments(int reviewId, int userId, int size, int pageNumber);

    public Comment getCommentById(int commentId);

    public boolean userHasLiked(int commentId, int userId);
    public boolean userHasDisliked(int commentId, int userId);

    public void likeComment(int commentId, int userId);
    public void removeLikeComment(int commentId, int userId);

    public void dislikeComment(int commentId, int userId);
    public void removeDislikeComment(int commentId, int userId);

    public void createComment(int reviewId, String content, User user);

    public void deleteComment(int commentId);

}
